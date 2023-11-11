import edu.princeton.cs.algs4.Queue;
import java.util.Stack;
import java.util.Random;
//agene001
public class Minefield {
    /**
     * Global Section
     */
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE_BRIGHT = "\u001b[34;1m";
    public static final String ANSI_BLUE = "\u001b[34m";
    public static final String ANSI_RED_BRIGHT = "\u001b[31;1m";
    public static final String ANSI_RED = "\u001b[31m";
    public static final String ANSI_GREEN = "\u001b[32m";
    public static final String ANSI_GREY_BG = "\u001b[0m";
    /**
     * Constructor
     *
     * @param rows       Number of rows.
     * @param columns    Number of columns.
     * @param flags      Number of flags, should be equal to mines
     */
    int rows;
    int cols;
    int flags;
    Cell[] board;

    public Minefield(int rows, int columns, int flags) {
        //initzializer
        this.rows = rows;
        cols = columns;
        this.flags = flags;

        board = new Cell[rows*cols+1];
        int h2=(cols*2);
        int max = cols*4;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                board[XYto1D(j,i)] = new Cell(false,"0");
            }
        }

    }

    /**
     * evaluateField
     *
     * @function When a mine is found in the field, calculate the surrounding 9x9 tiles values. If a mine is found, increase the count for the square.
     */
    void evaluateField() {
        //used to keep track of mines surronding
        int count;
        //goes through all and checks that adjacent pieces are in bounds and mine
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                    if (board[XYto1D(j, i)].getStatus().equalsIgnoreCase("M")) {
                        if (i + 1 < rows) {
                            if (j + 1 < cols) {
                                Cell cell = board[XYto1D(j+1,i+1)];
                                try{
                                cell.setStatus(String.valueOf(Integer.parseInt(cell.getStatus())+1));
                                }
                                catch (Exception ignored){

                                }
                            }
                            if (j - 1 >= 0) {
                                Cell cell = board[XYto1D(j-1,i+1)];
                                try{
                                cell.setStatus(String.valueOf(Integer.parseInt(cell.getStatus())+1));
                                }
                                catch (Exception ignored){

                                }
                            }
                            Cell cell = board[XYto1D(j,i+1)];
                            try{
                            cell.setStatus(String.valueOf(Integer.parseInt(cell.getStatus())+1));
                            }
                            catch (Exception ignored){

                            }
                        }
                        if(i-1>=0){
                            if (j + 1 < cols) {
                                Cell cell = board[XYto1D(j+1,i-1)];
                                try{
                                cell.setStatus(String.valueOf(Integer.parseInt(cell.getStatus())+1));
                                }
                                catch (Exception ignored){

                                }
                            }
                            if (j - 1 >= 0) {
                                Cell cell = board[XYto1D(j-1,i-1)];
                                try{
                                cell.setStatus(String.valueOf(Integer.parseInt(cell.getStatus())+1));
                                }
                                catch (Exception ignored){

                                }
                            }
                            Cell cell = board[XYto1D(j,i-1)];
                            try{
                            cell.setStatus(String.valueOf(Integer.parseInt(cell.getStatus())+1));
                            }
                            catch (Exception ignored){

                            }
                        }
                        if(j+1<cols){
                            Cell cell = board[XYto1D(j+1,i)];
                            try{
                            cell.setStatus(String.valueOf(Integer.parseInt(cell.getStatus())+1));
                            }
                            catch (Exception ignored){

                            }
                        }
                        if(j-1>=0){
                            Cell cell = board[XYto1D(j-1,i)];
                            try {
                                cell.setStatus(String.valueOf(Integer.parseInt(cell.getStatus()) + 1));
                            }
                            catch (Exception ignored){

                            }
                        }
                    }
            }
        }
    }

    private int XYto1D(int x,int y){
        return (x*cols)+y;
    }
    /**
     * createMines
     *
     * @param x     Start x, avoid placing on this square.
     * @param y     Start y, avoid placing on this square.
     * @param mines Number of mines to place.
     */
    public void createMines(int x, int y, int mines) {

        int a;
        int b;
        Random rand = new Random();
        while (mines > 0) {
            int upper = rows;
            a = rand.nextInt(upper);
            b = rand.nextInt(upper);
            //checks if mine is placed on starting coordinate
            if ((a == x && b == y)||(a+1 == x && b == y)||(a-1 == x && b == y)||(a+1 == x && b+1 == y)||(a+1 == x && b-1 == y)||(a-1 == x && b+1 == y)||(a-1 == x && b-1 == y)||(a == x && b+1 == y)||(a == x && b-1 == y)) {
            } else {
                    board[XYto1D(a,b)].setStatus("M");
                    mines--;

            }
        }
    }


    public Cell getCell(int x){
        return board[x];
    }

    /**
     * guess
     *
     * @param x    The x value the user entered.
     * @param y    The y value the user entered.
     * @param flag A boolean value that allows the user to place a flag on the corresponding square.
     * @return boolean Return false if guess did not hit mine or if flag was placed, true if mine found.
     */
    public boolean guess(int x, int y, boolean flag) {
        if (x <= cols && y <= rows && x >= 0 && y >= 0) {
            if (flag) {
                if (flags > 0) {
                    if (board[XYto1D(x,y)].getStatus().startsWith("F")) {
                        String tt = board[XYto1D(x,y)].getStatus().replace("F","");
                        board[XYto1D(x,y)].setStatus(tt);
                        board[XYto1D(x,y)].setRevealed(false);
                        flags++;
                        return false;
                    }
                    else {
                        board[XYto1D(x,y)].setStatus("F"+board[XYto1D(x,y)].getStatus());
                        board[XYto1D(x,y)].setRevealed(true);
                        flags--;
                        return false;
                    }
                }
            } else if (board[XYto1D(x,y)].getStatus().equals("0")) {
                revealZeroes(x, y);
                return false;
            } else if (board[XYto1D(x,y)].getStatus().toLowerCase().equals("m"))
                return true;
            else {
                board[XYto1D(x,y)].setRevealed(true);
            }

        }
        return false;
    }

    /**
     * gameOver
     *
     * @return boolean Return false if game is not over and squares have yet to be revealed, otheriwse return true.
     */
    public boolean gameOver() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (board[XYto1D(j,i)].getStatus() == "M") {
                    if (!board[XYto1D(j,i)].getRevealed()) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * revealField
     * <p>
     * This method should follow the psuedocode given.
     * Why might a stack be useful here rather than a queue?
     *
     * @param x The x value the user entered.
     * @param y The y value the user entered.
     */
    public void revealZeroes(int x, int y) {
        Stack<int[]> s = new Stack<>();
        int[] t = new int[2];
        t[0] = x;
        t[1] = y;
        s.push(t);
        while (!s.isEmpty()) {
            int[] temp = s.pop();
            if (board[XYto1D(temp[0],temp[1])].getStatus().equals("0") && !board[XYto1D(temp[0],temp[1])].getRevealed()) {
                board[XYto1D(temp[0],temp[1])].setRevealed(true);
                if (temp[1] + 1 < rows) {
                    if(!board[XYto1D(temp[0],temp[1]+1)].getStatus().equals("M")&&!board[XYto1D(temp[0],temp[1]+1)].getStatus().equals("0")) {
                        board[XYto1D(temp[0],temp[1]+1)].setRevealed(true);
                    }
                    if(temp[0]+1<cols){
                        if(!board[XYto1D(temp[0]+1,temp[1]+1)].getStatus().equals("M")&&!board[XYto1D(temp[0]+1,temp[1]+1)].getStatus().equals("0")) {
                            board[XYto1D(temp[0]+1,temp[1]+1)].setRevealed(true);
                        }
                    }
                    if(temp[0]-1>=0){
                        if(!board[XYto1D(temp[0]-1,temp[1]+1)].getStatus().equals("M")&&!board[XYto1D(temp[0]-1,temp[1]+1)].getStatus().equals("0")) {
                            board[XYto1D(temp[0]-1,temp[1]+1)].setRevealed(true);

                        }
                    }
                    int[] temp3 = new int[2];
                    temp3[1] = temp[1]+1;
                    temp3[0] = temp[0];
                    s.push(temp3);
                }
                if (temp[1] - 1 >= 0) {
                    if(!board[XYto1D(temp[0],temp[1]-1)].getStatus().equals("M")&&!board[XYto1D(temp[0],temp[1]-1)].getStatus().equals("0")) {
                        board[XYto1D(temp[0],temp[1]-1)].setRevealed(true);
                    }
                    if(temp[0]+1<cols){
                        if(!board[XYto1D(temp[0]+1,temp[1]-1)].getStatus().equals("M")&&!board[XYto1D(temp[0]+1,temp[1]-1)].getStatus().equals("0")) {
                            board[XYto1D(temp[0]+1,temp[1]-1)].setRevealed(true);
                        }
                    }
                    if(temp[0]-1>=0){
                        if(!board[XYto1D(temp[0]-1,temp[1]-1)].getStatus().equals("M")&&!board[XYto1D(temp[0]-1,temp[1]-1)].getStatus().equals("0")) {
                            board[XYto1D(temp[0]-1,temp[1]-1)].setRevealed(true);

                        }
                    }
                    int[] temp3 = new int[2];
                    temp3[1] = temp[1]-1;
                    temp3[0] = temp[0];
                    s.push(temp3);
                }
                if (temp[0]+ 1 < cols) {
                    if(!board[XYto1D(temp[0]+1,temp[1])].getStatus().equals("M")&&!board[XYto1D(temp[0]+1,temp[1])].getStatus().equals("0")) {
                        board[XYto1D(temp[0]+1,temp[1])].setRevealed(true);
                    }
                        int[] temp3 = new int[2];
                        temp3[1] = temp[1];
                        temp3[0] = temp[0] + 1;
                        s.push(temp3);

                }
                if (temp[0] - 1 >= 0) {
                    if(!board[XYto1D(temp[0]-1,temp[1])].getStatus().equals("M")&&!board[XYto1D(temp[0]-1,temp[1])].getStatus().equals("0")) {
                        board[XYto1D(temp[0] - 1, temp[1])].setRevealed(true);
                    }
                        int[] temp4 = new int[2];
                        temp4[1] = temp[1];
                        temp4[0] = temp[0] - 1;
                        s.push(temp4);

                }
            }

        }
    }

    /**
     * revealMines
     *
     * This method should follow the psuedocode given.
     * Why might a queue be useful for this function?
     *
     * @param x     The x value the user entered.
     * @param y     The y value the user entered.
     */
    public void revealMines(int x, int y) {
        //initializes queue with x and y in it
        Queue<int[]> queue = new Queue<>();
        int[] t = new int[2];
        t[0] = y;
        t[1] = x;
        queue.enqueue(t);
        //System.out.println(num);
        //continues until length is 0
        while (!queue.isEmpty()) {
            int[] check = queue.dequeue();
            if (!board[XYto1D(check[1],check[0])].getStatus().equalsIgnoreCase("M")) {
                // if zero reveal zeros
                if (board[XYto1D(check[1],check[0])].getStatus().equals("0")&&!board[XYto1D(check[1],check[0])].getRevealed()) {
                    this.revealZeroes(check[1],check[0]);
                    int[] add = new int[2];
                    add[0] = check[0];
                    add[1] = check[1];
                    queue.enqueue(add);
                    board[XYto1D(check[1],check[0])].setRevealed(true);
                }
                else{
                    //add surronding
                    if (check[0] + 1 < rows) {
                        if (check[1] + 1 < cols) {
                            int[] add = new int[2];
                            add[0] = check[0] + 1;
                            add[1] = check[1] + 1;
                            queue.enqueue(add);
                        }
                        if (check[1] - 1 >= 0) {
                            int[] add12 = new int[2];
                            add12[0] = check[0] + 1;
                            add12[1] = check[1] - 1;
                            queue.enqueue(add12);
                        }
                        int[] add13 = new int[2];
                        add13[0] = check[0] + 1;
                        add13[1] = check[1];
                        queue.enqueue(add13);
                    }
                    if (check[0] - 1 >= 0) {
                        if (check[1] + 1 < cols) {
                            int[] add = new int[2];
                            add[0] = check[0] - 1;
                            add[1] = check[1] + 1;
                            queue.enqueue(add);
                        }
                        if (check[1] - 1 >= 0) {
                            int[] add = new int[2];
                            add[0] = check[0] - 1;
                            add[1] = check[1] - 1;
                            queue.enqueue(add);
                        }
                        int[] add5 = new int[2];
                        add5[0] = check[0] - 1;
                        add5[1] = check[1];
                        queue.enqueue(add5);
                    }
                    if (check[1] + 1 < cols) {
                        int[] add6 = new int[2];
                        add6[0] = check[0];
                        add6[1] = check[1] + 1;
                        queue.enqueue(add6);
                    }
                    if (check[1] - 1 >= 0) {
                        int[] add10 = new int[2];
                        add10[0] = check[0];
                        add10[1] = check[1] - 1;
                        queue.enqueue(add10);
                    }
                }

                board[XYto1D(check[1],check[0])].setRevealed(true);
            }

            //break if mine
            else
                break;

        }
    }
    public void debug(){
        //sets all boards to revealed
        for(int i=0;i<rows;i++){
            for(int j=0;j<cols;j++){
                board[XYto1D(j,i)].setStatus("0");
                board[XYto1D(j,i)].setRevealed(true);
            }
        }
    }

    /**
     * revealStart
     *
     * @param x       The x value the user entered.
     * @param y       The y value the user entered.
     */
    public void revealStart(int x, int y) {
        this.revealMines(x,y);
    }

    /**
     *
     * printMinefield
     *
     * @fuctnion This method should print the entire minefield, regardless if the user has guessed a square.
     * *This method should print out when debug mode has been selected.
     */
    public void printMinefield() {
        for(int i=0;i<rows;i++) {
            if(i<10) {
                if (i == rows-1) System.out.println("  " + i);
                else System.out.print("   " + i + "     ");
            }
            else{
                if (i == rows-1) System.out.println("  " + i);
                else System.out.print("   " + i + "    ");

            }
        }
        for(int i=0;i<rows;i++){
            for(int j=0;j<cols;j++){
                if(j==0) {
                    if (i < 10) System.out.print(" " + i + " ");
                    else System.out.print(i + " ");
                }
                if(board[XYto1D(i,j)].getRevealed()) {
                    if(board[XYto1D(i,j)].getStatus().equals("1")) System.out.print(ANSI_RED+" " + board[XYto1D(i,j)].getStatus() + "       "+ANSI_GREY_BG);
                    else if(board[XYto1D(i,j)].getStatus().equals("2")) System.out.print(ANSI_YELLOW+" " + board[XYto1D(i,j)].getStatus() + "       "+ANSI_GREY_BG);
                    else if(board[XYto1D(i,j)].getStatus().equals("3")) System.out.print(ANSI_BLUE_BRIGHT+" " + board[XYto1D(i,j)].getStatus() + "       "+ANSI_GREY_BG);
                    else if(board[XYto1D(i,j)].getStatus().equals("4")) System.out.print(ANSI_RED_BRIGHT+" " + board[XYto1D(i,j)].getStatus() + "       "+ANSI_GREY_BG);
                    else if(board[XYto1D(i,j)].getStatus().equals("5")) System.out.print(ANSI_BLUE+" " + board[XYto1D(i,j)].getStatus() + "       "+ANSI_GREY_BG);
                    else if(board[XYto1D(i,j)].getStatus().equals("6")) System.out.print(ANSI_GREEN+" " + board[XYto1D(i,j)].getStatus() + "       "+ANSI_GREY_BG);
                    else System.out.print(" " + board[XYto1D(i,j)].getStatus() + "       "+ANSI_GREY_BG);
                }
                else System.out.print(" -       ");
            }
            System.out.println();
            System.out.println();
        }
    }

    /**
     * toString
     *
     * @return String The string that is returned only has the squares that has been revealed to the user or that the user has guessed.
     */
    public String toString() {
        String rev = "";
        for(int i=0;i<rows;i++){
            for(int j=0;j<cols;j++){
                if(board[XYto1D(i,j)].getRevealed()){
                    rev += board[XYto1D(i,j)].getStatus();
                }
                else
                    rev+= "- ";
            }
            rev += "\n";
        }
        return rev;
    }
}
