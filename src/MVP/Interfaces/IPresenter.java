package MVP.Interfaces;

import MVP.Coordinates;
import MVP.Enums.Difficulty;
import MVP.Enums.GameStatus;
import MVP.Enums.Player;
import MVP.Information;

import java.util.List;

public interface IPresenter
{
    Information startGame(Difficulty gameMode);

    Information playerTurn(Coordinates coordinates);
}
