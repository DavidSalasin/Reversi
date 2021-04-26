package MVP.Interfaces;

import BIT_MANAGEMENT.BitBoard;

import MVP.Enums.Player;
import MVP.Model;


/**
 * <h1>Interface reference type: 'IEvaluate'</h1>
 *
 * Reference implemented differently for each game difficulty by the 'Evaluate' class
 * as <b>lambdas expressions</b>.
 *
 * @author David Salasin
 */
public interface IEvaluate {
    /**
     * Evaluates a score according to board's pieces for the current player.
     *
     * @param model 'Model' class, including the 'BitBoard' structure and other bit board operations.
     * @param currentPlayer Player the score is being evaluated for.
     * @return Evaluated Integer score, by the set heuristic function.
     */
    int evaluate(Model model, Player currentPlayer);
}
