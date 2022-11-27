package de.cofinpro.visualizer.model;

import de.cofinpro.visualizer.controller.Algorithm;
import de.cofinpro.visualizer.controller.ApplicationModelListener;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static de.cofinpro.visualizer.model.AlgorithmModel.State.STOPPED;

/**
 * ApplicationModel state class, that stores the GraphModel and selected Mode. It keeps a list of registered listeners,
 * which are notified on changes.
 */
@Slf4j
public class ApplicationModel implements Serializable {

    @Serial
    private static final long serialVersionUID = 2405172041950251807L;

    @Getter
    private final GraphModel graphModel = new GraphModel();
    private transient AlgorithmModel algorithmModel;

    private final transient List<ApplicationModelListener> listeners = new ArrayList<>();

    public void registerListener(ApplicationModelListener listener) {
        listeners.add(listener);
    }

    private void notifyModeUpdate(Mode mode) {
        listeners.forEach(listener -> listener.updateMode(mode));
    }

    private void notifyAlgorithmUpdate(Algorithm algorithm) {
        listeners.forEach(listener -> listener.updateAlgorithm(algorithm));
    }

    private void notifyAlgorithmStateUpdate(AlgorithmModel algorithmModel) {
        listeners.forEach(listener -> listener.updateAlgorithmState(algorithmModel));
    }

    /**
     * on mode change, a possibly running algorithm is stopped.
     * @param mode the changed mode selected.
     */
    public void setMode(Mode mode) {
        if (algorithmIsRunning()) {
            switchAlgorithmState(STOPPED);
        }
        log.debug("setting mode to {}.", mode.getModeName());
        notifyModeUpdate(mode);
    }

    private boolean algorithmIsRunning() {
        return algorithmModel != null && algorithmModel.getState() != STOPPED;
    }

    public void switchAlgorithmState(AlgorithmModel.State newState) {
        algorithmModel.setState(newState);
        log.debug("switching algorithm state to {}.", newState.name());
        notifyAlgorithmStateUpdate(algorithmModel);
    }

    /**
     * callback used by algorithm to propagate a result string after termination.
     */
    public void propagateAlgorithmResult(String result) {
        algorithmModel.setResultText(result);
        switchAlgorithmState(AlgorithmModel.State.TERMINATED);
    }

    /**
     * create and start the algorithm of given type and notify listeners.
     * @param algorithmType the type of algorithm to start.
     */
    public void startAlgorithm(AlgorithmType algorithmType) {
        setMode(Mode.NONE);
        notifyAlgorithmUpdate(algorithmType.getAlgorithmProducer().get().setApplicationModel(this));
        algorithmModel = new AlgorithmModel();
        log.debug("starting algorithm {}.", algorithmType.getAlgorithmName());
        notifyAlgorithmStateUpdate(algorithmModel);
    }

    /**
     * request a full graph and graph model reset (as triggered by File->New)
     */
    public void requestResetGraph() {
        setMode(Mode.RESET_MODE);
    }
}