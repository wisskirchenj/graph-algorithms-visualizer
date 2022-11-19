package de.cofinpro.visualizer.controller;

import de.cofinpro.visualizer.model.GraphModel;
import de.cofinpro.visualizer.model.Mode;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * ActionListener implementation on the Mode menu, that sets the mode in the GraphModel
 */
public class ModeMenuListener implements ActionListener {
    private final Mode mode;
    private final GraphModel graphModel;

    public ModeMenuListener(Mode mode, GraphModel graphModel) {
        this.mode = mode;
        this.graphModel = graphModel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        graphModel.setMode(mode);
    }
}
