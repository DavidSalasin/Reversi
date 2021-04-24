package MVP;

import BIT_MANAGEMENT.BitBoard;
import MVP.Enums.Difficulty;
import MVP.Enums.GameStatus;
import MVP.Enums.Player;
import MVP.Interfaces.IPresenter;

import static MVP.Enums.Player.*;
import static MVP.Enums.GameStatus.*;

/**
 * Presenter class (PRESENTER LAYER) for Reversi.
 * Managed interaction between VIEW (player) and MODEL (game).
 *
 * @author David Salasin
 */
public class Presenter implements IPresenter
{
    private final Model model;

    private Player currentPlayer;

    public Presenter()
    {
        model = new Model();
    }

    public Information startGame(Difficulty gameMode)
    {
        // Initiating values: Starting player and board's pieces.
        currentPlayer = BLACK;

        model.init(gameMode);
        BitBoard board = model.getBoard();

        return new Information(SUCCESSFUL, currentPlayer, board);
    }

    public Information playerTurn(Coordinates coordinates)
    {
        if (coordinates == null)
        {
            coordinates = model.mostEvaluatedPlay(currentPlayer);
            // System.out.println("Y POS:   " + coordinates.y_position + "   |   X POS:   " + coordinates.x_position);
        }

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
