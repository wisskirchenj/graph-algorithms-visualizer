package de.cofinpro.visualizer.model;

import de.cofinpro.visualizer.view.Edge;
import de.cofinpro.visualizer.view.Vertex;
import lombok.Getter;

import javax.swing.JLabel;
import java.awt.Component;
import java.awt.Point;
import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;

/**
 * model class to keep track of the SwingComponents in the GraphPanel. Also, vertex connections and the
 * grouping of edges with reversed edges and their label are modeled and the graph traversal are performed.
 */
public class GraphModel implements Serializable {

    @Serial
    private static final long serialVersionUID = 120L;

    /**
     * vertices are a map with the vertex coordinates as key (since their name may not be unique).
     */
    private final Map<Point, ModelVertex> vertices = new HashMap<>();
    @Getter
    private final List<Edge> edges = new ArrayList<>();

    /**
     * create a ModelVertex from the Vertex and put it to the map
     */
    public void addVertex(Vertex vertex) {
        vertices.put(vertex.getCenter(), new ModelVertex(vertex));
    }

    /**
     * the three edge-associated Swing components are bundled into a ModelEdge and together with the destination
     * vertex added to the start and end ModelVertex. Also, the original edge is added to a simple list - used in paint.
     */
    public void addEdge(Edge edge, Edge reversedEdge, JLabel weightLabel) {
        edges.add(edge);
        var start = vertices.get(edge.getStart());
        var end = vertices.get(edge.getEnd());
        start.addModelEdge(new ModelEdge(edge, reversedEdge, weightLabel, start, end));
        end.addModelEdge(new ModelEdge(reversedEdge, edge, weightLabel, end, start));
    }

    public ModelVertex getModelVertex(Vertex vertex) {
        return vertices.get(vertex.getCenter());
    }

    /**
     * remove given vertex and all associated edges
     * @return all swing components removed within the model
     */
    public Collection<Component> removeVertexWithEdges(Vertex vertex) {
        var modelVertex = vertices.get(vertex.getCenter());
        Collection<Component> componentsToRemove = new HashSet<>();
        componentsToRemove.add(vertex);
        modelVertex.getEdges().forEach(modelEdge -> removeEdgeFromModelAndAddComponents(componentsToRemove, modelEdge));
        vertices.remove(vertex.getCenter());
        return componentsToRemove;
    }

    /**
     * for the given ModelEdge add edge components to remove to given set, remove them from the edges-list
     * and remove the opposite ModelEdge from the neighbor-vertex.
     */
    private void removeEdgeFromModelAndAddComponents(Collection<Component> componentsToRemove, ModelEdge modelEdge) {
        componentsToRemove.addAll(modelEdge.getEdgeComponents());
        edges.remove(modelEdge.to());
        edges.remove(modelEdge.from());
        var oppositeModelEdge = modelEdge.end().getEdges().stream()
                .filter(me -> me.to() == modelEdge.from()).findFirst();
        oppositeModelEdge.ifPresent(modelEdge.end().getEdges()::remove);
    }

    /**
     * remove given edge from list and associated ModelEdges from both connecting vertices.
     * @return all swing (edge) components removed within the model
     */
    public Collection<Component> removeEdge(Edge edge) {
        var start = vertices.get(edge.getStart());
        Collection<Component> edgeComponentsToRemove = new HashSet<>();
        var optional = start.getEdges().stream().filter(me -> me.from() == edge).findFirst();
        optional.ifPresent(modelEdge -> {
            removeEdgeFromModelAndAddComponents(edgeComponentsToRemove, modelEdge);
            start.getEdges().remove(modelEdge);
        });
        return edgeComponentsToRemove;
    }

    /**
     * model clear used when File->New is handled.
     */
    public void clear() {
        vertices.clear();
        edges.clear();
    }

    /**
     * unselect all vertices and edges.
     */
    public void unselect() {
        vertices.values().forEach(modelVertex -> {
            modelVertex.setVisited(false);
            modelVertex.setSelected(false);
            modelVertex.getVertex().unselect();
        });
        edges.forEach(Edge::unselect);
    }

    public void selectVertex(Vertex vertex) {
        vertices.get(vertex.getCenter()).setSelected(true);
        vertex.select();
    }

    private void selectVertex(ModelVertex vertex) {
        vertex.setVisited(true);
        vertex.setSelected(true);
        vertex.getVertex().select();
    }

    /**
     * called from Algorithm.Player and traverses the graph for one more edge and vertex (marking by colors)
     * @param edge the ModelEdge pointing towards the vertex to select.
     */
    public void selectEdgeAndNeighborVertex(ModelEdge edge) {
        selectVertex(edge.end());
        if (edges.contains(edge.from())) {
            edge.from().select();
        } else {
            edge.to().select();
        }
    }

    /**
     * entry point to perform a DFS on this graph model - calls private recursive method
     * @param startVertex the start vertex for DFS
     * @return Queue containing the ModelEdges in the order of traversal
     */
    public Queue<ModelEdge> depthFirstSearch(Vertex startVertex) {
        Queue<ModelEdge> traverseQueue = new ArrayDeque<>();
        var start = vertices.get(startVertex.getCenter());
        selectVertex(start);
        return depthFirstSearch(traverseQueue, start);
    }

    private Queue<ModelEdge> depthFirstSearch(Queue<ModelEdge> traverseQueue, ModelVertex vertex) {
        for (var edge: vertex.getEdges()) {
            if (!edge.end().isVisited()) {
                edge.end().setVisited(true);
                traverseQueue.offer(edge);
                depthFirstSearch(traverseQueue, edge.end());
            }
        }
        return traverseQueue;
    }

    /**
     * entry point to perform a BFS on this graph model - calls private recursive method
     * @param startVertex the start vertex for DFS
     * @return Queue containing the ModelEdges in the order of traversal
     */
    public Queue<ModelEdge> breadthFirstSearch(Vertex startVertex) {
        Queue<ModelEdge> traverseQueue = new ArrayDeque<>();
        var start = vertices.get(startVertex.getCenter());
        selectVertex(start);
        return breadthFirstSearch(traverseQueue, List.of(vertices.get(startVertex.getCenter())));
    }

    private Queue<ModelEdge> breadthFirstSearch(Queue<ModelEdge> traverseQueue, List<ModelVertex> levelVertices) {
        if (levelVertices.isEmpty()) {
            return traverseQueue;
        }
        var unvisitedNeighborEdges = levelVertices.stream()
                .flatMap(vertex -> vertex.getEdges().stream())
                .filter(edge -> !edge.end().isVisited()).toList();
        unvisitedNeighborEdges.forEach(edge -> {
            if (!edge.end().isVisited()) {
                edge.end().setVisited(true);
                traverseQueue.offer(edge);
            }
        });
        breadthFirstSearch(traverseQueue, unvisitedNeighborEdges.stream().map(ModelEdge::end).toList());
        return traverseQueue;
    }
}