package de.cofinpro.visualizer.model;

import de.cofinpro.visualizer.view.Vertex;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * TreeVertex object that wraps a Vertex and adds its connected edges (as <code>List&lt;TreeEdge&gt;</code>)
 */
@Data
public class TreeVertex {

    private final Vertex vertex;
    private boolean selected = false;
    private boolean visited = false;
    List<TreeEdge> edges = new ArrayList<>();

    public TreeVertex(Vertex vertex) {
        this.vertex = vertex;
    }

    public void addTreeEdge(TreeEdge treeEdge) {
        edges.add(treeEdge);
    }

    @Override
    public String toString() {
        return "TreeVertex{vertex:'" + vertex.getVertexLabel() +"', selected: " + selected
                +"', visited: " + visited + ", edges" + edges + "}";
    }
}