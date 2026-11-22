package hu.nye.rushhour.engine;

import hu.nye.rushhour.model.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GameEngineTest {

    @Test
    public void shouldCreateValidGameEngineWithValidGameState() {
        Board board = new Board(6, 12);

        Vehicle humanHero = new Vehicle("H", 2, Orientation.HORIZONTAL, new Position(2, 0));

        Vehicle machineHero = new Vehicle("M", 2, Orientation.HORIZONTAL, new Position(3, 10));

        board.addVehicle(humanHero);
        board.addVehicle(machineHero);

        GameState gameState = new GameState(board, "H", "M");

        GameEngine gameEngine = new GameEngine(gameState);

        assertNotNull(gameEngine);
    }

    @Test
    public void shouldRejectNullGameState() {
        assertThrows(NullPointerException.class, () -> new GameEngine(null));
    }
}
