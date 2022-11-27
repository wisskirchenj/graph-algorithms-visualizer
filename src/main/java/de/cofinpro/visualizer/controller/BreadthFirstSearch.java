package de.cofinpro.visualizer.controller;

import de.cofinpro.visualizer.model.ModelEdge;
import de.cofinpro.visualizer.model.ModelVertex;
import de.cofinpro.visualizer.view.Vertex;

import java.util.stream.Collectors;

/**
 * actual implementation of the BFS search. The traverseList is received by delegating to the graph model.
 */
public class BreadthFirstSearch extends Algorithm {

    @Override
    protected void performAlgorithm(Vertex vertex) {
        var traverseQueue = getApplicationModel().getGraphModel().breadthFirstSearch(vertex);
        String result = "BFS : " + vertex.getVertexLabel();
        if (!traverseQueue.isEmpty()) {
            result += " -> " + traverseQueue.stream().map(ModelEdge::neighborVertex)
                    .map(ModelVertex::getVertex).map(Vertex::getVertexLabel).collect(Collectors.joining(" -> "));
        }
        setResult(result);
        getPlayer().play(traverseQueue);
    }
}
