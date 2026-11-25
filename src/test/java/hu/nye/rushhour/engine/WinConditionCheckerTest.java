package hu.nye.rushhour.engine;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import hu.nye.rushhour.model.Board;
import hu.nye.rushhour.model.ExitSide;
import hu.nye.rushhour.model.GameState;
import hu.nye.rushhour.model.Orientation;
import hu.nye.rushhour.model.Position;
import hu.nye.rushhour.model.Vehicle;
import org.junit.jupiter.api.Test;

public class WinConditionCheckerTest {
    @Test
    void shouldReturnFalseWhenNobodyHasWon() {
        Board board = new Board(6, 12);
        board.addVehicle(new Vehicle("H", 2, Orientation.HORIZONTAL, new Position(2, 0)));
        board.addVehicle(new Vehicle("M", 2, Orientation.HORIZONTAL, new Position(3, 0)));

        GameState gameState = new GameState(
                board, "H", "M", 2, 3,
                ExitSide.RIGHT, ExitSide.LEFT
        );

        WinConditionChecker checker = new WinConditionChecker();

        assertFalse(checker.hasHumanWon(gameState));
        assertFalse(checker.hasMachineWon(gameState));
    }

    @Test
    void shouldReturnTrueWhenHumanHeroHasExited() {
        Board board = new Board(6, 12);
        board.addVehicle(new Vehicle("H", 2, Orientation.HORIZONTAL, new Position(2, 0)));
        board.addVehicle(new Vehicle("M", 2, Orientation.HORIZONTAL, new Position(3, 0)));

        GameState gameState = new GameState(
                board, "H", "M", 2, 3,
                ExitSide.RIGHT, ExitSide.LEFT
        );

        gameState.setHumanHeroExited(true);

        WinConditionChecker checker = new WinConditionChecker();

        assertTrue(checker.hasHumanWon(gameState));
        assertFalse(checker.hasMachineWon(gameState));
    }

    @Test
    void shouldReturnTrueWhenMachineHeroHasExited() {
        Board board = new Board(6, 12);
        board.addVehicle(new Vehicle("H", 2, Orientation.HORIZONTAL, new Position(2, 0)));
        board.addVehicle(new Vehicle("M", 2, Orientation.HORIZONTAL, new Position(3, 0)));

        GameState gameState = new GameState(
                board, "H", "M", 2, 3,
                ExitSide.RIGHT, ExitSide.LEFT
        );

        gameState.setMachineHeroExited(true);

        WinConditionChecker checker = new WinConditionChecker();

        assertFalse(checker.hasHumanWon(gameState));
        assertTrue(checker.hasMachineWon(gameState));
    }
}
