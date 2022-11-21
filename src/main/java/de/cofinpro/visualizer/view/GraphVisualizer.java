package de.cofinpro.visualizer.view;

import de.cofinpro.visualizer.controller.ModeMenuItemListener;
import de.cofinpro.visualizer.model.ApplicationModel;
import de.cofinpro.visualizer.model.Mode;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.util.Arrays;

import static de.cofinpro.visualizer.view.Vertex.*;

/**
 * JFrame based class representing the view of the GraphVisualizer
 */
public class GraphVisualizer extends JFrame {

    private static final Color PANEL_COLOR = Color.PINK.darker();
    private static final String TITLE = "Graph-Algorithms Visualizer";
    private static final int GRAPH_WIDTH = 800;
    private static final int GRAPH_HEIGHT = 600;
    private static final int HEIGHT_OFFSET = 72;

    private final ApplicationModel applicationModel = new ApplicationModel();

    public GraphVisualizer() {
        super(TITLE);
        setName(TITLE);
        setSize(GRAPH_WIDTH, GRAPH_HEIGHT);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        setLayout(new BorderLayout());
        setJMenuBar(createMenuBar());
        add(createStatusPanel(), BorderLayout.NORTH);
        add(createGraph(), BorderLayout.CENTER);
        setVisible(true);
    }

    private Component createStatusPanel() {
        var statusPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        statusPanel.setBackground(PANEL_COLOR);
        var statusLabel = new StatusLabel(Mode.START_MODE.getModeName());
        applicationModel.registerListener(statusLabel);
        statusPanel.add(statusLabel);
        return statusPanel;
    }

    /**
     * create the graph panel with vertices placed in all four corners
     */
    private Container createGraph() {
        var graph = new GraphPanel(applicationModel.getGraphModel());
        graph.setBackground(PANEL_COLOR);
        addCornerVertices(graph);
        applicationModel.registerListener(graph);
        return graph;
    }

    private JMenuBar createMenuBar() {
        var menubar = new JMenuBar();
        menubar.setName("MenuBar");
        menubar.add(createFileMenu());
        menubar.add(createModeMenu());
        return menubar;
    }

    private JMenu createFileMenu() {
        var fileMenu = new JMenu("File");
        fileMenu.setName("File");
        fileMenu.add(createMenuItem("New", e -> applicationModel.requestResetGraph()));
        fileMenu.add(createMenuItem("Exit",
                e -> dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING))));
        return fileMenu;
    }

    private JMenu createModeMenu() {
        var modeMenu = new JMenu("Mode");
        modeMenu.setName("Mode");
        modeMenu.add(createMenuItem(Mode.ADD_VERTEX));
        modeMenu.add(createMenuItem(Mode.ADD_EDGE));
        modeMenu.add(createMenuItem(Mode.REMOVE_VERTEX));
        modeMenu.add(createMenuItem(Mode.REMOVE_EDGE));
        modeMenu.add(createMenuItem(Mode.NONE));
        return modeMenu;
    }

    private JMenuItem createMenuItem(String name, ActionListener actionListener) {
        JMenuItem menuItem = new JMenuItem(name);
        menuItem.setName(name);
        menuItem.addActionListener(actionListener);
        return menuItem;
    }

    private JMenuItem createMenuItem(Mode mode) {
        JMenuItem menuItem = new JMenuItem(mode.getModeName());
        menuItem.setName(mode.getModeName());
        menuItem.addActionListener(new ModeMenuItemListener(mode, applicationModel));
        return menuItem;
    }

    private void addCornerVertices(GraphPanel graph) {
        Arrays.stream(Corner.values())
                .forEach(corner -> graph.addVertex(String.valueOf(corner.ordinal()),
                        switch (corner) {
                            case NW -> new Point(0, 0);
                            case NE -> new Point(GRAPH_WIDTH - getVERTEX_RADIUS(), 0);
                            case SW -> new Point(0,GRAPH_HEIGHT - getVERTEX_RADIUS() - HEIGHT_OFFSET);
                            case SE -> new Point(GRAPH_WIDTH - getVERTEX_RADIUS(),
                                    GRAPH_HEIGHT - getVERTEX_RADIUS() - HEIGHT_OFFSET);
                        }));
    }
}