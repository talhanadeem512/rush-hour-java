package rushhour.ai;

import org.junit.jupiter.api.Test;
import rushhour.ai.MoveGenerator;
import rushhour.engine.GameEngine;
import rushhour.model.Board;
import rushhour.model.GameState;
import rushhour.model.Move;
import rushhour.model.Orientation;
import rushhour.model.ExitSide;
import rushhour.model.Position;
import rushhour.model.Vehicle;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class MoveGeneratorTest {

    @Test
    void shouldGenerateAllValidMoves() {
        Board board = new Board(6, 6);

        Vehicle humanHero = new Vehicle("H", 2, Orientation.HORIZONTAL, new Position(2, 1));
        Vehicle machineHero = new Vehicle("M", 2, Orientation.HORIZONTAL, new Position(4, 1));

        board.addVehicle(humanHero);
        board.addVehicle(machineHero);

        GameState gameState = new GameState(board, "H", "M", 2, 4, ExitSide.RIGHT, ExitSide.RIGHT);
        GameEngine gameEngine = new GameEngine(gameState);
        MoveGenerator moveGenerator = new MoveGenerator();

        List<Move> validMoves = moveGenerator.generateValidMoves(gameState, gameEngine);

        assertFalse(validMoves.isEmpty());
        assertTrue(validMoves.stream().anyMatch((move -> move.getVehicleId().equals("H"))));
        assertTrue(validMoves.stream().anyMatch((move -> move.getVehicleId().equals("M"))));
    }

    @Test
    void shouldReturnEmptyListWhenBoardHasNoVehicles() {
        Board board = new Board(4, 4);

        Vehicle humanHero = new Vehicle(
                "H", 2, Orientation.HORIZONTAL, new Position(1, 1)
        );

        Vehicle machineHero = new Vehicle(
                "M", 2, Orientation.HORIZONTAL, new Position(3, 1)
        );

        board.addVehicle(humanHero);
        board.addVehicle(machineHero);

        GameState gameState = new GameState(
                board,
                "H",
                "M",
                1,
                3,
                ExitSide.RIGHT,
                ExitSide.RIGHT
        );

        GameEngine gameEngine = new GameEngine(gameState);
        MoveGenerator moveGenerator = new MoveGenerator();

        board.removeVehicleById("H");
        board.removeVehicleById("M");

        List<Move> validMoves = moveGenerator.generateValidMoves(
                gameState,
                gameEngine
        );

        assertTrue(validMoves.isEmpty());
    }

    @Test
    void shouldRejectNullGameState() {
        // Use the same board and vehicle setup as shouldGenerateAllValidMoves()

        Board board = new Board(6, 6);

        Vehicle humanHero = new Vehicle(
                "H", 2, Orientation.HORIZONTAL, new Position(2, 1)
        );

        Vehicle machineHero = new Vehicle(
                "M", 2, Orientation.HORIZONTAL, new Position(4, 1)
        );

        board.addVehicle(humanHero);
        board.addVehicle(machineHero);

        GameState gameState = new GameState(
                board, "H", "M", 2, 4,
                ExitSide.RIGHT, ExitSide.RIGHT
        );

        GameEngine gameEngine = new GameEngine(gameState);
        MoveGenerator moveGenerator = new MoveGenerator();

        assertThrows(
                IllegalArgumentException.class,
                () -> moveGenerator.generateValidMoves(null, gameEngine)
        );
    }

    @Test
    void shouldRejectNullGameEngine() {
        // Use the same board and vehicle setup as shouldGenerateAllValidMoves()

        Board board = new Board(6, 6);

        Vehicle humanHero = new Vehicle(
                "H", 2, Orientation.HORIZONTAL, new Position(2, 1)
        );

        Vehicle machineHero = new Vehicle(
                "M", 2, Orientation.HORIZONTAL, new Position(4, 1)
        );

        board.addVehicle(humanHero);
        board.addVehicle(machineHero);

        GameState gameState = new GameState(
                board, "H", "M", 2, 4,
                ExitSide.RIGHT, ExitSide.RIGHT
        );

        GameEngine gameEngine = new GameEngine(gameState);
        MoveGenerator moveGenerator = new MoveGenerator();

        assertThrows(
                IllegalArgumentException.class,
                () -> moveGenerator.generateValidMoves(gameState, null)
        );
    }

    @Test
    void shouldNotModifyBoardWhenGeneratingMoves() {
        Board board = new Board(6, 6);

        Vehicle humanHero = new Vehicle(
                "H", 2, Orientation.HORIZONTAL, new Position(2, 1)
        );

        Vehicle machineHero = new Vehicle(
                "M", 2, Orientation.HORIZONTAL, new Position(4, 1)
        );

        board.addVehicle(humanHero);
        board.addVehicle(machineHero);

        GameState gameState = new GameState(
                board, "H", "M", 2, 4,
                ExitSide.RIGHT, ExitSide.RIGHT
        );

        GameEngine gameEngine = new GameEngine(gameState);
        MoveGenerator moveGenerator = new MoveGenerator();

        List<Vehicle> vehiclesBefore = List.copyOf(board.getVehicles());

        moveGenerator.generateValidMoves(gameState, gameEngine);

        List<Vehicle> vehiclesAfter = board.getVehicles();

        assertEquals(vehiclesBefore, vehiclesAfter);
    }
}
