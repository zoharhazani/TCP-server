package pojo;

import org.jetbrains.annotations.NotNull;

/**
 * A class that represents a basic matrix (only contains 0/1 values).
 */
public class BasicMatrix extends TraversableMatrix{

    /**
     * Constructs a new instance of the BasicMatrix class with the given matrix.
     *
     * @param matrix the matrix to be traversed
     */
    public BasicMatrix(@NotNull Matrix matrix)
    {
        super(matrix);
    }

    /**
     * Checks if the given number is correct data for the basic matrix.
     *
     * @param aNumber the number to be checked
     * @return true if the number is correct data, false otherwise
     */
    public boolean correctData(int aNumber)
    {
        return aNumber == 1;
    }

}
