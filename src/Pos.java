import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.lang.Math.*;
public class Pos {
    public final int x;
    public final int y;
    public static final int size = 6;
    private static final Pos[][] instances = new Pos[size][size];
    private static final int[][] weights = new int[size][size];
    private static final LinkedList<Pos[]> allRows = new LinkedList<Pos[]>();

    private static final HashMap<Pos, LinkedList<Pos[]>> rows = createRows();

    public Pos(int x, int y) {
        this.x = x;
        this.y = y;
    }

    // rows.get(pos) will return a list of 3-in-a-rows that contains pos
    private static HashMap<Pos, LinkedList<Pos[]>> createRows() {
        HashMap<Pos, LinkedList<Pos[]>> rows = new HashMap<>();
        // initialise linkedLists
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                rows.put(toPos(i, j), new LinkedList<>());
            }
        }
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size-2; j++) {

                // [(x,y),(x,y+1),(x,y+2)]
                Pos[] row = new Pos[3];
                for (int k = 0; k < 3; k++) {
                    Pos pos = toPos(i, j+k);
                    row[k] = pos;
                    rows.get(pos).add(row);
                }
                allRows.add(row);

                // [(x,y),(x+1,y),(x+2,y)]
                Pos[] row2 = new Pos[3];
                for (int k = 0; k < 3; k++) {
                    Pos pos = toPos(j+k, i);
                    row2[k] = pos;
                    rows.get(pos).add(row2);
                }
                allRows.add(row2);
            }
        }

        for (int i = 0; i < size-2; i++) {
            for (int j = 0; j < size-2; j++) {
                // [(x,y),(x+1,y+1),(x+2,y+2)]
                Pos[] row = new Pos[3];
                for (int k = 0; k < 3; k++) {
                    Pos pos = toPos(i+k, j+k);
                    row[k] = pos;
                    rows.get(pos).add(row);
                }
                allRows.add(row);

                // [(x+2,y),(x+1,y+1),(x,y+2)]
                Pos[] row2 = new Pos[3];
                for (int k = 0; k < 3; k++) {
                    Pos pos = toPos(i+2-k, j+k);
                    row2[k] = pos;
                    rows.get(pos).add(row2);
                }
                allRows.add(row2);
            }
        }
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                weights[i][j] = rows.get(toPos(i, j)).size();
            }
        }

        return rows;
    }

    public static LinkedList<Pos[]> getRows(int x, int y) {
        return rows.get(toPos(x, y));
    }

    public static LinkedList<Pos[]> getAllRows() {
        return allRows;
    }

    public static int applyWeights(int[][] board) {
        int sum = 0;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                sum += board[i][j] * weights[i][j];
            }
        }
        return sum;
    }

    public static Pos toPos(int x, int y) {
        if (instances[x][y] == null) {
            instances[x][y] = new Pos(x, y);
        }
        return instances[x][y];
    }

    public String toString() {
        return String.format("(%d, %d)", x, y);
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return (int) (Math.pow(2, x) * Math.pow(3, y));
    }
}