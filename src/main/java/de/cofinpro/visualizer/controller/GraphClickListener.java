package de.cofinpro.visualizer.controller;

import de.cofinpro.visualizer.view.GraphPanel;
import lombok.extern.slf4j.Slf4j;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static de.cofinpro.visualizer.view.Vertex.*;
@Slf4j
public class GraphClickListener extends MouseAdapter {

    private static final int CLICK_OFFSET = getVERTEX_RADIUS() / 2;

    private final GraphPanel graphPanel;

    public GraphClickListener(GraphPanel graphPanel) {
        this.graphPanel = graphPanel;
    }

    @Override
    public void mouseClicked(MouseEvent event) {
        log.info("Mouse clicked {}", event.getPoint());
        var placePositionVertex
                = new Point(event.getX() - CLICK_OFFSET, event.getY() - CLICK_OFFSET);
        graphPanel.addVertex("T", placePositionVertex);
    }
}
