package de.cofinpro.visualizer.view;

import de.cofinpro.visualizer.controller.Algorithm;
import de.cofinpro.visualizer.controller.GraphClickListener;
import de.cofinpro.visualizer.controller.ApplicationModelListener;
import de.cofinpro.visualizer.model.AlgorithmModel;
import de.cofinpro.visualizer.model.GraphModel;
import de.cofinpro.visualizer.model.Mode;

import lombok.Getter;

import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.BasicStroke;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Line2D;
import java.util.Optional;

/**
 * JPanel derived Container class for vertices and edges etc. - e.g. representing the graph.
 */
public class GraphPanel extends JPanel implements ApplicationModelListener {

    private final GraphModel model;
    private Vertex selected = null;
    private transient Algorithm runningAlgorithm = null;
    @Getter
    private Mode mode = Mode.START_MODE;

    public GraphPanel(GraphModel model) {
        super(null);
        this.model = model;
        setName("Graph");
        addMouseListener(new GraphClickListener(this));
    }

    /**
     * ApplicationModelListener method, that is called on user changing the Mode in the menu. The mode is stored for use
     * in the MouseAdapter and a possible selection of a vertex is undone.
     */
    @Override
    public void updateMode(Mode mode) {
        this.mode = mode;
        model.unselect();
        selected = null;
        if (mode == Mode.RESET_MODE) {
            this.mode = Mode.START_MODE;
            model.clear();
            removeAll();
            repaint();
        }
    }

    /**
     * store the running algorithm and add it as mause listener (to choose a start vertex).
     */
    @Override
    public void updateAlgorithm(Algorithm algorithm) {
        this.runningAlgorithm = algorithm;
        algorithm.setGraphPanel(this);
        addMouseListener(algorithm);
    }

    /**
     * listen to stop-algorithm''s in which case the running algorithm is removed as mouse listener.
     */
    @Override
    public void updateAlgorithmState(AlgorithmModel algorithmModel) {
        if (algorithmModel.getState() == AlgorithmModel.State.STOPPED) {
            removeMouseListener(runningAlgorithm);
            runningAlgorithm.stopPlaying();
            runningAlgorithm = null;
        }
    }

    /**
     * mark the given vertex as selected or remove selection, if null is passed.
     */
    public void setSelected(Vertex vertex) {
        selected = vertex;
        if (vertex != null) {
            model.selectVertex(vertex);
        }
    }

    /**
     * return Optional with selected vertex if any - or Optional.empty() if none is selected.
     */
    public Optional<Vertex> getSelected() {
        return Optional.ofNullable(selected);
    }

    /**
     * find and return the vertex at the given point - or Optional.empty(), if the point is not inside a vertex circle.
     */
    public Optional<Vertex> getVertexAt(Point point) {
        return Optional.ofNullable(getComponentAt(point) instanceof Vertex v && v.isInside(point) ? v : null);
    }

    /**
     * dynamically add a vertex with given label and location to the graph.
     */
    public void addVertex(String label, Point location) {
        var vertex = new Vertex(label, getBackground());
        vertex.setLocation(location);
        model.addVertex(vertex);
        add(vertex);
        revalidate();
        repaint();
    }

    /**
     * add edges between the vertices given (in both directions), and position a weight label at the edge.
     */
    public void addEdge(String weight, Vertex first, Vertex second) {
        var edge = new Edge(first, second);
        edge.setBounds(new Rectangle(new Point((first.getCenter().x + second.getCenter().x) / 2 - 10,
                (first.getCenter().y + second.getCenter().y) / 2 - 10), new Dimension(20, 20)));
        add(edge);
        var reversedEdge = new Edge(second, first);
        add(reversedEdge);
        var weightLabel = createWeightLabel(weight, first, second);
        add(weightLabel);
        model.addEdge(edge, reversedEdge, weightLabel);
        revalidate();
        repaint();
    }

    /**
     * create an appropriately named edge weight label with given weight and place it near the midpoint of the associated edge.
     */
    private JLabel createWeightLabel(String weight, Vertex first, Vertex second) {
        var position = getLabelPosition(first.getCenter(), second.getCenter());
        var label = new JLabel(weight);
        label.setName("EdgeLabel <%s -> %s>".formatted(first.getVertexLabel(), second.getVertexLabel()));
        label.setForeground(Vertex.getVERTEX_COLOR());
        int fontSize = Vertex.getVERTEX_DIAMETER() * 2 / 5;
        label.setFont(new Font("Arial", Font.BOLD, fontSize));
        label.setBounds(position.x, position.y, fontSize * 3 / 2, fontSize * 3 / 2);
        return label;
    }

    /**
     * clever placement calculation of the edge label in a way, that it is appropriately scaled
     * and not struck through by the edge line.
     */
    private static Point getLabelPosition(Point first, Point second) {
        var midPoint = new Point((first.x + second.x) / 2, (first.y + second.y) / 2);
        Point position;
        int del = Vertex.getVERTEX_DIAMETER() / 10;
        if ((second.y - first.y) * (second.x - first.x) < 0) {
            position = new Point(midPoint.x + del, midPoint.y + del);
        } else {
            position = new Point(midPoint.x - 3 * del, midPoint.y + del);
        }
        return position;
    }

    /**
     * delegate remove request to the model, which returns all the swing components, the panel must remove.
     */
    public void removeVertexWithAssociateEdges(Vertex vertex) {
        var componentsToRemove = model.removeVertexWithEdges(vertex);
        componentsToRemove.forEach(this::remove);
        repaint();
    }

    /**
     * delegate remove request to the model, which returns all the swing components, the panel must remove.
     */
    public void removeEdge(Edge edge) {
        var componentsToRemove = model.removeEdge(edge);
        componentsToRemove.forEach(this::remove);
        repaint();
    }

    /**
     * overriden to paint the edges as thick lines using BasicStroke - before (and thus underneath) the vertices and labels.
     */
    @Override
    public void paintChildren(Graphics g) {
        Graphics2D g2D = (Graphics2D) g;
        g2D.setStroke(new BasicStroke(Vertex.getVERTEX_DIAMETER() / 10f));
        model.getEdges().forEach(e -> {
            g2D.setColor(e.isSelected() ? Vertex.getVERTEX_SELECTED_COLOR() : Vertex.getVERTEX_COLOR());
            g2D.draw(new Line2D.Float(e.getStart(), e.getEnd()));
        });
        super.paintChildren(g2D);
    }
}