package rushhour.engine;

import org.junit.jupiter.api.Test;
import rushhour.engine.GameEngine;
import rushhour.engine.Player;
import rushhour.model.*;
import rushhour.ai.MoveGenerator;
import rushhour.ai.MoveSelector;

import static org.junit.jupiter.api.Assertions.*;

public class GameEngineTest {

    @Test
    public void shouldCreateValidGameEngineWithValidGameState() {
        Board board = new Board(6, 12);

        Vehicle humanHero = new Vehicle("H", 2, Orientation.HORIZONTAL, new Position(2, 0));

        Vehicle machineHero = new Vehicle("M", 2, Orientation.HORIZONTAL, new Position(3, 10));

        board.addVehicle(humanHero);
        board.addVehicle(machineHero);

        GameState gameState = new GameState(board, "H", "M", 2, 3, ExitSide.RIGHT, ExitSide.RIGHT);

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
        GameState gameState = new GameState(board, "H", "M", 2, 3, ExitSide.RIGHT, ExitSide.RIGHT);
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
        GameState gameState = new GameState(board, "H", "M", 2, 3, ExitSide.RIGHT, ExitSide.RIGHT);
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
        GameState gameState = new GameState(board, "H", "M", 2, 3, ExitSide.RIGHT, ExitSide.RIGHT);
        GameEngine gameEngine = new GameEngine(gameState);

        assertThrows(IllegalArgumentException.class, () -> gameEngine.findVehicleById("X"));
    }

    @Test
    void shouldAllowValidHorizontalMovement() {
        Board board = new Board(6, 12);

        board.addVehicle(new Vehicle("H", 2, Orientation.HORIZONTAL, new Position(2, 1)));

        board.addVehicle(new Vehicle("M", 2, Orientation.VERTICAL, new Position(0, 10)));

        GameState gameState = new GameState(board, "H", "M", 2, 10, ExitSide.RIGHT, ExitSide.BOTTOM);
        GameEngine gameEngine = new GameEngine(gameState);

        assertTrue(gameEngine.isMoveValid(new Move("H", 2)));
    }

    @Test
    void shouldAllowValidVerticalMovement() {
        Board board = new Board(6, 12);

        board.addVehicle(new Vehicle("H", 2, Orientation.VERTICAL, new Position(0, 2)));

        board.addVehicle(new Vehicle("M", 2, Orientation.HORIZONTAL, new Position(4, 8)));

        GameState gameState = new GameState(board, "H", "M", 2, 4, ExitSide.BOTTOM, ExitSide.RIGHT);
        GameEngine gameEngine = new GameEngine(gameState);

        assertTrue(gameEngine.isMoveValid(new Move("H", 2)));
    }


    @Test
    void shouldAllowValidBackwardsMovement() {
        Board board = new Board(6, 12);

        board.addVehicle(new Vehicle("H", 2, Orientation.HORIZONTAL, new Position(2, 4)));

        board.addVehicle(new Vehicle("M", 2, Orientation.VERTICAL, new Position(0, 10)));

        GameState gameState = new GameState(board, "H", "M", 2, 10, ExitSide.RIGHT, ExitSide.BOTTOM);
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

        GameState state = new GameState(board, "A", "B", 2, 0, ExitSide.RIGHT, ExitSide.BOTTOM);
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

        GameState state = new GameState(board, "A", "B", 2, 2, ExitSide.RIGHT, ExitSide.LEFT);
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

        GameState state = new GameState(board, "A", "B", 2, 4, ExitSide.RIGHT, ExitSide.BOTTOM);
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

        GameState state = new GameState(board, "A", "B", 2, 10, ExitSide.RIGHT, ExitSide.BOTTOM);
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

        GameState state = new GameState(board, "A", "B", 2, 10, ExitSide.RIGHT, ExitSide.BOTTOM);
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

        GameState state = new GameState(board, "H", "M", 2, 8, ExitSide.RIGHT, ExitSide.BOTTOM);
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

        GameState state = new GameState(board, "H", "M", 1, 4, ExitSide.BOTTOM, ExitSide.RIGHT);
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

        GameState state = new GameState(board, "H", "M", 2, 8, ExitSide.RIGHT, ExitSide.BOTTOM);
        GameEngine engine = new GameEngine(state);

        Move move = new Move("H", 2);

        assertThrows(IllegalArgumentException.class, () -> engine.executeMove(move));
        assertEquals(9, board.getVehicles().get(0).getPosition().getColumn());
    }

    @Test
    void shouldAllowHUmanToMoveFirst(){
        Board board = new Board(6, 12);

        Vehicle humanVehicle = new Vehicle("H", 2, Orientation.HORIZONTAL, new Position(2, 1));

        Vehicle machineVehicle = new Vehicle("M", 2, Orientation.VERTICAL, new Position(4, 8));

        board.addVehicle(humanVehicle);
        board.addVehicle(machineVehicle);

        GameState state = new GameState(board, "H", "M", 2, 8, ExitSide.RIGHT, ExitSide.BOTTOM);
        GameEngine engine = new GameEngine(state);

        engine.executeTurnMove(Player.HUMAN, new Move("H", 2));

        assertEquals(Player.MACHINE, engine.getCurrentPlayer());
        assertEquals(3, board.getVehicles().get(0).getPosition().getColumn());
    }

    @Test
    void shouldRejectMachineTurnWhenItsHumanTurn(){
        Board board = new Board(6, 12);

        board.addVehicle(new Vehicle("H", 2, Orientation.HORIZONTAL, new Position(2, 1)));
        board.addVehicle(new Vehicle("M", 2, Orientation.VERTICAL, new Position(4, 8)));

        GameState state = new GameState(board, "H", "M", 2, 8, ExitSide.RIGHT, ExitSide.BOTTOM);
        GameEngine engine = new GameEngine(state);

        assertThrows(IllegalArgumentException.class, engine::executeMachineTurn);
        assertEquals(Player.HUMAN, engine.getCurrentPlayer());
    }
    @Test
    void shouldRejectMachineMoveOnHumanTurn(){
        Board board = new Board(6, 12);

        board.addVehicle(new Vehicle("H", 2, Orientation.HORIZONTAL, new Position(2, 1)));
        board.addVehicle(new Vehicle("M", 2, Orientation.VERTICAL, new Position(4, 8)));

        GameState state = new GameState(board, "H", "M", 2, 8, ExitSide.RIGHT, ExitSide.BOTTOM);
        GameEngine engine = new GameEngine(state);

        assertThrows(IllegalArgumentException.class, () -> engine.executeTurnMove(Player.MACHINE, new Move("M", 1)));
        assertEquals(Player.HUMAN, engine.getCurrentPlayer());
    }

    @Test
    void shouldNotSwitchTurnAfterInvalidMove(){
        Board board = new Board(6, 12);
        board.addVehicle(new Vehicle("H", 2, Orientation.HORIZONTAL, new Position(2, 1)));
        board.addVehicle(new Vehicle("M", 2, Orientation.VERTICAL, new Position(4, 8)));

        GameState state = new GameState(board, "H", "M", 2, 8, ExitSide.RIGHT, ExitSide.BOTTOM);
        GameEngine engine = new GameEngine(state);

        assertThrows(IllegalArgumentException.class, () -> engine.executeTurnMove(Player.HUMAN, new Move("H", 10)));
        assertEquals(Player.HUMAN, engine.getCurrentPlayer());
        assertEquals(1, board.getVehicles().get(0).getPosition().getColumn());
    }

    @Test
    public void shouldDetectHumanWinner(){
        Board board = new Board(6, 12);

        board.addVehicle(new Vehicle("H", 2, Orientation.HORIZONTAL, new Position(2, 0)));
        board.addVehicle(new Vehicle("M", 2, Orientation.HORIZONTAL, new Position(3, 0)));

        GameState gameState = new GameState(board, "H", "M", 2, 3, ExitSide.RIGHT, ExitSide.LEFT);

        gameState.setHumanHeroExited(true);

        GameEngine engine = new GameEngine(gameState);

        assertTrue(engine.hasHumanWon());
        assertFalse(engine.hasMachineWon());
    }

    @Test
    public void shouldDetectMachineWin() {
        Board board = new Board(6, 12);

        board.addVehicle(new Vehicle(
                "H", 2, Orientation.HORIZONTAL, new Position(2, 0)
        ));
        board.addVehicle(new Vehicle(
                "M", 2, Orientation.HORIZONTAL, new Position(3, 0)
        ));

        GameState gameState = new GameState(
                board, "H", "M", 2, 3, ExitSide.RIGHT, ExitSide.LEFT
        );

        gameState.setMachineHeroExited(true);

        GameEngine gameEngine = new GameEngine(gameState);

        assertFalse(gameEngine.hasHumanWon());
        assertTrue(gameEngine.hasMachineWon());
    }

    @Test
    void shouldRemoveHumanHeroAndDeclareHumanWinnerWhenHeroExits() {
        Board board = new Board(6, 12);

        board.addVehicle(new Vehicle(
                "H", 2, Orientation.HORIZONTAL, new Position(2, 8)
        ));

        board.addVehicle(new Vehicle(
                "M", 2, Orientation.HORIZONTAL, new Position(3, 0)
        ));

        GameState gameState = new GameState(
                board, "H", "M", 2, 3, ExitSide.RIGHT, ExitSide.LEFT
        );

        GameEngine gameEngine = new GameEngine(gameState);

        gameEngine.executeMove(new Move("H", 4));

        assertTrue(gameState.hasHumanHeroExited());
        assertTrue(gameEngine.hasHumanWon());
        assertFalse(gameEngine.hasMachineWon());
        assertFalse(board.containsVehicleId("H"));
        assertEquals(1, board.getVehicleCount());
    }

    @Test
    void shouldRemoveMachineHeroAndDeclareMachineWinnerWhenHeroExits() {
        Board board = new Board(6, 12);

        board.addVehicle(new Vehicle(
                "H", 2, Orientation.HORIZONTAL, new Position(2, 8)
        ));

        board.addVehicle(new Vehicle(
                "M", 2, Orientation.HORIZONTAL, new Position(3, 0)
        ));

        GameState gameState = new GameState(
                board, "H", "M", 2, 3, ExitSide.RIGHT, ExitSide.LEFT
        );

        GameEngine gameEngine = new GameEngine(gameState);

        gameEngine.executeMove(new Move("M", -2));

        assertTrue(gameState.hasMachineHeroExited());
        assertTrue(gameEngine.hasMachineWon());
        assertFalse(gameEngine.hasHumanWon());
        assertFalse(board.containsVehicleId("M"));
        assertEquals(1, board.getVehicleCount());
    }

    @Test
    void shouldRejectMovesAfterHumanHasWon() {
        Board board = new Board(6, 12);

        board.addVehicle(new Vehicle("H", 2, Orientation.HORIZONTAL, new Position(2, 0)));
        board.addVehicle(new Vehicle("M", 2, Orientation.HORIZONTAL, new Position(3, 0)));

        GameState gameState = new GameState(board, "H", "M", 2, 3, ExitSide.RIGHT, ExitSide.LEFT);
        gameState.setHumanHeroExited(true);
        GameEngine gameEngine = new GameEngine(gameState);
        assertThrows(IllegalArgumentException.class, () -> gameEngine.executeMove(new Move("M", 1)));
    }

    @Test
    void shouldExecuteMachineTurnAndSwitchBackToHuman() {
        Board board = new Board(6, 12);

        board.addVehicle(new Vehicle("H", 2, Orientation.HORIZONTAL, new Position(2, 1)));
        board.addVehicle(new Vehicle("M", 2, Orientation.HORIZONTAL, new Position(4, 1)));

        GameState state = new GameState(board, "H", "M", 2, 4, ExitSide.RIGHT, ExitSide.RIGHT);

        GameEngine engine = new GameEngine(state);

        engine.executeTurnMove(Player.HUMAN, new Move("H", 1));

        assertEquals(Player.MACHINE, engine.getCurrentPlayer());

        engine.executeMachineTurn();

        assertFalse(engine.hasHumanWon());
        assertFalse(engine.hasMachineWon());
    }

    @Test
    void shouldNotSwitchTurnAfterMachineWins() {
        Board board = new Board(6, 12);

        board.addVehicle(new Vehicle(
                "H", 2, Orientation.HORIZONTAL, new Position(2, 1)
        ));
        board.addVehicle(new Vehicle(
                "M", 2, Orientation.HORIZONTAL, new Position(3, 10)
        ));

        GameState state = new GameState(
                board, "H", "M", 2, 3, ExitSide.RIGHT, ExitSide.RIGHT
        );

        GameEngine engine = new GameEngine(state);

        engine.executeTurnMove(Player.HUMAN, new Move("H", 1));
        engine.executeTurnMove(Player.MACHINE, new Move("M", 2));

        assertTrue(engine.hasMachineWon());
        assertFalse(engine.hasHumanWon());
        assertEquals(Player.MACHINE, engine.getCurrentPlayer());
    }
}
