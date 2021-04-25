package MVP;

import java.util.HashMap;

import BIT_MANAGEMENT.BitBoard;

import MVP.Enums.Difficulty;
import MVP.Enums.Player;
import MVP.Interfaces.IEvaluate;

import static MVP.Enums.Difficulty.*;


/**
 * <h1>Class type: 'Evaluate'</h1>
 *
 * Contains <u>HashMap</u> instance <b>heuristicDic</b> for all of the existing heuristic
 * evaluation for different game difficulties.
 *
 * @author David Salasin
 */
public class Evaluate
{
    /**
     * Uses 'Difficulty' enum as KEY to access the implemented lambda expressions 'IEvaluate' class VALUE.
     * <p></p>
     * Each 'IEvaluate' contains different lambda <u>evaluate</u> expressions which evaluate the board's
     * disk positions points for the passed player.
     *
     * <b>NOTE:</b> static property.
     */
    public static HashMap<Difficulty, IEvaluate> heuristicDic = new HashMap<>();


    // 'IEvaluate' static lambda implementations for every game difficulty:
    static {

        // Player vs player's heuristic (not needing an implementation);
        heuristicDic.put(PVP, null);

        // Beginner's heuristic:
        heuristicDic.put(BEGINNER, (board, currentPlayer) -> {
            int score = 0;
            long playerPieces = board.getColorBits(currentPlayer);

            long sidesMask = -35604928818740737L;
            long edgesMask = -9151314442816847743L;

            score += Long.bitCount(playerPieces);
            playerPieces = playerPieces & sidesMask;

            score += Long.bitCount(playerPieces);
            playerPieces = playerPieces & edgesMask;

            score += 2 * Long.bitCount(playerPieces);

            return score;
        });

        // Intermediate's heuristic:
        heuristicDic.put(INTERMEDIATE, new IEvaluate() {

            @Override
            public int evaluate(BitBoard board, Player currentPlayer) {
                return 0;
            }

        });

        // Hardcore heuristic:
        heuristicDic.put(HARDCORE, new IEvaluate() {

            @Override
            public int evaluate(BitBoard board, Player currentPlayer) {
                return 0;
            }

        });

    }
}
