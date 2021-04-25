package BIT_MANAGEMENT;

import java.util.HashMap;

import MVP.Coordinates;
import MVP.Enums.Player;

import static MVP.Enums.Player.*;


/**
 * <h1>Class type: 'BitBoard'</h1>
 *
 * <b>Data structure</b> of the game's board.
 *
 * @author David Salasin
 */
public class BitBoard
{
    /**
     * Uses 'Player' enum as <b>KEY</b> to access player's board pieces long <b>VALUE</b>.
     *
     * @see Player
     */
    private final HashMap<Player, Long> pieces;

    static final long EMPTY_BOARD = 0L;

    /**
     * Constructor for 'BitBoard'.
     * Initiates <u>HashMap</u> board structure.
     */
    public BitBoard()
    {
        pieces = new HashMap<>();
        pieces.put(BLACK, EMPTY_BOARD);
        pieces.put(WHITE, EMPTY_BOARD);

    }


    /**
     * Constructor for 'BitBoard'.
     * Initiates <u>HashMap</u> board structure with set board pieces for both players.
     *
     * @param blackBits starting BLACK pieces.
     * @param whiteBits starting WHITE pieces.
     */
    public BitBoard(long blackBits, long whiteBits)
    {
        pieces = new HashMap<>();
        pieces.put(BLACK, blackBits);
        pieces.put(WHITE, whiteBits);
    }


    /**
     * Gets player's long board pieces.
     *
     * @param p 'Player' Enum.
     * @return long Board pieces.
     * @see Player
     */
    public long getColorBits(Player p)
    {
        return pieces.get(p);
    }


    /**
     * Sets player's long board pieces.
     *
     * @param p 'Player' Enum.
     * @param bits Board pieces.
     * @see Player
     */
    public void setColorBits(Player p, long bits)
    {
        pieces.put(p, bits);
    }


    /**
     * Calculates and returns the empty slots of the board.
     *
     * @return long board slots.
     */
    public long emptySlots()
    {
        return ~pieces.get(BLACK) & ~pieces.get(WHITE);
    }


    /**
     * Turns a 'Coordinates' instance to a long valued board position.
     *
     * <b>NOTE:</b> static method.
     *
     * @param coordinates 'Coordinates' instance.
     * @return long value (single bit board position).
     * @see Coordinates
     */
    public static long bitPosition(Coordinates coordinates)
    {
        long position = 1;
        position = (position << 8 - coordinates.x_position) << 8 * (coordinates.y_position - 1);
        return position;
    }


    /**
     * Turns a long valued board position, to a new 'Coordinates' instance.
     *
     * <b>NOTE:</b> static method.
     *
     * @param position long valued, single bit board position.
     * @return 'Coordinates' instance.
     * @see Coordinates
     */
    public static Coordinates boardPosition(long position)
    {
        int bitTrailingZeroCount = Long.numberOfTrailingZeros(position);
        int y_position = bitTrailingZeroCount / 8 + 1;
        int x_position = 8 - (bitTrailingZeroCount - (bitTrailingZeroCount >> 3 << 3));
        return new Coordinates(y_position, x_position);
    }


    /**
     * Copies One's board values to the other.
     *
     * <b>NOTE:</b> static method.
     *
     * @param from The 'BitBoard' instance being copied.
     * @param dest The 'BitBoard' instance to be copied to.
     */
    public static void copyBoard(BitBoard from, BitBoard dest)
    {
        dest.setColorBits(BLACK, from.getColorBits(BLACK));
        dest.setColorBits(WHITE, from.getColorBits(WHITE));
    }
}
