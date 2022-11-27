package de.cofinpro.visualizer.controller;

import de.cofinpro.visualizer.model.ModelEdge;
import de.cofinpro.visualizer.model.ModelVertex;
import de.cofinpro.visualizer.view.Vertex;

import java.util.*;
import java.util.stream.Collectors;

/**
 * actual implementation of the Dijkstra Algorithm using a priority queue.
 */
public class DijkstraAlgorithm extends Algorithm {

    private final PriorityQueue<VertexWithRouteLength> vertexQueue
            = new PriorityQueue<>(Comparator.comparingInt(VertexWithRouteLength::routeLength));
    private final Set<ModelVertex> processed = new HashSet<>();
    private final Map<ModelVertex, Integer> routeLengths = new HashMap<>();

    /**
     * clear the collections - needed if the algorithm has already run (on another start vertex).
     */
    private void resetCollections() {
        vertexQueue.clear();
        processed.clear();
        routeLengths.clear();
    }

    /**
     * callback of abstract algorithm class, that runs the Dijkstra, sets result string and plays it to status label.
     * @param vertex the start vertex chosen.
     */
    @Override
    protected void performAlgorithm(Vertex vertex) {
        resetCollections();
        setResult(dijkstra(vertex));
        getPlayer().play(new ArrayDeque<>());
    }

    /**
     * implementation of dijkstra algorithm, that calculates shortest routes to all vertixes from start vertex given
     * @param vertex start vertex
     * @return result string consisting of comma-separated "vertex=routeLength" pairs.
     */
    private String dijkstra(Vertex vertex) {
        var startVertex = getApplicationModel().getGraphModel().getModelVertex(vertex);
        vertexQueue.offer(new VertexWithRouteLength(startVertex, 0));
        routeLengths.put(startVertex, 0);
        while (!vertexQueue.isEmpty()) {
            var currentVertex = vertexQueue.poll().vertex();
            processUnprocessedNeighbors(currentVertex);
            processed.add(currentVertex);
        }
        return routeLengths.entrySet().stream().filter(entry -> entry.getValue() > 0)
                .map(entry -> "%s=%d".formatted(entry.getKey().getVertex().getVertexLabel(), entry.getValue()))
                .sorted()
                .collect(Collectors.joining(", "));
    }

    /**
     * calculate routeLengths on all unprocessed neighbors of this vertex by adding the edge weights to them.
     */
    private void processUnprocessedNeighbors(ModelVertex vertex) {
        vertex.getEdges().stream()
                .filter(edge -> !processed.contains(edge.neighborVertex()))
                .forEach(edge -> dijkstraUpdateNeighbor(vertex, edge, Integer.parseInt(edge.weightLabel().getText())));
    }

    /**
     * update a neighbor iff the newly calculated route length is the smallest so far. In this case, the routeLength
     * is stored in the results hashmap routeLengths as well as offered to the priority queue to be used from now on.
     */
    private void dijkstraUpdateNeighbor(ModelVertex vertex, ModelEdge edgeToNeighbor, int edgeWeight) {
        int lengthOnThisRoute = routeLengths.get(vertex) + edgeWeight;
        var neighbor = edgeToNeighbor.neighborVertex();
        int lengthSoFar = Optional.ofNullable(routeLengths.get(neighbor)).orElse(Integer.MAX_VALUE);
        if (lengthOnThisRoute < lengthSoFar) {
            vertexQueue.offer(new VertexWithRouteLength(neighbor, lengthOnThisRoute));
            routeLengths.put(neighbor, lengthOnThisRoute);
        }
    }

    /**
     * immutable union of a vertex and its currently minimal routeLength from start vertex for use in the priority queue
     */
    private record VertexWithRouteLength(ModelVertex vertex, int routeLength) {
    }
}