package rushhour.engine;

import rushhour.model.*;
import rushhour.ai.MoveSelector;
import rushhour.ai.MoveGenerator;
import java.util.List;

/**
 * Manages the core game operations, including move validation, move execution,
 * vehicle movement, turn management, and win-condition checking.
 */
public class GameEngine {

    private final GameState gameState;
    private final TurnManager turnManager;
    private final WinConditionChecker winConditionChecker;
    private final MoveSelector moveSelector;
    private final MoveGenerator moveGenerator;

    /**
     * Creates a game engine for the given game state.
     *
     * @param gameState the initial state of the game
     * @throws IllegalArgumentException if the game state is null
     */
    public GameEngine(GameState gameState) {
        if (gameState == null) {
            throw new IllegalArgumentException("GameState cannot be null null");
        }

        this.gameState = gameState;
        this.turnManager = new TurnManager();
        this.winConditionChecker = new WinConditionChecker();
        this.moveSelector = new MoveSelector();
        this.moveGenerator = new MoveGenerator();
    }

    /**
     * Finds a vehicle on the board using its unique ID.
     *
     * @param vehicleId the ID of the vehicle to find
     * @return the vehicle matching the specified ID
     * @throws IllegalArgumentException if the vehicle ID is null, blank, or does not exist
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
     * Calculates the new row and column of a vehicle after a requested move.
     *
     * @param vehicle the vehicle being moved
     * @param move the requested movement
     * @return an array containing the new row at index 0 and new column at index 1
     */
    private int[] calculateNewPosition(Vehicle vehicle, Move move) {
        int newRow = vehicle.getPosition().getRow();
        int newColumn = vehicle.getPosition().getColumn();

        if (vehicle.getOrientation() == Orientation.HORIZONTAL) {
            newColumn += move.getDistance();
        } else {
            newRow += move.getDistance();
        }

        return new int[]{newRow, newColumn};
    }

    /**
     * Checks whether a requested vehicle movement is valid.
     *
     * @param move the requested movement
     * @return true if the movement is valid; false otherwise
     */
    public boolean isMoveValid(Move move) {
        if (move == null) {
            return false;
        }

        Vehicle vehicle;

        try {
            vehicle = findVehicleById(move.getVehicleId());
        } catch (IllegalArgumentException e) {
            return false;
        }

        int[] newPosition = calculateNewPosition(vehicle, move);

        int newRow = newPosition[0];
        int newColumn = newPosition[1];

        Board board = gameState.getBoard();

        boolean withinBounds;

        if (vehicle.getOrientation() == Orientation.HORIZONTAL) {
            withinBounds = newRow >= 0
                    && newRow < board.getRows()
                    && newColumn >= 0
                    && newColumn + vehicle.getLength() <= board.getColumns();
        } else {
            withinBounds = newColumn >= 0
                    && newColumn < board.getColumns()
                    && newRow >= 0
                    && newRow + vehicle.getLength() <= board.getRows();
        }

        if (!withinBounds) {
            return isValidHeroExitMove(vehicle, move)
                    && isPathClear(vehicle, move);
        }

        if (!isPathClear(vehicle, move)) {
            return false;
        }

        return true;
    }

    /**
     * Checks whether the destination of a moved vehicle overlaps another vehicle.
     *
     * @param movedVehicle the vehicle whose destination is being checked
     * @return true if the vehicle overlaps another vehicle; false otherwise
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
     * @param vehicle the vehicle whose cell position is required
     * @param index the zero-based index of the cell within the vehicle
     * @return the position of the specified cell
     */
    private Position getVehicleCell(Vehicle vehicle, int index) {
        Position position = vehicle.getPosition();

        if (vehicle.getOrientation() == Orientation.HORIZONTAL) {
            return new Position(position.getRow(), position.getColumn() + index);
        }

        return new Position(position.getRow() + index, position.getColumn());
    }

    /**
     * Checks whether the complete path of a requested vehicle movement is clear.
     *
     * @param vehicle the vehicle being moved
     * @param move the requested movement
     * @return true if every cell along the movement path is unoccupied; false otherwise
     */
    private boolean isPathClear(Vehicle vehicle, Move move) {
        int direction = Integer.signum(move.getDistance());
        int distance = Math.abs(move.getDistance());

        Board board = gameState.getBoard();

        for (int step = 1; step <= distance; step++) {
            int[] position = calculateNewPosition(
                    vehicle,
                    new Move(move.getVehicleId(), direction * step)
            );

            int movedRow = position[0];
            int movedColumn = position[1];

            for (Vehicle other : board.getVehicles()) {
                if (other.getId().equals(vehicle.getId())) {
                    continue;
                }

                for (int i = 0; i < vehicle.getLength(); i++) {
                    int cellRow = movedRow;
                    int cellColumn = movedColumn;

                    if (vehicle.getOrientation() == Orientation.HORIZONTAL) {
                        cellColumn += i;
                    } else {
                        cellRow += i;
                    }

                    for (int j = 0; j < other.getLength(); j++) {
                        int otherRow = other.getPosition().getRow();
                        int otherColumn = other.getPosition().getColumn();

                        if (other.getOrientation() == Orientation.HORIZONTAL) {
                            otherColumn += j;
                        } else {
                            otherRow += j;
                        }

                        if (cellRow == otherRow && cellColumn == otherColumn) {
                            return false;
                        }
                    }
                }
            }
        }

        return true;
    }

    /**
     * Executes a valid move and updates the board.
     * If a hero vehicle exits the board, it is removed and its exit status is updated.
     *
     * @param move the move to execute
     * @throws IllegalArgumentException if the game has ended or the requested move is invalid
     */
    public void executeMove(Move move) {
        if (hasHumanWon() || hasMachineWon()) {
            throw new IllegalArgumentException("Cannot make moves after the game has ended");
        }
        if (!isMoveValid(move)) {
            throw new IllegalArgumentException("Invalid move.");
        }

        Vehicle vehicle = findVehicleById(move.getVehicleId());

        int[] newPosition = calculateNewPosition(vehicle, move);

        int newRow = newPosition[0];
        int newColumn = newPosition[1];

        Board board = gameState.getBoard();

        boolean withinBounds;

        if (vehicle.getOrientation() == Orientation.HORIZONTAL) {
            withinBounds = newRow >= 0
                    && newRow < board.getRows()
                    && newColumn >= 0
                    && newColumn + vehicle.getLength() <= board.getColumns();
        } else {
            withinBounds = newColumn >= 0
                    && newColumn < board.getColumns()
                    && newRow >= 0
                    && newRow + vehicle.getLength() <= board.getRows();
        }

        if (!withinBounds) {
            board.removeVehicleById(vehicle.getId());

            if (vehicle.getId().equals(gameState.getHumanHeroID())) {
                gameState.setHumanHeroExited(true);
            } else {
                gameState.setMachineHeroExited(true);
            }

            return;
        }

        Position position = new Position(newRow, newColumn);

        Vehicle updatedVehicle = new Vehicle(
                vehicle.getId(),
                vehicle.getLength(),
                vehicle.getOrientation(),
                position
        );

        board.updateVehicle(updatedVehicle);
    }

    /**
     * Executes a move on behalf of the specified player and updates the turn.
     *
     * @param player the player attempting to make the move
     * @param move the move to execute
     * @throws IllegalArgumentException if the player is null, it is not their turn,
     *                                  or the requested move is invalid
     */
    public void executeTurnMove(Player player, Move move) {
        if(player == null || player != turnManager.getCurrentPlayer()) {
            throw new IllegalArgumentException("It is not this player's turn.");
        }

        executeMove(move);

        if(!hasHumanWon() && !hasMachineWon()) {
            turnManager.switchTurn();
        }
    }

    /**
     * Generates and executes a random valid move for the machine player.
     *
     * @throws IllegalArgumentException if it is not the machine's turn or no valid moves exist.
     */
    public void executeMachineTurn(){
        if(turnManager.getCurrentPlayer() != Player.MACHINE){
            throw new IllegalArgumentException("It is not the machine's turn.");
        }

        List<Move> validMoves = moveGenerator.generateValidMoves(gameState, this);

        Move selectedMove = moveSelector.selectRandomMove(validMoves);

        executeTurnMove(Player.MACHINE, selectedMove);
    }
    /**
     * Checks whether the human player has won the game.
     *
     * @return true if the human hero has exited the board; false otherwise
     */
    public boolean hasHumanWon(){
        return winConditionChecker.hasHumanWon(gameState);
    }

    /**
     * Checks whether the machine player has won the game.
     *
     * @return true if the machine hero has exited the board; false otherwise
     */
    public boolean hasMachineWon(){
        return winConditionChecker.hasMachineWon(gameState);
    }

    /**
     * Checks whether a hero vehicle is moving toward its assigned exit.
     *
     * @param vehicle the vehicle being moved
     * @param move the requested movement
     * @return true if the vehicle is a hero moving toward its assigned exit; false otherwise
     */
    private boolean isMovingTowardAssignedExit(Vehicle vehicle, Move move) {
        String vehicleId = vehicle.getId();
        int distance = move.getDistance();

        if(vehicleId.equals(gameState.getHumanHeroID())){
            return switch (gameState.getHumanExitSide()){
                case LEFT, TOP -> distance < 0;
                case RIGHT, BOTTOM -> distance > 0;
            };
        }

        if(vehicleId.equals(gameState.getMachineHeroID())){
            return switch(gameState.getMachineExitSide()){
                case LEFT, TOP -> distance < 0;
                case RIGHT, BOTTOM -> distance > 0;
            };
        }

        return false;
    }

    /**
     * Checks whether a hero vehicle is aligned with its assigned exit coordinate.
     *
     * @param vehicle the hero vehicle being checked
     * @return true if the vehicle is aligned with its assigned exit; false otherwise
     */
    private boolean isHeroAtAssignedExitCoordinate(Vehicle vehicle) {
        if (vehicle.getId().equals(gameState.getHumanHeroID())) {
            if (vehicle.getOrientation() == Orientation.HORIZONTAL) {
                return vehicle.getPosition().getRow()
                        == gameState.getHumanExitCoordinate();
            }

            return vehicle.getPosition().getColumn()
                    == gameState.getHumanExitCoordinate();
        }

        if (vehicle.getId().equals(gameState.getMachineHeroID())) {
            if (vehicle.getOrientation() == Orientation.HORIZONTAL) {
                return vehicle.getPosition().getRow()
                        == gameState.getMachineExitCoordinate();
            }

            return vehicle.getPosition().getColumn()
                    == gameState.getMachineExitCoordinate();
        }

        return false;
    }

    /**
     * Calculates the movement distance required for a hero vehicle to leave the board
     * completely through the specified exit side.
     *
     * @param vehicle the hero vehicle leaving the board
     * @param exitSide the side of the board containing the assigned exit
     * @return the signed distance required to move the entire vehicle outside the board
     */
    private int calculateExitDistance(Vehicle vehicle, ExitSide exitSide) {
        Position position = vehicle.getPosition();
        Board board = gameState.getBoard();

        return switch (exitSide) {
            case RIGHT -> board.getColumns() - position.getColumn();
            case LEFT -> -(position.getColumn() + vehicle.getLength());
            case BOTTOM -> board.getRows() - position.getRow();
            case TOP -> -(position.getRow() + vehicle.getLength());
        };
    }

    /**
     * Checks whether a move allows a hero vehicle to exit through its assigned exit.
     *
     * @param vehicle the vehicle being moved
     * @param move the requested movement
     * @return true if the move completely takes the hero through its assigned exit; false otherwise
     */
    private boolean isValidHeroExitMove(Vehicle vehicle, Move move) {
        if (!isMovingTowardAssignedExit(vehicle, move)) {
            return false;
        }

        if (!isHeroAtAssignedExitCoordinate(vehicle)) {
            return false;
        }

        ExitSide exitSide;

        if (vehicle.getId().equals(gameState.getHumanHeroID())) {
            exitSide = gameState.getHumanExitSide();
        } else {
            exitSide = gameState.getMachineExitSide();
        }

        return move.getDistance() == calculateExitDistance(vehicle, exitSide);
    }

    /**
     * Gets the player whose turn it currently is.
     *
     * @return the player whose turn is active
     */
    public Player getCurrentPlayer() {
        return turnManager.getCurrentPlayer();
    }

}
