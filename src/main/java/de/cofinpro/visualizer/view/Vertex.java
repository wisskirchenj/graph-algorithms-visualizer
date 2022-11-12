package de.cofinpro.visualizer.view;

import lombok.Getter;
import lombok.Setter;

import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

/**
 * Vertex representing class consisting of a square-shaped JPanel with a colored circle and a centered label
 * drawn inside
 */
@Setter
@Getter
public class Vertex extends JPanel {

    @Getter
    private static final int VERTEX_RADIUS = 50;
    private static final Color VERTEX_COLOR = Color.YELLOW;

    private Color labelBackground = Color.WHITE;

    public Vertex(String label, Color background) {
        super();
        setName("Vertex %s".formatted(label));
        setBackground(background);
        setLabelBackground(VERTEX_COLOR);
        addLabel(label);
        setSize(VERTEX_RADIUS, VERTEX_RADIUS);
    }

    /**
     * draw a filled white circle in the vertex panel
     * @param graphics the <code>Graphics</code> object to protect
     */
    @Override   // paintComponent only draws the container - not its contents
    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        graphics.setColor(labelBackground);
        graphics.fillOval(0, 0, VERTEX_RADIUS, VERTEX_RADIUS);
    }

    /**
     * adds a label with given text and appropriate scaled font size to fin in the center of the circle drawn
     */
    public void addLabel(String labelText) {
        var label = new JLabel(labelText);
        label.setName("VertexLabel %s".formatted(labelText));
        label.setForeground(getBackground());
        label.setFont(new Font("Arial", Font.BOLD,  VERTEX_RADIUS * 4 / 5));
        add(label);
    }
}
