package hu.nye.rushhour.engine;

import hu.nye.rushhour.model.Board;
import hu.nye.rushhour.model.Vehicle;
import hu.nye.rushhour.model.GameState;

public class GameEngine {

    private final GameState gameState;

    /**
     * Create a game engine for the given game state.
     *
     */
    public GameEngine(GameState gameState){
        if(gameState == null){
            throw new NullPointerException("GameState cannot be null null");
        }

        this.gameState = gameState;
    }

    /**
     * Finds a vehicle on the board using its ID.
     *
     * @return the matching vehicle
     */
    public Vehicle findVehicleById(String vehicleId){
        if(vehicleId == null){
            throw new IllegalArgumentException("Vehicle Id cannot be empty.");
        }

        for(Vehicle vehicle : gameState.getBoard().getVehicles()){
            if(vehicle.getId().equals(vehicleId)){
                return vehicle;
            }
        }

        throw new IllegalArgumentException("Vehicle not found: " + vehicleId);
    }
}
