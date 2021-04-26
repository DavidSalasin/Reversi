package MVP;

import java.util.HashMap;

import BIT_MANAGEMENT.BitBoard;

import BIT_MANAGEMENT.BitShifters;
import BIT_MANAGEMENT.Enums.Direction;
import MVP.Enums.GameMode;
import MVP.Enums.Player;
import MVP.Interfaces.IEvaluate;

import static MVP.Enums.GameMode.*;
import static BIT_MANAGEMENT.Enums.Direction.*;


/**
 * <h1>Class type: 'Evaluate'</h1>
 *
 * Contains <u>HashMap</u> instance <b>heuristicDic</b> for all of the existing heuristic
 * evaluation for different game modes (AI difficulties).
 *
 * @author David Salasin
 */
public class Evaluate
{
    /**
     * Uses 'GameMode' enum as KEY to access the implemented lambda expressions 'IEvaluate' class VALUE.
     *
     * Each 'IEvaluate' contains different lambda <u>evaluate</u> expressions which evaluate the board's
     * disk positions points for the passed player.
     *
     * <b>NOTE:</b> static property.
     */
    public static HashMap<GameMode, IEvaluate> heuristicDic = new HashMap<>();


    /**
     * Constant long value for an empty board.
     */
    private static final long EMPTY_BOARD = 0L;


    /**
     * Constant long value for a Bit Board's edges.
     */
    private static final long EDGES_MASK = -35604928818740737L;


    /**
     * Constant long value for a Bit Board's corners.
     */
    private static final long CORNERS_MASK = -9151314442816847743L;


    /**
     * Constant long value for a Bit Board's X slots.
     */
    private static final long X_MASK = 18577348462920192L;


    /**
     * Evaluating the end-game evaluation score from player's POV.
     *
     * @param model 'Model' reference of the game, for calling <u>EndingScenarios()</u>
     * @param currentPlayer playing evaluation point of view.
     * @return the evaluated score for the end-game scenario.
     */
    public static int winnerStatement(Model model, Player currentPlayer)
    {
        // Calculation of the winner:
        Player winner = model.EndingScenarios();

        // If player had won: Positive winning score. Else, Negative losing score.
        if (winner == currentPlayer) return Integer.MAX_VALUE;
        else if (winner == Player.currentOpponent(currentPlayer)) return -Integer.MAX_VALUE;
        else return 0;
    }

    /**
     * Uses 'Direction' as KEY to access the opposite 'Direction' on the compass (HashMap).
     * Used for handling <i>Stable</i> disks.
     */
    public static HashMap<Direction, Direction> oppositeDirections = new HashMap<>();

    static {
        oppositeDirections.put(NORTH_WEST, SOUTH_EAST);
        oppositeDirections.put(NORTH, SOUTH);
        oppositeDirections.put(NORTH_EAST, SOUTH_WEST);
        oppositeDirections.put(EAST, WEST);
    }

    // 'IEvaluate' static lambda implementations for every game difficulty:
    static {


        // Player vs player's heuristic (not needing an implementation);
        heuristicDic.put(PVP, null);

        // Beginner's heuristic:
        heuristicDic.put(BEGINNER, (model, currentPlayer) ->
        {
            BitBoard board = model.getBoard();
            Player currentOpponent = Player.currentOpponent(currentPlayer);

            int score = 0;
            long playerPieces = board.getColorBits(currentPlayer);
            long opponentPieces = board.getColorBits(currentOpponent);

            // Strategy based on having the sides and the corners:
            // It is a beginner strategy, and can be countered easily.
            // Difficulty level made for new players.
            score += (Long.bitCount(playerPieces) - Long.bitCount(opponentPieces));
            playerPieces &= EDGES_MASK;
            opponentPieces &= EDGES_MASK;

            score += (Long.bitCount(playerPieces) - Long.bitCount(opponentPieces));
            playerPieces &= CORNERS_MASK;
            opponentPieces &= CORNERS_MASK;

            score += ((Long.bitCount(playerPieces) << 2) - (Long.bitCount(opponentPieces) << 2));

            return score;
        });

        // Intermediate's heuristic:
        heuristicDic.put(INTERMEDIATE, (model, currentPlayer) ->
        {
            Player currentOpponent = Player.currentOpponent(currentPlayer);

            long availableMovesPlayer = model.availableMoves(currentPlayer);
            long availableMovesOpponent = model.availableMoves(currentOpponent);

            // If the game has ended: Returns winning evaluation score from current player's perspective.
            if ((availableMovesPlayer | availableMovesOpponent) == EMPTY_BOARD)
            {
                return winnerStatement(model, currentPlayer);
            }

            // Strategy based on having move turns then the opponent ->
            // The more safe moves, corner moves and less safe moves the player has, the better his
            // score is going to be. The values are going against the opponent's moves.

            // Players' counter move count.
            int cCountPlayer = Long.bitCount(availableMovesPlayer & CORNERS_MASK);
            int cCountOpponent = Long.bitCount(availableMovesOpponent & CORNERS_MASK);

            // Players' "safe" (not giving any corner from X tile) move count.
            int dCountPlayer = Long.bitCount(availableMovesPlayer & X_MASK);
            int dCountOpponent = Long.bitCount(availableMovesOpponent & X_MASK);

            // Players' BAD (giving up corners from X tiles) move count.
            int sCountPlayer = Long.bitCount((availableMovesPlayer ^ X_MASK) ^ CORNERS_MASK);
            int sCountOpponent = Long.bitCount((availableMovesOpponent ^ X_MASK) ^ CORNERS_MASK);


            return (cCountPlayer * 4) + (sCountPlayer * 2) + dCountPlayer
                - (cCountOpponent * 4) - (sCountOpponent * 2) - dCountOpponent;
        });

        // Hardcore heuristic:
        heuristicDic.put(HARDCORE, (model, currentPlayer) ->
        {
            BitBoard board = model.getBoard();
            Player currentOpponent = Player.currentOpponent(currentPlayer);

            long availableMovesPlayer = model.availableMoves(currentPlayer);
            long availableMovesOpponent = model.availableMoves(currentOpponent);

            // If the game has ended: Returns winning evaluation score.
            if ((availableMovesPlayer | availableMovesOpponent) == EMPTY_BOARD)
            {
                return winnerStatement(model, currentPlayer);
            }

            // Strategy based on the gathering of Corners, stable and inner disks.
            // Playing hard to catch, sticking to cover the entire match while the
            // enemy is slowly running out of moves.
            // ! PLANING TO ADD STABLE DISKS !


            long playerPieces = board.getColorBits(currentPlayer);
            long opponentPieces = board.getColorBits(currentOpponent);


            // Calculating inner pieces:
            long frontierPieces = ~(playerPieces | opponentPieces);
            long wallShift = 0;

            for (Direction d : BitShifters.shiftDict.keySet()) {
                wallShift |= BitShifters.shiftDict.get(d).bitShift(frontierPieces);
            }
            frontierPieces |= wallShift;

            int innerPlayerPieces = Long.bitCount((~frontierPieces) & playerPieces);
            int innerOpponentPieces = Long.bitCount((~frontierPieces) & opponentPieces);

//            int frontierPlayerPieces = Long.bitCount(frontierPieces & playerPieces);
//            int frontierOpponentPieces = Long.bitCount(frontierPieces & opponentPieces);

            // Finding corners:
            int cPlayerPieces = Long.bitCount(playerPieces & CORNERS_MASK);
            int cOpponentPieces = Long.bitCount(opponentPieces & CORNERS_MASK);


            return innerPlayerPieces * 3 + cPlayerPieces * 5 - innerOpponentPieces * 3 - cOpponentPieces * 5;
        });

    }
}
