package pojo;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.Objects;


/**
 * A class representing an index with row and column values.
 */
public class Index implements Serializable {
    private static final @NotNull Long serialVersionUID = 1L;
    int row, column;

    /**
     * Constructs a new instance of the Index class with the given row and column values.
     *
     * @param i_Row    the row value of the index
     * @param i_Column the column value of the index
     */
    public Index(final int i_Row, final int i_Column) {
        this.row=i_Row;
        this.column=i_Column;
    }

    /**
     * Gets the row value of the index.
     *
     * @return the row value
     */
    public int getRow() {
        return row;
    }

    /**
     * Gets the column value of the index.
     *
     * @return the column value
     */
    public int getColumn() {
        return column;
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Index index = (Index) o;
        return row == index.row &&
                column == index.column;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, column);
    }

    @Override
    public @NotNull String toString() {
        return "("+row +
                "," + column +
                ')';
    }

}
