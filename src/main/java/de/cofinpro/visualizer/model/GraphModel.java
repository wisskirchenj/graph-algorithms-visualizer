package de.cofinpro.visualizer.model;

import de.cofinpro.visualizer.view.Edge;
import de.cofinpro.visualizer.view.Vertex;
import lombok.Getter;

import javax.swing.*;
import java.awt.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.*;
import java.util.List;

/**
 * model class to keep track of the SwingComponents in the GraphPanel. Also, vertex connections and the
 * grouping of edges with reversed edges and their label are modeled.
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
    public Set<Component> removeVertexWithEdges(Vertex vertex) {
        var treeVertex = vertices.get(vertex.getCenter());
        Set<Component> componentsToRemove = new HashSet<>();
        componentsToRemove.add(vertex);
        treeVertex.getEdges().forEach(treeEdge -> removeEdgeFromModelAndAddComponents(componentsToRemove, treeEdge));
        vertices.remove(vertex.getCenter());
        return componentsToRemove;
    }

    /**
     * for the given TreeEdge add edge components to remove to given set, remove them from the edges-list
     * and remove the opposite TreeEdge from the neighbor-vertex.
     */
    private void removeEdgeFromModelAndAddComponents(Set<Component> componentsToRemove, TreeEdge treeEdge) {
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
    public Set<Component> removeEdge(Edge edge) {
        var start = vertices.get(edge.getStart());
        Set<Component> edgeComponentsToRemove = new HashSet<>();
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
}