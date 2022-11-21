package de.cofinpro.visualizer.controller;

import de.cofinpro.visualizer.model.Mode;
import de.cofinpro.visualizer.view.Edge;
import de.cofinpro.visualizer.view.GraphPanel;
import de.cofinpro.visualizer.view.Vertex;
import lombok.extern.slf4j.Slf4j;

import javax.swing.JOptionPane;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

import static de.cofinpro.visualizer.view.Vertex.*;

/**
 * MouseClickListener for the GraphPanel, that initiates diverse actions depending on the mode and selections.
 */
@Slf4j
public class GraphClickListener extends MouseAdapter {

    private static final String VERTEX_CLICKED = "Vertex {} clicked!";
    private static final int CLICK_OFFSET = getVERTEX_RADIUS() / 2;

    private final GraphPanel graphPanel;

    public GraphClickListener(GraphPanel graphPanel) {
        this.graphPanel = graphPanel;
    }

    @Override
    public void mouseClicked(MouseEvent event) {
        log.debug("Mouse clicked {}", event.getPoint());
        log.debug("Hit component: {}", graphPanel.getComponentAt(event.getPoint()));
        switch (graphPanel.getMode()) {
            case ADD_VERTEX -> handlePlaceVertex(event);
            case ADD_EDGE -> handlePlaceEdge(event);
            case REMOVE_VERTEX -> handleRemoveVertex(event);
            case REMOVE_EDGE -> handleRemoveEdge(event);
            case NONE -> { /* do nothing */ }
            default -> throw new IllegalStateException("Unexpected value: " + graphPanel.getMode());
        }
    }

    /**
     * check, if an edge was clicked and if so delegate the removal to the GraphPanel.
     */
    private void handleRemoveEdge(MouseEvent event) {
        if (graphPanel.getComponentAt(event.getPoint()) instanceof Edge edge) {
            log.debug("Edge '{}' clicked ", edge.getName());
            graphPanel.removeEdge(edge);
        }
    }

    /**
     * check, if a vertex was clicked and if so delegate the removal with connected edges to the GraphPanel.
     */
    private void handleRemoveVertex(MouseEvent event) {
        graphPanel.getVertexAt(event.getPoint()).ifPresent(vertex -> {
            log.debug(VERTEX_CLICKED, vertex.getVertexLabel());
            graphPanel.removeVertexWithAssociateEdges(vertex);
        });
    }

    /**
     * method called on mouse clicks in the graph panel, when in Add_EDGE mode. If a vertex circle was hit by the mouse click,
     * an edge is created and added, if another vertex was already selected, or the clicked vertex is selected, if none was before.
     */
    private void handlePlaceEdge(MouseEvent event) {
        graphPanel.getVertexAt(event.getPoint()).ifPresent(vertex -> {
            log.debug(VERTEX_CLICKED, vertex.getVertexLabel());
            graphPanel.getSelected()
                    .ifPresentOrElse(selected -> addEdge(selected, vertex), () -> graphPanel.setSelected(vertex));
        });
    }

    /**
     * if the given vertices are different, ask the user for a weight with a modal dialog and create edges with this weight.
     */
    private void addEdge(Vertex first, Vertex second) {
        if (first == second) {
            return;
        }
        log.debug("adding an edge from vertex {} to vertex {}.", first.getVertexLabel(), second.getVertexLabel());
        new Dialog().getLabelFromDialog().ifPresent(weight -> graphPanel.addEdge(weight, first, second));
        first.unselect();
        graphPanel.setSelected(null);
    }

    /**
     * method called on mouse clicks in the graph panel, when in Add_VERTEX mode, that places a new vertex with center at
     * mouse click position, if there is room (i.e. no other vertex or edge).
     */
    private void handlePlaceVertex(MouseEvent event) {
        if (graphPanel.getComponentAt(event.getPoint()) instanceof GraphPanel) {
            new Dialog().getLabelFromDialog().ifPresent(label -> graphPanel.addVertex(label,
                    new Point(event.getX() - CLICK_OFFSET, event.getY() - CLICK_OFFSET)));
        }
    }

    /**
     * inner class that parametrizes the input dialog for various modes (also to come...)
     */
    private class Dialog {

        private static final Map<Mode, Predicate<String>> VALIDATORS = Map.of(
                Mode.ADD_VERTEX, input -> input == null || input.length() == 1 && !input.isBlank(),
                Mode.ADD_EDGE, input -> input == null || input.matches("[+-]?\\d")
        );
        private static final Map<Mode, String> DIALOG_TITLES = Map.of(
                Mode.ADD_VERTEX, "Vertex",
                Mode.ADD_EDGE, "Edge"
        );
        private static final Map<Mode, String> DIALOG_MESSAGES = Map.of(
                Mode.ADD_VERTEX, "Enter the Vertex Id (Should be 1 char):",
                Mode.ADD_EDGE, "Enter Weight:"
        );

        private Optional<String> getLabelFromDialog() {
            String label;
            Mode currentMode = graphPanel.getMode();
            do {
                label = JOptionPane.showInputDialog(graphPanel,
                        DIALOG_MESSAGES.get(currentMode),
                        DIALOG_TITLES.get(currentMode),
                        JOptionPane.QUESTION_MESSAGE);
            } while (!VALIDATORS.get(currentMode).test(label));
            return Optional.ofNullable(label);
        }
    }
}