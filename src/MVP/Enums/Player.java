package MVP.Enums;


/**
 * <h1>Enum type: 'Player'</h1>
 *
 * Used to represent each of the two players (separated by
 * opposing colors).
 *
 * Includes heuristic label parameter for score calculation,
 * and a textual icon char.
 *
 * @author David Salasin
 */
public enum Player
{
    BLACK(1, 'X'),
    WHITE(-1, 'O');


    /**
     * Integer heuristic label parameter for score calculation.
     */
    public final int label;


    /**
     * Char textual icon for the player.
     */
    public final char icon;


    /**
     * 'Player' enum constructor.
     *
     * @param label Heuristic label parameter.
     * @param icon Textual icon.
     */
    Player(int label, char icon)
    {
        this.label = label;
        this.icon = icon;
    }


    /**
     * Returns the current opponent to player's X color.
     *
     * @param p Current Player.
     * @return The other available player color.
     */
    public static Player currentOpponent(Player p)
    {
        return switch (p) {
            case BLACK -> WHITE;
            case WHITE -> BLACK;
        };
    }
}
