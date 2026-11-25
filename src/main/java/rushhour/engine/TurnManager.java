package rushhour.engine;

/**
 * Manages the turns of the human and machine players.
 */
public class TurnManager {

    private Player currentPlayer;

    /**
     * Creates a turn manager with the human player starting first.
     */
    public TurnManager() {
        this.currentPlayer = Player.HUMAN;
    }

    /**
     * Returns the player whose turn it currently is.
     *
     * @return the current player
     */
    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * Switches the turn to the other player.
     */
    public void switchTurn() {
        if (currentPlayer == Player.HUMAN) {
            currentPlayer = Player.MACHINE;
        } else {
            currentPlayer = Player.HUMAN;
        }
    }
}