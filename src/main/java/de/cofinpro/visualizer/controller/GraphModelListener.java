package de.cofinpro.visualizer.controller;

import de.cofinpro.visualizer.model.GraphModel;

/**
 * callback interface for listeners to changes in the GraphModel.
 */
public interface GraphModelListener {

    void update(GraphModel model);
}
