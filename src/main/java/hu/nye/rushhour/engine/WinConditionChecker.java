package hu.nye.rushhour.engine;

import hu.nye.rushhour.model.GameState;

public class WinConditionChecker {
    public boolean hasHumanWon(GameState gameState) {
        return gameState.hasHumanHeroExited();
    }

    public boolean hasMachineWon(GameState gameState) {
        return gameState.hasMachineHeroExited();
    }
}
