package pojo;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A class representing a Breadth-First Search (BFS) algorithm.
 *
 * @param <T> the type of elements in the BFS algorithm
 */
public class Bfs<T> implements Serializable {
    private static final @NotNull Long serialVersionUID = 1L;
    @NotNull
    Queue<List<T>> queue;
    @Nullable
    ExecutorService threadPool;
    private volatile static Bfs<?> instance;

    private Bfs(){
         queue = new LinkedList<>();
    }

    public static @NotNull <T> Bfs<T> getInstance() {
        if (instance == null) {
            synchronized (Bfs.class){
                if (instance==null){
                    instance = new Bfs<>();
                }
            }
        }
        return (Bfs<T>) instance;
    }

    /**
     * Finds the shortest paths from a graph source to a destination using the BFS algorithm.
     *
     * @param i_Traversable the traversable graph
     * @param i_GraphSource the source node of the graph
     * @param i_Destination the destination node
     * @return a set of shortest paths from the source to the destination
     */

    public HashSet<List<T>> findShortestPaths(@NotNull Traversable<T> i_Traversable, @NotNull T i_GraphSource, @NotNull T i_Destination) {
        HashSet<List<T>> shortestPaths = new HashSet<>();
        int sizeOfShortestPath = -1;
        List<T> initialPath = new ArrayList<>();
        initialPath.add(i_GraphSource);
        queue.offer(initialPath);

        while (!queue.isEmpty()) {
            List<T> currentPath = queue.poll();
            T lastNode = currentPath.get(currentPath.size() - 1);

            if (lastNode.equals(i_Destination)) {
                // Found the shortest path to the destination
                if(sizeOfShortestPath == -1 ) {sizeOfShortestPath= currentPath.size();}
                else if(currentPath.size() > sizeOfShortestPath) { break; }

                shortestPaths.add(currentPath);
            }

            Collection<Node<T>> adjacentNodes = i_Traversable.getReachableNodes(new Node<>(lastNode));
            //threadPool = Executors.newFixedThreadPool(adjacentNodes.size());
            checkAdjacentNodes(adjacentNodes, currentPath);

        }

        return shortestPaths;
    }

    /**
     * Checks the adjacent nodes of a given node and enqueues the new paths to the queue.
     *
     * @param i_AdjacentNodes the collection of adjacent nodes
     * @param i_CurrentPath the current path being processed
     */
    private void checkAdjacentNodes(Collection<Node<T>> i_AdjacentNodes, @NotNull List<T> i_CurrentPath)
    {
        threadPool = Executors.newFixedThreadPool(i_AdjacentNodes.size());
        for (Node<T> adjacentNode : i_AdjacentNodes) {
            threadPool.execute(()-> {
                // Check if the adjacent node is already visited in the current path
                if (!i_CurrentPath.contains(adjacentNode.getData())) {
                    // Create a new path by appending the adjacent node
                    List<T> newPath = new ArrayList<>(i_CurrentPath);
                    newPath.add(adjacentNode.getData());
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

}
