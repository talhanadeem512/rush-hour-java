package rushhour.model;

/**
 * Represents the current state of a Rush Hour game, including its board,
 * players' hero vehicles, exit locations, and exit statuses.
 */
public class GameState {

    private final Board board;
    private final String humanHeroID;
    private final String machineHeroID;
    private final int humanExitCoordinate;
    private final int machineExitCoordinate;
    private boolean humanHeroExited;
    private boolean machineHeroExited;
    private final ExitSide humanExitSide;
    private final ExitSide machineExitSide;

    /**
     * Creates a game state with the specified board, hero vehicles, and exits.
     *
     * @param board the game board
     * @param humanHeroID the ID of the human player's hero vehicle
     * @param machineHeroID the ID of the machine player's hero vehicle
     * @param humanExitCoordinate the coordinate of the human player's exit
     * @param machineExitCoordinate the coordinate of the machine player's exit
     * @param humanExitSide the side of the board containing the human player's exit
     * @param machineExitSide the side of the board containing the machine player's exit
     * @throws IllegalArgumentException if the board is null, hero IDs are invalid
     *                                  or identical, hero vehicles do not exist,
     *                                  exit coordinates are negative, exit sides
     *                                  are null, or an exit is incompatible with
     *                                  its hero vehicle
     */
    public GameState(Board board, String humanHeroID, String machineHeroID,
                     int humanExitCoordinate, int machineExitCoordinate,
                     ExitSide humanExitSide, ExitSide machineExitSide) {
        if (board == null) {
            throw new IllegalArgumentException("Board cannot be null.");
        }
        if (humanHeroID == null || humanHeroID.isBlank()
                || machineHeroID == null || machineHeroID.isBlank()) {
            throw new IllegalArgumentException("Hero IDs cannot be null.");
        }

        if (!board.containsVehicleId(humanHeroID)
                || !board.containsVehicleId(machineHeroID)) {
            throw new IllegalArgumentException("Both hero vehicles must exist on the board.");
        }

        if (humanHeroID.equals(machineHeroID)) {
            throw new IllegalArgumentException("Players must have different hero vehicles.");
        }

        if (humanExitCoordinate < 0 || machineExitCoordinate < 0) {
            throw new IllegalArgumentException("Exit coordinates cannot be negative.");
        }

        if (humanExitSide == null || machineExitSide == null) {
            throw new IllegalArgumentException("Exit sides cannot be null.");
        }

        if (!isExitCompatible(board, humanHeroID, humanExitSide)) {
            throw new IllegalArgumentException("Invalid human hero exit side");
        }

        if (!isExitCompatible(board, machineHeroID, machineExitSide)) {
            throw new IllegalArgumentException("Invalid machine hero exit side");
        }

        if (!isExitCoordinateValid(board, humanHeroID, humanExitCoordinate)) {
            throw new IllegalArgumentException("Invalid human hero exit coordinate");
        }

        if (!isExitCoordinateValid(board, machineHeroID, machineExitCoordinate)) {
            throw new IllegalArgumentException("Invalid machine hero exit coordinate");
        }

        this.board = board;
        this.humanHeroID = humanHeroID;
        this.machineHeroID = machineHeroID;
        this.humanExitCoordinate = humanExitCoordinate;
        this.machineExitCoordinate = machineExitCoordinate;
        this.humanHeroExited = false;
        this.machineHeroExited = false;
        this.humanExitSide = humanExitSide;
        this.machineExitSide = machineExitSide;
    }

    /**
     * Checks whether the specified exit side is compatible with the hero
     * vehicle's orientation.
     *
     * @param board the game board containing the hero vehicle
     * @param heroID the ID of the hero vehicle
     * @param exitSide the assigned exit side
     * @return {@code true} if the exit side matches the vehicle's orientation;
     *         {@code false} otherwise
     */
    private boolean isExitCompatible(Board board, String heroID, ExitSide exitSide) {
        for (Vehicle vehicle : board.getVehicles()) {
            if (vehicle.getId().equals(heroID)) {
                if (vehicle.getOrientation() == Orientation.HORIZONTAL) {
                    return exitSide == ExitSide.LEFT || exitSide == ExitSide.RIGHT;
                }

                return exitSide == ExitSide.TOP || exitSide == ExitSide.BOTTOM;
            }
        }

        return false;
    }

    /**
     * Checks whether the specified exit coordinate aligns with the hero
     * vehicle's row or column.
     *
     * @param board the game board containing the hero vehicle
     * @param heroID the ID of the hero vehicle
     * @param exitCoordinate the coordinate assigned to the exit
     * @return {@code true} if the exit coordinate aligns with the hero vehicle
     *         and is within the board dimensions; {@code false} otherwise
     */
    private boolean isExitCoordinateValid(Board board, String heroID, int exitCoordinate) {
        for (Vehicle vehicle : board.getVehicles()) {
            if (vehicle.getId().equals(heroID)) {
                if (vehicle.getOrientation() == Orientation.HORIZONTAL) {
                    return vehicle.getPosition().getRow() == exitCoordinate
                            && exitCoordinate < board.getRows();
                }

                return vehicle.getPosition().getColumn() == exitCoordinate
                        && exitCoordinate < board.getColumns();
            }
        }

        return false;
    }

    /**
     * Returns the game board.
     *
     * @return the current game board
     */
    public Board getBoard() {
        return board;
    }

    /**
     * Returns the human player's hero vehicle ID.
     *
     * @return the human hero vehicle ID
     */
    public String getHumanHeroID() {
        return humanHeroID;
    }

    /**
     * Returns the machine player's hero vehicle ID.
     *
     * @return the machine hero vehicle ID
     */
    public String getMachineHeroID() {
        return machineHeroID;
    }

    /**
     * Returns the human player's exit coordinate.
     *
     * @return the human exit coordinate
     */
    public int getHumanExitCoordinate() {
        return humanExitCoordinate;
    }

    /**
     * Returns the machine player's exit coordinate.
     *
     * @return the machine exit coordinate
     */
    public int getMachineExitCoordinate() {
        return machineExitCoordinate;
    }

    /**
     * Checks whether the human player's hero vehicle has exited the board.
     *
     * @return {@code true} if the human hero has exited; {@code false} otherwise
     */
    public boolean hasHumanHeroExited() {
        return humanHeroExited;
    }

    /**
     * Checks whether the machine player's hero vehicle has exited the board.
     *
     * @return {@code true} if the machine hero has exited; {@code false} otherwise
     */
    public boolean hasMachineHeroExited() {
        return machineHeroExited;
    }

    /**
     * Updates the exit status of the human player's hero vehicle.
     *
     * @param exited {@code true} if the human hero has exited; {@code false} otherwise
     */
    public void setHumanHeroExited(boolean exited) {
        this.humanHeroExited = exited;
    }

    /**
     * Updates the exit status of the machine player's hero vehicle.
     *
     * @param exited {@code true} if the machine hero has exited; {@code false} otherwise
     */
    public void setMachineHeroExited(boolean exited) {
        this.machineHeroExited = exited;
    }

    /**
     * Returns the side of the board containing the human player's exit.
     *
     * @return the human player's exit side
     */
    public ExitSide getHumanExitSide() {
        return humanExitSide;
    }

    /**
     * Returns the side of the board containing the machine player's exit.
     *
     * @return the machine player's exit side
     */
    public ExitSide getMachineExitSide() {
        return machineExitSide;
    }
}