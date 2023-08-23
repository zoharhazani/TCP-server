package pojo;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * This class uses the adapter pattern, also known as
 * wrapper/decorator.
 * It adapts a Matrix to the functionality of Graph Interface
 */
public abstract class TraversableMatrix implements Traversable<Index>, Serializable {
    private static final @NotNull Long serialVersionUID = 1L;
    private final @NotNull Matrix innerMatrix;
    private @NotNull Index source;

    /**
     * Constructs a new instance of the TraversableMatrix class with the given matrix.
     *
     * @param matrix the matrix to be traversed
     */
    public TraversableMatrix(@NotNull Matrix matrix){
        this.innerMatrix = matrix;
        source = new Index(0,0);
        setRoot(source);
    }

    /**
     * Gets the inner matrix.
     *
     * @return the inner matrix
     */
    public @NotNull Matrix getInnerMatrix() {
        return innerMatrix;
    }

    /**
     * Checks if the given number is valid data for the specific matrix type (weighted or basic(0/1)).
     *
     * @param aNumber the number to be checked
     * @return true if the number is valid data, false otherwise
     */
    public abstract boolean correctData(int aNumber);
    @Override
    public void setRoot(@NotNull Index source){
        if((source.row >=0 && source.row<innerMatrix.primitiveMatrix.length)
        && (source.column >=0 &&
                source.column < innerMatrix.primitiveMatrix[0].length)){
            this.source = source;
        }

    }

    /**
     * Gets the data of the specified element in the matrix.
     *
     * @param i_Element the element in the matrix which is index.
     * @return the data of the element(index)
     */
    public int getElementData(@NotNull Index i_Element)
    {
        return innerMatrix.getValue(i_Element);
    }

    @Override
    public @NotNull Node<Index> getRoot()
    {
        return new Node<>(source);
    }

    /**
     * A reachable node is a node that you can get to from the current node
     * and its value is bigger than 0 (depends on the concurrent implementation)
     * @param  i_Node
     * @return Collection<Node<Index>> reachableNodes
     */
    @Override
    public @Nullable Collection<Node<Index>> getReachableNodes(Node<Index> i_Node) {
        if (correctData(innerMatrix.getValue(i_Node.getData()))){
            List<Node<Index>> reachableNodes = new ArrayList<>();
            for(Index index:innerMatrix.getNeighbors(i_Node.getData())){
                if (correctData(innerMatrix.getValue(index))){
                    Node<Index> singleNode = new Node<>(index);
                    reachableNodes.add(singleNode);
                }
            }
            return reachableNodes;
        }
        return null;
    }
}
