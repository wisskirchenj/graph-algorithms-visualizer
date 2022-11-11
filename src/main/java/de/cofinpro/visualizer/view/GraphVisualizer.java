package de.cofinpro.visualizer.view;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import java.awt.*;
import java.util.Arrays;

/**
 * JFrame based class representing the view of the GraphVisualizer
 */
public class GraphVisualizer extends JFrame {
    private static final Color PANEL_COLOR = Color.BLACK;

    private static final int GRAPH_WIDTH = 800;
    private static final int GRAPH_HEIGHT = 600;
    private static final int VERTEX_RADIUS = 50;
    private static final int HEIGHT_OFFSET = 28;

    public GraphVisualizer() {
        super("Graph-Algorithms Visualizer");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setName("Graph-Algorithms Visualizer");
        setSize(GRAPH_WIDTH, GRAPH_HEIGHT);
        setResizable(false);
        setContentPane(createGraph());
        setVisible(true);
    }

    /**
     * create the graph panel with vertices placed in all four corners
     */
    private Container createGraph() {
        var graph = new JPanel();
        graph.setName("Graph");
        graph.setBackground(PANEL_COLOR);
        graph.setLayout(null); // needed for absolute positioning of components
        Arrays.stream(Corner.values()).forEach(corner -> graph.add(createVertex(corner)));
        return graph;
    }

    /**
     * creates a vertex and places it in the corner given by the enum value
     */
    private Component createVertex(Corner corner) {
        var vertex = new Vertex();
        vertex.setName("Vertex %d".formatted(corner.ordinal()));
        vertex.setBackground(PANEL_COLOR);
        vertex.setSize(VERTEX_RADIUS);
        vertex.setLocation(switch (corner) {
            case NW -> new Point(0, 0);
            case NE -> new Point(GRAPH_WIDTH - VERTEX_RADIUS, 0);
            case SW -> new Point(0, GRAPH_HEIGHT - VERTEX_RADIUS - HEIGHT_OFFSET);
            case SE -> new Point(GRAPH_WIDTH - VERTEX_RADIUS, GRAPH_HEIGHT - VERTEX_RADIUS - HEIGHT_OFFSET);
        });
        vertex.addLabel(String.valueOf(corner.ordinal()));
        vertex.setLabelBackground(Color.WHITE);
        return vertex;
    }
}
