package de.cofinpro.visualizer.view;

import lombok.Getter;

import javax.swing.JComponent;
import java.awt.Point;

/**
 * very lightweight Edge class (extending JComponent - only because Hs-tests demand it). It does NOT care for its own
 * painting, because we do that in the GraphPanel to get the layering right. It just stores its start and end point
 * and selection state.
 */
@Getter
public class Edge extends JComponent {

    private final Point start;
    private final Point end;
    private boolean selected;

    public Edge(Vertex from, Vertex to) {
        super();
        setName("Edge <%s -> %s>".formatted(from.getVertexLabel(), to.getVertexLabel()));
        this.start = from.getCenter();
        this.end = to.getCenter();
    }

    /**
     * switch an edge into selected stage and call parent (=GraphPanel's) repaint
     */
    public void select() {
        if (!selected) {
            selected = true;
            getParent().repaint();
        }
    }
    /**
     * switch an edge into unselected stage and call parent (=GraphPanel's) repaint
     */
    public void unselect() {
        if (selected) {
            selected = false;
            getParent().repaint();
        }
    }
}