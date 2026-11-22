package hu.nye.rushhour.model;

public class Move {

    private final String vehicleId;
    private final int distance;

    /**
     * Creates a vehicle movement.
     */
    public Move(String vehicleId, int distance) {
        if(vehicleId == null || vehicleId.isBlank()) {
            throw new IllegalArgumentException("Vehicle ID cannot be empty");
        }

        if(distance == 0) {
            throw new IllegalArgumentException("Movement distance cannot be zero");
        }

        this.vehicleId = vehicleId;
        this.distance = distance;
    }

    /**
     * Returns the ID of the vehicle to move.
     */
    public String getVehicleId() {
        return vehicleId;
    }

    /**
     * Returns the movement distance.
     */
    public int getDistance() {
        return distance;
    }
}
