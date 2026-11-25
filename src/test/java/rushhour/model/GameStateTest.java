package rushhour.model;

import org.junit.jupiter.api.Test;
import rushhour.model.*;

import static org.junit.jupiter.api.Assertions.*;

public class GameStateTest {

    @Test
    public void shouldCreateGameStateWithValidValues() {
        Board board = new Board(6, 12);

        Vehicle humanHero = new Vehicle("H", 2, Orientation.HORIZONTAL, new Position(2, 0));

        Vehicle machineHero = new Vehicle("M", 2, Orientation.HORIZONTAL, new Position(3, 0));

        board.addVehicle(humanHero);
        board.addVehicle(machineHero);

        GameState gameState = new GameState(board, "H", "M", 2, 3, ExitSide.RIGHT, ExitSide.LEFT);
        assertEquals(board, gameState.getBoard());
        assertEquals("H", gameState.getHumanHeroID());
        assertEquals("M", gameState.getMachineHeroID());
    }

    @Test
    public void shouldRejectNullBoard() {
        assertThrows(IllegalArgumentException.class, () -> new GameState(null, "H", "M", 0, 0, ExitSide.RIGHT, ExitSide.RIGHT));    }

    @Test
    public void shouldRejectNullOrEmptyHeroIDs() {
        Board board = new Board(6, 12);

        board.addVehicle(new Vehicle("H", 2, Orientation.HORIZONTAL, new Position(2, 0)));
        board.addVehicle(new Vehicle("M", 2, Orientation.HORIZONTAL, new Position(3, 0)));

        assertThrows(IllegalArgumentException.class, () -> new GameState(board, "", "M", 2, 3, ExitSide.RIGHT, ExitSide.LEFT));

        assertThrows(IllegalArgumentException.class, () -> new GameState(board, null, "M", 2, 3, ExitSide.RIGHT, ExitSide.LEFT));

        assertThrows(IllegalArgumentException.class, () -> new GameState(board, "H", "", 2, 3, ExitSide.RIGHT, ExitSide.LEFT));

        assertThrows(IllegalArgumentException.class, () -> new GameState(board, "H", null, 2, 3, ExitSide.RIGHT, ExitSide.LEFT));
    }

    @Test
    public void shouldRejectIdenticalHeroIDs() {
        Board board = new Board(6, 12);

        board.addVehicle(new Vehicle("H", 2, Orientation.HORIZONTAL, new Position(2, 0)));

        assertThrows(IllegalArgumentException.class, () -> new GameState(board, "H", "H", 2, 2, ExitSide.RIGHT, ExitSide.LEFT));    }

    @Test
    public void shouldRejectHeroIdsThatDoNotExistOnBoard() {
        Board board = new Board(6, 12);

        board.addVehicle(new Vehicle("H", 2, Orientation.HORIZONTAL, new Position(2, 0)));

        assertThrows(IllegalArgumentException.class, () -> new GameState(board, "H", "M", 2, 2, ExitSide.RIGHT, ExitSide.RIGHT));
    }
}


