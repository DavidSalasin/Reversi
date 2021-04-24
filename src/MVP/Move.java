package MVP;

public class Move extends Coordinates
{
    public int score;


    public Move(int y_position, int x_position)
    {
        super(y_position, x_position);
        score = -1000;
    }
}
