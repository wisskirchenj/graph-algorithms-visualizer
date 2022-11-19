package de.cofinpro.visualizer.model;

import de.cofinpro.visualizer.controller.GraphModelListener;
import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * GraphModel state class, that (currently only) stores the selected Mode. It keeps a list of registered listeners,
 * which are notified on changes.
 */
public class GraphModel implements Serializable {

    @Serial
    private static final long serialVersionUID = 2405172041950251807L;

    @Getter
    private Mode mode = Mode.START_MODE;
    private final transient List<GraphModelListener> listeners = new ArrayList<>();

    public void registerListener(GraphModelListener listener) {
        listeners.add(listener);
    }

    public void notifyListeners() {
        listeners.forEach(listener -> listener.update(this));
    }

    public void setMode(Mode mode) {
        this.mode = mode;
        notifyListeners();
    }
}
