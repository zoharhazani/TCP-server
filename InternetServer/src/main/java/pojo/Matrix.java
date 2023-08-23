package pojo;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.*;


public class Matrix implements Serializable {
    private static final @NotNull Long serialVersionUID = 1L;
    @NotNull
    int[][] primitiveMatrix;
    int numOfRows;
    int numOfColumns;

    /**
     * Constructs a new instance of the Matrix class with the given 2D array.
     *
     * @param oArray the 2D array representing the matrix
     */
    public Matrix(int[][] oArray){
        List<int[]> list = new ArrayList<>();
        setNumOfRows(oArray.length);
        setNumOfColumns(oArray[0].length);
        for (int[] row : oArray) {
            int[] clone = row.clone();
            list.add(clone);
        }
        primitiveMatrix = list.toArray(new int[0][]);
    }

    /**
     * Gets the number of rows in the matrix.
     *
     * @return the number of rows
     */
    public int getNumOfRows() {
        return numOfRows;
    }

    /**
     * Gets the number of columns in the matrix.
     *
     * @return the number of columns
     */
    public int getNumOfColumns() {
        return numOfColumns;
    }

    /**
     * Sets the number of rows in the matrix.
     *
     * @param i_NumOfRows the number of rows to set
     */
    public void setNumOfRows(int i_NumOfRows) {
        if(i_NumOfRows > 0)
        {
            this.numOfRows = i_NumOfRows;
        }
        else{
            this.numOfRows = 1;
        }

    }

    /**
     * Sets the number of columns in the matrix.
     *
     * @param i_NumOfColumns the number of columns to set
     */
    public void setNumOfColumns(int i_NumOfColumns) {
        if(i_NumOfColumns > 0)
        {
            this.numOfColumns = numOfRows;
        }
        else{
            this.numOfColumns = 1;
        }
    }

    @Override
    public @NotNull String toString(){
        StringBuilder stringBuilder = new StringBuilder();
        for (int[] row : primitiveMatrix) {
            stringBuilder.append(Arrays.toString(row));
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }

    /**
     * Gets the neighbors of the given index in the matrix.
     *
     * @param i_Index the index in the matrix
     * @return a collection of neighbor indices
     */
    public @NotNull Collection<Index> getNeighbors(Index i_Index) {
        Collection<Index> list = new ArrayList<>();

        if(i_Index.row > 0){
            list.add(new Index(i_Index.row-1,i_Index.column));
            if(i_Index.column > 0){list.add(new Index(i_Index.row-1,i_Index.column-1));}
            if(i_Index.column + 1 < numOfColumns) {list.add(new Index(i_Index.row-1 ,i_Index.column+1));}
        }

        if(i_Index.row + 1 < numOfRows) {
            list.add(new Index(i_Index.row+1,i_Index.column));
            if(i_Index.column + 1 < numOfColumns) {list.add(new Index(i_Index.row+1 ,i_Index.column+1));}
            if(i_Index.column > 0) {list.add(new Index(i_Index.row+1 ,i_Index.column-1));}
        }

        if(i_Index.column > 0) {list.add(new Index(i_Index.row,i_Index.column-1));}
        if(i_Index.column + 1 < numOfColumns) {list.add(new Index(i_Index.row,i_Index.column+1));}

        return list;
    }

    /**
     * Gets the value at the specified index in the matrix.
     *
     * @param i_Index the index in the matrix
     * @return the value at the specified index
     */
    public int getValue(final Index i_Index){
        return primitiveMatrix[i_Index.row][i_Index.column];
    }

    /**
     * Prints the matrix to the console.
     */
    public void printMatrix(){
        for (int[] row : primitiveMatrix) {
            String s = Arrays.toString(row);
            System.out.println(s);
        }
    }

    /**
     * Gets the primitive matrix.
     *
     * @return the primitive matrix
     */
    public final int[][] getPrimitiveMatrix() {
        return primitiveMatrix;
    }

}
