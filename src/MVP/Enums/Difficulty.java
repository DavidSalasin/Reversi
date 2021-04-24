package MVP.Enums;

public enum Difficulty {

    BEGINNER(4),
    INTERMEDIATE(8),
    HARDCORE(12);

    public final int depth;

    Difficulty(int depth)
    {
        this.depth = depth;
    }
}
