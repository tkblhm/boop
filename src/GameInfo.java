import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

// simple class that represents game state
public class GameInfo {
    private final int[][] board;
    private final int[][] pieces;
    // A set of selected squares for display
    private final HashSet<Pos> selected;

    private boolean isHashed = false;
    private int hash;
    private static final int size = 6;
    // Given a GameInfo and a Move, return a GameInfo representing the new game state
    private static final HashMap<GameInfo, HashMap<Move, GameInfo>> map = new HashMap<>();
//    private static final HashSet<GameInfo> set = new HashSet<>();

    public GameInfo(int[][] board, int[][] pieces) {
        this.board = new int[size][size];
        for (int i = 0; i < size; i++) {
            System.arraycopy(board[i], 0, this.board[i], 0, size);
        }
        this.pieces = new int[2][2];
        for (int i = 0; i < 2; i++) {
            System.arraycopy(pieces[i], 0, this.pieces[i], 0, 2);
        }
        selected = new HashSet<>();

    }

    // Copy and return the board
    public int[][] getBoard() {
        int[][] newBoard = new int[size][size];
        for (int i = 0; i < size; i++) {
            System.arraycopy(board[i], 0, newBoard[i], 0, size);
        }
        return newBoard;
    }

    // Copy and return pieces
    public int[][] getPieces() {
        int[][] newPieces = new int[2][2];
        for (int i = 0; i < 2; i++) {
            System.arraycopy(pieces[i], 0, newPieces[i], 0, 2);
        }
        return newPieces;
    }

    // select or deselect a square
    public void select(int x, int y) {
        Pos pos = Pos.toPos(x, y);
        if (selected.contains(pos)) {
            selected.remove(pos);
        } else {
            selected.add(pos);
        }
    }

    public boolean isSelected(int x, int y) {
        return selected.contains(Pos.toPos(x, y));
    }

    // deselect all
    public void clear() {
        selected.clear();
    }

//    public synchronized static GameInfo createGameInfo(int[][] board, int[][] pieces) {
//        GameInfo gameInfo = new GameInfo(board, pieces);
//        if
//    }

    // check if map contains input
    public synchronized static boolean contains(GameInfo gameInfo, Move move) {
        if (!map.containsKey(gameInfo)) {
            map.put(gameInfo, new HashMap<>());
            return false;
        } else {
            return (map.get(gameInfo).containsKey(move));
        }
    }

    // add an entry to the map
    public synchronized static void add(GameInfo gameInfo, Move move, GameInfo gameInfo2) {
//        System.out.println("add to HashMap: " + gameInfo + move + gameInfo2);
        map.get(gameInfo).put(move, gameInfo2);

    }

    public synchronized static void print() {
        for (GameInfo game: map.keySet()) {
            for (Move move1: map.get(game).keySet()) {
                System.out.println("entry: " + game + move1 + map.get(game).get(move1));
            }
        }
    }

    // get an entry
    public synchronized static GameInfo get(GameInfo gameInfo, Move move) {
        return map.get(gameInfo).get(move);
    }

    // compute hash code and store the value in hash
    @Override
    public int hashCode() {
        if (!isHashed) {
            hash = Arrays.deepHashCode(board);
            isHashed = true;
//            System.out.println("Hash of " + this + " is " + hash);
        }
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof GameInfo)) {
            return false;
        }
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (board[i][j] != ((GameInfo) obj).board[i][j]) {
                    return false;
                }
            }
        }
        return (pieces[0][1] == ((GameInfo) obj).pieces[0][1] && pieces[1][1] == ((GameInfo) obj).pieces[1][1]);
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder("GameInfo: ");
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (board[i][j] != 0) {
                    str.append(String.format("(%d, %d, %d) ", i, j, board[i][j]));
                }
            }
        }
        return str.toString();
    }
}
