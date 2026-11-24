package hu.nye.rushhour.engine;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TurnManagerTest {

    @Test
    void shouldStartWihHumanPlayer() {
        TurnManager turnManager = new TurnManager();

        assertEquals(Player.HUMAN, turnManager.getCurrentPlayer());
    }

    @Test
    void shouldSwitchFromHumanToMachine() {
        TurnManager turnManager = new TurnManager();

        turnManager.switchTurn();

        assertEquals(Player.MACHINE, turnManager.getCurrentPlayer());
    }

    @Test
    void shouldSwitchFromMachineToHuman() {
        TurnManager turnManager = new TurnManager();

        turnManager.switchTurn();
        turnManager.switchTurn();

        assertEquals(Player.HUMAN, turnManager.getCurrentPlayer());
    }
}
