package MVP.Interfaces;

import BIT_MANAGEMENT.BitBoard;
import MVP.Enums.Player;

public interface IEvaluate {

    int evaluate(BitBoard board, Player currentPlayer);
}
