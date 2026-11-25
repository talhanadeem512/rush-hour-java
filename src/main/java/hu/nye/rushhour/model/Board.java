package hu.nye.rushhour.model;

import  java.util.ArrayList;
import  java.util.List;

/**
 * Represents the Rush hour game board.
 */
public class Board {

    private final int rows;
    private final int columns;
    private final List<Vehicle> vehicles;

    public Board(int rows, int columns) {
        if(rows <= 0 || columns <= 0) {
            throw new IllegalArgumentException(
                    "Board dimensions must be positive."
            );
        }

        this.rows = rows;
        this.columns = columns;
        this.vehicles = new ArrayList<>();
    }

    /**
     * Adds a vehicle to board.
     *
     * @param vehicle the vehicle to add
     *
     */
    public void addVehicle(Vehicle vehicle) {
        if ( vehicle == null ) {
            throw new IllegalArgumentException(
                    "Vehicle cannot be null."
            );
        }

        if(!isWithinBounds(vehicle)) {
            throw new IllegalArgumentException(
                    "Vehicle is outside the Board."
            );
        }

        if(overlapsWithExistingVehicle(vehicle)) {
            throw new IllegalArgumentException("Vehicle overlaps with another vehicle.");
        }

        if(containsVehicleId(vehicle.getId())) {
            throw new IllegalArgumentException("Vehicle already exists.");
        }

        vehicles.add(vehicle);
    }

    public boolean isWithinBounds(Vehicle vehicle) {
        Position position = vehicle.getPosition();

        if(vehicle.getOrientation() == Orientation.HORIZONTAL) {
            return position.getColumn() + vehicle.getLength() <= columns
                    && position.getRow() < rows;
        }

        return position.getRow() + vehicle.getLength() <= rows
                && position.getColumn() < columns;
    }

    /**
     * Checks whether a vehicle overlaps with any existing vehicle.
     *
     * @return true if vehicle overlaps with an existing vehicle.
     */
    public boolean overlapsWithExistingVehicle(Vehicle vehicle) {
        for(Vehicle existingVehicle : vehicles) {
            for(int i = 0; i < vehicle.getLength(); i++) {
                Position newPosition = getVehicleCell(vehicle, i);

                for (int j = 0; j < existingVehicle.getLength(); j++) {
                    Position existingPosition = getVehicleCell(existingVehicle, j);

                    if(newPosition.getRow() == existingPosition.getRow() && newPosition.getColumn() == existingPosition.getColumn()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Calculates the position of a vehicle cell at a given offset.
     *
     * @return The position of specified cell.
     */

    private Position getVehicleCell(Vehicle vehicle, int offset) {
        Position start = vehicle.getPosition();

        if(vehicle.getOrientation() == Orientation.HORIZONTAL) {
            return new Position(start.getRow(), start.getColumn() + offset);
        }

        return new Position(start.getRow() + offset, start.getColumn());
    }

    /**
     * Checks whether a vehicle ID already exists on the board.
     *
     * @return true if the ID already exists
     */
    public boolean containsVehicleId(String id) {
        for(Vehicle vehicle : vehicles) {
            if(vehicle.getId().equals(id)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Returns an unmodified view of vehicles on the board.
     *
     * @return vehicles currently on the board.
     */
    public List<Vehicle> getVehicles() {
        return java.util.Collections.unmodifiableList(vehicles);
    }

    /**
     * Updates the position of an existing vehicle on the board.
     *
     * @throws IllegalArgumentException if the vehicle does not exist or the update is invalid.
     * @return
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
     * @throws IllegalArgumentException if the ID is invalid or the vehicle does not exist.
     */
    public void removeVehicleById(String vehicleId){
        if(vehicleId == null || vehicleId.isBlank()){
            throw new IllegalArgumentException("Vehicle Id cannot be empty.");
        }

        for(int i = 0; i < vehicles.size(); i++) {
            if(vehicles.get(i).getId().equals(vehicleId)) {
                vehicles.remove(i);
                return;
            }
        }

        throw new IllegalArgumentException("Vehicle does not exist.");
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }

    public int getVehicleCount(){
        return vehicles.size();
    }
}