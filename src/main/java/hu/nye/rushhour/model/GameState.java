package hu.nye.rushhour.model;

/**
 * Represents the current state of a Rush Hour game.
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
     * Creates a game state.
     */
    public GameState(Board board, String humanHeroID, String machineHeroID,  int humanExitCoordinate, int machineExitCoordinate,  ExitSide humanExitSide, ExitSide machineExitSide) {
        if(board == null) {
            throw new IllegalArgumentException("Board cannot be null.");
        }
        if(humanHeroID == null || humanHeroID.isBlank() || machineHeroID == null || machineHeroID.isBlank()) {
            throw new IllegalArgumentException("Hero IDs cannot be null.");
        }

        if(!board.containsVehicleId(humanHeroID) || !board.containsVehicleId(machineHeroID)) {
            throw new IllegalArgumentException("Both hero vehicles must exist on the board.");
        }

        if(humanHeroID.equals(machineHeroID)) {
            throw new IllegalArgumentException("Players must have different hero vehicles.");
        }

        if (humanExitCoordinate < 0 || machineExitCoordinate < 0) {
            throw new IllegalArgumentException("Exit coordinates cannot be negative.");
        }

        if(humanExitSide == null || machineExitSide == null){
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
     * Checks whether an exit side matches the vehicle orientation.
     *
     * @return true if exit side matches the vehicle orientation.
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
     * Checks whether the assigned exit coordinate aligns with the hero vehicle.
     *
     * @return true if the exit coordinate aligns with the hero
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
     * @return the current game board.
     */
    public Board getBoard() {
        return board;
    }

    /**
     * Return the human player's hero vehicle ID.
     *
     * @return human hero vehicle ID
     */
    public String getHumanHeroID() {
        return humanHeroID;
    }

    /**
     * Returns the machine player's hero vehicle ID.
     *
     *@return machine hero vehicle ID
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

    public int getMachineExitCoordinate() {
        return machineExitCoordinate;
    }

    /**
     * Returns the machine player's exit coordinate.
     *
     * @return the machine exit coordinate
     */

    /**
     * Checks whether the human hero has exited the board.
     *
     * @return true if the human hero has exited
     */
    public boolean hasHumanHeroExited() {
        return humanHeroExited;
    }

    /**
     * Checks whether the machine hero has exited the board.
     *
     * @return true if the machine hero has exited
     */
    public boolean hasMachineHeroExited() {
        return machineHeroExited;
    }

    /**
     * Updates the human hero's exit status.
     *
     * @param exited whether the human hero has exited
     */
    public void setHumanHeroExited(boolean exited) {
        this.humanHeroExited = exited;
    }

    /**
     * Updates the machine hero's exit status.
     *
     * @param exited whether the machine hero has exited
     */
    public void setMachineHeroExited(boolean exited) {
        this.machineHeroExited = exited;
    }

    /**
     * Returns the human player's exit side.
     *
     * @return the human exit side
     */
    public ExitSide getHumanExitSide() {
        return humanExitSide;
    }

    /**
     * Returns the machine player's exit side.
     *
     * @return the machine exit side
     */
    public ExitSide getMachineExitSide() {
        return machineExitSide;
    }
}
