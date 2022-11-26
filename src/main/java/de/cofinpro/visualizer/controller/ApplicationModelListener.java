package de.cofinpro.visualizer.controller;

import de.cofinpro.visualizer.model.AlgorithmModel;
import de.cofinpro.visualizer.model.Mode;

/**
 * callback interface for listeners to changes in the ApplicationModel. Listeners only need to implement (override)
 * update methods for the events they are interested it.
 */
public interface ApplicationModelListener {

    default void updateMode(Mode changedMode) {}
    default void updateAlgorithm(Algorithm algorithm) {}
    default void updateAlgorithmState(AlgorithmModel algorithmModel) {}
}