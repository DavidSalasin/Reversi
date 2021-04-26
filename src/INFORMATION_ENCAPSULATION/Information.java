package INFORMATION_ENCAPSULATION;

import BIT_MANAGEMENT.BitBoard;
import MVP.Enums.GameStatus;
import MVP.Enums.Player;

/**
 * <h1>Class type: 'Information'</h1>
 *
 * Encapsulates game information/variables passed between the VIEW and
 * the PRESENTER layers of the game.
 *
 * @author David Salasin
 */
public class Information
{
    /**
     * 'GameStatus' for the state of the game.
     */
    public GameStatus status;


    /**
     * The Player the information is referring to.
     */
    public Player player;


    /**
     * 'BitBoard' reference, if a need to represent it exists.
     */
    public BitBoard board;


    /**
     * Constructor for 'Information'.
     *
     * Initiates Information's properties with the passed parameters
     * (Encapsulating them with as <u>public</u> properties).
     *
     * @param status State of the game.
     * @param player Referred player with the information.
     * @param board 'BitBoard' reference.
     * @see GameStatus
     */
    public Information(GameStatus status, Player player, BitBoard board)
    {
        this.status = status;
        this.player = player;
        this.board = board;
    }
}
