package BIT_MANAGEMENT;

import BIT_MANAGEMENT.Interfaces.IBitBoard;
import MVP.Coordinates;
import MVP.Enums.Player;

import static MVP.Enums.Player.*;
import java.util.HashMap;

public class BitBoard implements IBitBoard
{
    private final HashMap<Player, Long> pieces;

    public BitBoard()
    {
        pieces = new HashMap<>();
        pieces.put(BLACK, 0L);
        pieces.put(WHITE, 0L);

    }

    public BitBoard(long blackBits, long whiteBits)
    {
        pieces = new HashMap<>();
        pieces.put(BLACK, blackBits);
        pieces.put(WHITE, whiteBits);
    }

    public long getColorBits(Player p)
    {
        return pieces.get(p);
    }

    public void setColorBits(Player p, long bits)
    {
        pieces.put(p, bits);
    }

    public long emptySlots()
    {
        return ~pieces.get(BLACK) & ~pieces.get(WHITE);
    }

    public static long bitPosition(Coordinates coordinates)
    {
        long position = 1;
        position = (position << 8 - coordinates.x_position) << 8 * (coordinates.y_position - 1);
        return position;
    }

    public static Coordinates boardPosition(long position)
    {
        int bitTrailingZeroCount = Long.numberOfTrailingZeros(position);
        int y_position = bitTrailingZeroCount / 8 + 1;
        int x_position = 8 - (bitTrailingZeroCount - (bitTrailingZeroCount >> 3 << 3));
        return new Coordinates(y_position, x_position);
    }

    public static void copyBoard(BitBoard from, BitBoard dest)
    {
        dest.setColorBits(BLACK, from.getColorBits(BLACK));
        dest.setColorBits(WHITE, from.getColorBits(WHITE));
    }
}
