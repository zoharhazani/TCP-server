package pojo;

import java.util.Collection;

/**
 * This interface defines the common functionality required of all concrete graphs
 * @param <T> the type of elements in the graph
 */
public interface Traversable<T> {

    /**
     * Sets the root element of the graph.
     *
     * @param element the root element
     */
    public abstract void setRoot(T element);

    /**
     * Gets the root node of the graph.
     *
     * @return the root node
     */
    public abstract Node<T> getRoot();

    /**
     * Gets the collection of reachable nodes from a given node in the graph.
     *
     * @param aNode the node to get reachable nodes from
     * @return the collection of reachable nodes
     */
    public abstract Collection<Node<T>> getReachableNodes(Node<T> aNode);

    /**
     * Gets the data of the element in the graph.
     *
     * @param element the element to get data for
     * @return the data associated with the element
     */
    public abstract int getElementData(T element);
}
