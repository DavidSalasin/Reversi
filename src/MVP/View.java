package MVP;

import BIT_MANAGEMENT.BitBoard;
import MVP.Enums.Difficulty;
import MVP.Enums.Player;
import MVP.Interfaces.IPresenter;
import MVP.Interfaces.IView;

import java.util.*;

import static MVP.Enums.Player.*;
import static MVP.Enums.GameStatus.*;

/**
 * ReversiView class (VIEW LAYER) for Reversi.
 * Frontend interaction with player from the game.
 *
 * @author David Salasin
 */
public class View implements IView
{
    // Scanner for user console input.
    private static final Scanner input = new Scanner(System.in);

    // Presenter layer for View.
    private final IPresenter presenter;

    private boolean playFlag;
    private boolean inputFlag;
    private Player currentPlayer;

    /*
     * Constructor of VIEW.
     */
    public View()
    {
        // Constructing Presenter for view to contact Model.
        presenter = new Presenter();
    }


    // Prints current game board positions.
    /* Customised GUI that fits the next game board edges: 2 (for testing), 4, 8 (BY DEFAULT), 16 and 32 */
    public void printBoard(BitBoard board)
    {
        int i, j, edge = 8;

        long bBits = board.getColorBits(BLACK);
        long wBits = board.getColorBits(WHITE);
        long leftBitMask = -9223372036854775808L;

        System.out.print("      ");
        for (j = 0; j < edge; j++) System.out.print("_____ ");

        for (i = edge; i > 0; i--)
        {
            System.out.print("\n     |");
            for (j = 0; j < edge; j++) System.out.format("     |");

            System.out.printf("\n  %d  |", i);
            for (j = 0; j < edge; j++)
            {
                if ((bBits & leftBitMask) == leftBitMask  && (wBits & leftBitMask) == leftBitMask) {
                    System.out.println("\n\nERROR.");
                    return;
                }

                char x = ' ';
                if ((bBits & leftBitMask) == leftBitMask)
                    x = BLACK.icon;
                else if ((wBits & leftBitMask) == leftBitMask)
                    x = WHITE.icon;

                System.out.printf("  %c  |", x);

                bBits = bBits << 1;
                wBits = wBits << 1;
            }

            System.out.print("\n     |");
            for (j = 0; j < edge; j++) System.out.format("_____|");
        }

        System.out.print("\n      ");
        for (i = 'A'; i < 'I'; i++) System.out.printf("  %c   ", i);
        System.out.print("\n\n");
    }

    private static Coordinates playerInput()
    {
        ArrayList<Object> codes = null;

        boolean falseInputFlag = true;
        while (falseInputFlag)
        {
            System.out.print("> ");

            codes = new ArrayList<>(Arrays.asList(input.nextLine().split("[ ]+")));
            codes.removeIf(n -> Objects.equals(n, ""));

            //
            if (codes.size() != 2)
            {
                System.out.printf("! WRONG INPUT: amount of parameters entered: %d, instead of 2 !\n", codes.size());

                continue;
            }

            //
            String s1 = (String)codes.get(0), s2 = (String)codes.get(1);
            if ((s1.length() > 1) || (s2.length() > 1))
            {
                System.out.print("! WRONG INPUT: wrong length input: ");

                if (s1.length() > 1)
                    System.out.printf("'%s' (%d) ", s1, s1.length());
                if (s2.length() > 1)
                    System.out.printf("'%s' (%d) ", s2, s2.length());

                System.out.print("!\n");

                continue;
            }

            //
            char c1 = s1.charAt(0), c2 = s2.charAt(0);
            if (!Character.isDigit(c1) || !Character.isLetter(c2))
            {
                System.out.print("! WRONG INPUT: wrong character input: ");

                if (!Character.isDigit(c1))
                    System.out.printf("'%c' (Not a digit) ", c1);
                if (!Character.isLetter(c2))
                    System.out.printf("'%c' (Not a letter) ", c2);

                System.out.print("!\n");

                continue;
            }

            //
            int v1 = Character.getNumericValue(c1);
            if ((v1 < 1) || (v1 > 8) || (c2 > 'h') || (c2 < 'a' && c2 > 'H'))
            {
                System.out.print("! WRONG INPUT: input out of range: ");

                if ((v1 < 1) || (v1 > 8))
                    System.out.printf("'%d' (1 - 8) ", v1);
                if ((c2 > 'h') || (c2 < 'a' && c2 > 'H'))
                    System.out.printf("'%c' (A - H / a - h) ", c2);

                System.out.print("!\n");

                continue;
            }

            codes.set(0, v1);
            codes.set(1, (int)Character.toUpperCase(c2) - '@');

            falseInputFlag = false;
        }

        //
        return new Coordinates((int) codes.get(0), (int) codes.get(1));
    }


    // Presenting an information after a current player play.
    public void presentInformation(Information pInfo)
    {
        if (pInfo.status == SUCCESSFUL)
        {
            printBoard(pInfo.board);
            currentPlayer = pInfo.player;
            inputFlag = false;
        }
        else if (pInfo.status == SKIPPED)
        {
            printBoard(pInfo.board);
            System.out.println("              ! Skipped turn for " + pInfo.player + " !");
            inputFlag = false;
        }
        else if (pInfo.status == ENDED)
        {
            printBoard(pInfo.board);

            String endingMessage = pInfo.player != null ? pInfo.player + " has won the game!" :
                    "A draw as occurred!";

            System.out.println(endingMessage);

            int scoreBLACK = Long.bitCount(pInfo.board.getColorBits(BLACK));
            int scoreWHITE = Long.bitCount(pInfo.board.getColorBits(WHITE));

            System.out.println("BLACK's score:    " + scoreBLACK + "    |    WHITE's score:    " + scoreWHITE);

            inputFlag = false;
            playFlag = false;
        }
        else
        {
            System.out.println("! INVALID MOVE !");
        }
    }

    // Static function for playing the game.
    public void play(Difficulty gameMode)
    {
        boolean AIFlag = false;

        // Starts the game at the user end -> GUI view front.
        String title = "\n\t\t\t\t\t< REVERSI GAME >";
        System.out.println(title);

        // Starts game at backend, receiving the starting game information.
        Information pInfo = presenter.startGame(gameMode);

        // Prints the starting game board, and sets starting player as received from backend.
        printBoard(pInfo.board);
        input.nextLine();

        playFlag = true;
        currentPlayer = pInfo.player;

        while (playFlag)
        {
            System.out.println(currentPlayer +"'s turn. Enter Y row (1 - 8) and X (A - H / a - h) column:");

            inputFlag = true;

            while (inputFlag)
            {

                // Player vs Player/AI:

//                Coordinates coordinates = playerInput();
//                pInfo = presenter.playerTurn(coordinates);
//                presentInformation(pInfo);

                // AI vs AI:

                long start = System.currentTimeMillis();

                pInfo = presenter.playerTurn(null);

                long finish = System.currentTimeMillis();

                System.out.printf("Finished in %f seconds.\n", (float)(finish - start) / 1000);

                presentInformation(pInfo);
            }




                // Player vs AI:

            input.nextLine();

                //            if (AIFlag == true && pInfo.status == SUCCESSFUL)
                //            {
                //                System.out.println("AI's turn.");
                //                presentInformation(presenter.playerTurn(null));
                //            }
        }
    }
}
