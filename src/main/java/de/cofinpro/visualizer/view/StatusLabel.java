package de.cofinpro.visualizer.view;

import de.cofinpro.visualizer.controller.ApplicationModelListener;
import de.cofinpro.visualizer.model.ApplicationModel;

import javax.swing.JLabel;
import java.awt.Color;
import java.awt.Font;

/**
 * The status Label (extends JLabel) that sits on top of the graph panel and displays the current Mode selection by
 * listening for Mode updates of the ApplicationModel.
 */
public class StatusLabel extends JLabel implements ApplicationModelListener {

    private static final Color FG_COLOR = Color.BLACK;

    public StatusLabel(String modeName) {
        super("Current Mode -> %s".formatted(modeName));
        setName("Mode");
        setFont(new Font("Arial", Font.PLAIN, 18));
        setForeground(FG_COLOR);
    }

    @Override
    public void update(ApplicationModel model) {
        setText("Current Mode -> %s".formatted(model.getMode().getModeName()));
    }
}
