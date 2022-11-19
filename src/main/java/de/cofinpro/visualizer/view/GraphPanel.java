package de.cofinpro.visualizer.view;

import de.cofinpro.visualizer.controller.GraphClickListener;
import de.cofinpro.visualizer.controller.GraphModelListener;
import de.cofinpro.visualizer.model.GraphModel;
import de.cofinpro.visualizer.model.Mode;

import lombok.Getter;

import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.BasicStroke;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.geom.Line2D;
import java.util.Arrays;
import java.util.Optional;

/**
 * JPanel derived Container class for vertices and edges etc. - e.g. representing the graph.
 */
public class GraphPanel extends JPanel implements GraphModelListener {

    private Vertex selected = null;
    @Getter
    private Mode mode = Mode.START_MODE;

    public GraphPanel(LayoutManager layout) {
        super(layout);
        setName("Graph");
        addMouseListener(new GraphClickListener(this));
    }

    /**
     * dynamically add a vertex with given label and location to the graph.
     */
    public void addVertex(String label, Point location) {
        var vertex = new Vertex(label, getBackground());
        vertex.setLocation(location);
        add(vertex);
        revalidate();
        repaint();
    }

    /**
     * find and return the vertex at the given point - or Optional.empty(), if the point is not inside a vertex circle.
     */
    public Optional<Vertex> getVertexAt(Point point) {
        return Optional.ofNullable(getComponentAt(point) instanceof Vertex v && v.isInside(point) ? v : null);
    }

    /**
     * GraphModelListener method, that is called on user changing the Mode in the menu. The mode is stored for use
     * in the MouseAdapter and a possible selection of a vertex is undone.
     */
    @Override
    public void update(GraphModel model) {
        mode = model.getMode();
        getSelected().ifPresent(Vertex::unselect);
        selected = null;
    }

    /**
     * mark the given vertex as selected or remove selection, if null is passed.
     */
    public void setSelected(Vertex vertex) {
        selected = vertex;
        if (vertex != null) {
            vertex.select();
        }
    }

    /**
     * return Optional with selected vertex if any - or Optional.empty() if none is selected.
     */
    public Optional<Vertex> getSelected() {
        return Optional.ofNullable(selected);
    }

    /**
     * add edges between the vertices given (in both directions), and position a weight label at the edge.
     */
    public void addEdge(String weight, Vertex first, Vertex second) {
        add(new Edge(first, second));
        add(new Edge(second, first));
        addWeightLabel(weight, first, second);
        revalidate();
        repaint();
    }

    /**
     * create an appropriately named edge weight label with given weight and place & add it to the GraphPanel near the
     * midpoint of the associated edge.
     */
    private void addWeightLabel(String weight, Vertex first, Vertex second) {
        var position = getLabelPosition(first.getCenter(), second.getCenter());
        var label = new JLabel(weight);
        label.setName("EdgeLabel <%s -> %s>".formatted(first.getVertexLabel(), second.getVertexLabel()));
        label.setForeground(Vertex.getVERTEX_COLOR());
        int fontSize = Vertex.getVERTEX_RADIUS() * 2 / 5;
        label.setFont(new Font("Arial", Font.BOLD, fontSize));
        label.setBounds(position.x, position.y, fontSize, fontSize);
        add(label);
    }

    /**
     * clever placement calculation of the edge label in a way, that it is appropriately scaled
     * and not struck through by the edge line.
     */
    private static Point getLabelPosition(Point first, Point second) {
        var midPoint = new Point((first.x + second.x) / 2, (first.y + second.y) / 2);
        Point position;
        int del = Vertex.getVERTEX_RADIUS() / 10;
        if ((second.y - first.y) * (second.x - first.x) < 0) {
            position = new Point(midPoint.x + del, midPoint.y + del);
        } else {
            position = new Point(midPoint.x - 3 * del, midPoint.y + del);
        }
        return position;
    }

    /**
     * overriden to paint the edges as thick lines using BasicStroke - before (and thus underneath) the vertices and labels.
     */
    @Override
    public void paintChildren(Graphics g) {
        Graphics2D g2D = (Graphics2D) g;
        g2D.setColor(Vertex.getVERTEX_COLOR());
        g2D.setStroke(new BasicStroke(Vertex.getVERTEX_RADIUS() / 10f));
        Arrays.stream(getComponents()).filter(Edge.class::isInstance).forEach(e -> {
            Edge edge = (Edge) e;
            g2D.draw(new Line2D.Float(edge.getStart(), edge.getEnd()));
        });
        super.paintChildren(g2D);
    }
}
