package hu.nye.rushhour.engine;

/**
 * Manages the turns of the human and machine players.
 */
public class TurnManager {

    private Player currentplayer;

    /**
     * Creates a turn manager with the human player starting first.
     */
    public TurnManager() {
        this.currentplayer = Player.HUMAN;
    }

    /**
     * Returns the player whose turn it currently is.
     *
     * @return the current player.
     */
    public Player getCurrentPlayer() {
        return currentplayer;
    }

    /**
     * Switches the turn to the other players.
     */
    public void switchTurn() {
        if(currentplayer == Player.HUMAN) {
            currentplayer = Player.MACHINE;
        }
        else  {
            currentplayer = Player.HUMAN;
        }
    }
}
