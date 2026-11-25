package rushhour.model;

/**
 * Represents a vehicle on the Rush Hour game board.
 * Each vehicle has a unique ID, a length, a fixed orientation, and a position.
 */
public class Vehicle {

    private final String id;
    private final int length;
    private final Orientation orientation;
    private final Position position;

    /**
     * Creates a vehicle with the specified ID, length, orientation, and position.
     *
     * @param id the unique identifier of the vehicle
     * @param length the length of the vehicle in board cells
     * @param orientation the vehicle's horizontal or vertical orientation
     * @param position the vehicle's starting position on the board
     * @throws IllegalArgumentException if the ID is null or blank, the length
     *                                  is not between 2 and 3, or the orientation
     *                                  or position is null
     */
    public Vehicle(String id, int length, Orientation orientation, Position position) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Vehicle ID cannot be empty");
        }

        if (length < 2 || length > 3) {
            throw new IllegalArgumentException("Vehicle length must be between 2 and 3");
        }

        if (orientation == null || position == null) {
            throw new IllegalArgumentException("Orientation and position cannot be null.");
        }

        this.id = id;
        this.length = length;
        this.orientation = orientation;
        this.position = position;
    }

    /**
     * Returns the vehicle's unique identifier.
     *
     * @return the vehicle ID
     */
    public String getId() {
        return id;
    }

    /**
     * Returns the vehicle's length in board cells.
     *
     * @return the vehicle length
     */
    public int getLength() {
        return length;
    }

    /**
     * Returns the vehicle's orientation.
     *
     * @return the vehicle's orientation
     */
    public Orientation getOrientation() {
        return orientation;
    }

    /**
     * Returns the vehicle's starting position on the board.
     *
     * @return the vehicle's position
     */
    public Position getPosition() {
        return position;
    }
}