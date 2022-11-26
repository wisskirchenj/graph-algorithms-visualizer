package de.cofinpro.visualizer.controller;

import de.cofinpro.visualizer.model.TreeEdge;
import de.cofinpro.visualizer.model.TreeVertex;
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
            result += " -> " + traverseQueue.stream().map(TreeEdge::neighborVertex)
                    .map(TreeVertex::getVertex).map(Vertex::getVertexLabel).collect(Collectors.joining(" -> "));
        }
        setResult(result);
        getPlayer().play(traverseQueue);
    }
}
