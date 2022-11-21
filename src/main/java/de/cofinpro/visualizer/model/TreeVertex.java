package de.cofinpro.visualizer.model;

import de.cofinpro.visualizer.view.Vertex;
import lombok.Value;

import java.util.ArrayList;
import java.util.List;

/**
 * Imuutable TreeVertex object that wraps a Vertex and adds its connected edges (as <code>List&lt;TreeEdge&gt;</code>)
 */
@Value
public class TreeVertex {

    Vertex vertex;
    List<TreeEdge> edges = new ArrayList<>();

    public TreeVertex(Vertex vertex) {
        this.vertex = vertex;
    }

    public void addTreeEdge(TreeEdge treeEdge) {
        edges.add(treeEdge);
    }
}