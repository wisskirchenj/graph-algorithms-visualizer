package de.cofinpro.visualizer.controller;

import de.cofinpro.visualizer.model.AlgorithmType;
import de.cofinpro.visualizer.model.ApplicationModel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Listener for the menu items of the algorithms menu.
 */
public class AlgorithmMenuItemListener implements ActionListener {
    private final AlgorithmType algorithmType;
    private final ApplicationModel applicationModel;

    public AlgorithmMenuItemListener(AlgorithmType algorithmType, ApplicationModel applicationModel) {
        this.algorithmType = algorithmType;
        this.applicationModel = applicationModel;
    }

    /**
     * start the chosen algorithm on selecting the menu item.
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        applicationModel.startAlgorithm(algorithmType);
    }
}
