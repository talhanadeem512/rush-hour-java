package hu.nye.rushhour.model;

/**
 * Represents the current state of a Rush Hour game.
 */
public class GameState {

    private final Board board;
    public final String humanHeroID;
    public final String machineHeroID;

    /**
     * Creates a game state.
     */
    public GameState(Board board, String humanHeroID, String machineHeroID) {
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

        this.board = board;
        this.humanHeroID = humanHeroID;
        this.machineHeroID = machineHeroID;
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
}
