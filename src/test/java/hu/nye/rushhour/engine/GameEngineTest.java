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
        assertThrows(IllegalArgumentException.class, () -> new GameEngine(null));
    }

    @Test
    public void shouldFindVehicleByID() {
        Board board = new Board(6, 12);

        Vehicle humanHero = new Vehicle("H", 2, Orientation.HORIZONTAL, new Position(2, 0));

        Vehicle machineHero = new Vehicle("M", 2, Orientation.HORIZONTAL, new Position(3, 10));

        board.addVehicle(humanHero);
        board.addVehicle(machineHero);
        GameState gameState = new GameState(board, "H", "M");
        GameEngine gameEngine = new GameEngine(gameState);

        Vehicle result = gameEngine.findVehicleById("H");
    }

    @Test
    public void shouldRejectNullOrEmptyVehicleId() {
        Board board = new Board(6, 12);

        Vehicle humanHero = new Vehicle("H", 2, Orientation.HORIZONTAL, new Position(2, 0));

        Vehicle machineHero = new Vehicle("M", 2, Orientation.HORIZONTAL, new Position(3, 10));

        board.addVehicle(humanHero);
        board.addVehicle(machineHero);
        GameState gameState = new GameState(board, "H", "M");
        GameEngine gameEngine = new GameEngine(gameState);

        assertThrows(IllegalArgumentException.class, () -> gameEngine.findVehicleById(""));

        assertThrows(IllegalArgumentException.class, () -> gameEngine.findVehicleById(null));
    }

    @Test
    public void shouldRejectNonExistingVehicleID() {
        Board board = new Board(6, 12);

        Vehicle humanHero = new Vehicle("H", 2, Orientation.HORIZONTAL, new Position(2, 0));

        Vehicle machineHero = new Vehicle("M", 2, Orientation.HORIZONTAL, new Position(3, 10));

        board.addVehicle(humanHero);
        board.addVehicle(machineHero);
        GameState gameState = new GameState(board, "H", "M");
        GameEngine gameEngine = new GameEngine(gameState);

        assertThrows(IllegalArgumentException.class, () -> gameEngine.findVehicleById("X"));
    }
}
