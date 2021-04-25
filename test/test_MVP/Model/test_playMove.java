package test_MVP.Model;

import BIT_MANAGEMENT.BitBoard;
import INFORMATION_ENCAPSULATION.Coordinates;
import MVP.Model;
import org.junit.jupiter.api.Test;

import static MVP.Enums.Player.*;
import static MVP.Enums.GameMode.*;

class test_playMove
{
    @Test
    void test_firstBlackPlay()
    {
        Model model = new Model();
        model.init(BEGINNER);
        model.playMove(BLACK, new Coordinates(3, 5));

        BitBoard board = model.getBoard();
        long pieces_BLACK = board.getColorBits(BLACK);
        long pieces_WHITE = board.getColorBits(WHITE);

        assert pieces_BLACK == 34762915840L && pieces_WHITE == 68719476736L;
    }
}