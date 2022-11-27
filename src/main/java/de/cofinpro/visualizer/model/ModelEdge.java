package de.cofinpro.visualizer.model;


import de.cofinpro.visualizer.view.Edge;

import javax.swing.JLabel;
import java.awt.Component;
import java.util.Collection;
import java.util.Set;

/**
 * Immutable ModelEdge structure to be used in the GraphModel, that groups the Swing components of an edge and
 * has the neighbor vertex, the edge connects to. It "sits in" a ModelVertex's edge list.
 */
public record ModelEdge(Edge from, Edge to, JLabel weightLabel, ModelVertex neighborVertex) {

    /**
     * convenience method that returns only the edge associated swing components as a set (2 Edges and JLabel)
     */
    public Collection<Component> getEdgeComponents() {
        return Set.of(from, to, weightLabel);
    }

    @Override
    public String toString() {
        return "ModelEdge{from: " + from.getName() + ", to: " + to.getName() + ", weight: " + weightLabel.getName() + "}";
    }
}