import javax.swing.*;
/*

player:
1: grey, -1: orange

board:
0: empty, 1: grey kitten, 2: grey cat, -1: orange kitten, -2: orange cat

move:
(x, y, 1/2/-1/-2), (x, y, 1/2/-1/-2, x2, y2), (x, y, 1/2/-1/-2,

 */

/*

todo: Ai, optimise layout, new game
 */

public class Main extends JFrame {
    // set player1/2 to be controlled by ai
//    static boolean AI1 = false;
    static boolean AI2 = true;
    static boolean AI1 = false;
//    static boolean AI2 = false;
    public static void main(String[] args) {
        EventMonitor monitor = new EventMonitor();
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new UserInterface(monitor);
            }
        });
        MainGame mainGame = null;
        if (args.length > 0 && args[0].equals("-g")) {
            mainGame = new MainGame(false, true, monitor);
        } else if (args.length > 0 && args[0].equals("-o")) {
            mainGame = new MainGame(true, false, monitor);
        } else {
            mainGame = new MainGame(AI1, AI2, monitor);

        }
        mainGame.play();
    }
}
