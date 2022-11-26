package de.cofinpro.visualizer.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * enum to store the possible Mode-values as selectable in the GraphVisualizer's Mode Menu
 */
@AllArgsConstructor
@Getter
public enum Mode {
    NONE("None"),
    RESET_MODE("Add a Vertex"),
    ADD_VERTEX("Add a Vertex"),
    ADD_EDGE("Add an Edge"),
    REMOVE_VERTEX("Remove a Vertex"),
    REMOVE_EDGE("Remove an Edge");

    private final String modeName;

    public static final Mode START_MODE = Mode.ADD_VERTEX;
}