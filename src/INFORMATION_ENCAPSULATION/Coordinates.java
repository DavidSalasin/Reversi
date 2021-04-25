package INFORMATION_ENCAPSULATION;

/**
 * <h1>Class type: 'Coordinates'</h1>
 *
 * Represents an 8 x 8 board slot coordination.
 *
 * @author David Salasin
 */
public class Coordinates
{
    /**
     * Vertical position of a board slot.
     */
    public int y_position;


    /**
     * Horizontal position of a board slot.
     */
    public int x_position;


    /**
     * Constructor for 'Coordinates'.
     * <p></p>
     * Initiates vertical/horizontal properties accordingly.
     *
     * @param y_position Vertical position of a board slot.
     * @param x_position Horizontal position of a board slot.
     */
    public Coordinates(int y_position, int x_position)
    {
        this.y_position = y_position;
        this.x_position = x_position;
    }
}
