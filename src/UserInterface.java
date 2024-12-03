import javax.swing.*;
import java.awt.BorderLayout;
// Main window
public class UserInterface extends JFrame {
    public BoardPanel boardPanel;
    public ControlPanel controlPanel;
    public UserInterface(EventMonitor monitor) {
        super("boop.");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // game board
        boardPanel = new BoardPanel(monitor);

        // control panel
        controlPanel = new ControlPanel(monitor);
        getContentPane().add(controlPanel.scrollPane);

        setLayout(new BorderLayout());
        add(boardPanel, BorderLayout.WEST);
        add(controlPanel, BorderLayout.EAST);

        setSize(1300, 900);
        setLocationRelativeTo(null);
        setVisible(true);

        monitor.push(new Event(this));
    }
}
