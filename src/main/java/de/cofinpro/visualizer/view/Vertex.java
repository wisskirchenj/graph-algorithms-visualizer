package de.cofinpro.visualizer.view;

import lombok.Getter;
import lombok.Setter;

import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;

/**
 * Vertex representing class consisting of a square-shaped JPanel with a colored circle and a centered label
 * drawn inside
 */
@Setter
@Getter
public class Vertex extends JPanel {

    @Getter
    private static final int VERTEX_RADIUS = 50;
    @Getter
    private static final Color VERTEX_COLOR = Color.WHITE;
    @Getter
    private static final Color VERTEX_SELECTED_COLOR = Color.YELLOW;

    private final String vertexLabel;
    private Color labelBackground = VERTEX_COLOR;

    public Vertex(String label, Color background) {
        super();
        this.vertexLabel = label;
        setName("Vertex %s".formatted(label));
        setBackground(background);
        addLabel(label);
        setSize(VERTEX_RADIUS, VERTEX_RADIUS);
    }

    /**
     * draw a filled circle in labelBackgroundColor into the vertex panel
     * @param graphics the <code>Graphics</code> object to protect
     */
    @Override   // paintComponent only draws the container - not its contents
    public void paintComponent(Graphics graphics) {
        graphics.setColor(labelBackground);
        graphics.fillOval(0, 0, VERTEX_RADIUS, VERTEX_RADIUS);
    }

    /**
     * adds a label with given text and appropriate scaled font size to the vertex
     */
    public void addLabel(String labelText) {
        var label = new JLabel(labelText);
        label.setName("VertexLabel %s".formatted(labelText));
        label.setForeground(getBackground());
        label.setFont(new Font("Arial", Font.BOLD,  VERTEX_RADIUS * 4 / 5));
        add(label);
    }

    /**
     * set a new label background color (i.e. the circle fill color) and repaint only the circle and the label
     */
    public void setLabelBackground(Color newColor) {
        if (newColor.equals(labelBackground)) {
            return;
        }
        labelBackground = newColor;
        paintComponent(getGraphics());
        paintChildren(getGraphics());
    }

    /**
     * set background color to the selected color (called, when selected by mouse click)
     */
    public void select() {
        setLabelBackground(VERTEX_SELECTED_COLOR);
    }

    /**
     * reset background color to the default - non-selected color
     */
    public void unselect() {
        setLabelBackground(VERTEX_COLOR);
    }

    /**
     * checks, if a given point lies inside the vertex circle
     * @param point a point to check for
     */
    public boolean isInside(Point point) {
        return point.distance(getCenter()) <= VERTEX_RADIUS / 2.0;
    }

    /**
     * calculate and return the vertex center as Point
     */
    public Point getCenter() {
        return new Point(getX() + VERTEX_RADIUS / 2, getY() + VERTEX_RADIUS / 2);
    }
}