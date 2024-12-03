import com.sun.jdi.event.ExceptionEvent;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// instance of game board
public class BoardPanel extends JPanel {


    private Square[][] squares;
    private EventMonitor monitor;

    private Font myFont = new Font(Font.SANS_SERIF, Font.BOLD, 15);
    private Font largeFont = new Font(Font.SANS_SERIF, Font.BOLD, 180);
    private Color grey = new Color(188, 196, 215);
    private Color orange = new Color(237, 185, 68);
    private Color white = new Color(250,250,250);
    private Border hightlightBorder = BorderFactory.createLineBorder(Color.YELLOW, 2);
    private Border noBorder = new JButton().getBorder();

    public BoardPanel(EventMonitor monitor) {
        this.monitor = monitor;
        setLayout(new GridLayout(Game.size, Game.size));
        squares = new Square[Game.size][Game.size];
        for (int i=0; i<Game.size; i++) {
            for (int j=0; j<Game.size; j++) {
                Square square = new Square(i, j);
                squares[i][j] = square;
                square.setFont(myFont);
                add(square);
            }
        }

        setPreferredSize(new Dimension(900, 900));
    }

    public void updateSquare(int r, int c, int type, boolean selected) {
        if (selected) {
            squares[r][c].setBorder(hightlightBorder);
        } else {
            squares[r][c].setBorder(noBorder);
        }
        if (type == Game.GREYKITTEN) {
            squares[r][c].setForeground(grey);
            squares[r][c].setText("=^.^=");
        } else if (type == Game.GREYCAT) {
            squares[r][c].setForeground(grey);
            squares[r][c].setText("(=^・ω・^=)");
        } else if (type == Game.ORANGEKITTEN) {
            squares[r][c].setForeground(orange);
            squares[r][c].setText("=^.^=");
        } else if (type == Game.ORANGECAT) {
            squares[r][c].setForeground(orange);
            squares[r][c].setText("(=^・ω・^=)");
        } else {
            squares[r][c].setForeground(white);
            squares[r][c].setText("");
        }
    }


    // instance of a square
    class Square extends JButton {
        public Square(int r, int c) {
            addActionListener(new ButtonListener(r, c));
            setFont(myFont);
        }
    }

    // listener for squares
    class ButtonListener implements ActionListener {
        private int x;
        private int y;
        public ButtonListener(int r, int c) {
            x = r;
            y = c;
        }
        public void actionPerformed(ActionEvent e) {
            monitor.push(new Event(Pos.toPos(x, y)));
        }
    }
}


