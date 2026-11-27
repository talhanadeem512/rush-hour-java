package rushhour.ai;

import rushhour.model.Move;
import java.util.List;
import java.util.Random;

/**
 * Selects a random move from the list of valid moves.
 */
public class MoveSelector {
    private final Random random;

    /**
     * Creates a move selector using a new random number generator.
     */
    public MoveSelector(){
        this.random = new Random();
    }

    /**
     * Selects one move uniformly at random from the supplied list.
     *
     * @param validMoves the list of valid moves.
     * @return the randomly selected move.
     * @throws IllegalArgumentException if the list is null or empty.
     */
    public Move selectRandomMove(List<Move> validMoves){
        if(validMoves == null || validMoves.isEmpty()){
            throw new IllegalArgumentException("Valid moves cannot be null or empty.");
        }

        int randomIndex = random.nextInt(validMoves.size());
        return validMoves.get(randomIndex);
    }
}
