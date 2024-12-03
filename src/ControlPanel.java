import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ControlPanel extends JPanel {
    private EventMonitor monitor;
    private JLabel greyLabel;
    private JLabel orangeLabel;
    private JButton kittenButton;
    private JButton catButton;
    private JButton confirmButton;
    private JTextArea textArea;
    public JScrollPane scrollPane;
    public ControlPanel(EventMonitor monitor) {
        this.monitor = monitor;

        // stats panel
        JPanel statsPanel = new JPanel();
        statsPanel.setLayout(new BoxLayout(statsPanel, BoxLayout.Y_AXIS));

        greyLabel = new JLabel("grey : 8, 0");
        statsPanel.add(greyLabel);

        orangeLabel = new JLabel("orange : 8, 0");
        statsPanel.add(orangeLabel);

        // cat panel
        JPanel piecePanel = new JPanel();
        piecePanel.setLayout(new BorderLayout());

        kittenButton = new JButton();
        kittenButton.setText("Kitten");
        kittenButton.addActionListener(new ButtonListener("kitten"));
        piecePanel.add(kittenButton, BorderLayout.WEST);

        catButton = new JButton();
        catButton.setText("Cat");
        catButton.addActionListener(new ButtonListener("cat"));
        piecePanel.add(catButton, BorderLayout.EAST);

        // confirm button
        confirmButton = new JButton();
        confirmButton.setText("Confirm");
        confirmButton.addActionListener(new ButtonListener("confirm"));

        // text area
        textArea = new JTextArea(5, 5);
        textArea.setPreferredSize(new Dimension(240, 600));
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        scrollPane = new JScrollPane(textArea);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        add(statsPanel);
        add(piecePanel);
        add(confirmButton);
        add(textArea);
        add(scrollPane);
    }

    public void setStats(int player, int kitten, int cat) {
        if (player == Game.GREY) {
            greyLabel.setText(String.format("grey : %d, %d", kitten, cat));
        } else if (player == Game.ORANGE) {
            orangeLabel.setText(String.format("orange : %d, %d", kitten, cat));
        } else {
            System.out.println("error in setStats");
        }
    }

    public void addText(String text) {
        textArea.insert(text, 0);
    }

    class ButtonListener implements ActionListener {
        private Event event;
        public ButtonListener(String type) {
            event = new Event(type);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            monitor.push(event);
        }
    }
}
