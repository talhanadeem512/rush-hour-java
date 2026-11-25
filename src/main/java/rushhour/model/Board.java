package rushhour.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents the Rush Hour game board and manages the vehicles placed on it.
 * The board has fixed dimensions and prevents vehicles from being placed
 * outside its boundaries or overlapping with one another.
 */
public class Board {

    private final int rows;
    private final int columns;
    private final List<Vehicle> vehicles;

    /**
     * Creates an empty board with the specified dimensions.
     *
     * @param rows the number of rows on the board
     * @param columns the number of columns on the board
     * @throws IllegalArgumentException if either dimension is not positive
     */
    public Board(int rows, int columns) {
        if (rows <= 0 || columns <= 0) {
            throw new IllegalArgumentException(
                    "Board dimensions must be positive."
            );
        }

        this.rows = rows;
        this.columns = columns;
        this.vehicles = new ArrayList<>();
    }

    /**
     * Adds a vehicle to the board after validating its position and ID.
     *
     * @param vehicle the vehicle to add
     * @throws IllegalArgumentException if the vehicle is null, outside the board,
     *                                  overlaps another vehicle, or has a duplicate ID
     */
    public void addVehicle(Vehicle vehicle) {
        if (vehicle == null) {
            throw new IllegalArgumentException(
                    "Vehicle cannot be null."
            );
        }

        if (!isWithinBounds(vehicle)) {
            throw new IllegalArgumentException(
                    "Vehicle is outside the Board."
            );
        }

        if (overlapsWithExistingVehicle(vehicle)) {
            throw new IllegalArgumentException("Vehicle overlaps with another vehicle.");
        }

        if (containsVehicleId(vehicle.getId())) {
            throw new IllegalArgumentException("Vehicle already exists.");
        }

        vehicles.add(vehicle);
    }

    /**
     * Checks whether the specified vehicle fits within the board boundaries.
     *
     * @param vehicle the vehicle whose position must be checked
     * @return {@code true} if the vehicle is within the board boundaries;
     *         {@code false} otherwise
     */
    public boolean isWithinBounds(Vehicle vehicle) {
        Position position = vehicle.getPosition();

        if (vehicle.getOrientation() == Orientation.HORIZONTAL) {
            return position.getColumn() + vehicle.getLength() <= columns
                    && position.getRow() < rows;
        }

        return position.getRow() + vehicle.getLength() <= rows
                && position.getColumn() < columns;
    }

    /**
     * Checks whether the specified vehicle overlaps with any vehicle
     * already on the board.
     *
     * @param vehicle the vehicle to check for overlapping
     * @return {@code true} if the vehicle overlaps with an existing vehicle;
     *         {@code false} otherwise
     */
    public boolean overlapsWithExistingVehicle(Vehicle vehicle) {
        for (Vehicle existingVehicle : vehicles) {
            for (int i = 0; i < vehicle.getLength(); i++) {
                Position newPosition = getVehicleCell(vehicle, i);

                for (int j = 0; j < existingVehicle.getLength(); j++) {
                    Position existingPosition = getVehicleCell(existingVehicle, j);

                    if (newPosition.getRow() == existingPosition.getRow()
                            && newPosition.getColumn() == existingPosition.getColumn()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Calculates the board position of a vehicle cell at the specified offset
     * from the vehicle's starting position.
     *
     * @param vehicle the vehicle whose cell position is being calculated
     * @param offset the zero-based offset along the vehicle's orientation
     * @return the position of the specified vehicle cell
     */
    private Position getVehicleCell(Vehicle vehicle, int offset) {
        Position start = vehicle.getPosition();

        if (vehicle.getOrientation() == Orientation.HORIZONTAL) {
            return new Position(start.getRow(), start.getColumn() + offset);
        }

        return new Position(start.getRow() + offset, start.getColumn());
    }

    /**
     * Checks whether a vehicle with the specified ID exists on the board.
     *
     * @param id the vehicle ID to check
     * @return {@code true} if a vehicle with the ID exists; {@code false} otherwise
     */
    public boolean containsVehicleId(String id) {
        for (Vehicle vehicle : vehicles) {
            if (vehicle.getId().equals(id)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Returns an unmodifiable view of the vehicles currently on the board.
     *
     * @return an unmodifiable list of the board's vehicles
     */
    public List<Vehicle> getVehicles() {
        return Collections.unmodifiableList(vehicles);
    }

    /**
     * Updates the position of an existing vehicle on the board.
     * If the updated position is invalid, the original vehicle is restored.
     *
     * @param updatedVehicle the vehicle containing the updated position
     * @throws IllegalArgumentException if the vehicle is null, does not exist,
     *                                  is outside the board, or overlaps another vehicle
     */
    public void updateVehicle(Vehicle updatedVehicle) {
        if (updatedVehicle == null) {
            throw new IllegalArgumentException("Vehicle cannot be null.");
        }

        int vehicleIndex = -1;
        for (int i = 0; i < vehicles.size(); i++) {
            if (vehicles.get(i).getId().equals(updatedVehicle.getId())) {
                vehicleIndex = i;
                break;
            }
        }

        if (vehicleIndex == -1) {
            throw new IllegalArgumentException("Vehicle does not exist.");
        }

        Vehicle originalVehicle = vehicles.remove(vehicleIndex);

        try {
            if (!isWithinBounds(updatedVehicle)) {
                throw new IllegalArgumentException("Vehicle is outside the board.");
            }

            if (overlapsWithExistingVehicle(updatedVehicle)) {
                throw new IllegalArgumentException(
                        "Vehicle overlaps with another vehicle."
                );
            }

            vehicles.add(vehicleIndex, updatedVehicle);

        } catch (IllegalArgumentException exception) {
            vehicles.add(vehicleIndex, originalVehicle);
            throw exception;
        }
    }

    /**
     * Removes a vehicle from the board using its ID.
     *
     * @param vehicleId the ID of the vehicle to remove
     * @throws IllegalArgumentException if the ID is null, blank, or does not
     *                                  identify an existing vehicle
     */
    public void removeVehicleById(String vehicleId) {
        if (vehicleId == null || vehicleId.isBlank()) {
            throw new IllegalArgumentException("Vehicle Id cannot be empty.");
        }

        for (int i = 0; i < vehicles.size(); i++) {
            if (vehicles.get(i).getId().equals(vehicleId)) {
                vehicles.remove(i);
                return;
            }
        }

        throw new IllegalArgumentException("Vehicle does not exist.");
    }

    /**
     * Returns the number of rows on the board.
     *
     * @return the board's row count
     */
    public int getRows() {
        return rows;
    }

    /**
     * Returns the number of columns on the board.
     *
     * @return the board's column count
     */
    public int getColumns() {
        return columns;
    }

    /**
     * Returns the number of vehicles currently on the board.
     *
     * @return the number of vehicles
     */
    public int getVehicleCount() {
        return vehicles.size();
    }
}