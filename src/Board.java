//import java.util.HashMap;
//
//public class Board {
//    private int[][] board;
//    private boolean isHashed;
//    private int hash;
//    private static int size = 6;
//    private static int[] primes = {2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 53, 59, 61, 67, 71, 73, 79, 83, 89, 97, 101, 103, 107, 109, 113, 127, 131, 137, 139, 149, 151};
//
//    private static HashMap
//    public Board(int[][] board) {
//        this.board = board;
//    }
//
//    public static
//    @Override
//    public int hashCode() {
//        if (!isHashed) {
//            int result = 1;
//            for (int i = 0; i < size; i++) {
//                for (int j = 0; j < size; j++) {
//                    result *= Math.pow(primes[i * size + j], board[i][j] + 2);
//                }
//            }
//            hash = result;
//        }
//        return hash;
//    }
//
//    @Override
//    public boolean equals(Object obj) {
//        return (hashCode() == obj.hashCode());
//    }
//}
