package pojo;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * A class representing the calculation of lowest weight paths in a graph.
 *
 * @param <T> the type of elements in the graph
 */
public class LowestWeightPaths<T>  implements Serializable {
    private static final @NotNull Long serialVersionUID = 1L;
    private @NotNull Queue<List<Node<T>>> queue;
    private @NotNull Map<Node<T>, Integer> pathAndWeightMap;
    @Nullable ExecutorService threadPool;
    Traversable<T> traversable;
    private volatile static LowestWeightPaths<?> instance;

    public static <T> LowestWeightPaths<T> getInstance() {
        if (instance == null) {
            synchronized (LowestWeightPaths.class){
                if (instance==null){
                    instance = new LowestWeightPaths<>();
                }
            }
        }
        return (LowestWeightPaths<T>) instance;
    }

    private LowestWeightPaths() {
        queue = new LinkedList<>();
        pathAndWeightMap = new HashMap<>();
    }

    /**
     * Finds the lowest weight paths from a source node to a destination node in a graph.
     *
     * @param i_Traversable the traversable graph
     * @param i_Source       the source node
     * @param i_Destination  the destination node
     * @return a set of lowest weight paths from the source to the destination
     */
    public @NotNull HashSet<List<T>> FindLowestWeightPaths(Traversable<T> i_Traversable, @NotNull T i_Source, @NotNull T i_Destination) {

        this.traversable = i_Traversable;
        Node<T> dest = new Node<>(i_Destination);
        Node<T> source = new Node<>(i_Source);
        HashSet<List<Node<T>>> lowestWeightPaths = new HashSet<>();
        int lowestWeight = Integer.MAX_VALUE;

        pathAndWeightMap.put(source, i_Traversable.getElementData(source.getData()));

        // Enqueue the initial path with the source node
        List<Node<T>> initialPath = new ArrayList<>();
        initialPath.add(source);
        queue.offer(initialPath);

        while (!queue.isEmpty()) {
            List<Node<T>> currentPath = queue.poll();
            Node<T> currentNode = currentPath.get(currentPath.size() - 1);

            if (currentNode.equals(dest)) {
                int currentPathWeight = calculatePathWeight(currentPath, i_Traversable);
                if (currentPathWeight < lowestWeight) {
                    // Found a new lightest weight
                    lowestWeight = currentPathWeight;
                    lowestWeightPaths.clear();
                    lowestWeightPaths.add(currentPath);
                }
                else if (currentPathWeight == lowestWeight) {
                    // Add the path to the set of lightest weight paths
                    lowestWeightPaths.add(currentPath);
                }
            }

            Collection<Node<T>> adjacentNodes = i_Traversable.getReachableNodes(currentNode);
            addAdjacentNodes( currentPath,currentNode,adjacentNodes);
        }

        return turnListToGenericType(lowestWeightPaths);
    }

    /**
     * Adds the adjacent nodes of the current node to the queue for further processing.
     *
     * @param i_CurrentPath   the current path being processed
     * @param i_CurrentNode  the current node
     * @param i_AdjacentNodes the collection of adjacent nodes
     */
    private void addAdjacentNodes(@NotNull List<Node<T>> i_CurrentPath, @NotNull Node<T> i_CurrentNode, Collection<Node<T>> i_AdjacentNodes)
    {
        threadPool = Executors.newFixedThreadPool(i_AdjacentNodes.size());
        for (Node<T> adjacentNode : i_AdjacentNodes) {
            threadPool.execute(()-> {
            // Calculate the distance to the adjacent node
            int distanceToAdjacent = pathAndWeightMap.get(i_CurrentNode) + traversable.getElementData(adjacentNode.getData());

            // Check if the distance is shorter than the current recorded distance
            if (distanceToAdjacent <= pathAndWeightMap.getOrDefault(adjacentNode, Integer.MAX_VALUE)) {
                // Update the distance
                pathAndWeightMap.put(adjacentNode, distanceToAdjacent);

                // Create a new path by appending the adjacent node
                List<Node<T>> newPath = new ArrayList<>(i_CurrentPath);
                newPath.add(adjacentNode);

                // Enqueue the new path
                queue.offer(newPath);
            }
            });
        }

        threadPool.shutdown();
        try {
            if (!threadPool.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS)) {
                // Timeout elapsed before all tasks completed
                System.err.println("Timeout elapsed before all tasks completed.");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("InterruptedException occurred while waiting for thread pool termination.");
        }

    }

    /**
     * Calculates the weight of a path in the graph.
     *
     * @param i_Path the path for which to calculate the weight
     * @param i_Traversable the traversable graph
     * @return the weight of the path
     */
    private int calculatePathWeight(List<Node<T>> i_Path, Traversable<T> i_Traversable) {
        int weight = i_Traversable.getElementData(i_Path.get(0).getData());
        for (int i = 0; i < i_Path.size() - 1; i++) {
            Node<T> nextNode = i_Path.get(i + 1);
            weight += i_Traversable.getElementData(nextNode.getData());
        }
        return weight;
    }

    /**
     * Converts a set of paths with Node elements to a set of paths with generic type elements.
     *
     * @param i_LowestWeightPaths the set of paths with Node elements
     * @return the set of paths with generic type elements
     */
    private HashSet<List<T>> turnListToGenericType( HashSet<List<Node<T>>> i_LowestWeightPaths)
    {
        HashSet<List<T>> specificTypePath = new HashSet<>();
        for(List<Node<T>> lowestWeightPath : i_LowestWeightPaths)
        {
            List<T> listOfShortPath = new ArrayList<>();

            for(Node<T> node : lowestWeightPath)
            {
                listOfShortPath.add(node.getData());
            }
            specificTypePath.add(listOfShortPath);
        }
        return specificTypePath;
    }

}
