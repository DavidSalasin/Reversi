package BIT_MANAGEMENT;

import BIT_MANAGEMENT.Enums.Direction;
import BIT_MANAGEMENT.Interfaces.IBitShift;

import static BIT_MANAGEMENT.Enums.Direction.*;

import java.util.HashMap;

public class BitShifters {

    public static HashMap<Direction, IBitShift> shift = new HashMap<>();

    private final static long LEFT_EDGE_MASK = -9187201950435737472L;
    private final static long RIGHT_EDGE_MASK = 72340172838076673L;

    // BIT SHIFTING LAMBDAS:
    static {

        // NORTH_WEST:
        shift.put(NORTH_WEST, new IBitShift() {
            @Override
            public long bitShift(long position) {
                return (position << 9) & ~RIGHT_EDGE_MASK;
            }
        });

        // NORTH:
        shift.put(NORTH, new IBitShift() {
            @Override
            public long bitShift(long position) {
                return position << 8;
            }
        });

        // NORTH EAST:
        shift.put(NORTH_EAST, new IBitShift() {
            @Override
            public long bitShift(long position) {
                return (position << 7) & ~LEFT_EDGE_MASK;
            }
        });

        // EAST:
        shift.put(EAST, new IBitShift() {
            @Override
            public long bitShift(long position) {
                return (position >>> 1) & ~LEFT_EDGE_MASK;
            }
        });

        // SOUTH EAST:
        shift.put(SOUTH_EAST, new IBitShift() {
            @Override
            public long bitShift(long position) {
                return (position >>> 9) & ~LEFT_EDGE_MASK;
            }
        });

        // SOUTH:
        shift.put(SOUTH, new IBitShift() {
            @Override
            public long bitShift(long position) {
                return position >>> 8;
            }
        });

        // SOUTH WEST:
        shift.put(SOUTH_WEST, new IBitShift() {
            @Override
            public long bitShift(long position) {
                return (position >>> 7) & ~RIGHT_EDGE_MASK;
            }
        });

        // WEST:
        shift.put(WEST, new IBitShift() {
            @Override
            public long bitShift(long position) {
                return (position << 1) & ~RIGHT_EDGE_MASK;
            }
        });
    }
}
