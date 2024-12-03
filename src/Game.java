import java.awt.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.lang.Math;

public class Game {

    public static final int size = 6;
    public static final int pieceNumber = 8;
    public static final int GREY = 1;
    public static final int ORANGE = -1;
    public static final int GREYKITTEN = 1;
    public static final int GREYCAT = 2;
    public static final int ORANGEKITTEN = -1;
    public static final int ORANGECAT = -2;
    public static final int EMPTY = 0;

    // represents the board situation
    protected int[][] board;
    // 1: grey, -1: orange
    protected int nextPlayer;
    protected LinkedList<Move> legalMoves = new LinkedList<>();
    protected boolean isLegalMovesUpdated;
    protected int winner;
    // number of pieces available
    //[[grey kitten, grey cat], [orange kitten, orange cat]]
    protected int[][] pieces;

    public Game() {
        board = new int[size][size];
        isLegalMovesUpdated = false;
        nextPlayer = GREY;
        winner = 0;
        pieces = new int[2][2];
        pieces[0][0] = pieceNumber;
        pieces[1][0] = pieceNumber;
        updateLegalMoves();
    }

    public Game(int[][] board, int nextPlayer, int winner, int[][] pieces) {
        this.board = board;
        this.nextPlayer = nextPlayer;
        this.winner = winner;
        this.pieces = pieces;
        isLegalMovesUpdated = false;
    }

    public int[][] getBoard() {
        return board;
    }

    public int[][] getPieces() {
        return pieces;
    }


    public LinkedList<Move> getLegalMoves() {
        updateLegalMoves();
        return legalMoves;
    }

    public int getNextPlayer() {
        return nextPlayer;
    }

    // return number of pieces left of certain type.
    public int getLeftPieces(int type) {
        if (type != EMPTY) {
            return pieces[(int)(-Math.signum(type)+1)/2][Math.abs(type/2)];
        }
        System.out.println("error in getLeftPieces");
        return 0;
    }

    // find and store legal moves in legalMoves
    protected void updateLegalMoves() {
        if (isLegalMovesUpdated) {
            return;
        }
        legalMoves = new LinkedList<Move>();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                // try if this square is empty
                if (board[i][j] == EMPTY) {
                    int kitten = nextPlayer;
                    // try placing a kitten
                    if (getLeftPieces(kitten) > 0) {
                        GameInfo gameInfo = boopedBoard(i, j, kitten);
                        LinkedList<HashSet<Pos>> list = getGraduatedPieces(gameInfo.getBoard(), nextPlayer);
                        // add corresponding moves
                        if (list.isEmpty()) {
                            legalMoves.add(new Move(i, j, kitten));
                        } else {
                            for (HashSet<Pos> set: list) {
                                legalMoves.add(new Move(i, j, kitten, set));
                            }
                        }
                    }
                    int cat = kitten * 2;
                    // try placing a cat
                    if (getLeftPieces(cat) > 0) {
                        GameInfo gameInfo = boopedBoard(i, j, cat);
                        LinkedList<HashSet<Pos>> list = getGraduatedPieces(gameInfo.getBoard(), nextPlayer);
                        // add corresponding moves
                        if (list.isEmpty()) {
                            legalMoves.add(new Move(i, j, cat));
                        } else {
                            for (HashSet<Pos> set: list) {
                                legalMoves.add(new Move(i, j, cat, set));
                            }
                        }
                    }
                }
            }
        }

        isLegalMovesUpdated = true;
    }

    protected void boop(int[][] board, int[][] pieces, int type, int xAdj, int yAdj) {
        int adj = board[xAdj][yAdj];
        if (adj != EMPTY && Math.abs(adj) <= Math.abs(type)) {
            board[xAdj][yAdj] = EMPTY;
            // add 1 to the available pieces
            pieces[(int)(-Math.signum(adj)+1)/2][Math.abs(adj/2)] += 1;
        }
    }


    protected void boop(int[][] board, int type,  int xAdj, int yAdj, int xDest, int yDest) {
        int adj = board[xAdj][yAdj];
        int dest = board[xDest][yDest];
        if (adj != EMPTY && dest == EMPTY && Math.abs(adj) <= Math.abs(type)) {
            board[xAdj][yAdj] = EMPTY;
            board[xDest][yDest] = adj;
        }
    }

    // return the new board after a piece is placed
    protected GameInfo boopedBoard(int x, int y, int type) {
        int[][] newBoard = copyBoard();
        int[][] newPieces = copyPieces();
        GameInfo gameInfo = new GameInfo(newBoard, newPieces);
//        Move move = new Move(x, y, type);
//        if (GameInfo.contains(gameInfo, move)) {
//            return GameInfo.get(gameInfo, move);
//        }
        // remove the placed piece
        newPieces[(int)(-Math.signum(type)+1)/2][Math.abs(type/2)] -= 1;

        if (newBoard[x][y] != EMPTY) {
            System.out.println("error in boopedBoard");
        }
        newBoard[x][y] = type;

        int last = size - 1;
        int sec = size - 2;

        // check (x-1, y)
        if (x == 1) {
            boop(newBoard, newPieces, type, x-1, y);
        } else if (x > 1) {
            boop(newBoard, type, x-1, y, x-2, y);
        }

        // check (x, y-1)
        if (y == 1) {
            boop(newBoard, newPieces, type, x, y-1);
        } else if (y > 1) {
            boop(newBoard, type, x, y-1, x, y-2);
        }

        // check (x+1, y)
        if (x == sec) {
            boop(newBoard, newPieces, type, x+1, y);
        } else if (x < sec) {
            boop(newBoard, type, x+1, y, x+2, y);
        }

        // check (x, y+1)
        if (y == sec) {
            boop(newBoard, newPieces, type, x, y+1);
        } else if (y < sec) {
            boop(newBoard, type, x, y+1, x, y+2);
        }

        // check (x-1, y-1)
        if ((x == 1 && y >= 1) || (y == 1 && x >= 1)) {
            boop(newBoard, newPieces, type, x-1, y-1);
        } else if (x > 1 && y > 1) {
            boop(newBoard, type, x-1, y-1, x-2, y-2);
        }

        // check (x-1, y+1)
        if ((x == 1 && y < last) || (x >= 1 && y == sec)) {
            boop(newBoard, newPieces, type, x-1, y+1);
        } else if (x > 1 && y < sec) {
            boop(newBoard, type, x-1, y+1, x-2, y+2);
        }

        // check (x+1, y-1)
        if ((x < last && y == 1) || (x == sec && y >= 1)) {
            boop(newBoard, newPieces, type, x+1, y-1);
        } else if (y > 1 && x < sec) {
            boop(newBoard, type, x+1, y-1, x+2, y-2);
        }

        // check (x+1, y+1)
        if ((x < last && y == sec) || (x == sec && y < last)) {
            boop(newBoard, newPieces, type, x+1, y+1);
        } else if (x < sec && y < sec) {
            boop(newBoard, type, x+1, y+1, x+2, y+2);
        }

        //System.out.println("To add: " + gameInfo + move + gameInfo1);
//        GameInfo.add(gameInfo, move, gameInfo1);
        return new GameInfo(newBoard, newPieces);
    }

    // given a board, check if any pieces of a player can graduate
    protected LinkedList<HashSet<Pos>> getGraduatedPieces(int[][] board, int player) {
        LinkedList<HashSet<Pos>> list = new LinkedList<>();
        int pieceOnBoard = 0;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                // if piece of the specified player
                if (board[i][j] * player > 0) {
                    pieceOnBoard++;
                    for (Pos[] row: Pos.getRows(i, j)) {
                        // if every piece in the row belongs to the player
                        if (Arrays.stream(row).allMatch(pos -> board[pos.x][pos.y] * player > 0)) {
                            HashSet<Pos> set = new HashSet<Pos>(Arrays.asList(row));
                            if (!list.contains(set)) {
                                list.add(set);
                            }
                        }
                    }
                }
            }
        }

        if (pieceOnBoard == pieceNumber) {
            for (int i = 0; i < 6; i++) {
                for (int j = 0; j < 6; j++) {
                    if (board[i][j] * player > 0) {
                        HashSet<Pos> set = new HashSet<>();
                        set.add(Pos.toPos(i, j));
                        list.add(set);
                    }
                }
            }
        }
        return list;
    }
    // return true if a move is legal
    public boolean isLegalMove(Move move) {
        updateLegalMoves();
        return legalMoves.contains(move);
    }

    // make a move and return if finished
    public boolean makeMove(Move move) {
        if (!isLegalMove(move)) {
            System.out.println("error in makeMove");
        }
        GameInfo gameInfo = boopedBoard(move.x, move.y, move.type);
        board = gameInfo.getBoard();
        pieces = gameInfo.getPieces();

        boolean finished = true;
        // remove graduated pieces
        for (Pos pos: move.graduated) {
            int piece = board[pos.x][pos.y];
            if (piece == EMPTY) {
                System.out.println("error in move.graduated in makeMove!!!!!!!!!!!!!!!!!!!!!!");
            }
            if (piece == nextPlayer) {
                finished = false;
            }
            board[pos.x][pos.y] = EMPTY;
            pieces[(int)(-Math.signum(piece)+1)/2][1] += 1;
        }

        nextPlayer =  - nextPlayer;
        isLegalMovesUpdated = false;
        if (finished && move.graduated.size() == 3) {
            winner = - nextPlayer;
            return true;
        }
        return false;
    }

    public Game copy() {
        return new Game(copyBoard(), nextPlayer, winner, copyPieces());
    }
    public Game copyAndMove(Move move) {
        Game newGame = new Game(copyBoard(), nextPlayer, winner, copyPieces());
        newGame.makeMove(move);
        return newGame;
    }

    // return a copy of board
    protected int[][] copyBoard() {
        int[][] newBoard = new int[size][size];
        for (int i = 0; i < size; i++) {
            System.arraycopy(board[i], 0, newBoard[i], 0, size);
        }
        return newBoard;
    }

    // return a copy of pieces
    protected int[][] copyPieces() {
        int [][] newPieces = new int[2][2];
        System.arraycopy(pieces[0], 0, newPieces[0], 0, 2);
        System.arraycopy(pieces[1], 0, newPieces[1], 0, 2);
        return newPieces;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder("Game: ");
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (board[i][j] != 0) {
                    str.append(String.format("(%d, %d, %d) ", i, j, board[i][j]));
                }
            }
        }
        return str.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Game)) {
            return false;
        }
        int[][] board1 = ((Game) obj).getBoard();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (board[i][j] != board1[i][j]) {
                    return false;
                }
            }
        }
        int[][] pieces1 = ((Game) obj).getPieces();
        return (nextPlayer == ((Game) obj).nextPlayer && pieces[0][0] == pieces1[0][0] && pieces[1][0] == pieces1[1][0] && winner == ((Game) obj).winner);
    }
}
