//import java.util.HashMap;
//
//// get 3 by 3 booped board
//public class Box {
//    public static int size = 3;
//    public static int[] primes = {2, 3, 5, 7, 11, 13, 17, 19, 23};
//    private static HashMap<Box, int[][]> map = new HashMap<>();
//
//    public int[][] board;
//    private boolean hashed;
//    private int hash;
//
//    public Box(int[][] board) {
//        this.board = board;
//    }
//
//    public static void createMap() {
//
//    }
//
//    public static int[][] getBooped(int[][] board) {
//        Box box = new Box(board);
//        if (!map.containsKey(box)) {
//            map.put(box, boop(box.board));
//        }
//        return map.get(box);
//    }
//
//    private static int[][] boop(int[][] board) {
//        int[][] newBoard = new int[size][size];
//        int centre = newBoard[1][1];
//        push(newBoard, centre, )
//
//    }
//
//    private void push(int[][] board, int type,  int xAdj, int yAdj, int xDest, int yDest) {
//        int adj = board[xAdj][yAdj];
//        int dest = board[xDest][yDest];
//        if (adj != 0 && dest == 0 && Math.abs(adj) <= Math.abs(type)) {
//            board[xAdj][yAdj] = 0;
//            board[xDest][yDest] = adj;
//        }
//    }
//
//    @Override
//    public boolean equals(Object obj) {
//        if (!(obj instanceof Box)) {
//            return false;
//        }
//        for (int i = 0; i < size; i++) {
//            for (int j = 0; j < size; j++) {
//                if (((Box) obj).board[i][j] != board[i][j]) {
//                    return false;
//                }
//            }
//        }
//        return true;
//    }
//
//    @Override
//    public int hashCode() {
//        if (!hashed) {
//            hash = 1;
//            for (int i = 0; i < size; i++) {
//                for (int j = 0; j < size; j++) {
//                    hash *= Math.pow(primes[i*3+j], board[i][j]);
//                }
//            }
//        }
//        return hash;
//    }
//}
