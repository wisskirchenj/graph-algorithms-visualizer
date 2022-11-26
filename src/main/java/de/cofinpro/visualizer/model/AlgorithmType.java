package de.cofinpro.visualizer.model;

import de.cofinpro.visualizer.controller.Algorithm;
import de.cofinpro.visualizer.controller.BreadthFirstSearch;
import de.cofinpro.visualizer.controller.DepthFirstSearch;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.function.Supplier;

/**
 * enum to store the algorithms as selectable in the GraphVisualizer's Algorithms Menu with their
 * associated names and an instance supplier.
 */
@AllArgsConstructor
@Getter
public enum AlgorithmType {
    NONE("", null),
    DEPTH_FIRST("Depth-First Search", DepthFirstSearch::new),
    BREADTH_FIRST("Breadth-First Search", BreadthFirstSearch::new);

    private final String algorithmName;
    private final Supplier<Algorithm> algorithmProducer;
}
