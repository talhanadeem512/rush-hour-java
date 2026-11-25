package rushhour.ai;

import rushhour.engine.GameEngine;
import rushhour.model.Board;
import rushhour.model.GameState;
import rushhour.model.Move;
import rushhour.model.Vehicle;

import java.util.ArrayList;
import java.util.List;

/**
 * Generates all valid moves available on the current board.
 */
public class MoveGenerator {

    /**
     * Generates every valid move for every vehicle on the board.
     *
     * @param gameState the current state of the game
     * @param gameEngine the game engine used to validate each move
     * @return a list containing all valid moves available on the board
     * @throws IllegalArgumentException if the game state or game engine is null
     */
    public List<Move> generateValidMoves(GameState  gameState, GameEngine gameEngine){
        if(gameState == null || gameEngine == null){
            throw new IllegalArgumentException("gameState and game engine cannot be null");
        }

        Board board = gameState.getBoard();
        List<Move> validMoves = new ArrayList<>();
        for(Vehicle vehicle : board.getVehicles()){
            int maxDistance = Math.max(board.getRows(), board.getColumns()) + vehicle.getLength();

            for(int distance = -maxDistance; distance <= maxDistance; distance++){
                if(distance == 0){
                    continue;
                }

                Move move = new Move(vehicle.getId(), distance);

                if(gameEngine.isMoveValid(move)){
                    validMoves.add(move);
                }
            }
        }

        return validMoves;
    }
}
