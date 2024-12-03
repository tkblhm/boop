import java.util.Objects;

public class Event {
    public String type;
    public Pos pos;
    public UserInterface ui;
    public Event(String type) {
        this.type = type;
    }

    public Event(Pos pos) {
        this.type = "pos";
        this.pos = pos;
    }

    public Event(UserInterface ui) {
        this.ui = ui;
        this.type = "ui";
    }

    @Override
    public String toString() {
        if (Objects.equals(type, "pos")) {
            return String.format("(%d, %d)", pos.x, pos.y);
        }
        return type;
    }
}
