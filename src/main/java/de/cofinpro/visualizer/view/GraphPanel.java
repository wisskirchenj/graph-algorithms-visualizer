package de.cofinpro.visualizer.view;

import de.cofinpro.visualizer.controller.GraphClickListener;

import javax.swing.*;
import java.awt.*;

public class GraphPanel extends JPanel {
    public GraphPanel(LayoutManager layout) {
        super(layout);
        setName("Graph");
        addMouseListener(new GraphClickListener(this));
    }

    public void addVertex(String label, Point location) {
        var vertex = new Vertex(label);
        vertex.setBackground(getBackground());
        vertex.addLabel(label);
        vertex.setLocation(location);
        add(vertex);
        revalidate();
        repaint();
    }
}
