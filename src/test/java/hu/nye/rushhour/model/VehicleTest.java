package hu.nye.rushhour.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class VehicleTest {

    @Test
    void shouldCreateVehicleWithValidValues(){
        Position position = new Position(2,3);

        Vehicle vehicle = new Vehicle (
                "A",
                2,
                Orientation.HORIZONTAL,
                position
        );

        assertEquals("A", vehicle.getId());
        assertEquals(2, vehicle.getLength());
        assertEquals(Orientation.HORIZONTAL, vehicle.getOrientation());
        assertEquals(position, vehicle.getPosition());
    }

    @Test
    void shouldRejectEmptyVehicleId() {
        Position position = new Position(2, 3);

        assertThrows(IllegalArgumentException.class, () ->
                new Vehicle("", 2, Orientation.HORIZONTAL, position)
        );
    }

    @Test
    void shouldRejectNullVehicleId() {
        Position position = new Position(2, 3);

        assertThrows(IllegalArgumentException.class, () ->
                new Vehicle(null, 2, Orientation.HORIZONTAL, position)
        );
    }

    @Test
    void shouldRejectInvalidVehicleLength() {
        Position position = new Position(2, 3);

        assertThrows(IllegalArgumentException.class, () ->
                new Vehicle("A", 1, Orientation.HORIZONTAL, position)
        );

        assertThrows(IllegalArgumentException.class, () ->
                new Vehicle("A", 4, Orientation.HORIZONTAL, position)
        );
    }

    @Test
    void shouldRejectNullOrientation() {
        Position position = new Position(2, 3);

        assertThrows(IllegalArgumentException.class, () ->
                new Vehicle("A", 2, null, position)
        );
    }

    @Test
    void shouldRejectNullPosition() {
        assertThrows(IllegalArgumentException.class, () ->
                new Vehicle("A", 2, Orientation.HORIZONTAL, null)
        );
    }
}
