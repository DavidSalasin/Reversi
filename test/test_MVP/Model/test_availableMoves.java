package test_MVP.Model;

import MVP.Model;
import org.junit.jupiter.api.Test;

import static MVP.Enums.Player.*;
import static MVP.Enums.Difficulty.*;

public class test_availableMoves
{
    @Test
    void test_startingPositions()
    {

        Model model = new Model();
        model.init(BEGINNER);

        long available_BLACK = model.availableMoves(BLACK);
        long available_WHITE = model.availableMoves(WHITE);

        assert available_BLACK == 17729692631040L && available_WHITE == 8813810810880L;
    }
}
