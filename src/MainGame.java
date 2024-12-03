import java.util.LinkedList;
import java.util.Objects;

// The game shown in the window
public class MainGame extends Game{
    private boolean[] isAI;
    private Monitor<Move>[] aiMonitors;
    private Thread[] ais;
    private UserInterface ui;
    private EventMonitor em;
    // game state displayed in ui
    private GameInfo gameInfo;

    public MainGame(boolean isGreyAI, boolean isOrangeAI, EventMonitor em) {
        super();
        isAI = new boolean[2];
        isAI[0] = isGreyAI;
        isAI[1] = isOrangeAI;
        this.em = em;

        aiMonitors = (Monitor<Move>[]) new Monitor<?>[2];
        ais = new Thread[2];
        if (isGreyAI) {
            aiMonitors[0] = new Monitor<Move>();
            //ais[0] = new Thread(new AI(Game.GREY, aiMonitors[0]));
//            ais[0] = new Thread(new AIPruning2(this, Game.GREY, aiMonitors[0], 4));
            //ais[0] = new Thread(new AIPruning2(this, Game.GREY, aiMonitors[0], 2));
            ais[0] = new Thread(new AIMCTS(this, Game.GREY, aiMonitors[0], 20, 5));

            ais[0].start();
        }
        if (isOrangeAI) {
            aiMonitors[1] = new Monitor<Move>();
            //ais[1] = new Thread(new AI(Game.ORANGE, aiMonitors[1]));
            //ais[1] = new Thread(new AIRandom(this, Game.ORANGE, aiMonitors[1]));
            //ais[1] = new Thread(new AIMinimax(this, Game.ORANGE, aiMonitors[1]));
//            ais[1] = new Thread(new AIPruning2(this, Game.ORANGE, aiMonitors[1], 3));
            ais[1] = new Thread(new AIMCTS(this, Game.ORANGE, aiMonitors[1], 20, 5));

            ais[1].start();
        }
        Event event = em.pop();
        if (Objects.equals(event.type, "ui")) {
            ui = event.ui;
        } else {
            System.out.println("error in MainGame");
        }

        gameInfo = new GameInfo(board, pieces);
    }

    private int toIndex(int player) {
        return (1 - player) / 2;
    }

    @Override
    public boolean makeMove(Move move) {
        boolean finished = super.makeMove(move);
        gameInfo = new GameInfo(board, pieces);
        gameInfo.clear();
        return finished;
    }

    public void play(){
        ui.controlPanel.addText("Game start\n");
        while (true) {
            //System.out.println("new loop in play()");

            //System.out.println("legal moves:");
//            for (Move move: getLegalMoves()) {
//                System.out.print(move);
//            }
//            System.out.println();
            int index = toIndex(nextPlayer);
            Move move;


            // get move
            if (isAI[index]) {
                System.out.println("ai's turn");

                aiMonitors[index].setSlot(new Move(-1, -1, 0), 0);
                move = aiMonitors[index].getSlot(1);
                ui.controlPanel.addText("AI placed a piece at: " + move + "\n");
                if (!isLegalMove(move)) {
                    System.out.println("error in getting ai move");
                }

            } else {
                ui.controlPanel.addText("Waiting for player's move\n");
                move = getUIMove();
            }

            // exit if finished
            if (makeMove(move)) {
                ui.controlPanel.addText("Game over. Winner is " + winner + "\n");
                if (aiMonitors[0] != null) {
                    aiMonitors[0].setEnd();
                }
                if (aiMonitors[1] != null) {
                    aiMonitors[1].setEnd();
                }
                break;
            }

//            index = toIndex(nextPlayer);
//            // send move
//            if (isAI[index]) {
//                aiMonitors[index].setSlot(move, 1-index);
//            }
            updateBoardPanel(gameInfo);
            //System.out.println(this);
//            GameInfo.print();

        }
    }

    // receive events from EventMonitor and convert to a move
    private Move getUIMove() {
        //System.out.println("getting ui move");
        // waiting for: 0: a move, 1: choosing kitten/cat, 2: graduated move
        int state = 0;
        Move move = new Move();
        LinkedList<Move> list = null;

        GameInfo gameInfo1 = gameInfo;

        int index = toIndex(nextPlayer);
        if (pieces[index][0] > 0) {
            if (pieces[index][1] > 0) {
                state = 1;
            } else {
                move.type = nextPlayer;
            }
        } else {
            move.type = nextPlayer * 2;
        }

        boolean loop = true;
        while (loop) {

            Event event = em.pop();
            System.out.println("receiving event: " + event + " in state " + state);
            switch (state) {
                case 0 -> {
                    // check if a square button is clicked
                    if (Objects.equals(event.type, "pos")) {
                        list = new LinkedList<>();
                        // get possible moves
                        for (Move move1 : getLegalMoves()) {
                            if (move1.x == event.pos.x && move1.y == event.pos.y && move1.type == move.type) {
                                list.add(move1);
                            }
                        }
                        if (!list.isEmpty()) {

                            gameInfo1 = boopedBoard(event.pos.x, event.pos.y, move.type);
                            // select the move if only 1 match
                            if (list.size() == 1) {
                                move = list.pop();
                                loop = false;
                                ui.controlPanel.addText("You placed a piece at: " + move + "\n");

                            } else {
                                System.out.println("list:");
                                for (Move move1: list) {
                                    System.out.println(move1);
                                }
                                move.x = event.pos.x;
                                move.y = event.pos.y;
                                state = 2;
                                ui.controlPanel.addText("Select pieces to graduate\n");
                            }
                        }

                    }
                }
                case 1 -> {
                    if (Objects.equals(event.type, "kitten")) {
                        move.type = nextPlayer;
                        state = 0;
                    } else if (Objects.equals(event.type, "cat")) {
                        move.type = nextPlayer * 2;
                        state = 0;
                    } else {
                        ui.controlPanel.addText("First choose to place a kitten or cat\n");
                    }
                }
                case 2 -> {
                    if (Objects.equals(event.type, "pos")) {
                        if (move.graduated.contains(event.pos)) {
                            move.graduated.remove(event.pos);
                        } else {
                            move.graduated.add(event.pos);
                        }
                        gameInfo1.select(event.pos.x, event.pos.y);
                    } else if (Objects.equals(event.type, "confirm")) {
                        System.out.println("move: " + move);
                        if (list.contains(move)) {
                            loop = false;
                            ui.controlPanel.addText("You placed a piece at: " + move + "\n");
                        } else {
                            move.graduated.clear();
                            ui.controlPanel.addText("Invalid pieces to graduate\n");
                        }
                        gameInfo1.clear();
                    }
                }
            }

            updateBoardPanel(gameInfo1);
        }

        return move;
    }

    private void updateBoardPanel(GameInfo gameInfo) {
        int[][] b = gameInfo.getBoard();
        int[][] p = gameInfo.getPieces();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                ui.boardPanel.updateSquare(i, j, b[i][j], gameInfo.isSelected(i, j));
            }
        }

        ui.controlPanel.setStats(GREY, p[0][0], p[0][1]);
        ui.controlPanel.setStats(ORANGE, p[1][0], p[1][1]);
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
