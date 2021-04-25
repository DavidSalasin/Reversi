package BIT_MANAGEMENT;

import java.util.HashMap;

import BIT_MANAGEMENT.Enums.Direction;
import BIT_MANAGEMENT.Interfaces.IBitShift;

import static BIT_MANAGEMENT.Enums.Direction.*;


/**
 * <h1>Class type: 'BitShifters'</h1>
 *
 * Contains <u>HashMap</u> instance <b>shiftDict</b> for all of the board directional shifting expressions.
 *
 * @author David Salasin
 */
public class BitShifters
{
    /**
     * Uses 'Direction' enum as KEY to access the implemented lambda expressions 'IBitShift' class VALUE.
     *
     * <b>NOTE:</b> static property.
     *
     * @see Direction
     * @see IBitShift
     */
    public static HashMap<Direction, IBitShift> shiftDict = new HashMap<>();


    /**
     * Board left edge mask, existing for guaranteeing a successful piece shift.
     */
    private final static long LEFT_EDGE_MASK = -9187201950435737472L;


    /**
     * Board right edge mask, existing for guaranteeing a successful piece shift.
     */
    private final static long RIGHT_EDGE_MASK = 72340172838076673L;

    // 'IBitShift' static lambda implementations for each board direction:
    static {

        // North west direction:
        shiftDict.put(NORTH_WEST, new IBitShift() {
            @Override
            public long bitShift(long position) {
                return (position << 9) & ~RIGHT_EDGE_MASK;
            }
        });

        // North direction:
        shiftDict.put(NORTH, new IBitShift() {
            @Override
            public long bitShift(long position) {
                return position << 8;
            }
        });

        // North east direction:
        shiftDict.put(NORTH_EAST, new IBitShift() {
            @Override
            public long bitShift(long position) {
                return (position << 7) & ~LEFT_EDGE_MASK;
            }
        });

        // east direction:
        shiftDict.put(EAST, new IBitShift() {
            @Override
            public long bitShift(long position) {
                return (position >>> 1) & ~LEFT_EDGE_MASK;
            }
        });

        // South east direction:
        shiftDict.put(SOUTH_EAST, new IBitShift() {
            @Override
            public long bitShift(long position) {
                return (position >>> 9) & ~LEFT_EDGE_MASK;
            }
        });

        // south direction:
        shiftDict.put(SOUTH, new IBitShift() {
            @Override
            public long bitShift(long position) {
                return position >>> 8;
            }
        });

        // south west direction:
        shiftDict.put(SOUTH_WEST, new IBitShift() {
            @Override
            public long bitShift(long position) {
                return (position >>> 7) & ~RIGHT_EDGE_MASK;
            }
        });

        // west direction:
        shiftDict.put(WEST, new IBitShift() {
            @Override
            public long bitShift(long position) {
                return (position << 1) & ~RIGHT_EDGE_MASK;
            }
        });
    }
}
