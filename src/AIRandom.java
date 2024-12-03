import java.util.LinkedList;
import java.util.Random;


public class AIRandom extends AI{
    private Random random = new Random();

    public AIRandom(Game game, int player, Monitor<Move> slot) {
        super(game, player, slot);
    }

    @Override
    public Move setMove() {
        LinkedList<Move> moves = game.getLegalMoves();

        return moves.get(random.nextInt(moves.size()));
    }

}
