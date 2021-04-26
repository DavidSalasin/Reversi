import MVP.View;

import static MVP.Enums.GameMode.HARDCORE;

public class Main
{
    // Runs as main program.
    public static void main(String[] args)
    {
        // Plays Reversi (WITH INSERTED GAME MODE)
        new View().play(HARDCORE);
    }
}
