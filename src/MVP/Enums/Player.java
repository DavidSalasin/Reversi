package MVP.Enums;

public enum Player
{
    BLACK(1, 'X'),
    WHITE(-1, 'O');

    public final int label;
    public final char icon;

    Player(int label, char icon)
    {
        this.label = label;
        this.icon = icon;
    }

    public static Player currentOpponent(Player p)
    {
        return switch (p) {
            case BLACK -> WHITE;
            case WHITE -> BLACK;
        };
    }
}
