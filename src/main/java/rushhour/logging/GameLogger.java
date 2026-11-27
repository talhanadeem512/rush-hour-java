
package rushhour.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rushhour.engine.Player;
import rushhour.model.Move;

/**
 * Handles logging of important events during a Rush Hour game.
 */
public class GameLogger {

    private static final Logger LOGGER = LoggerFactory.getLogger(GameLogger.class);

    /**
     * Logs the start of a new game.
     */
    public void logGameStarted() {
        LOGGER.info("A new Rush Hour game has started.");
    }

    /**
     * Logs a successfully executed move.
     *
     * @param player the player who performed the move
     * @param move the move that was executed
     * @throws IllegalArgumentException if the player or move is null
     */
    public void logMove(Player player, Move move) {
        if (player == null || move == null) {
            throw new IllegalArgumentException("Player and move cannot be null.");
        }

        LOGGER.info("{} moved vehicle {} by {} cells.", player, move.getVehicleId(), move.getDistance());
    }

    /**
     * Logs the winner of the game.
     *
     * @param winner the player who won the game
     * @throws IllegalArgumentException if the winner is null
     */
    public void logGameWon(Player winner) {
        if (winner == null) {
            throw new IllegalArgumentException("Winner cannot be null.");
        }

        LOGGER.info("The game has ended. {} is the winner.", winner);
    }
}
