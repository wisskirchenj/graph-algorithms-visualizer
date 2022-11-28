package de.cofinpro.visualizer.controller;

import de.cofinpro.visualizer.model.AlgorithmModel;
import de.cofinpro.visualizer.model.ApplicationModel;
import de.cofinpro.visualizer.model.ModelEdge;
import de.cofinpro.visualizer.view.GraphPanel;
import de.cofinpro.visualizer.view.Vertex;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Queue;

/**
 * base class for algorithm implementations who listen to the choice of a start vertex by mouse click.
 * The algorithm works on the GraphModel as part of the ApplicationModel, with the latter it communicates by callbacks.
 */
@Slf4j
public abstract class Algorithm extends MouseAdapter {

    @Getter
    private ApplicationModel applicationModel;
    private GraphPanel graphPanel;
    private String algorithmResult;
    @Getter
    private final Player player = new Player();

    public void setGraphPanel(GraphPanel graphPanel) {
        this.graphPanel = graphPanel;
    }

    public Algorithm setApplicationModel(ApplicationModel applicationModel) {
        this.applicationModel = applicationModel;
        return this;
    }

    @Override
    public void mouseClicked(MouseEvent event) {
        log.debug("Mouse clicked {}", event.getPoint());
        graphPanel.getVertexAt(event.getPoint()).ifPresent(vertex -> {
            log.debug("Vertex clicked {}", vertex.getVertexLabel());
            applicationModel.getGraphModel().unselect();
            applicationModel.switchAlgorithmState(AlgorithmModel.State.RUNNING);
            performAlgorithm(vertex);
        });
    }

    /**
     * abstract hook method that implements the actual algorithm.
     * @param vertex the start vertex chosen.
     */
    protected abstract void performAlgorithm(Vertex vertex);

    protected void propagateResult() {
        applicationModel.propagateAlgorithmResult(algorithmResult);
    }

    protected void setResult(String result) {
        algorithmResult = result;
    }

    public void stopPlaying() {
        if (player.playTimer != null) {
            player.playTimer.stop();
        }
    }

    /**
     * inner class that does the graphical playing of the traversal in slow motion using a swing timer
     */
    protected class Player implements ActionListener {

        private static final int ALGORITHM_PLAY_DELAY = 700; //millisecs
        private Timer playTimer;
        private Queue<ModelEdge> playList;

        protected void play(Queue<ModelEdge> traverseQueue) {
            playList = traverseQueue;
            playTimer = new Timer(ALGORITHM_PLAY_DELAY, this);
            playTimer.start();
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (playList.isEmpty()) {
                playTimer.stop();
                propagateResult();
            } else {
                applicationModel.getGraphModel().selectEdgeAndNeighborVertex(playList.remove());
            }
        }
    }
}