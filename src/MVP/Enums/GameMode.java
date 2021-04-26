package MVP.Enums;

import MVP.Model;


/**
 * <h1>Enum type: 'GameMode'</h1>
 *
 * Used to represent a chosen game mode (PVP / AI difficulty).
 *
 * Includes depth parameter, for AI's game-tree generic setting of the
 * depth for each difficulty.
 *
 * @author David Salasin
 */
public enum GameMode
{
    PVP(0),
    BEGINNER(5),
    INTERMEDIATE(7),
    HARDCORE(9);


    /**
     * Integer depth parameter, for 'Model' AI's game-tree depth.
     *
     * @see Model
     */
    public final int depth;


    /**
     * 'GameMode' enum constructor.
     *
     * @param depth AI's game-tree depth.
     */
    GameMode(int depth)
    {
        this.depth = depth;
    }
}
