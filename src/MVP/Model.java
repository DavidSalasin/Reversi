package MVP;

import BIT_MANAGEMENT.BitBoard;
import BIT_MANAGEMENT.BitShifters;
import BIT_MANAGEMENT.Enums.Direction;
import BIT_MANAGEMENT.Interfaces.IBitShift;
import MVP.Enums.Difficulty;
import MVP.Enums.GameStatus;
import MVP.Enums.Player;


import static MVP.Enums.Player.*;
import static MVP.Enums.GameStatus.*;

/**
 * Model class (MODEL LAYER) for Reversi.
 * The brain behind the game, contains database of the board game and the functionality between plays.
 *
 * @author David Salasin
 */
public class Model
{
    //
    private final BitBoard board;

    private Difficulty gameMode;

    static final long emptyBoard = 0L;

    public Model()
    {
        board = new BitBoard();
        gameMode = null;
    }

    public void init(Difficulty gameMode)
    {
        this.gameMode = gameMode;
        board.setColorBits(BLACK, 34628173824L);
        board.setColorBits(WHITE, 68853694464L);
    }

    public Player EndingScenarios()
    {
        // Translates BitBoard to score.
        int scoreBLACK = Long.bitCount(board.getColorBits(BLACK));
        int scoreWHITE = Long.bitCount(board.getColorBits(WHITE));

        // GAME ENDING CASES:

        // BLACK WON.
        if (scoreBLACK > scoreWHITE)
            return BLACK;

        // WHITE won.
        else if (scoreBLACK < scoreWHITE)
            return WHITE;

        // DRAW.
        return null;
    }


    // Checks if there are any available moves.
    public long availableMoves(Player currentPlayer)
    {
        // Long variable to act as a bit logical unit for all of the valid player moves.
        long validMoves = 0L;

        long playerPieces = board.getColorBits(currentPlayer);
        long opponentPieces = board.getColorBits(Player.currentOpponent(currentPlayer));
        long emptySlots = board.emptySlots();

        // Checks available moves for each direction from existing player pieces.
        for (Direction d : BitShifters.shift.keySet())
        {
            IBitShift shifter = BitShifters.shift.get(d);
            long potentialMoves = shifter.bitShift(playerPieces) & opponentPieces;

            while (potentialMoves != 0L)
            {
                validMoves = validMoves | (shifter.bitShift(potentialMoves) & emptySlots);
                potentialMoves = shifter.bitShift(potentialMoves) & opponentPieces;
            }
        }

        return validMoves;
    }

    public boolean isLegalMove(Player currentPlayer, long position)
    {
        return (availableMoves(currentPlayer) & position) != emptyBoard;
    }


    /**
     * Plays player's move.
     * Returns ENUM value for if the played move has been done successfully.
     */
    public GameStatus playMove(Player currentPlayer, Coordinates coordinates)
    {
        long position = BitBoard.bitPosition(coordinates);

        if (isLegalMove(currentPlayer, position))
        {
            Player currentOpponent = Player.currentOpponent(currentPlayer);

            long playerPieces = board.getColorBits(currentPlayer);
            long opponentPieces = board.getColorBits(currentOpponent);

            // Final bridge mask for the bit board's player pieces to adapt.
            long bridgesMask = position;

            // For every direction from the current player's played slot:
            for (Direction d : BitShifters.shift.keySet())
            {
                IBitShift shifter = BitShifters.shift.get(d);
                long bitIterator = shifter.bitShift(position);
                long bitBridge = 0;

                // If the neighboring piece to current player's played slot is an opponent piece:
                // Checks if there is an available bridge in that direction.
                if ((bitIterator & opponentPieces) == bitIterator && bitIterator != emptyBoard)
                {
                    // While an opponent piece is in the next direction slot -> expand bit bridge.
                    do {
                        bitBridge = bitBridge | bitIterator;
                        bitIterator = shifter.bitShift(bitIterator);
                    } while ((bitIterator & opponentPieces) == bitIterator && bitIterator != emptyBoard);

                    // If the last slot to complete the bridge doesn't contain one of current player's pieces:
                    // Doesn't add the bridge to the final bridge mask (Resets variable to 0).
                    if ((bitIterator & playerPieces) != bitIterator || bitIterator == emptyBoard)
                    {
                        bitBridge = emptyBoard;
                    }
                }

                // Adapts bit Bridge for the current direction (if created).
                bridgesMask = bridgesMask | bitBridge;
            }

            // Updates board's pieces by the move mask:
            playerPieces = playerPieces | bridgesMask;
            opponentPieces = opponentPieces & ~bridgesMask;

            board.setColorBits(currentPlayer, playerPieces);
            board.setColorBits(currentOpponent, opponentPieces);

            return SUCCESSFUL;
        }

        // If the move was illegal:
        return FAILED;
    }

    // Returns BitBoard reference for Unit Testing purposes
    public BitBoard getBoard()
    {
        return board;
    }


    // AI related work:

    // Evaluates the board's disk positions points for the current player.
    //    public int evaluate(Player currentPlayer)
    //    {
    //
    //    }


    // Recursive Game Tree function, which returns the highest score play for the current player.
    public int negamax(int depth, Player currentPlayer, int alpha, int beta)
    {
        Player currentOpponent = Player.currentOpponent(currentPlayer);

        long playerMoves = availableMoves(currentPlayer);
        long opponentMoves = availableMoves(currentOpponent);

        // Leaf in a game tree:
        if (depth == 0 || (playerMoves | opponentMoves) == emptyBoard)
        {
            //return -1 * currentPlayer.label * evaluate(currentOpponent);
            return -1 * currentPlayer.label * Evaluate.heuristicDic.get(gameMode).evaluate(board, currentOpponent);
        }
        // Turn skip in a game tree:
        else if (playerMoves == emptyBoard)
        {
            return -1 * negamax(depth - 1, currentOpponent, -beta, -alpha);
        }


        BitBoard boardCopy = new BitBoard();
        BitBoard.copyBoard(board, boardCopy);

        int score = -1000;
        long moveIterator = 1L;

        int bitTrailingZeroCount;

        while (playerMoves != emptyBoard)
        {
            // Retrieving the next bit position available move.
            bitTrailingZeroCount = Long.numberOfTrailingZeros(playerMoves);
            moveIterator = moveIterator << bitTrailingZeroCount;

            // Translating move from bit position, playing it as a branch.
            playMove(currentPlayer, BitBoard.boardPosition(moveIterator));

            score = Math.max(score, negamax(depth - 1, currentOpponent, -beta, -alpha));
            alpha = Math.max(alpha, score);

            // Reversing the last move that was made, and moving on to the next bit position move.
            BitBoard.copyBoard(boardCopy, board);
            playerMoves = playerMoves >>> bitTrailingZeroCount;
            playerMoves = playerMoves >>> 1;
            moveIterator = moveIterator << 1;

            if (alpha >= beta)
            {
                return -beta;
            }
        }

        return -1 * score;
    }


    // Returns the best position play for the current player.
    public Move mostEvaluatedPlay(Player currentPlayer)
    {
        Move bestPlay = new Move(0, 0);

        long playerMoves = availableMoves(currentPlayer);

        Player currentOpponent = Player.currentOpponent(currentPlayer);

        BitBoard boardCopy = new BitBoard();
        BitBoard.copyBoard(board, boardCopy);

        int depth = gameMode.depth;
        long moveIterator = 1L;

        int bitTrailingZeroCount, currentMoveScore;

        while (playerMoves != 0L)
        {
            // Retrieving the next bit position available move.
            bitTrailingZeroCount = Long.numberOfTrailingZeros(playerMoves);
            moveIterator = moveIterator << bitTrailingZeroCount;

            // Translating move from bit position, playing it as a branch.
            Coordinates potentialMove = BitBoard.boardPosition(moveIterator);
            playMove(currentPlayer, potentialMove);

            currentMoveScore = currentPlayer.label * negamax(depth, currentOpponent, -1000, -bestPlay.score);

            if (currentMoveScore > bestPlay.score)
            {
                bestPlay.y_position = potentialMove.y_position;
                bestPlay.x_position = potentialMove.x_position;
                bestPlay.score = currentMoveScore;
            }

            // Reversing the last move that was made, and moving on to the next bit position move.
            BitBoard.copyBoard(boardCopy, board);
            playerMoves = playerMoves >>> bitTrailingZeroCount;
            playerMoves = playerMoves >>> 1;
            moveIterator = moveIterator << 1;
        }

        System.out.println("Score: " + bestPlay.score);

        return bestPlay;
    }
}
