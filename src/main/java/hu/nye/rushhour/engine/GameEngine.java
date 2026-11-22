package hu.nye.rushhour.engine;

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
}
