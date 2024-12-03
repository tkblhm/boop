import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.stream.IntStream;

public class AIPruning extends AI{
    private int depth = 3;
    private static int MAX = 1000;
    private static int MIN = -1000;
    private long time1;
    private long time2;
    public AIPruning(Game game, int player, Monitor<Move> monitor) {
        super(game, player, monitor);
    }

    @Override
    public Move setMove() {
        System.out.println("times:" + time1 + "  " + time2);
        if (player == Game.GREY) {
            Move maxMove = new Move(-1, -1, 0);
            int max = MIN;
            for (Move move: game.getLegalMoves()){
                int val = minNode(game.copyAndMove(move), depth, MIN, MAX);
                if (val >= max) {
                    maxMove = move;
                    max = val;
                }
            }
            return maxMove;

        } else {
            Move minMove = new Move(-1, -1, 0);
            int min = MAX;
            for (Move move: game.getLegalMoves()){
                int val = maxNode(game.copyAndMove(move), depth, MIN, MAX);
                if (val <= min) {
                    minMove = move;
                    min = val;
                }
            }
            return minMove;
        }
    }

    private int maxNode(Game game, int depth, int alpha, int beta) {
        if (game.winner != 0 || depth == 0) {
            return heuristic(game);
        }
        int val = MIN;
        long time = System.currentTimeMillis();
        LinkedList<Move> moves = game.getLegalMoves();
        time1 += System.currentTimeMillis() - time;
        for (Move move: moves) {
            time = System.currentTimeMillis();
            Game newGame = game.copyAndMove(move);
            time2 += System.currentTimeMillis() - time;
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
        long time = System.currentTimeMillis();
        LinkedList<Move> moves = game.getLegalMoves();
        time1 += System.currentTimeMillis() - time;
        for (Move move: moves) {
            time = System.currentTimeMillis();
            Game newGame = game.copyAndMove(move);
            time2 += System.currentTimeMillis() - time;
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
//        for (Pos[] row: Pos.getAllRows()) {
//            int[] arr = Arrays.stream(row).map(pos -> board[pos.x][pos.y]).mapToInt(n -> n).toArray();
//            int posVal = (int) Arrays.stream(arr).filter(n -> n > 0).count();
//            int negVal = (int) Arrays.stream(arr).filter(n -> n < 0).count();
//            if (!(posVal > 0 && negVal > 0)) {
//                sum += (posVal + negVal) * Arrays.stream(arr).sum();
//            }
//        }

        sum += Pos.applyWeights(board);

        return sum;
    }
}
