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
 * grouping of edges with reversed edges and their label are modeled and the tree traversal are performed.
 */
public class GraphModel implements Serializable {

    @Serial
    private static final long serialVersionUID = 120L;

    /**
     * vertices are a map with the vertex coordinates as key (since their name may not be unique).
     */
    private final Map<Point, TreeVertex> vertices = new HashMap<>();
    @Getter
    private final List<Edge> edges = new ArrayList<>();

    /**
     * create a TreeVertex from the Vertex and put it to the map
     */
    public void addVertex(Vertex vertex) {
        vertices.put(vertex.getCenter(), new TreeVertex(vertex));
    }

    /**
     * the three edge-associated Swing components are bundled into a TreeEdge and together with the destination
     * vertex added to the start and end TreeVertex. Also, the original edge is added to a simple list - used in paint.
     */
    public void addEdge(Edge edge, Edge reversedEdge, JLabel weightLabel) {
        edges.add(edge);
        var start = vertices.get(edge.getStart());
        var end = vertices.get(edge.getEnd());
        start.addTreeEdge(new TreeEdge(edge, reversedEdge, weightLabel, end));
        end.addTreeEdge(new TreeEdge(reversedEdge, edge, weightLabel, start));
    }

    /**
     * remove given vertex and all associated edges
     * @return all swing components removed within the model
     */
    public Collection<Component> removeVertexWithEdges(Vertex vertex) {
        var treeVertex = vertices.get(vertex.getCenter());
        Collection<Component> componentsToRemove = new HashSet<>();
        componentsToRemove.add(vertex);
        treeVertex.getEdges().forEach(treeEdge -> removeEdgeFromModelAndAddComponents(componentsToRemove, treeEdge));
        vertices.remove(vertex.getCenter());
        return componentsToRemove;
    }

    /**
     * for the given TreeEdge add edge components to remove to given set, remove them from the edges-list
     * and remove the opposite TreeEdge from the neighbor-vertex.
     */
    private void removeEdgeFromModelAndAddComponents(Collection<Component> componentsToRemove, TreeEdge treeEdge) {
        componentsToRemove.addAll(treeEdge.getEdgeComponents());
        edges.remove(treeEdge.to());
        edges.remove(treeEdge.from());
        var oppositeTreeEdge = treeEdge.neighborVertex().getEdges().stream()
                .filter(te -> te.to() == treeEdge.from()).findFirst();
        oppositeTreeEdge.ifPresent(treeEdge.neighborVertex().getEdges()::remove);
    }

    /**
     * remove given edge from list and associated TreeEdges from both connecting vertices.
     * @return all swing (edge) components removed within the model
     */
    public Collection<Component> removeEdge(Edge edge) {
        var start = vertices.get(edge.getStart());
        Collection<Component> edgeComponentsToRemove = new HashSet<>();
        var optional = start.getEdges().stream().filter(te -> te.from() == edge).findFirst();
        optional.ifPresent(treeEdge -> {
            removeEdgeFromModelAndAddComponents(edgeComponentsToRemove, treeEdge);
            start.getEdges().remove(treeEdge);
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
        vertices.values().forEach(treeVertex -> {
            treeVertex.setVisited(false);
            unselectVertex(treeVertex.getVertex());
        });
        edges.forEach(Edge::unselect);
    }

    public void selectVertex(Vertex vertex) {
        vertices.get(vertex.getCenter()).setSelected(true);
        vertex.select();
    }


    public void unselectVertex(Vertex vertex) {
        vertices.get(vertex.getCenter()).setSelected(false);
        vertex.unselect();
    }

    private void selectVertex(TreeVertex vertex) {
        vertex.setVisited(true);
        vertex.setSelected(true);
        vertex.getVertex().select();
    }

    /**
     * called from Algorithm.Player and traverses the tree for one more edge and vertex (marking by colors)
     * @param edge the TreeEdge pointing towards the vertex to select.
     */
    public void selectEdgeAndNeighborVertex(TreeEdge edge) {
        selectVertex(edge.neighborVertex());
        if (edges.contains(edge.from())) {
            edge.from().select();
        } else {
            edge.to().select();
        }
    }

    /**
     * entry point to perform a DFS on this graph model - calls private recursive method
     * @param startVertex the start vertex for DFS
     * @return deque containing the TreeEdges in the order of traversal
     */
    public Queue<TreeEdge> depthFirstSearch(Vertex startVertex) {
        Queue<TreeEdge> traverseQueue = new ArrayDeque<>();
        var start = vertices.get(startVertex.getCenter());
        selectVertex(start);
        return depthFirstSearch(traverseQueue, start);
    }

    private Queue<TreeEdge> depthFirstSearch(Queue<TreeEdge> traverseQueue, TreeVertex vertex) {
        for (var edge: vertex.getEdges()) {
            if (!edge.neighborVertex().isVisited()) {
                edge.neighborVertex().setVisited(true);
                traverseQueue.offer(edge);
                depthFirstSearch(traverseQueue, edge.neighborVertex());
            }
        }
        return traverseQueue;
    }

    /**
     * entry point to perform a BFS on this graph model - calls private recursive method
     * @param startVertex the start vertex for DFS
     * @return deque containing the TreeEdges in the order of traversal
     */
    public Queue<TreeEdge> breadthFirstSearch(Vertex startVertex) {
        Queue<TreeEdge> traverseQueue = new ArrayDeque<>();
        var start = vertices.get(startVertex.getCenter());
        selectVertex(start);
        return breadthFirstSearch(traverseQueue, List.of(vertices.get(startVertex.getCenter())));
    }

    private Queue<TreeEdge> breadthFirstSearch(Queue<TreeEdge> traverseQueue, List<TreeVertex> levelVertices) {
        if (levelVertices.isEmpty()) {
            return traverseQueue;
        }
        var unvisitedNeighborEdges = levelVertices.stream()
                .flatMap(vertex -> vertex.getEdges().stream())
                .filter(edge -> !edge.neighborVertex().isVisited()).toList();
        unvisitedNeighborEdges.forEach(edge -> {
            if (!edge.neighborVertex().isVisited()) {
                edge.neighborVertex().setVisited(true);
                traverseQueue.offer(edge);
            }
        });
        breadthFirstSearch(traverseQueue, unvisitedNeighborEdges.stream().map(TreeEdge::neighborVertex).toList());
        return traverseQueue;
    }
}