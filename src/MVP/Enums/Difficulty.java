package MVP.Enums;

import MVP.Model;


/**
 * <h1>Enum type: 'Difficulty'</h1>
 *
 * Used to represent a chosen game difficulty.
 * <p></p>
 * Includes depth parameter, for AI's game-tree generic setting of the
 * depth for each difficulty.
 *
 * @author David Salasin
 */
public enum Difficulty
{
    PVP(0),
    BEGINNER(4),
    INTERMEDIATE(8),
    HARDCORE(12);


    /**
     * Integer depth parameter, for 'Model' AI's game-tree depth.
     *
     * @see Model
     */
    public final int depth;


    /**
     * 'Difficulty' enum constructor.
     *
     * @param depth AI's game-tree depth.
     */
    Difficulty(int depth)
    {
        this.depth = depth;
    }
}
