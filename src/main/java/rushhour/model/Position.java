package rushhour.model;

/**
 * Represents a position on the Rush Hour game board.
 * Coordinates use zero-based indexing, where the first row and column
 * are represented by zero.
 */
public class Position {

    private final int row;
    private final int column;

    /**
     * Creates a position using the specified row and column coordinates.
     *
     * @param row the zero-based row coordinate
     * @param column the zero-based column coordinate
     * @throws IllegalArgumentException if either coordinate is negative
     */
    public Position(int row, int column) {
        if (row < 0 || column < 0) {
            throw new IllegalArgumentException(
                    "Row and column cannot be negative."
            );
        }

        this.row = row;
        this.column = column;
    }

    /**
     * Returns the row coordinate.
     *
     * @return the zero-based row coordinate
     */
    public int getRow() {
        return row;
    }

    /**
     * Returns the column coordinate.
     *
     * @return the zero-based column coordinate
     */
    public int getColumn() {
        return column;
    }

    /**
     * Returns a string representation of this position in row-column format.
     *
     * @return the position formatted as {@code (row, column)}
     */
    @Override
    public String toString() {
        return "(" + row + ", " + column + ")";
    }
}