package rushhour.model;

/**
 * Represents a movement of a vehicle on the Rush Hour game board.
 * A positive distance moves the vehicle right or down, while a negative
 * distance moves it left or up, depending on its orientation.
 */
public class Move {

    private final String vehicleId;
    private final int distance;

    /**
     * Creates a vehicle movement with the specified vehicle ID and distance.
     *
     * @param vehicleId the ID of the vehicle to move
     * @param distance the signed number of cells to move the vehicle
     * @throws IllegalArgumentException if the vehicle ID is null or blank,
     *                                  or if the movement distance is zero
     */
    public Move(String vehicleId, int distance) {
        if (vehicleId == null || vehicleId.isBlank()) {
            throw new IllegalArgumentException("Vehicle ID cannot be empty");
        }

        if (distance == 0) {
            throw new IllegalArgumentException("Movement distance cannot be zero");
        }

        this.vehicleId = vehicleId;
        this.distance = distance;
    }

    /**
     * Returns the ID of the vehicle to move.
     *
     * @return the vehicle ID
     */
    public String getVehicleId() {
        return vehicleId;
    }

    /**
     * Returns the signed movement distance.
     *
     * @return the movement distance in board cells
     */
    public int getDistance() {
        return distance;
    }
}