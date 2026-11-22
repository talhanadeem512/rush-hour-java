package hu.nye.rushhour.model;

/**
 * Represents a position on the Rush Hour game board.
 * Coordinates use zero-based indexing.
 */
public class Position {

    private final int row;
    private final int column;

    public Position (int row, int column) {
        if(row < 0 || column < 0){
            throw new IllegalArgumentException(
                    "Row and column cannot be negative."
            );
        }

        this.row = row;
        this.column = column;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    @Override
    public String toString() {
        return "(" + row + ", " + column + ")";
    }
}
