package de.cofinpro.visualizer.controller;

import de.cofinpro.visualizer.model.ApplicationModel;

/**
 * callback interface for listeners to changes in the ApplicationModel.
 */
public interface ApplicationModelListener {

    void update(ApplicationModel model);
}