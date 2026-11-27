package rushhour.logging;

import org.junit.jupiter.api.Test;
import rushhour.engine.Player;
import rushhour.model.Move;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class GameLoggerTest {
    private final GameLogger gameLogger = new GameLogger();

    @Test
    void shouldLogGameStart(){
        assertDoesNotThrow(() -> gameLogger.logGameStarted());
    }

    @Test
    void shouldLogValidMove(){
        Move move = new Move("H", 2);

        assertDoesNotThrow(() -> gameLogger.logMove(Player.HUMAN, move));
    }

    @Test
    void shouldRejectNullPlayerWhenLoggingMove(){
        Move move = new Move("H", 2);

        assertThrows(IllegalArgumentException.class, () -> gameLogger.logMove(null, move));
    }

    @Test
    void shouldRejectNullMoveWhenLoggingMove() {
        assertThrows(
                IllegalArgumentException.class,
                () -> gameLogger.logMove(Player.HUMAN, null)
        );
    }

    @Test
    void shouldLogGameWinner() {
        assertDoesNotThrow(() -> gameLogger.logGameWon(Player.HUMAN));
    }

    @Test
    void shouldRejectNullWinner() {
        assertThrows(
                IllegalArgumentException.class,
                () -> gameLogger.logGameWon(null)
        );
    }
}
