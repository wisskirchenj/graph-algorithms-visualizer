package de.cofinpro.visualizer.controller;

import de.cofinpro.visualizer.model.ApplicationModel;
import de.cofinpro.visualizer.model.Mode;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * ActionListener implementation on the Mode menu items, that sets the mode in the ApplicationModel
 */
public class ModeMenuItemListener implements ActionListener {
    private final Mode mode;
    private final ApplicationModel applicationModel;

    public ModeMenuItemListener(Mode mode, ApplicationModel applicationModel) {
        this.mode = mode;
        this.applicationModel = applicationModel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        applicationModel.setMode(mode);
    }
}