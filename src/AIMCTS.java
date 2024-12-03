import java.util.LinkedList;
import java.util.Random;

public class AIMCTS extends AI{
    private int time;
    private int n;
    private static int MAX = 100;
    private static int MIN = -100;
    private boolean stop = false;
    private int depth = 10;
    private int N;

    private Random rand = new Random();
    public AIMCTS(Game game, int player, Monitor<Move> monitor, int workers, int time) {
        super(game, player, monitor);
        this.time = time;
        this.n = workers;
    }

    @Override
    public Move setMove() {
        stop = false;
        N = 1;
        Node root = new Node(game);
        root.expand(true);
        for (int i = 0; i < n; i++) {
            Thread thread = worker(root);
            thread.start();
        }
        try {
            Thread.sleep(time * 1000L);
        } catch (InterruptedException e) {

        }
        stop = true;
        Node nextNode = root.getChild();
        System.out.println("AIMCTS result: ");
        System.out.println("N: " + N);
        for (Node node: root.getChildren()) {
            System.out.println("" + node.lastMove + " : " + (node.value / node.visits));
        }

        return nextNode.lastMove;
    }

    private Thread worker(Node root) {
        return new Thread(new Runnable() {
            @Override
            public void run() {
                while (!stop) {
                    LinkedList<Node> chain = new LinkedList<>();
                    Node node = root;
                    while (node.isExpanded()) {
                        chain.add(node);
                        node = node.getChild();
                    }
                    node.expand();
                    node = node.getChild();
                    chain.add(node);
                    int val = rollout(node.state);
                    for (Node node1: chain) {
                        node1.visit(val);
                    }
                    increment();
                }
            }
        });
    }

    private int rollout(Game game) {
        Game game1 = game.copy();
        for (int i = 0; i < depth; i++) {
            if (game1.winner != 0) {
                return heuristic(game1);
            }
            LinkedList<Move> moves = game1.getLegalMoves();
            game1.makeMove(moves.get(rand.nextInt(moves.size())));
        }
        return heuristic(game1);
    }

    private int heuristic(Game game) {
        if (game.winner != 0) {
            return game.winner * MAX;
        }
        int sum = (game.pieces[0][1] - game.pieces[1][1]) * 10;
        int[][] board = game.getBoard();

        sum += Pos.applyWeights(board);

        return sum / 2;
    }

    private synchronized void increment() {
        N++;
    }

    class Node{
        private int visits;
        private int value;
        private LinkedList<Node> children;
        private boolean isExpanded;
        private Game state;

        private Move lastMove;

        public Node(Game state, Move lastMove) {
            this.state = state;
            this.lastMove = lastMove;
        }

        public Node(Game state) {
            this.state = state;
        }

        public boolean isExpanded() {
            return isExpanded;
        }

        public Node getChild() {
            double max = Double.NEGATIVE_INFINITY;
            Node node = null;
            for (Node node1: children) {
                if (node1.ucb1() > max) {
                    max = node1.ucb1();
                    node = node1;
                }
            }
            return node;
        }

        public double ucb1() {
            if (visits == 0) {
                return Double.POSITIVE_INFINITY;
            }
            return (double) (-state.nextPlayer * value) / visits + Math.sqrt(Math.log(N) / visits);
        }

        public LinkedList<Node> getChildren() {
            return children;
        }

        public synchronized void expand() {
            if (!isExpanded) {
                children = new LinkedList<>();
                for (Move move : state.getLegalMoves()) {
                    children.add(new Node(state.copyAndMove(move)));
                }
                isExpanded = true;
            }

        }

        public synchronized void expand(boolean isRoot) {
            if (isRoot) {
                children = new LinkedList<>();
                for (Move move : state.getLegalMoves()) {
                    children.add(new Node(state.copyAndMove(move), move));
                }
                isExpanded = true;
            } else {
                expand();
            }
        }
        public synchronized void visit(int value) {
            visits++;
            this.value += value;
        }

        @Override
        public String toString() {
            return state.toString();
        }
    }


}
