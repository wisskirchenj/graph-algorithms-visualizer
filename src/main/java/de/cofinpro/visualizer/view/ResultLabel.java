package de.cofinpro.visualizer.view;

import de.cofinpro.visualizer.controller.ApplicationModelListener;
import de.cofinpro.visualizer.model.AlgorithmModel;

import javax.swing.JLabel;
import java.awt.Color;
import java.awt.Font;

/**
 * JLabel derived class that displays the algorithm-state specific texts and the result on algorithm terminate.
 */
public class ResultLabel extends JLabel implements ApplicationModelListener {

    private static final Color FG_COLOR = Color.BLACK;

    public ResultLabel(String text) {
        super(text);
        setName("Display");
        setFont(new Font("Arial", Font.PLAIN, 18));
        setForeground(FG_COLOR);
    }

    /**
     * listen to algorithm state changes and change panel color if algorithm is started or terminated.
     * @param algorithmModel the algorithm model transporting state and display text.
     */
    @Override
    public void updateAlgorithmState(AlgorithmModel algorithmModel) {
        switch (algorithmModel.getState()) {
            case SELECT_VERTEX -> getParent().setBackground(Color.LIGHT_GRAY);
            case STOPPED -> getParent().setBackground(GraphVisualizer.PANEL_COLOR);
            default -> {/* no action */}
        }
        setText(algorithmModel.getDisplayText());
    }
}