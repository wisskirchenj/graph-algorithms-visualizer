package de.cofinpro.visualizer.controller;

import de.cofinpro.visualizer.model.ModelEdge;
import de.cofinpro.visualizer.model.ModelVertex;
import de.cofinpro.visualizer.view.Vertex;

import java.util.stream.Collectors;

/**
 * actual implementation of the DFS search. The traverseList is received by delegating to the graph model.
 */
public class DepthFirstSearch extends Algorithm {

    @Override
    protected void performAlgorithm(Vertex vertex) {
        var traverseQueue = getApplicationModel().getGraphModel().depthFirstSearch(vertex);
        String result = "DFS : " + vertex.getVertexLabel();
        if (!traverseQueue.isEmpty()) {
            result += " -> " + traverseQueue.stream().map(ModelEdge::end)
                    .map(ModelVertex::getVertex).map(Vertex::getVertexLabel).collect(Collectors.joining(" -> "));
        }
        setResult(result);
        getPlayer().play(traverseQueue);
    }
}