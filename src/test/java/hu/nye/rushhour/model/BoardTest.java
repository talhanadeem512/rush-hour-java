package hu.nye.rushhour.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BoardTest {

    @Test
    public void shouldCreateBoardWithValidDimensions() {
        Board board = new Board(6, 12);

        assertEquals(6, board.getRows());
        assertEquals(12, board.getColumns());
    }

    @Test
    public void shouldRejectInvalidBoardDimensions() {
        assertThrows(IllegalArgumentException.class, () -> new Board(0, 12));

        assertThrows(IllegalArgumentException.class, () -> new Board(6, 0));

        assertThrows(IllegalArgumentException.class, () -> new Board(-1, 12));
    }

    @Test
    public void shouldStartWithZeroVehicles() {
        Board board = new Board(6, 12);

        assertEquals(0, board.getVehicleCount());
    }
}
