package pojo;

import server.IHandler;
import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *  This class handles Matrix-related tasks
 * Adapts the functionality  of IHandler to a Matrix object
 */
public class MatrixHandler implements IHandler {
    private Matrix matrix;
    private Index sourceIndex;
    private boolean doWork;
    List<Index> indexList;

    @Override
    public void handleClient(InputStream fromClient, OutputStream toClient) throws IOException, ClassNotFoundException {
        /*
        data is sent eventually as bytes
        read data as bytes then transform to meaningful data
        ObjectInputStream and ObjectOutputStream can read and write both primitives
        and Reference types
         */

        ObjectInputStream objectInputStream = new ObjectInputStream(fromClient);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(toClient);
        indexList = new ArrayList<>();
        doWork = true;

        while(doWork)
        {
            switch (objectInputStream.readObject().toString()) {
                case "matrix" -> {
                    // expect to get a 2d array. handler will create a Matrix object
                    int[][] anArray = (int[][]) objectInputStream.readObject();
                    System.out.println("Got 2d array");
                    this.matrix = new Matrix(anArray);
                    this.matrix.printMatrix();
                }
                case "all connected components" -> {
                    if (this.matrix != null) {
                        TraversableMatrix matrixAsGraph = new BasicMatrix(this.matrix);
                        matrixAsGraph.setRoot(new Index(0, 0));
                        DfsVisit<Index> algorithm = DfsVisit.getInstance();
                        initializeIndexList();
                        HashSet<Set<Index>> allConnectedComponents = algorithm.traverseAllOver(matrixAsGraph, indexList);
                        objectOutputStream.writeObject(allConnectedComponents);
                    }
                }
                case "all shortest paths" -> {
                    this.sourceIndex = (Index) objectInputStream.readObject();
                    Index destinationIndex = (Index) objectInputStream.readObject();
                    if (this.matrix != null) {
                        TraversableMatrix matrixAsGraph = new BasicMatrix(this.matrix);
                        matrixAsGraph.setRoot(this.sourceIndex);
                        Bfs<Index> algorithm = Bfs.getInstance();
                        HashSet<List<Index>> allShortestPaths = algorithm.findShortestPaths(matrixAsGraph, this.sourceIndex, destinationIndex);
                        objectOutputStream.writeObject(allShortestPaths);
                    }
                }
                case "submarines" -> {
                    if (this.matrix != null) {
                        TraversableMatrix matrixAsGraph = new BasicMatrix(this.matrix);
                        Submarines algorithm = Submarines.getInstance();
                        if (indexList.size() == 0) {
                            initializeIndexList();
                        }
                        int validSubmarines = algorithm.countValidSubmarines(matrixAsGraph, indexList);
                        objectOutputStream.writeObject(validSubmarines);

                    }
                }
                case "find the lowest weight paths" -> {
                    this.sourceIndex = (Index) objectInputStream.readObject();
                    Index destinationIndex = (Index) objectInputStream.readObject();
                    if (this.matrix != null) {
                        TraversableMatrix matrixAsGraph = new WeightedMatrix(matrix);
                        LowestWeightPaths<Index> algorithm = LowestWeightPaths.getInstance();
                        HashSet<List<Index>> allLowestWeightPaths = algorithm.FindLowestWeightPaths(matrixAsGraph, this.sourceIndex, destinationIndex);
                        objectOutputStream.writeObject(allLowestWeightPaths);
                    }
                }
                case "stop" -> {
                    doWork = false;
                }
            }
        }
    }

    /**
     * Initializes the 'indexList' data member with the valid indices in the given matrix.
     * ( their value equals to 1 )
     */
    private void initializeIndexList()
    {
        for (int i = 0; i < this.matrix.getNumOfRows(); i++) {
            for (int j = 0; j < this.matrix.getNumOfColumns(); j++) {
                if (this.matrix.primitiveMatrix[i][j] == 1) {
                    indexList.add(new Index(i, j));
                }
            }
        }
    }
}
