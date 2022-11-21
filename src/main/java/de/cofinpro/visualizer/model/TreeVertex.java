package de.cofinpro.visualizer.model;

import de.cofinpro.visualizer.view.Vertex;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * GraphModel object that wraps a Vertex and adds its connected edges (as List&lt;TreeEdge&gt;)
 */
@Data
public class TreeVertex {

    private final Vertex vertex;
    private final List<TreeEdge> edges = new ArrayList<>();

    public TreeVertex(Vertex vertex) {
        this.vertex = vertex;
    }

    public void addTreeEdge(TreeEdge treeEdge) {
        edges.add(treeEdge);
    }
}
