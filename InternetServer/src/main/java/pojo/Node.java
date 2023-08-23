package pojo;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.io.Serializable;
import java.util.Objects;

/**
 * This class represents a node in a traversable (graph/tree/matrix) structure.
 *
 * @param <T> the type of data stored in the node
 */
public class Node<T> implements Serializable {
    private static final @NotNull Long serialVersionUID = 1L;
    private @NotNull T data;

    /**
     * Constructs a new instance of the Node class with the given data.
     *
     * @param data the data stored in the node
     */
    public Node(T data){
        this.data = data;
    }

    /**
     * Gets the data stored in the node.
     *
     * @return the data stored in the node
     */
    public T getData() {
        return data;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Node)) return false;
        Node<?> state1 = (Node<?>) o;
        return Objects.equals(data, state1.data);
    }

    @Override
    public int hashCode() {
        return data != null ? data.hashCode():0;
    }
}

