package hu.nye.rushhour.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MoveTest {
    @Test
    public void shouldCreateMoveWithValidValues() {
        Move move = new Move("H", 2);

        assertEquals("H", move.getVehicleId());
        assertEquals(2, move.getDistance());
    }

    @Test
    public void shouldRejectEmptyVehicleId() {
        assertThrows(IllegalArgumentException.class, () -> new Move("", 2));
    }

    @Test
    public void shouldRejectNullVehicleId() {
        assertThrows(IllegalArgumentException.class, () -> new Move(null, 2));
    }

    @Test
    public void shouldRejectZeroDistance() {
        assertThrows(IllegalArgumentException.class, () -> new Move("H", 0));
    }


}
