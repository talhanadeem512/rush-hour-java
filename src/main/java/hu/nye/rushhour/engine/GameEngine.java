package hu.nye.rushhour.engine;

import hu.nye.rushhour.model.*;

public class GameEngine {

    private final GameState gameState;
    private final TurnManager turnManager;

    /**
     * Create a game engine for the given game state.
     *
     */
    public GameEngine(GameState gameState) {
        if (gameState == null) {
            throw new IllegalArgumentException("GameState cannot be null null");
        }

        this.gameState = gameState;
        this.turnManager = new TurnManager();
    }

    /**
     * Finds a vehicle on the board using its ID.
     *
     * @return the matching vehicle
     */
    public Vehicle findVehicleById(String vehicleId) {
        if (vehicleId == null || vehicleId.isBlank()) {
            throw new IllegalArgumentException("Vehicle Id cannot be empty.");
        }

        for (Vehicle vehicle : gameState.getBoard().getVehicles()) {
            if (vehicle.getId().equals(vehicleId)) {
                return vehicle;
            }
        }

        throw new IllegalArgumentException("Vehicle not found: " + vehicleId);
    }

    /**
     * Calculates the new position of vehicle after a requested move.
     *
     * @return the new position
     */
    private Position calculateNewPosition(Vehicle vehicle, Move move) {

        Position currentPosition = vehicle.getPosition();

        if (vehicle.getOrientation() == Orientation.HORIZONTAL) {
            return new Position(
                    currentPosition.getRow(),
                    currentPosition.getColumn() + move.getDistance()
            );
        }

        return new Position(
                currentPosition.getRow() + move.getDistance(),
                currentPosition.getColumn()
        );
    }

    public boolean isMoveValid(Move move) {
        if (move == null) {
            return false;
        }

        Vehicle vehicle;

        try{
            vehicle = findVehicleById(move.getVehicleId());
        }catch(IllegalArgumentException e){
            return false;
        }

        Position newPosition = calculateNewPosition(vehicle, move);

        Board board = gameState.getBoard();

        // Check whether the vehicle remains inside the board.
        Vehicle movedVehicle = new Vehicle(
                vehicle.getId(),
                vehicle.getLength(),
                vehicle.getOrientation(),
                newPosition
        );

        if (!board.isWithinBounds(movedVehicle)) {
            return false;
        }

        if (!isPathClear(vehicle, move)) {
            return false;
        }

        return true;
    }

    /**
     * Checks whether a vehicle's destination overlaps another vehicle.
     *
     * @return true if another vehicle occupies one of its cells.
     */
    private boolean overlapsWithOtherVehicle(Vehicle movedVehicle) {
        Board board = gameState.getBoard();

        for (Vehicle other : board.getVehicles()) {
            if (other.getId().equals(movedVehicle.getId())) {
                continue;
            }

            for (int i = 0; i < movedVehicle.getLength(); i++) {
                Position movedCell = getVehicleCell(movedVehicle, i);

                for (int j = 0; j < other.getLength(); j++) {
                    Position otherCell = getVehicleCell(other, j);

                    if (movedCell.getRow() == otherCell.getRow()
                            && movedCell.getColumn() == otherCell.getColumn()) {
                        return true;
                    }
                }
            }
        }

        return false;
    }
    /**
     * Gets the position of a specific cell occupied by a vehicle.
     *
     * @return the cell position
     */
    private Position getVehicleCell(Vehicle vehicle, int index) {
        Position position = vehicle.getPosition();

        if (vehicle.getOrientation() == Orientation.HORIZONTAL) {
            return new Position(position.getRow(), position.getColumn() + index);
        }

        return new Position(position.getRow() + index, position.getColumn());
    }

    /**
     * Checks whether the complete movement path is free of other vehicles.
     *
     * @param vehicle the vehicle being moved
     * @param move the requested movement
     * @return true if the complete path is clear
     */
    private boolean isPathClear(Vehicle vehicle, Move move) {
        int direction = Integer.signum(move.getDistance());
        int distance = Math.abs(move.getDistance());

        for (int step = 1; step <= distance; step++) {
            Move intermediateMove = new Move(
                    move.getVehicleId(),
                    direction * step
            );

            Position intermediatePosition =
                    calculateNewPosition(vehicle, intermediateMove);

            Vehicle intermediateVehicle = new Vehicle(
                    vehicle.getId(),
                    vehicle.getLength(),
                    vehicle.getOrientation(),
                    intermediatePosition
            );

            if (overlapsWithOtherVehicle(intermediateVehicle)) {
                return false;
            }
        }

        return true;
    }

    /**
     * Executes a valid move nad updates the board.
     *
     * @throws IllegalArgumentException if the move is invalid.
     */
    public void executeMove(Move move) {
        if (!isMoveValid(move)) {
            throw new IllegalArgumentException("Invalid move.");
        }

        Vehicle vehicle = findVehicleById(move.getVehicleId());

        Position newPosition = calculateNewPosition(vehicle, move);

        Vehicle updatedvehicle = new Vehicle(vehicle.getId(), vehicle.getLength(), vehicle.getOrientation(), newPosition);

        gameState.getBoard().updateVehicle(updatedvehicle);
    }

    /**
     * Executes a move on behalf of the current player.
     *
     * @throws IllegalArgumentException if the player is null or it is not their turn
     * @throws IllegalArgumentException if the move is invalid
     */
    public void executeTurnMove(Player player, Move move) {
        if(player == null || player != turnManager.getCurrentPlayer()) {
            throw new IllegalArgumentException("It is not this player's turn.");
        }

        executeMove(move);

        turnManager.switchTurn();
    }

    public Player getCurrentPlayer() {
        return turnManager.getCurrentPlayer();
    }

}
