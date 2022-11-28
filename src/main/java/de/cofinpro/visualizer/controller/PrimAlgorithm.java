package de.cofinpro.visualizer.controller;

import de.cofinpro.visualizer.model.ModelEdge;
import de.cofinpro.visualizer.model.ModelVertex;
import de.cofinpro.visualizer.view.Vertex;

import java.util.ArrayDeque;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * actual implementation of the Prim Algorithm to find a minimum spanning tree.
 */
public class PrimAlgorithm extends Algorithm {

    private final Set<ModelEdge> currentEdges = new HashSet<>();
    private final Set<ModelVertex> connected = new HashSet<>();
    private final Queue<ModelEdge> spanningTree = new ArrayDeque<>();

    /**
     * clear the collections - needed if the algorithm has already run (on another start vertex).
     */
    private void resetCollections() {
        currentEdges.clear();
        connected.clear();
        spanningTree.clear();
    }

    /**
     * callback of abstract algorithm class, that runs Prim's algorithm, sets result string and plays its spanning tree.
     * @param vertex the start vertex chosen.
     */
    @Override
    protected void performAlgorithm(Vertex vertex) {
        resetCollections();
        setResult(prim(vertex));
        getPlayer().play(spanningTree);
    }

    private String prim(Vertex vertex) {
        getApplicationModel().getGraphModel().selectVertex(vertex);
        var startVertex = getApplicationModel().getGraphModel().getModelVertex(vertex);
        connect(startVertex);
        while (!currentEdges.isEmpty()) {
            var nextEdgeToConnect = findMinimalWeightEdge();
            spanningTree.offer(nextEdgeToConnect);
            connect(nextEdgeToConnect.end());
        }
        return spanningTree.stream()
                .map(edge -> "%s=%s".formatted(edge.end().getVertex().getVertexLabel(),
                        edge.start().getVertex().getVertexLabel()))
                .sorted().collect(Collectors.joining(", "));
    }

    /**
     * find  and return one edge of minimal weight under the edges currently in the set to proceed with.
     */
    private ModelEdge findMinimalWeightEdge() {
        return currentEdges.stream()
                .min(Comparator.comparingInt(edge -> Integer.parseInt(edge.weightLabel().getText())))
                .orElseThrow(); // never throws, because we only get here, if set is non-empty...
    }

    /**
     * connect given vertex to the growing spanning tree, cleanup all edges in the currentEdges set, that lead to this
     * vertex (we can do that, since the connecting edge was already offered to the spanningTree queue) and
     * add all new edges from this vertex to the currentEdges set, that do not point to an already connected vertex.
     */
    private void connect(ModelVertex vertex) {
        connected.add(vertex);
        currentEdges.stream().filter(edge -> edge.end() == vertex).toList().forEach(currentEdges::remove);
        currentEdges.addAll(vertex.getEdges().stream()
                .filter(edge -> !connected.contains(edge.end())).toList());
    }
}