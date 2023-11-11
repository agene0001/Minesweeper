

import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.StdDraw;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Scanner;


public class MinefieldGUI extends MouseAdapter {
    Picture flag = new Picture("flag.png");
    boolean game = true;

    private class Node{
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

    private int count = 0;
    private ArrayList<Node> arr;
    private Minefield minefield;
    int rc;
    int flags;
    int size;

    Color tan = new Color(210, 180, 140);

    JFrame frame;
    JPanel panel3 = new JPanel();
    JLabel lab = new JLabel("Mines:");
    JLabel lab1 = new JLabel();

    public MinefieldGUI() {

        start();
//        minefield = new Minefield(20, 20, 40);
//        rc = 20;
        //flags = 40;
        frame = new JFrame();
        frame.setLayout(new BorderLayout());
        frame.addMouseListener(this);
        JPanel panel = new JPanel();
        JPanel panel2 = new JPanel();
        panel2.setSize(new Dimension(40,40));
        panel3.add(lab);
        lab1.setText(String.valueOf(minefield.flags));
        panel3.add(lab1);
        panel.addMouseListener(this);
        panel2.add(new JLabel("MineSweeper"));
        panel.setSize(rc*25,rc*25);
        panel.setLayout(new GridLayout(rc, rc));
        size = rc * rc;
        arr = new ArrayList<Node>(rc * rc);
        setUp(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Our MinefieldGUI");
        frame.add(panel2, BorderLayout.NORTH);
        frame.add(panel3,BorderLayout.CENTER);
        //frame.pack();
        frame.add(panel,BorderLayout.SOUTH);
        frame.pack();
        frame.setVisible(true);
        frame.setResizable(false);


    }

    public void setUp(JPanel panel){
        for (int y = 0; y < rc; y++) {
            for (int x = 0; x < rc; x++) {
                JLabel button = new JLabel();
                button.setPreferredSize(new Dimension(25,25));
                button.setOpaque(true);
                Point2D pp = new Point2D(x*25,y*25);
                arr.add(new Node(button,pp));
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
            }
        }
    }


    private void start() {
        Scanner s = new Scanner(System.in);
        while (true) {
            System.out.println("What difficulty would you like. (Easy,Medium,Hard,Nuts,Insane)");
            String difficult = s.nextLine();
            if (difficult.toLowerCase().startsWith("e")) {
                minefield = new Minefield(5, 5, 5);
                rc = 5;
                flags = 5;
                break;
            } else if (difficult.toLowerCase().startsWith("m")) {
                minefield = new Minefield(10, 10, 20);
                rc = 10;
                flags = 20;
                break;
            } else if (difficult.toLowerCase().startsWith("h")) {
                minefield = new Minefield(21, 21, 100);
                rc = 21;
                flags = 100;
                break;
            } else if (difficult.toLowerCase().startsWith("n")) {
                minefield = new Minefield(25, 25, 125);
                rc = 25;
                flags = 125;
                break;
            } else if (difficult.toLowerCase().startsWith("i")) {
                minefield = new Minefield(31, 31, 200);
                rc = 31;
                flags = 200;
                break;
            } else {
                System.out.println("Not valid difficulty");
            }
        }
        System.out.println("In debug mode (yes or no)?");
        String debug = s.nextLine();
        if (debug.toLowerCase().startsWith("y")) minefield.debug();


    }

    private int XYto1D(int x, int y) {
        return (x * rc) + y;
    }

    public void eval() {
        for (int y = 0; y < rc; y++) {
            for (int x = 0; x < rc; x++) {
                Cell cell = minefield.getCell(XYto1D(y, x));
                if (cell.revealed) {
                    String stat = cell.getStatus().toLowerCase();

                    if (stat.equals("m")) {
                        arr.get(XYto1D(y, x)).getLabel().setText("  M");
                        arr.get(XYto1D(y, x)).getLabel().setBackground(tan);
                        frame.repaint();

                    }
                    else if(stat.toLowerCase().startsWith("f")){
                        arr.get(XYto1D(y, x)).getLabel().setText("  F");
                        arr.get(XYto1D(y, x)).getLabel().setBackground(tan);
                        arr.get(XYto1D(y, x)).getLabel().setForeground(Color.red);

                        frame.repaint();
                    }
                    else if (stat.equals("0")) {
                        arr.get(XYto1D(y, x)).getLabel().setBackground(tan);
                        frame.repaint();
                    }
                    else  {
                        arr.get(XYto1D(y, x)).getLabel().setText("   "+stat);
                        switch(Integer.parseInt(stat)){
                            case 1,4,7:
                                arr.get(XYto1D(y, x)).getLabel().setForeground(Color.magenta);
                                break;
                            case 2,5,8:
                                arr.get(XYto1D(y, x)).getLabel().setForeground(Color.red);
                                break;
                            case 3,6:
                                arr.get(XYto1D(y, x)).getLabel().setForeground(Color.blue);
                                break;
                        }
                        arr.get(XYto1D(y, x)).getLabel().setBackground(tan);
                        frame.repaint();

                    }
                }
            }
        }
    }

    public boolean getGame() {
        return game;
    }

    @Override
    public void mouseClicked(MouseEvent me){
        int x = me.getX()/25;
        int y = me.getY()/25;
        if(me.getButton()==MouseEvent.BUTTON1){
            //--left click
            if(count==0){
                minefield.createMines(y,x,flags);
                minefield.evaluateField();
                minefield.revealStart(y,x);
                count++;
                eval();
                this.minefield.printMinefield();
            }
            else{
                System.out.printf("(%d,%d)",x,y);
                if(!minefield.guess(y,x,false)) {
                    eval();
                    this.minefield.printMinefield();
                }
                else{
                    System.out.println("Game over");
                    game = false;
                }
            }
        }
        if(me.getButton()==MouseEvent.BUTTON3){
            System.out.printf("(%d,%d)",x,y);
            //--right click
            if(count==0){
                minefield.createMines(y,x,flags);
                minefield.evaluateField();
                minefield.revealStart(y,x);
                count++;
                eval();
                this.minefield.printMinefield();
            }
            else {
                lab1.setText(String.valueOf(minefield.flags));
                minefield.guess(y,x,true);
                lab1.setText(String.valueOf(minefield.flags));
                eval();
                this.minefield.printMinefield();

            }
        }

    }




    public static void main(String[] args){
        MinefieldGUI mine =new MinefieldGUI();
      //  Scanner scan = new Scanner(System.in);

    }
}
