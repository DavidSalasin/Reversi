package MVP;

import BIT_MANAGEMENT.BitBoard;
import BIT_MANAGEMENT.BitShifters;
import BIT_MANAGEMENT.Enums.Direction;
import BIT_MANAGEMENT.Interfaces.IBitShift;
import MVP.Enums.Difficulty;
import MVP.Enums.GameStatus;
import MVP.Enums.Player;
import MVP.Interfaces.IEvaluate;

import static MVP.Enums.Player.*;
import static MVP.Enums.GameStatus.*;


/**
 * <h1>Class type: 'Model'</h1>
 *
 * The <b>MODEL</b> layer for the Reversi game: The brain behind the game,
 * contains database of the board game and the functionality between plays.
 * <p></p>
 * 'Model' also holds to AI methods, which operate generically according to
 * the player's choice of game difficulty.
 *
 * @author David Salasin
 */
public class Model
{
    /**
     * Database of the board game.
     *
     * @see BitBoard
     */
    private final BitBoard board;

    
    /**
     * Difficulty of the game/AI.
     * <p></p>
     * <u>PVP</u> when the game is player vs player.
     *
     * @see Difficulty
     * @see Evaluate
     */
    private Difficulty gameMode;

    
    /**
     * IEvaluate heuristic instance according to game's difficulty move.
     * <p></p>
     * <b>NOTE:</b> INITIATED TO NULL when the game is player vs player.
     */
    private IEvaluate heuristic;

    
    /**
     * Constant long value for the starting board BLACK pieces.
     */
    private static final long FIRST_BITS_BLACK = 34628173824L;


    /**
     * Constant long value for the starting board WHITE pieces.
     */
    private static final long FIRST_BITS_WHITE = 68853694464L;

    
    /**
     * Constant long value for an empty board.
     */
    private static final long EMPTY_BOARD = 0L;

    
    /**
     * Constructor for 'Model'.
     * <p></p>
     * Creates an instance of game board's 'BitBoard' database.
     * Sets game's mode to <u>null</u>.
     * 
     * @see BitBoard
     */
    public Model()
    {
        board = new BitBoard();
        gameMode = null;
    }


    /**
     * Initiates board according to the starting pieces, and game's mode
     * to player's choice, also updating the heuristic instance.
     * 
     * @param gameMode Player's chosen game difficulty.
     */
    public void init(Difficulty gameMode)
    {
        board.setColorBits(BLACK, FIRST_BITS_BLACK);
        board.setColorBits(WHITE, FIRST_BITS_WHITE);

        this.gameMode = gameMode;
        this.heuristic = Evaluate.heuristicDic.get(gameMode);
    }


    /**
     * Finds who is the winner, according to board's pieces status.
     *
     * @return The winning 'Player' enum.
     */
    public Player EndingScenarios()
    {
        // Translates 'BitBoard' to score.
        int scoreBLACK = Long.bitCount(board.getColorBits(BLACK));
        int scoreWHITE = Long.bitCount(board.getColorBits(WHITE));

        // GAME ENDING CASES:

        // BLACK WON.
        if (scoreBLACK > scoreWHITE)
            return BLACK;

        // WHITE won.
        if (scoreBLACK < scoreWHITE)
            return WHITE;

        // DRAW.
        return null;
    }


    /**
     * Checks if there are any available moves for the current searched-for player.
     *
     * @param currentPlayer The player the moves are searched for.
     * @return long value of all of the available board slots move for the player.
     */
    public long availableMoves(Player currentPlayer)
    {
        // Long variable to act as a bit logical unit for all of the valid player moves.
        long validMoves = EMPTY_BOARD;

        long playerPieces = board.getColorBits(currentPlayer);
        long opponentPieces = board.getColorBits(Player.currentOpponent(currentPlayer));
        long emptySlots = board.emptySlots();

        // Checks available moves for each direction from existing player pieces.
        for (Direction d : BitShifters.shiftDict.keySet())
        {
            IBitShift shifter = BitShifters.shiftDict.get(d);
            long potentialMoves = shifter.bitShift(playerPieces) & opponentPieces;

            // While there are moves available for a certain direction:
            // append them to the 'valid moves' long value.
            while (potentialMoves != EMPTY_BOARD)
            {
                validMoves = validMoves | (shifter.bitShift(potentialMoves) & emptySlots);
                potentialMoves = shifter.bitShift(potentialMoves) & opponentPieces;
            }
        }

        return validMoves;
    }


    /**
     * Check if the passed move is legal for the current player.
     *
     * @param player The player asking to play the move.
     * @param position Player's requested move.
     * @return boolean for if player's move is legal to do.
     */
    public boolean isLegalMove(Player player, long position)
    {
        return (availableMoves(player) & position) != EMPTY_BOARD;
    }


    /**
     * Plays player's move.
     *
     * @param currentPlayer Current playing player.
     * @param coordinates 'Coordinates' instance of the played board slot.
     * @return 'GameStatus' enum value for if the played move has been done successfully.
     */
    public GameStatus playMove(Player currentPlayer, Coordinates coordinates)
    {
        return playMove(currentPlayer, BitBoard.bitPosition(coordinates));
    }


    /**
     * Plays player's move.
     *
     * @param currentPlayer Current playing player.
     * @param position long value of the playing slot of the board.
     * @return 'GameStatus' enum value for if the played move has been done successfully.
     */
    public GameStatus playMove(Player currentPlayer, long position)
    {
        // If played move is legal:
        if (isLegalMove(currentPlayer, position))
        {
            Player currentOpponent = Player.currentOpponent(currentPlayer);

            long playerPieces = board.getColorBits(currentPlayer);
            long opponentPieces = board.getColorBits(currentOpponent);

            // Final bridge mask for the bit board's player pieces to adapt.
            long bridgesMask = position;

            // For every direction from the current player's played slot:
            for (Direction d : BitShifters.shiftDict.keySet())
            {
                IBitShift shifter = BitShifters.shiftDict.get(d);
                long bitIterator = shifter.bitShift(position);
                long bitBridge = 0;

                // If the neighboring piece to current player's played slot is an opponent piece:
                // Checks if there is an available bridge in that direction.
                if ((bitIterator & opponentPieces) == bitIterator && bitIterator != EMPTY_BOARD)
                {
                    // While an opponent piece is in the next direction slot -> expand bit bridge.
                    do {
                        bitBridge = bitBridge | bitIterator;
                        bitIterator = shifter.bitShift(bitIterator);
                    } while ((bitIterator & opponentPieces) == bitIterator && bitIterator != EMPTY_BOARD);

                    // If the last slot to complete the bridge doesn't contain one of current player's pieces:
                    // Doesn't add the bridge to the final bridge mask (Resets variable to 0).
                    if ((bitIterator & playerPieces) != bitIterator || bitIterator == EMPTY_BOARD)
                    {
                        bitBridge = EMPTY_BOARD;
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


    /**
     * Returns current 'BitBoard' reference.
     *
     * @return 'BitBoard' reference.
     */
    public BitBoard getBoard()
    {
        return board;
    }


    // AI related work:


    /**
     * Game-tree algorithm, searching recursively for the best score move available
     * to the current player (for the current board status).
     * <p></p>
     * <b>Called only</b> by <u>mostEvaluatedPlay</u> and itself, and <b>always</b> returns
     * the negative value of the maximum value found, as the function is called recursively
     * between each of the players turns and one's maximum score is the worst score there
     * could be for the opponent.
     * <p></p>
     * ! MISSING EXPLANATION FOR ALPHA-BETA !
     *
     * @param depth Current level of depth of the game tree.
     * @param currentPlayer Current player checked for the level of play.
     * @param alpha * missing *
     * @param beta * missing *
     * @return Best score (int) found in that node of the game tree.
     */
    public int negamax(int depth, Player currentPlayer, int alpha, int beta)
    {
        Player currentOpponent = Player.currentOpponent(currentPlayer);

        long playerMoves = availableMoves(currentPlayer);
        long opponentMoves = availableMoves(currentOpponent);

        // Leaf in a game tree:
        // If the game has ended / depth reached to 0.
        if (depth == 0 || (playerMoves | opponentMoves) == EMPTY_BOARD) {
            return -1 * currentPlayer.label * heuristic.evaluate(board, currentOpponent);
        }

        // Turn skip in a game tree:
        // If current player can't move.
        if (playerMoves == EMPTY_BOARD) {
            return -1 * negamax(depth - 1, currentOpponent, -beta, -alpha);
        }

        BitBoard boardCopy = new BitBoard();
        BitBoard.copyBoard(board, boardCopy);

        int score = -1000;
        long moveIterator = 1L;

        int bitTrailingZeroCount;

        /*
         * While there are still available moves:
         * Move down on the game-tree as the move was actually played and calculate its
         * score according to heuristic function of its game difficulty.
         */
        while (playerMoves != EMPTY_BOARD)
        {
            // Retrieving the next bit position available move.
            bitTrailingZeroCount = Long.numberOfTrailingZeros(playerMoves);
            moveIterator = moveIterator << bitTrailingZeroCount;

            // Translating move from bit position, playing it as a branch.
            playMove(currentPlayer, moveIterator);

            // Updating the maximum score and alpha's score accordingly.
            score = Math.max(score, negamax(depth - 1, currentOpponent, -beta, -alpha));
            alpha = Math.max(alpha, score);

            // Reversing the last move that was made, and moving on to the next bit position move.
            BitBoard.copyBoard(boardCopy, board);
            playerMoves = playerMoves >>> bitTrailingZeroCount;
            playerMoves = playerMoves >>> 1;
            moveIterator = moveIterator << 1;

            // * missing *
            if (alpha >= beta)
            {
                return -beta;
            }
        }

        // Returns the negative of the best found score.
        return -1 * score;
    }


    /**
     * Returns the best position play for the player.
     * <p></p>
     * The algorithm runs through all of the available moves for the current
     * player, and returns the board coordinates for the most evaluated play.
     *
     * @param currentPlayer The player the move is searched for.
     * @return 'Move' instance (sub-class of 'Coordinates').
     */
    public Move mostEvaluatedPlay(Player currentPlayer)
    {
        // "No available move" as the difficult returned move,
        // in the case of player having no available moves.
        Move bestPlay = new Move(0, 0);

        long playerMoves = availableMoves(currentPlayer);

        if (playerMoves == EMPTY_BOARD) return bestPlay;

        Player currentOpponent = Player.currentOpponent(currentPlayer);

        // Copy of the board, to revert the board after each play.
        BitBoard boardCopy = new BitBoard();
        BitBoard.copyBoard(board, boardCopy);

        // Long move iterator, to move through all of the available move positions.
        long moveIterator = 1L;

        int bitTrailingZeroCount, currentMoveScore;

        /*
         * While there are still available moves:
         * Move down on the game-tree as the move was actually played and calculate its
         * score according to heuristic function of its game difficulty.
         */
        while (playerMoves != EMPTY_BOARD)
        {
            // Retrieving the next bit position available move.
            bitTrailingZeroCount = Long.numberOfTrailingZeros(playerMoves);
            moveIterator = moveIterator << bitTrailingZeroCount;

            // Translating move from bit position, playing it as a branch.
            Coordinates potentialMove = BitBoard.boardPosition(moveIterator);
            playMove(currentPlayer, moveIterator);

            // Calculate the score of the current tested move.
            currentMoveScore = currentPlayer.label * negamax(gameMode.depth, currentOpponent,-1000, -bestPlay.score);

            // If found score is better than current's best -> update best play's properties.
            if (currentMoveScore > bestPlay.score) {
                bestPlay.updateMove(potentialMove.y_position, potentialMove.x_position, currentMoveScore);
            }

            // Reversing the last move that was made, and moving on to the next bit position move.
            BitBoard.copyBoard(boardCopy, board);
            playerMoves = playerMoves >>> bitTrailingZeroCount;
            playerMoves = playerMoves >>> 1;
            moveIterator = moveIterator << 1;
        }

        // System.out.println("Score: " + bestPlay.score);

        return bestPlay;
    }
}
