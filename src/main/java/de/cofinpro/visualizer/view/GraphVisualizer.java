package de.cofinpro.visualizer.view;

import javax.swing.JFrame;
import javax.swing.WindowConstants;
import java.awt.*;
import java.util.Arrays;

import static de.cofinpro.visualizer.view.Vertex.*;

/**
 * JFrame based class representing the view of the GraphVisualizer
 */
public class GraphVisualizer extends JFrame {
    private static final Color PANEL_COLOR = Color.PINK.darker();

    private static final int GRAPH_WIDTH = 800;
    private static final int GRAPH_HEIGHT = 600;
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
        var graph = new GraphPanel(null);
        graph.setBackground(PANEL_COLOR);
        Arrays.stream(Corner.values())
                .forEach(corner -> graph.addVertex(String.valueOf(corner.ordinal()),
                        switch (corner) {
                            case NW -> new Point(0, 0);
                            case NE -> new Point(GRAPH_WIDTH - getVERTEX_RADIUS(), 0);
                            case SW -> new Point(0,GRAPH_HEIGHT - getVERTEX_RADIUS() - HEIGHT_OFFSET);
                            case SE -> new Point(GRAPH_WIDTH - getVERTEX_RADIUS(),
                                    GRAPH_HEIGHT - getVERTEX_RADIUS() - HEIGHT_OFFSET);
                        }));
        return graph;
    }
}
