

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;

import javax.swing.*;

import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.Point2D;


public class MinefieldGUI extends MouseAdapter {
    Picture flag = new Picture("flag.png");
    boolean game = true;

    private class Node {
        public JLabel getLabel() {
            return label;
        }

        public void setLabel(JLabel label) {
            this.label = label;
        }

        public Point2D getPoint() {
            return point;
        }

        public void setPoint(Point2D point) {
            this.point = point;
        }

        JLabel label;
        Point2D point;


        public Node(JLabel lab, Point2D poin) {
            label = lab;
            point = poin;

        }

    }

    ArrayList<List<Integer>> flagged = new ArrayList<>();

    int count = 0;
    int unopened;
    private ArrayList<Node> arr;
    Minefield minefield;
    //    row/col len
    int rc;
    int flags;
    int size;

    Color tan = new Color(210, 180, 140);

    JFrame frame;
    JPanel panel;
    JPanel panel3 = new JPanel();
    JLabel lab = new JLabel("Mines:");
    JLabel flagsLabel = new JLabel();
    JButton ai = new JButton("AI Move");
    JButton reset = new JButton("Reset");
    Ai solver;

    public MinefieldGUI() {

        start();
//        rc = 6;
//
//        flags = 20;
        minefield = new Minefield(rc, rc, flags);
        solver = new Ai(rc, rc);
//        rc = 10;
//        minefield = new Minefield(20, 20, 40);
//        rc = 20;
        //flags = 40;
        frame = new JFrame();
        frame.setLayout(new BorderLayout());
        frame.addMouseListener(this);
         panel = new JPanel();
        JPanel panel2 = new JPanel();
        panel2.setSize(new Dimension(40, 40));
        panel3.add(lab);
        flagsLabel.setText(String.valueOf(minefield.flags));
        panel3.add(flagsLabel);
        panel3.add(ai);
        panel3.add(reset);
        panel.addMouseListener(this);
        panel2.add(new JLabel("Minesweeper"));
        panel.setSize(rc * 25, rc * 25);
        panel.setLayout(new GridLayout(rc, rc));
        size = rc * rc;
        arr = new ArrayList<Node>(rc * rc);
        setUp(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Our MinefieldGUI");
        frame.add(panel2, BorderLayout.NORTH);
        frame.add(panel3, BorderLayout.CENTER);
        //frame.pack();

//        frame.add(ai, BorderLayout.SOUTH);
        reset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetGameState();
            }
        });
        ai.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                solve();
            }
        });
        frame.add(panel, BorderLayout.SOUTH);
        frame.pack();
        frame.setVisible(true);
        frame.setResizable(false);


    }

    public void resetGameState() {
        frame.dispose();
        panel3.removeAll();
        // reset variable values and update GUI
        // consider also calling start() method again to restart the game


        start();
//        rc = 6;
//
//        flags = 20;
        minefield = new Minefield(rc, rc, flags);
        solver = new Ai(rc, rc);
//        rc = 10;
//        minefield = new Minefield(20, 20, 40);
//        rc = 20;
        //flags = 40;
        frame = new JFrame();
        count = 0;
        frame.setLayout(new BorderLayout());
        frame.addMouseListener(this);
        panel = new JPanel();
        JPanel panel2 = new JPanel();
        panel2.setSize(new Dimension(40, 40));
        flagged = new ArrayList<>();
        unopened = 0;
        panel3.add(lab);
        flagsLabel.setText(String.valueOf(minefield.flags));
        panel3.add(flagsLabel);
        panel3.add(ai);
        panel3.add(reset);
        panel.addMouseListener(this);
        panel2.add(new JLabel("Minesweeper"));
        panel.setSize(rc * 25, rc * 25);
        panel.setLayout(new GridLayout(rc, rc));
        size = rc * rc;
        arr = new ArrayList<Node>(rc * rc);
        setUp(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Our MinefieldGUI");
        frame.add(panel2, BorderLayout.NORTH);
        frame.add(panel3, BorderLayout.CENTER);
        //frame.pack();

//        frame.add(ai, BorderLayout.SOUTH);

        frame.add(panel, BorderLayout.SOUTH);
        frame.pack();
        frame.setVisible(true);
        frame.setResizable(false);


    }
//    public void setup()

    public MinefieldGUI(int rows, int flags, Set<List<Integer>> mines) {

        start();
        rc = rows;
        count = 1;

        this.flags = flags;
        minefield = new Minefield(rc, rc, flags, mines);
        solver = new Ai(rc, rc);
        frame = new JFrame();
        frame.setLayout(new BorderLayout());
        frame.addMouseListener(this);
        JPanel panel = new JPanel();
        JPanel panel2 = new JPanel();
        panel2.setSize(new Dimension(40, 40));
        panel3.add(lab);
        flagsLabel.setText(String.valueOf(minefield.flags));
        panel3.add(flagsLabel);
        panel3.add(ai);
        panel.addMouseListener(this);
        panel2.add(new JLabel("Minesweeper"));
        panel.setSize(rc * 25, rc * 25);
        panel.setLayout(new GridLayout(rc, rc));
        size = rc * rc;
        arr = new ArrayList<Node>(rc * rc);
        setUp(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Our MinefieldGUI");
        frame.add(panel2, BorderLayout.NORTH);
        frame.add(panel3, BorderLayout.CENTER);
        //frame.pack();

//        frame.add(ai, BorderLayout.SOUTH);
        ai.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                solve();
            }
        });
        frame.add(panel, BorderLayout.SOUTH);
        frame.pack();
        frame.setVisible(true);
        frame.setResizable(false);


    }

    public void solve() {
        List<Integer> move = solver.makeSafeMove();
        boolean flag = false;
        if (move != null) {
            System.out.println("Minefield ai using safe move");
        } else {
            if (!solver.getMines().isEmpty()) {
                for (List<Integer> mine : solver.getMines()) {
                    if (!flagged.contains(mine)) {
                        flagged.add(mine);
                        move = new ArrayList<>();
                        move.add(mine.get(0));
                        move.add(mine.get(1));
                        flag = true;
                        break;
                    }
                }
                    if (move == null) {
                        move = solver.makeRandomMove(unopened,flags);
                        System.out.println("Minefield ai using random move");
                    }

            } else {
                move = solver.makeRandomMove(unopened,flags);
                System.out.println("Minefield ai using random move");
            }
        }
            if(!minefield.board[minefield.XYto1D(move.get(0),move.get(1))].getRevealed())
                doClick(move.get(1), move.get(0), flag);
            else{
                solverEval(move.get(1),move.get(0),minefield,solver);
                solve();
            }
//        if(!flag) solverEval(move.get(0),move.get(1),minefield,solver);
    }

    /**
     * Sets up the game board by creating JLabel buttons and adding them to the given JPanel.
     * Each button is assigned a background color based on its position on the board.
     *
     * @param panel the JPanel to add the buttons to
     */
    public void setUp(JPanel panel) {
        for (int y = 0; y < rc; y++) {
            for (int x = 0; x < rc; x++) {
                JLabel button = new JLabel();
                button.setPreferredSize(new Dimension(25, 25));
                button.setOpaque(true);
                Point2D pp = new Point2D(x * 25, y * 25);
                arr.add(new Node(button, pp));
                if (x % 2 == 0) {
                    if (y % 2 == 0) {
                        button.setBackground(Color.GREEN);
                    } else {
                        button.setBackground(new Color(144, 238, 144));
                    }
                } else if (y % 2 == 0) {
                    Color tt = new Color(144, 238, 144);
                    button.setBackground(tt);
                } else {
                    button.setBackground(Color.GREEN);
                }
                panel.add(button);
//                panel.add(ai);
            }
        }
    }


    /**
     * Prompts the user to select a difficulty level and start the game.
     * The user's input is read from the console using a Scanner object.
     * Based on the selected difficulty level, a Minefield object is created
     * with the corresponding dimensions and number of mines.
     * <p>
     * Available difficulty levels:
     * - Easy: 5x5 grid with 5 mines
     * - Medium: 10x10 grid with 20 mines
     * - Hard: 21x21 grid with 100 mines
     * - Nuts: 25x25 grid with 125 mines
     * - Insane: 31x31 grid with 200 mines
     * <p>
     * After selecting the difficulty level, the user is prompted to
     * enable or disable debug mode. If debug mode is enabled, the Minefield
     * object will display the location of the mines.
     * <p>
     * This method does not return any value.
     */
    private void start() {
        Scanner s = new Scanner(System.in);
        while (true) {
            System.out.println("What difficulty would you like. (Easy, Medium,Hard,Nuts,Insane)");
            String difficult = s.nextLine();
            if (difficult.toLowerCase().startsWith("e")) {
                minefield = new Minefield(5, 5, 5);
                solver = new Ai(5, 5);
                rc = 5;
                flags = 5;
                break;
            } else if (difficult.toLowerCase().startsWith("m")) {
                minefield = new Minefield(10, 10, 20);
                solver = new Ai(10, 10);
                rc = 10;
                flags = 20;
                break;
            } else if (difficult.toLowerCase().startsWith("h")) {
                minefield = new Minefield(21, 21, 100);
                solver = new Ai(21, 21);
                rc = 21;
                flags = 100;
                break;
            } else if (difficult.toLowerCase().startsWith("n")) {
                minefield = new Minefield(25, 25, 125);
                solver = new Ai(25, 25);
                rc = 25;
                flags = 125;
                break;
            } else if (difficult.toLowerCase().startsWith("i")) {
                minefield = new Minefield(31, 31, 200);
                solver = new Ai(31, 31);
                rc = 31;
                flags = 200;
                break;
            } else System.out.println("Not valid difficulty");

        }
        System.out.println("In debug mode (yes or no)?");
        String debug = s.nextLine();
        if (debug.toLowerCase().startsWith("y")) minefield.debug();


    }

    /**
     * Converts the x and y coordinates of a two-dimensional grid to a one-dimensional index.
     *
     * @param x the x-coordinate of the element in the grid
     * @param y the y-coordinate of the element in the grid
     * @return the one-dimensional index representing the element in the grid
     */
    private int XYto1D(int x, int y) {
        return (y * rc) + x;
    }

    /**
     * Updates the game board by evaluating the status of each cell and updating the corresponding JLabel button.
     */
    public int eval() {
int ctr = 0;
        for (int y = 0; y < rc; y++) {
            for (int x = 0; x < rc; x++) {
                Cell cell = minefield.getCell(XYto1D(y, x));
                if (cell.revealed) {
                    List<Integer> lis = new ArrayList<>();
                    lis.add(y);
                    lis.add(x);
                    String stat = cell.getStatus().toLowerCase();


                    if (stat.equals("m")) {
                        arr.get(XYto1D(y, x)).getLabel().setText("  M");

                    } else if (stat.toLowerCase().startsWith("f")) {
                        arr.get(XYto1D(y, x)).getLabel().setText("  F");
                        arr.get(XYto1D(y, x)).getLabel().setForeground(Color.red);

                    } else if (stat.equals("0")) {
                        List<Integer> lis1 = new ArrayList<>();
                        lis1.add(y);
                        lis1.add(x);
//                            solver.movesMade.add(lis1);
                    } else {
                        arr.get(XYto1D(y, x)).getLabel().setText("   " + stat);

                        ArrayList<Integer> lis1 = new ArrayList<>();
                        lis1.add(y);
                        lis1.add(x);
                        switch (Integer.parseInt(stat)) {
                            case 1, 4, 7:
                                arr.get(XYto1D(y, x)).getLabel().setForeground(Color.magenta);
                                break;
                            case 2, 5, 8:
                                arr.get(XYto1D(y, x)).getLabel().setForeground(Color.red);
                                break;
                            case 3, 6:
                                arr.get(XYto1D(y, x)).getLabel().setForeground(Color.blue);
                                break;
                        }

                    }
//                            }

                    arr.get(XYto1D(y, x)).getLabel().setBackground(tan);


                } else {

                    arr.get(XYto1D(y, x)).getLabel().setText("  ");
                    if ((x + y) % 2 == 0) arr.get(XYto1D(y, x)).getLabel().setBackground(Color.green);
                    ctr++;
                }

                arr.get(XYto1D(y, x)).getLabel().repaint();
                frame.repaint();

            }


        }
        return ctr;
    }


    /**
     * Performs a click action on the game board.
     *
     * @param x          The x-coordinate of the cell to click.
     * @param y          The y-coordinate of the cell to click.
     * @param rightClick True if it is a right-click action, false otherwise.
     */
    public void doClick(int x, int y, boolean rightClick) {
        System.out.printf("Move made was (x,y)->(%d,%d) \n", x, y);

        if (rightClick) {
            // process right click
            if (count == 0) {
                setupStart(x, y);
            } else {
                flagsLabel.setText(String.valueOf(minefield.flags));
                minefield.guess(y, x, true);
                System.out.println(minefield.flags);
                if (minefield.flags == 0) {
                    panel3.removeAll();
                        panel3.add(new JLabel("You Won"));

                    panel3.add(ai);
                    panel3.add(reset);
                    frame.validate();
                    frame.repaint();
                }
                flagsLabel.setText(String.valueOf(minefield.flags));



                this.minefield.printMinefield();
                System.out.println();
            }
        } else {

            if (count == 0) {
                // process left click
                setupStart(x, y);
            } else {
                System.out.printf("(%d,%d)\n", x, y);
                if (!minefield.guess(y, x, false)) {
                    System.out.println(minefield.flags);
                    if (minefield.flags == 0) {
                        System.out.println("You win");
                        panel3.removeAll();
                        panel3.add(new JLabel("You Won"));

                    panel3.add(ai);
                    panel3.add(reset);

                        frame.validate();
                        frame.repaint();
                    }

                    this.minefield.printMinefield();
                } else {
                    System.out.println("Game over");
                    game = false;
                    panel3.removeAll();
                    panel3.add(new JLabel("Game Over"));
                    frame.remove(panel);
                    panel3.add(ai);
                    panel3.add(reset);
                    frame.validate();
                    frame.repaint();
                }
            }
        }
        solverEval(x,y,minefield,solver);
        unopened =eval();
//        solver.printKnowledge();
    }

    static void solverEval(int x, int y, Minefield minefield, Ai solver) {
        if (minefield.board[minefield.XYto1D(y, x)].getRevealed()) {
            var val = -1;
            try {
                val = Integer.parseInt(minefield.getCell(minefield.XYto1D(y, x)).symbol);
            } catch (Exception e) {
            } finally {
                if (val != -1) solver.addKnowledge(new Integer[]{y, x}, val);

            }
        }
    }



    private void setupStart(int x, int y) {
        minefield.createMines(x, y, flags);
//        minefield.evaluateField();
        minefield.revealStart(x, y);
        count++;
        System.out.println(x + ", " + y);
        minefield.printMinefield();
//        while(true){}
        for (int i = 0; i < rc; i++) {
            for (int j = 0; j < rc; j++) {
//                solverEval(j, i, minefield, solver);
                if (minefield.getCell(minefield.XYto1D(i, j)).getRevealed()) {
                    var val = -1;
                    try {
                        val = Integer.parseInt(minefield.getCell(minefield.XYto1D(i, j)).symbol);
                    } catch (Exception e) {
                    } finally {
//                        for(List<Integer> row : closeCells(new Integer[]{y, x})){
//                            if(minefield.board[minefield.XYto1D(row.get(0), row.get(1))].getRevealed()){
//                                solver.addKnowledge(new Integer[]{row.get(0), row.get(1)}, closeClosedCells(new Integer[]{row.get(0), row.get(1)}), val);
//                            }
//                        }
                        if (val != -1) {
                            solver.addKnowledge(new Integer[]{i, j}, val);
                        }
                    }
                }
            }
        }
        unopened = eval();


//        int ctr = minefield.nearbyMines(new int[]{x, y});
//        solver.addKnowledge(new Integer[]{x, y}, ctr);
    }

    @Override
    public void mouseClicked(MouseEvent me) {
        int x = me.getX() / 25;
        int y = me.getY() / 25;
        System.out.println(me.getButton());
        System.out.println(me.isControlDown());
        // process right click. Since on mac you must also have control down since one mouse
        if (me.getButton() == MouseEvent.BUTTON1 && me.isControlDown()) doClick(x, y, true);
            // process left click
        else if (me.getButton() == MouseEvent.BUTTON1) doClick(x, y, false);

    }


    public static void main(String[] args) {

        MinefieldGUI mine = new MinefieldGUI();
        //  Scanner scan = new Scanner(System.in);

    }
}
