package de.cofinpro.visualizer.controller;

import de.cofinpro.visualizer.view.GraphPanel;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Optional;

import static de.cofinpro.visualizer.view.Vertex.*;

/**
 * MouseClickListener for the GraphPanel, that opens a Vertex-Id input dialog on mouse clicks.
 */
@Slf4j
public class GraphClickListener extends MouseAdapter {

    private static final int CLICK_OFFSET = getVERTEX_RADIUS() / 2;
    private static final String VERTEX_ID_LABEL = "Enter the Vertex Id (Should be 1 char):";

    private final GraphPanel graphPanel;

    public GraphClickListener(GraphPanel graphPanel) {
        this.graphPanel = graphPanel;
    }

    @Override
    public void mouseClicked(MouseEvent event) {
        log.info("Mouse clicked {}", event.getPoint());
        var placePositionVertex
                = new Point(event.getX() - CLICK_OFFSET, event.getY() - CLICK_OFFSET);
        getLabelFromDialog().ifPresent(label -> graphPanel.addVertex(label, placePositionVertex));
    }

    private Optional<String> getLabelFromDialog() {
        String label;
        do {
            label = JOptionPane.showInputDialog(graphPanel, VERTEX_ID_LABEL, "Vertex",
                    JOptionPane.QUESTION_MESSAGE);
        } while (label != null && (label.isBlank() || label.length() != 1));
        return Optional.ofNullable(label);
    }
}
