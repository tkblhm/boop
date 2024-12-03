// Base class of AI
public class AI implements Runnable{
    protected Game game;
    protected int player;
    protected Monitor<Move> monitor;
    public AI(Game game, int player, Monitor<Move> monitor) {
        this.player = player;
        this.monitor = monitor;
        this.game = game;
    }

    // Must be overridden by the subclass
    // Return the next move selected by the AI
    public Move setMove() {
        return new Move(-1, -1, 0);
    }
    public void run() {
//        // Keep putting and getting moves from the monitor
//        Move nextMove;
//        Move move;
//        if (player == Game.GREY) {
//            nextMove = setMove();
//            monitor.setSlot(nextMove, (1 - player) / 2);
//            game.makeMove(nextMove);
//        }
//        while (!monitor.getEnd()) {
//            move = monitor.getSlot((1+player)/2);
//            System.out.println("AI get move:" + move);
//            System.out.println("AI legal moves:");
//            for (Move move1: game.getLegalMoves()) {
//                System.out.print(move1);
//            }
//            System.out.println();
//            game.makeMove(move);
//            nextMove = setMove();
//            monitor.setSlot(nextMove, (1-player)/2);
//            System.out.println("AI set move:" + nextMove);
//            game.makeMove(nextMove);
//        }
        while (!monitor.getEnd()) {
            //System.out.println("ai new loop");
            monitor.getSlot(0);
            //System.out.println("ai picking a move");
            Move move = setMove();
            monitor.setSlot(move, 1);
            //System.out.println("ai sent a move");
        }

    }
}
