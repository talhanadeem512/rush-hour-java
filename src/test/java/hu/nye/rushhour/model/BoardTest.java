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

    @Test
    public void shouldUpdateAnExistingVehicle() {
        Board board = new Board(6, 12);

        Vehicle original = new Vehicle("H", 2, Orientation.HORIZONTAL, new Position(2, 1));

        board.addVehicle(original);

        Vehicle updated = new Vehicle("H", 2, Orientation.HORIZONTAL, new Position(2, 4));

        board.updateVehicle(updated);

        assertEquals(1, board.getVehicleCount());
        assertEquals(4, board.getVehicles().get(0).getPosition().getColumn());
    }

    @Test
    void shouldRejectNullVehicleUpdate() {
        Board board = new Board(6, 12);

        assertThrows(IllegalArgumentException.class, () -> board.updateVehicle(null));
    }

    @Test
    void shouldRejectUpdateForNonExistingVehicle(){
        Board board = new Board(6, 12);

        Vehicle vehicle = new Vehicle("H",  2, Orientation.HORIZONTAL, new Position(2, 1) );

        assertThrows(IllegalArgumentException.class, () -> board.updateVehicle(vehicle));
    }

    @Test
    void shouldRejectUpdateOutsideTheBoard(){
        Board board = new Board(6, 12);

        Vehicle vehicle = new Vehicle("H",  2, Orientation.HORIZONTAL, new Position(2, 9));

        board.addVehicle(vehicle);

        Vehicle updated = new Vehicle("H",  2, Orientation.HORIZONTAL, new Position(2, 13));

        assertThrows(IllegalArgumentException.class, () -> board.updateVehicle(updated));

        assertEquals(9, board.getVehicles().get(0).getPosition().getColumn());
    }

    @Test
    void shouldRejectAnOverlappingUpdate(){
        Board board = new Board(6, 12);

        Vehicle vehicle1 = new Vehicle("H",  2, Orientation.HORIZONTAL, new Position(2, 1));

        Vehicle vehicle2 = new Vehicle("M",  3, Orientation.VERTICAL, new Position(2, 4));

        board.addVehicle(vehicle1);
        board.addVehicle(vehicle2);

        Vehicle updatedVehicle = new Vehicle("H", 2, Orientation.HORIZONTAL, new Position(2, 3));

        assertThrows(IllegalArgumentException.class, () -> board.updateVehicle(updatedVehicle));
    }

    @Test
    void shouldPreserveOriginalVehicleAfterRejectionUpdate(){
        Board board = new Board(6, 12);

        Vehicle vehicle1 = new Vehicle("H",  2, Orientation.HORIZONTAL, new Position(2, 1));

        Vehicle vehicle2 = new Vehicle("M",  3, Orientation.VERTICAL, new Position(2, 4));

        board.addVehicle(vehicle1);
        board.addVehicle(vehicle2);

        Vehicle updatedVehicle = new Vehicle("H", 2, Orientation.HORIZONTAL, new Position(2, 3));

        assertThrows(IllegalArgumentException.class, () -> board.updateVehicle(updatedVehicle));

        assertEquals(2,  board.getVehicleCount());
        assertEquals(1,  board.getVehicles().get(0).getPosition().getColumn());
        assertEquals(4,  board.getVehicles().get(1).getPosition().getColumn());
    }
}
