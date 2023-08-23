package pojo;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * A class representing the counting of valid submarines.
 */
public class Submarines implements Serializable  {
    private static final @NotNull Long serialVersionUID = 1L;
    @NotNull AtomicInteger validSubmarines;
    TraversableMatrix matrix;
    ExecutorService threadPool;
    private volatile static Submarines instance;

    public static Submarines getInstance() {
        if (instance == null) {
            synchronized (Submarines.class){
                if (instance==null){
                    instance = new Submarines();
                }
            }
        }
        return instance;
    }

    private Submarines() {
        this.validSubmarines = new AtomicInteger(0);
    }

    /**
     * Counts the number of valid submarines in the given matrix.
     *
     * @param matrix the traversable matrix
     * @param indexList the list of valid indexes(not equals to 0)
     * @return the count of valid submarines
     */
    public int countValidSubmarines(@NotNull TraversableMatrix matrix, @NotNull List<Index> indexList) {

        this.matrix=matrix;
        matrix.setRoot(new Index(0, 0));
        DfsVisit<Index> algorithm = DfsVisit.getInstance();
        HashSet<Set<Index>> allConnectedComponents = algorithm.traverseAllOver(matrix, indexList);
        threadPool = Executors.newFixedThreadPool(allConnectedComponents.size());

        for (Set<Index> connectedComponent : allConnectedComponents) {
            threadPool.execute(()-> {
                boolean flag = checkConnectedComponents(connectedComponent);

                //critical section
                if(flag) {
                    validSubmarines.incrementAndGet();
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

        return validSubmarines.get();
    }

    /**
     * Checks if a connected component of indices represents a valid submarine.
     *
     * @param connectedComponent the set of indexes representing a connected component
     * @return true if the connected component represents a valid submarine, false otherwise
     */
    private boolean checkConnectedComponents(Set<Index> connectedComponent)
    {
        int maxX=Integer.MIN_VALUE ,maxY=Integer.MIN_VALUE, minX=Integer.MAX_VALUE, minY=Integer.MAX_VALUE;
        for (Index index : connectedComponent) {

            int currentX = index.getRow();
            if (currentX > maxX) {
                maxX = currentX;
            }
            if (currentX<minX){
                minX = currentX;
            }

            int currentY=index.getColumn();
            if(currentY>maxY){
                maxY = currentY;
            }
            if (currentY<minY){
                minY = currentY;
            }
        }

        boolean flag = true;
        for (int i = minX; i < maxX + 1; i++) {
            for (int j = minY; j < maxY + 1; j++) {
                if (matrix.getInnerMatrix().primitiveMatrix[i][j] == 0) {
                    flag=false;
                    break;
                }
            }
        }

        return flag;
    }
}