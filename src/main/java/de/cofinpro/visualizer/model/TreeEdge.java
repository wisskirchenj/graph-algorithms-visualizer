package de.cofinpro.visualizer.model;


import de.cofinpro.visualizer.view.Edge;

import javax.swing.*;
import java.awt.*;
import java.util.Collection;
import java.util.Set;

/**
 * Immutable TreeEdge structure to be used in the GraphModel, that groups the Swing components of an edge and
 * has the neighbor vertex, the edge connects to. It "sits in" a TreeVertex's edge list.
 */
public record TreeEdge(Edge from, Edge to, JLabel weightLabel, TreeVertex neighborVertex) {

    /**
     * convenience method that returns only the edge associated swing components as a set (2 Edges and JLabel)
     */
    public Collection<Component> getEdgeComponents() {
        return Set.of(from, to, weightLabel);
    }
}
