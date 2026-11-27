package rushhour.ai;

import org.junit.jupiter.api.Test;
import rushhour.model.Move;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class MoveSelectorTest {

    @Test
    void shouldSelectMovesFromValidMoves(){
        MoveSelector selector = new MoveSelector();

        Move firstMove = new Move("H", 1);
        Move secondMove = new Move("V", -2);
        List<Move> validMoves = List.of(firstMove, secondMove);

        Move selectedMove = selector.selectRandomMove(validMoves);

        assertTrue(validMoves.contains(selectedMove));
    }

    @Test
    void shouldRejectNullMoveList(){
        MoveSelector selector = new MoveSelector();

        assertThrows(IllegalArgumentException.class, () -> selector.selectRandomMove(null));
    }

    @Test
    void shouldRejectEmptyMoveList(){
        MoveSelector selector = new MoveSelector();

        assertThrows(IllegalArgumentException.class, () -> selector.selectRandomMove(List.of()));
    }

    @Test
    void shouldSelectMovesWithEqualFrequency(){
        MoveSelector selector = new MoveSelector();

        Move firstMove = new Move("H", 1);
        Move secondMove = new Move("V", -2);
        List<Move> validMoves = List.of(firstMove, secondMove);

        int firstMoveCount = 0;
        int secondMoveCount = 0;
        int numberOfSelections = 10000;

        for(int i = 0; i < numberOfSelections; i++){
            Move selectedMove = selector.selectRandomMove(validMoves);

            if(selectedMove.equals(firstMove)){
                firstMoveCount++;
            }
            else if (selectedMove.equals(secondMove)){
                secondMoveCount++;
            }
        }

        assertTrue(firstMoveCount > 4000 && firstMoveCount < 6000);
        assertTrue(secondMoveCount > 4000 && secondMoveCount < 6000);
    }
}
