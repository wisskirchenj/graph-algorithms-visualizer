package de.cofinpro.visualizer.model;

import de.cofinpro.visualizer.view.Vertex;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * ModelVertex object that wraps a Vertex and adds its connected edges (as <code>List&lt;ModelEdge&gt;</code>)
 */
@Data
public class ModelVertex {

    private final Vertex vertex;
    private boolean selected = false;
    private boolean visited = false;
    List<ModelEdge> edges = new ArrayList<>();

    public ModelVertex(Vertex vertex) {
        this.vertex = vertex;
    }

    public void addModelEdge(ModelEdge modelEdge) {
        edges.add(modelEdge);
    }

    @Override
    public int hashCode() {
        return vertex.hashCode() * 31 + Objects.hashCode(selected) * 11 + Objects.hashCode(visited);
    }

    @Override
    public boolean equals(Object other) {
        return (other instanceof ModelVertex mv) && vertex.equals(mv.vertex)
                && selected == mv.selected && visited == mv.visited;
    }

    @Override
    public String toString() {
        return "ModelVertex{vertex:'" + vertex.getVertexLabel() +"', selected: " + selected
                + "', visited: " + visited + ", edges" + edges + "}";
    }
}