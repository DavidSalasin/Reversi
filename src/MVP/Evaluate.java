package MVP;

import BIT_MANAGEMENT.BitBoard;
import MVP.Enums.Difficulty;
import MVP.Enums.Player;
import MVP.Interfaces.IEvaluate;

import java.util.HashMap;

import static MVP.Enums.Difficulty.*;

// Evaluates the board's disk positions points for the current player (BY AI DIFFICULTY).
public class Evaluate
{

    public static HashMap<Difficulty, IEvaluate> heuristicDic = new HashMap<>();

    static {

        // BEGINNER DIFFICULTY HEURISTIC:
        heuristicDic.put(BEGINNER, new IEvaluate() {

            @Override
            public int evaluate(BitBoard board, Player currentPlayer)
            {
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
            }

        });

        // INTERMEDIATE DIFFICULTY HEURISTIC:
        heuristicDic.put(INTERMEDIATE, new IEvaluate() {

            @Override
            public int evaluate(BitBoard board, Player currentPlayer) {
                return 0;
            }

        });

        // HARDCORE DIFFICULTY HEURISTIC:
        heuristicDic.put(HARDCORE, new IEvaluate() {

            @Override
            public int evaluate(BitBoard board, Player currentPlayer) {
                return 0;
            }

        });

    }
}
