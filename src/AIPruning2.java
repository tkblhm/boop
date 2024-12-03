import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Objects;
import java.util.stream.*;

public class AIPruning2 extends AI{
    private int depth;
    private static int MAX = 1000;
    private static int MIN = -1000;
    private int[] result;
    private boolean[] finished;
    private long time;
    private final Object lock = new Object();
    public AIPruning2(Game game, int player, Monitor<Move> monitor, int depth) {
        super(game, player, monitor);
        this.depth = depth;
    }

    @Override
    public Move setMove() {
        synchronized (lock) {
            System.out.println("time:" + time);
            long start = System.currentTimeMillis();
            Move[] moves = game.getLegalMoves().toArray(new Move[0]);
            result = new int[moves.length];
            finished = new boolean[moves.length];
            for (int i = 0; i < moves.length; i++) {
                Thread worker = new Thread(new Worker(moves[i], i));
                worker.start();
            }
            while (!allFinished()) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                }
            }
            time += System.currentTimeMillis() - start;
            System.out.println("AIPruning2 result:");
            for (int i = 0; i < moves.length; i++) {
                System.out.println("" + moves[i] + " : " + result[i]);
            }
            if (player == Game.GREY) {
                int max = MIN - 1;
                Move maxMove = new Move(-1, -1, 0);
                for (int i = 0; i < result.length; i++) {
                    if (result[i] > max) {
                        max = result[i];
                        maxMove = moves[i];
                    }
                }
                return maxMove;

            } else {
                int min = MAX + 1;
                Move minMove = new Move(-1, -1, 0);
                for (int i = 0; i < result.length; i++) {
                    if (result[i] < min) {
                        min = result[i];
                        minMove = moves[i];
                    }
                }
                return minMove;
            }
        }
    }

    private int maxNode(Game game, int depth, int alpha, int beta) {
        if (game.winner != 0 || depth == 0) {
            return heuristic(game);
        }
        int val = MIN;
        LinkedList<Move> moves = game.getLegalMoves();
        for (Move move: moves) {
            Game newGame = game.copyAndMove(move);
            int val2 = minNode(newGame, depth-1, alpha, beta);
            if (val2 >= beta) {
                return val2;
            }
            if (val2 > val) {
                val = val2;
            }
            if (val2 > alpha) {
                alpha = val2;
            }
        }
        return val;
    }

    private int minNode(Game game, int depth, int alpha, int beta) {
        if (game.winner != 0 || depth == 0) {
            return heuristic(game);
        }
        int val = MAX;
        LinkedList<Move> moves = game.getLegalMoves();
        for (Move move: moves) {
            Game newGame = game.copyAndMove(move);
            int val2 = maxNode(newGame, depth-1, alpha, beta);
            if (val2 <= alpha) {
                return val2;
            }
            if (val2 < val) {
                val = val2;
            }
            if (val2 < beta) {
                beta = val2;
            }
        }
        return val;
    }



    private int heuristic(Game game) {
        if (game.winner != 0) {
            return game.winner * MAX;
        }
        int sum = (game.pieces[0][1] - game.pieces[1][1]) * 10;
        int[][] board = game.getBoard();

        sum += Pos.applyWeights(board);

        return sum;
    }

    public class Worker implements Runnable{
        private int index;
        private Move move;
        public Worker(Move move, int index) {
            this.move = move;
            this.index = index;
        }

        @Override
        public void run() {
            int val;
            if (player == Game.ORANGE) {
                val = maxNode(game.copyAndMove(move), depth, MIN, MAX);
            } else {
                val = minNode(game.copyAndMove(move), depth, MIN, MAX);
            }
            synchronized (lock) {
                result[index] = val;
                finished[index] = true;
                lock.notify();
            }
        }
    }

    private boolean allFinished() {
        for (boolean b: finished) {
            if (!b) {
                return false;
            }
        }
        return true;
    }
}
