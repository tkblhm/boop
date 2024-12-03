import java.util.HashSet;
import java.util.Objects;

public class Move {
    public int x;
    public int y;
    public int type;
    public HashSet<Pos> graduated;

    public Move() {
        this.graduated = new HashSet<>();
    }

    public Move(int x, int y, int type) {
        this.x = x;
        this.y = y;
        this.type = type;
        this.graduated = new HashSet<>();
    }

    public Move(int x, int y, int type, HashSet<Pos> graduated) {
        this.x = x;
        this.y = y;
        this.type = type;
        this.graduated = graduated;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Move) {
            return (x == ((Move) obj).x && y == ((Move) obj).y && type == ((Move) obj).type && Objects.equals(graduated, ((Move) obj).graduated));
        }
        return false;
    }

    @Override
    public int hashCode() {
        return (int)( Math.pow(2, x) * Math.pow(3, y) * Math.pow(5, type+2));
    }

    @Override
    public String toString() {
        if (graduated.isEmpty()) {
            return String.format("(%d, %d, %d)", x, y, type);
        } else {
            StringBuilder str = new StringBuilder();
            for (Pos pos: graduated) {
                str.append(pos.toString()).append(" ");
            }
            return String.format("(%d, %d, %d) + [", x, y, type) + str + "]";
        }
    }
}
