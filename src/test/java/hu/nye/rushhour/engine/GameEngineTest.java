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

    @Test
    void shouldAllowValidHorizontalMovement() {
        Board board = new Board(6, 12);

        board.addVehicle(new Vehicle("H", 2, Orientation.HORIZONTAL, new Position(2, 1)));

        board.addVehicle(new Vehicle("M", 2, Orientation.VERTICAL, new Position(0, 10)));

        GameState gameState = new GameState(board, "H", "M");
        GameEngine gameEngine = new GameEngine(gameState);

        assertTrue(gameEngine.isMoveValid(new Move("H", 2)));
    }

    @Test
    void shouldAllowValidVerticalMovement() {
        Board board = new Board(6, 12);

        board.addVehicle(new Vehicle("H", 2, Orientation.VERTICAL, new Position(0, 2)));

        board.addVehicle(new Vehicle("M", 2, Orientation.HORIZONTAL, new Position(4, 8)));

        GameState gameState = new GameState(board, "H", "M");
        GameEngine gameEngine = new GameEngine(gameState);

        assertTrue(gameEngine.isMoveValid(new Move("H", 2)));
    }

    @Test
    void shouldAllowValidBackwardsMovement() {
        Board board = new Board(6, 12);

        board.addVehicle(new Vehicle("H", 2, Orientation.HORIZONTAL, new Position(2, 4)));

        board.addVehicle(new Vehicle("M", 2, Orientation.VERTICAL, new Position(0, 10)));

        GameState gameState = new GameState(board, "H", "M");
        GameEngine gameEngine = new GameEngine(gameState);

        assertTrue(gameEngine.isMoveValid(new Move("H", -2)));
    }

    @Test
    void shouldRejectMovementOutsideBoard() {
        Board board = new Board(6, 12);

        board.addVehicle(new Vehicle(
                "A", 2, Orientation.HORIZONTAL, new Position(2, 9)
        ));

        board.addVehicle(new Vehicle(
                "B", 2, Orientation.VERTICAL, new Position(0, 0)
        ));

        GameState state = new GameState(board, "A", "B");
        GameEngine engine = new GameEngine(state);

        assertFalse(engine.isMoveValid(new Move("A", 2)));
    }

    @Test
    void shouldRejectMovementIntoAnotherVehicle() {
        Board board = new Board(6, 12);

        board.addVehicle(new Vehicle(
                "A", 2, Orientation.HORIZONTAL, new Position(2, 1)
        ));

        board.addVehicle(new Vehicle(
                "B", 2, Orientation.HORIZONTAL, new Position(2, 4)
        ));

        GameState state = new GameState(board, "A", "B");
        GameEngine engine = new GameEngine(state);

        assertFalse(engine.isMoveValid(new Move("A", 2)));
    }

    @Test
    void shouldRejectMovementThroughAnotherVehicle() {
        Board board = new Board(6, 12);

        board.addVehicle(new Vehicle(
                "A", 2, Orientation.HORIZONTAL, new Position(2, 1)
        ));

        board.addVehicle(new Vehicle(
                "B", 2, Orientation.VERTICAL, new Position(1, 4)
        ));

        GameState state = new GameState(board, "A", "B");
        GameEngine engine = new GameEngine(state);

        assertFalse(engine.isMoveValid(new Move("A", 4)));
    }

    @Test
    void shouldRejectNullMove() {
        Board board = new Board(6, 12);

        board.addVehicle(new Vehicle(
                "A", 2, Orientation.HORIZONTAL, new Position(2, 1)
        ));

        board.addVehicle(new Vehicle(
                "B", 2, Orientation.VERTICAL, new Position(0, 10)
        ));

        GameState state = new GameState(board, "A", "B");
        GameEngine engine = new GameEngine(state);

        assertFalse(engine.isMoveValid(null));
    }

    @Test
    void shouldRejectMoveForNonExistingVehicle() {
        Board board = new Board(6, 12);

        board.addVehicle(new Vehicle(
                "A", 2, Orientation.HORIZONTAL, new Position(2, 1)
        ));

        board.addVehicle(new Vehicle(
                "B", 2, Orientation.VERTICAL, new Position(0, 10)
        ));

        GameState state = new GameState(board, "A", "B");
        GameEngine engine = new GameEngine(state);

        assertFalse(engine.isMoveValid(new Move("C", 2)));
    }

    @Test
    void shouldExecuteValidHorizontalMove(){
        Board board = new Board(6, 12);

        Vehicle vehicle = new Vehicle("H", 2, Orientation.HORIZONTAL, new Position(2, 1));

        Vehicle vehicle2 = new Vehicle("M", 2, Orientation.VERTICAL, new Position(4, 8));

        board.addVehicle(vehicle);
        board.addVehicle(vehicle2);

        GameState state = new GameState(board, "H", "M");
        GameEngine engine = new GameEngine(state);

        Move move = new Move("H", 2);

        engine.executeMove(move);

        assertEquals(3, board.getVehicles().get(0).getPosition().getColumn());
    }

    @Test
    void shouldExecuteValidVerticalMove(){
        Board board = new Board(6, 12);

        Vehicle vehicle = new Vehicle("H", 2, Orientation.VERTICAL, new Position(2, 1));

        Vehicle vehicle2 = new Vehicle("M", 2, Orientation.HORIZONTAL, new Position(4, 8));

        board.addVehicle(vehicle);
        board.addVehicle(vehicle2);

        GameState state = new GameState(board, "H", "M");
        GameEngine engine = new GameEngine(state);

        Move move = new Move("H", 2);

        engine.executeMove(move);

        assertEquals(4, board.getVehicles().get(0).getPosition().getRow());
    }

    @Test
    void shouldRejectInvalidMove(){
        Board board = new Board(6, 12);

        Vehicle vehicle = new Vehicle("H", 2, Orientation.HORIZONTAL, new Position(2, 9));
        Vehicle vehicle2 = new Vehicle("M", 2, Orientation.VERTICAL, new Position(4, 8));

        board.addVehicle(vehicle);
        board.addVehicle(vehicle2);

        GameState state = new GameState(board, "H", "M");
        GameEngine engine = new GameEngine(state);

        Move move = new Move("H", 2);

        assertThrows(IllegalArgumentException.class, () -> engine.executeMove(move));
        assertEquals(9, board.getVehicles().get(0).getPosition().getColumn());
    }
}
