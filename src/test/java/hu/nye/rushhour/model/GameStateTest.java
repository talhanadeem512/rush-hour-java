package hu.nye.rushhour.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GameStateTest {

    @Test
    public void shouldCreateGameStateWithValidValues() {
        Board board = new Board(6, 12);

        Vehicle humanHero = new Vehicle("H", 2, Orientation.HORIZONTAL, new Position(2, 0));

        Vehicle machineHero = new Vehicle("M", 2, Orientation.HORIZONTAL, new Position(3, 0));

        board.addVehicle(humanHero);
        board.addVehicle(machineHero);

        GameState gameState = new GameState(board, "H", "M");
        assertEquals(board, gameState.getBoard());
        assertEquals("H", gameState.getHumanHeroID());
        assertEquals("M", gameState.getMachineHeroID());
    }

    @Test
    public void shouldRejectNullBoard() {
        assertThrows(IllegalArgumentException.class, () -> new GameState(null, "H", "M"));
    }

    @Test
    public void shouldRejectNullOrEmptyHeroIDs() {
        Board board = new Board(6, 12);

        board.addVehicle(new Vehicle("H", 2, Orientation.HORIZONTAL, new Position(2, 0)));
        board.addVehicle(new Vehicle("M", 2, Orientation.HORIZONTAL, new Position(3, 0)));

        assertThrows(IllegalArgumentException.class, () -> new GameState(board, "", "M"));

        assertThrows(IllegalArgumentException.class, () -> new GameState(board, null, "M"));

        assertThrows(IllegalArgumentException.class, () -> new GameState(board, "H", ""));

        assertThrows(IllegalArgumentException.class, () -> new GameState(board, "H", null));
    }

    @Test
    public void shouldRejectIdenticalHeroIDs() {
        Board board = new Board(6, 12);

        board.addVehicle(new Vehicle("H", 2, Orientation.HORIZONTAL, new Position(2, 0)));

        assertThrows(IllegalArgumentException.class, () -> new GameState(board, "H", "H"));
    }

    @Test
    public void shouldRejectHeroIdsThatDoNotExistOnBoard() {
        Board board = new Board(6, 12);

        board.addVehicle(new Vehicle("H", 2, Orientation.HORIZONTAL, new Position(2, 0)));

        assertThrows(IllegalArgumentException.class, () -> new GameState(board, "H", "M"));

    }
}


