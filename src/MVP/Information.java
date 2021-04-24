package MVP;

import BIT_MANAGEMENT.BitBoard;
import MVP.Enums.GameStatus;
import MVP.Enums.Player;

/**
 * Information class, encapsulating variables passed between the view and the presenter layers of the game.
 */
public class Information
{
    // Game status for the view to represent.
    public GameStatus status;

    // Current player according to the situation.
    public Player player;

    // Board, if a need to represent it exists.
    public BitBoard board;

    public Information(GameStatus status, Player player, BitBoard board)
    {
        this.status = status;
        this.player = player;
        this.board = board;
    }
}
