package hu.nye.rushhour.model;

/**
 * Represents a Vehicle on the Rush Hour game board.
 */
public class Vehicle {

    private final String id;
    private final int length;
    private final Orientation orientation;
    private final Position position;

    public Vehicle (String id, int length, Orientation orientation, Position position) {
        if(id == null || id.isBlank()) {
            throw new IllegalArgumentException("Vehicle ID cannot be empty");
        }

        if (length < 2 || length > 3) {
            throw new IllegalArgumentException("Vehicle length must be between 2 and 3");
        }

        if( orientation == null  || position == null) {
            throw new IllegalArgumentException("Orientation and position cannot be null.");
        }

        this.id = id;
        this.length = length;
        this.orientation = orientation;
        this.position = position;

    }

    public String getId() {
        return id;
    }

    public int getLength() {
        return length;
    }

    public Orientation getOrientation() {
        return orientation;
    }

    public Position getPosition() {
        return position;
    }


}