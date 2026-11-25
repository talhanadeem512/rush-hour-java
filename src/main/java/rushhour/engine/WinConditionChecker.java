package rushhour.engine;

import rushhour.model.GameState;

/**
 * Checks whether either player has won the Rush Hour game.
 */
public class WinConditionChecker {

    /**
     * Checks whether the human player's hero vehicle has exited the board.
     *
     * @param gameState the current state of the game
     * @return {@code true} if the human player's hero has exited; {@code false} otherwise
     */
    public boolean hasHumanWon(GameState gameState) {
        return gameState.hasHumanHeroExited();
    }

    /**
     * Checks whether the machine player's hero vehicle has exited the board.
     *
     * @param gameState the current state of the game
     * @return {@code true} if the machine player's hero has exited; {@code false} otherwise
     */
    public boolean hasMachineWon(GameState gameState) {
        return gameState.hasMachineHeroExited();
    }
}