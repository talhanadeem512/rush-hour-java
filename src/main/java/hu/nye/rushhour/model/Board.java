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
    public void addvehicle(Vehicle vehicle) {
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

        if(containsVehicle(vehicle.getId())) {
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
    public boolean containsVehicle(String id) {
        for(Vehicle vehicle : vehicles) {
            if(vehicle.getId().equals(id)) {
                return true;
            }
        }

        return false;
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