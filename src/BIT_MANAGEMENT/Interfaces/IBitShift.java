package BIT_MANAGEMENT.Interfaces;


/**
 * <h1>Interface reference type: 'IBitShift'</h1>
 *
 * Reference implemented differently for each board tile direction by the 'BitShifters' class
 * as <b>lambdas expressions</b>.
 *
 * @author David Salasin
 */
public interface IBitShift
{
    /**
     * Shifts the board (long value) bits by the lambda implementation of the method stamp.
     *
     * @param position board pieces to be shifted to a certain X direction.
     * @return Long values for the shifted board pieces.
     */
    long bitShift(long position);
}
