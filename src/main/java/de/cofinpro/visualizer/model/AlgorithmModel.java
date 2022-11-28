package de.cofinpro.visualizer.model;

import lombok.Data;

import java.util.EnumMap;
import java.util.Map;

/**
 * state object to the algorithm implementation, that keeps track on the running algorithm's state and knows what to
 * display in the ResultLabel at each state (EnumMap) - also the algorithm result can be set.
 */
@Data
public class AlgorithmModel {

    private final Map<State, String> stateTexts = new EnumMap<>(State.class);

    private State state;
    private String displayText;

    public AlgorithmModel() {
        this.state = State.SELECT_VERTEX;
        stateTexts.put(State.SELECT_VERTEX, "Please choose a starting vertex");
        stateTexts.put(State.RUNNING, "Please wait...");
    }

    public enum State {
        SELECT_VERTEX,
        RUNNING,
        STOPPED,
        TERMINATED
    }

    /**
     * called by algorithm to transfer the result and signalize the termination.
     * @param resultText result of the algorithm
     */
    public void setResultText(String resultText) {
        stateTexts.put(State.TERMINATED, resultText);
    }

    public String getDisplayText() {
        return stateTexts.get(state);
    }
}