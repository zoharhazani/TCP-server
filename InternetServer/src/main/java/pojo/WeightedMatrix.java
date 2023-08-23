package pojo;

import org.jetbrains.annotations.NotNull;

/**
 * A class that represents a matrix with different values.
 */
public class WeightedMatrix extends TraversableMatrix{

    /**
     * Constructs a new instance of the WeightedMatrix class with the given matrix.
     *
     * @param i_Matrix the matrix to be traversed
     */
    public WeightedMatrix(@NotNull Matrix i_Matrix)
    {
        super(i_Matrix);
    }

    /**
     * Checks if the given number is correct data for the weighted matrix.
     *
     * @param i_Number the number to be checked
     * @return true if the number is correct data, false otherwise
     */
    public boolean correctData(int i_Number)
    {
        return i_Number >= 0;
    }
}
