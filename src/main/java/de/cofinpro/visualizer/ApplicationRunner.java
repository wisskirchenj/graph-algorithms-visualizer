package de.cofinpro.visualizer;

import de.cofinpro.visualizer.view.GraphVisualizer;

import javax.swing.SwingUtilities;

/**
 * application runner that starts the GraphVisualizer in the EDT-thread
 */
public class ApplicationRunner {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(GraphVisualizer::new);
    }
}