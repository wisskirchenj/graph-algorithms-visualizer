package de.cofinpro.visualizer.model;

import de.cofinpro.visualizer.controller.ApplicationModelListener;
import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * ApplicationModel state class, that stores the GraphModel and selected Mode. It keeps a list of registered listeners,
 * which are notified on changes.
 */
public class ApplicationModel implements Serializable {

    @Serial
    private static final long serialVersionUID = 2405172041950251807L;

    @Getter
    private final GraphModel graphModel = new GraphModel();
    @Getter
    private Mode mode = Mode.START_MODE;
    @Getter
    private boolean newGraphRequested = false;
    private final transient List<ApplicationModelListener> listeners = new ArrayList<>();

    public void registerListener(ApplicationModelListener listener) {
        listeners.add(listener);
    }

    private void notifyListeners() {
        listeners.forEach(listener -> listener.update(this));
    }

    public void setMode(Mode mode) {
        this.mode = mode;
        notifyListeners();
    }

    public void requestResetGraph() {
        mode = Mode.START_MODE;
        newGraphRequested = true;
        notifyListeners();
        newGraphRequested = false;
    }
}