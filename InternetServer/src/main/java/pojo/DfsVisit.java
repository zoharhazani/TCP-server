package pojo;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * A class representing a Depth-First Search (DFS) algorithm.
 *
 * @param <T> the type of elements in the traversable graph used in the DFS algorithm
 */
public class DfsVisit<T> implements Serializable {
    private static final @NotNull Long serialVersionUID = 1L;
    private @NotNull Stack<Node<T>> workingStack;
    private @NotNull Set<Node<T>> finished;
    @Nullable ExecutorService threadPool;
    private volatile static DfsVisit<?> instance;

    public static @NotNull <T> DfsVisit<T> getInstance() {
        if (instance == null) {
            synchronized (DfsVisit.class){
                if (instance==null){
                    instance = new DfsVisit<>();
                }
            }
        }
        return (DfsVisit<T>) instance;
    }

    private DfsVisit(){
        workingStack = new Stack<>();
        finished = new LinkedHashSet<>();
    }

    /**
     * Traverses around the connected components of the root using the DFS algorithm.
     *
     * @param aTraversable the traversable graph
     * @return  a set of connected elements.
     */
    public @NotNull Set<T> traverse(Traversable<T> aTraversable){
        workingStack.push(aTraversable.getRoot());
        while (!workingStack.empty()){
            Node<T> removed = workingStack.pop();
            finished.add(removed);
             Collection<Node<T>> reachableNodes = aTraversable.getReachableNodes(removed);
            threadPool = Executors.newFixedThreadPool(reachableNodes.size());
             for(Node<T> reachableNode :reachableNodes){
                 threadPool.execute(()-> {
                 if (!finished.contains(reachableNode) &&
                 !workingStack.contains(reachableNode)){
                     workingStack.push(reachableNode);
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

        Set<T> blackSet = new LinkedHashSet<>();
        for (Node<T> node: finished)
            blackSet.add(node.getData());
        finished.clear();
        return blackSet;
    }

    /**
     * Traverses all over a graph in order to find all the connected components of it.
     *
     * @param i_Traversable the traversable graph
     * @param i_ElementsList the list of elements to traverse
     * @return a set of all the connected components
     */
    public @NotNull HashSet<Set<T>> traverseAllOver(@NotNull Traversable<T> i_Traversable, List<T> i_ElementsList) {

        HashSet<Set<T>> allConnectedComponents = new HashSet<>();
        while(!i_ElementsList.isEmpty())
        {
            Set<T> connectedComponent = this.traverse(i_Traversable);
            allConnectedComponents.add(connectedComponent);
            i_ElementsList = removeElements(i_ElementsList,connectedComponent);
            if(i_ElementsList.isEmpty())
            {
                break;
            }
            i_Traversable.setRoot(i_ElementsList.get(0));
        }

        return allConnectedComponents;

    }

    /**
     * Removes elements from a list based on a set of elements to be removed.
     *
     * @param i_L1 the original list
     * @param i_L2 the set of elements to be removed
     * @return a list equals to l1 without the elements of l2
     */
    public @NotNull List<T> removeElements(@NotNull List<T> i_L1, Set<T> i_L2) {
        List<T> result = new ArrayList<>(i_L1);  // Create a copy of l1

        for (T element : i_L2) {
            result.remove(element);  // Remove elements from result that match the indexes in l2
        }
        return result;
    }

}
