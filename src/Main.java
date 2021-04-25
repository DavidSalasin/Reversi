import MVP.View;

import static MVP.Enums.Difficulty.*;

public class Main
{
    // Runs as main program.
    public static void main(String[] args)
    {
        // Plays Reversi.
        new View().play(PVP);
    }
}
