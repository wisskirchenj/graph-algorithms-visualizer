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

    private Color labelBackground = Color.WHITE;

    /**
     * draw a filled white circle and afterwards(!) paint the label component
     * @param graphics the <code>Graphics</code> context in which to paint
     */
    @Override
    public void paint(Graphics graphics) {
        super.paint(graphics);
        int sideLength = getHeight();
        graphics.setColor(labelBackground);
        graphics.fillOval(0, 0, sideLength, sideLength);
        paintComponents(graphics);
    }

    /**
     * adds a label with given text and appropriate scaled font size to fin in the center of the circle drawn
     */
    public void addLabel(String labelText) {
        var label = new JLabel(labelText);
        label.setName("VertexLabel %s".formatted(labelText));
        label.setForeground(getBackground());
        label.setFont(new Font("Arial", Font.BOLD, (int)(0.8 * getHeight())));
        add(label);
    }

    /**
     * set height and width to same value given
     */
    public void setSize(int sideLength) {
        setBounds(0, 0, sideLength, sideLength);
    }
}
