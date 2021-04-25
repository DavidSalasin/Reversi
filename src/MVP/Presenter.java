package MVP;

import BIT_MANAGEMENT.BitBoard;
import INFORMATION_ENCAPSULATION.Coordinates;
import INFORMATION_ENCAPSULATION.Information;
import MVP.Enums.GameMode;
import MVP.Enums.GameStatus;
import MVP.Enums.Player;

import static MVP.Enums.Player.*;
import static MVP.Enums.GameStatus.*;


/**
 * <h1>Class type: 'Presenter'</h1>
 *
 * The <b>PRESENTER</b> layer for the Reversi game:
 * Manages interaction between the VIEW layer(player, FRONTEND) and MODEL (game, BACKEND).
 * <p></p>
 * ! FILL ACCORDING TO WEB VERSION !
 *
 * @author David Salasin
 */
public class Presenter
{
    /**
     * The game itself, an instance for the MODEL layer.
     */
    private final Model model;


    /**
     * Parameter for current player.
     */
    private Player currentPlayer;


    /**
     * Constructor for 'Presenter'.
     * <p></p>
     * Initiates the 'Model' instance.
     */
    public Presenter()
    {
        model = new Model();
    }


    /**
     * Initiates the game at the BACKEND level.
     * <p></p>
     * Sets up starting <b>player's value (BY DEFAULT BLACK)</b> and <b>initiates the MODEL
     * layer to the game</b>: board data structure and game mode (PVP / AI difficulty).
     *
     * @param gameMode player's game mode preference.
     * @return 'Information' reference about the starting setup of the game.
     */
    public Information startGame(GameMode gameMode)
    {
        // Initiating values: Starting player and board's pieces.
        currentPlayer = BLACK;

        model.init(gameMode);
        BitBoard board = model.getBoard();

        return new Information(SUCCESSFUL, currentPlayer, board);
    }


    /**
     * Plays the board coordinates for the current player.
     * <p></p>
     * Returns game information to view according to the played move (with an 'Information'
     * instance), according to 4 situations:
     * - a <b>successful</b> move
     * - a <b>failed</b> move (illegal play)
     * - a <b>skip</b> situation (where a move was legal, but the opponent can't play after the player).
     * - a <b>game ending</b> situation (where one of the sides or neither, has won).
     *
     * @param coordinates Board's move coordinates.
     * @return 'Information' reference according to the just played move.
     */
    public Information playerTurn(Coordinates coordinates)
    {
        // If player didn't pass coordinates -> play with the set difficulty AI.
        coordinates = coordinates == null ? model.mostEvaluatedPlay(currentPlayer) : coordinates;

        // System.out.println("Y POS:   " + coordinates.y_position + "   |   X POS:   " + coordinates.x_position);

        GameStatus status = model.playMove(currentPlayer, coordinates);

        // If a move was invalid:
        if (status == FAILED)
        {
            return new Information(FAILED, null, null);
        }

        Player opponent = Player.currentOpponent(currentPlayer);

        BitBoard board = model.getBoard();

        long availablePlayer = model.availableMoves(currentPlayer);
        long availableOpponent = model.availableMoves(opponent);

        // In case no player can make a move:
        if ((availablePlayer | availableOpponent) == 0L)
        {
            return new Information(ENDED, model.EndingScenarios(), board);
        }

        // In case of a SKIP scenario:
        if (availableOpponent == 0L)
        {
            return new Information(SKIPPED, opponent, board);
        }

        // In case of a successful move:
        currentPlayer = opponent;

        return new Information(SUCCESSFUL, currentPlayer, board);
    }
}
