package MVP;


/**
 * <h1>Class type: 'Move'</h1>
 *
 * <u>Extends</u> 'Coordinates', adding a score property.
 *
 * @author David Salasin
 */
public class Move extends Coordinates
{
    /**
     * Integer score property, for good moves calculation.
     *
     * @see Model
     */
    public int score;


    /**
     * Constructor for 'Move'.
     * <p></p>
     * Sets 'Coordinate' properties accordingly, and initiates score to minus
     * infinity (a really big negative number).
     *
     * @param y_position Vertical position of a board slot.
     * @param x_position Horizontal position of a board slot.
     */
    public Move(int y_position, int x_position)
    {
        super(y_position, x_position);
        score = -1000;
    }


    /**
     * Updates instance's properties.
     *
     * @param y_position Vertical position of a board slot.
     * @param x_position Horizontal position of a board slot.
     * @param score Move's heuristic score.
     */
    public void updateMove(int y_position, int x_position, int score)
    {
        this.y_position = y_position;
        this.x_position = x_position;
        this.score = score;
    }
}
