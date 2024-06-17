import java.util.Random;

public class MinesweeperAI extends MinefieldGUI {
    public MinesweeperAI() {
        super();
        solve();
    }

    /**
     * solve
     *
     * This method solves the Minesweeper game using an AI algorithm. It uses a combination of flagging and revealing
     * cells based on certain conditions. The algorithm starts by generating a random position on the minefield and
     * placing mines on the field. It then evaluates the field to calculate the number of mines surrounding each
     * non-mine cell.
     *
     * The method then iterates through each cell of the minefield and checks if the cell has been revealed. If it has,
     * it checks if the number of revealed surrounding cells plus the number of flagged mines surrounding the cell is
     * equal to the number represented by the cell's symbol. If it is, it flags all the unrevealed surrounding cells as
     * mines and marks them as checked. If the number of flagged mines surrounding the cell is equal to the number
     * represented by the cell's symbol, it reveals all the unrevealed surrounding cells and marks them as checked.
     *
     * The method continues to iterate through the minefield until the game is over. The game is considered over when
     * all the mines have been revealed or when the AI algorithm encounters an error.
     *
     * @throws Exception if any error occurs while solving the game
     * Not complete
     */
    public void solve() {
        Random rand = new Random();

        // Generate random integers in range 0 to 999
        int i = rand.nextInt(this.rc);
        int j = rand.nextInt(this.rc);
        this.count++;
        super.minefield.createMines(i, j, super.flags);
        super.minefield.evaluateField();
        super.minefield.revealStart(i, j);
        System.out.println(super.rc);
        refresh();
        boolean gameOvr = false;
        boolean change = false;
        boolean[] checked = new boolean[this.rc * this.rc + 1];
        outer:
        while (!gameOvr) {
            inner:
            for (int y = 0; y < this.rc; y++) {
                for (int x = 0; x < this.rc; x++) {
                    if (!gameOvr) {
                        if (super.minefield.board[super.minefield.XYto1D(y, x)].getRevealed()) {

                            int[] ctr = surrondingsCount(x, y);
                            try {
                                if (!checked[super.minefield.XYto1D(y, x)]) {
                                    String querySymbol = super.minefield.board[super.minefield.XYto1D(y, x)].symbol.replaceAll("[FM]", "");
                                    if (!querySymbol.equals("") && !querySymbol.equals("0")) {
                                        int str = Integer.parseInt(querySymbol);
                                        int opened = ctr[0];
                                        int surrondingLen = ctr[1];
                                        int mineRev = ctr[2];
                                        System.out.println("{\nstr: " + str + ", ctr difference: opened " + ctr[0] + ", surroundingLen: " + ctr[1] + ", mineRev: " + ctr[2] + "\n(X,Y): (" + x + ", " + y + ")\n}");
                                        if (surrondingLen - opened + mineRev == str) {
                                            gameOvr = flagAllSurrondingGuess(x, y);
                                            checked[super.minefield.XYto1D(y, x)] = true;
                                            break inner;
                                        }
                                        else if(mineRev==str){
                                            gameOvr = openAllSurroundingGuess(x, y);
                                            checked[super.minefield.XYto1D(y, x)] = true;
                                        }
                                    }
                                }
                            } catch (Exception e) {
                                System.out.println(e);
                                gameOvr = true;
                                break outer;
                            }
                        }
                    }
                }
            }
            gameOvr = minefield.gameOver();

            System.out.println("Is Game over: " + gameOvr);
            refresh();


        }
    }

/**
 * Guesses all the surrounding cells of the given position on the Minefield. It does this regardless of whether the cell is open or not
 * This method checks all the cells surrounding the given position (up, down, left, right, and diagonal)
 * and calls the guess() method for each of them. It returns true if any of the guessed cells contains a mine,
 * otherwise it returns false.
 *
 * @param x The x coordinate of the position
 * @param y The y coordinate of the position
 * @return boolean Returns true if any of the surrounding cells contains a mine, otherwise false.
 */
public boolean openAllSurroundingGuess(int x, int y){
        boolean correct = false;
        if (x + 1 < this.rc) {
            if (y + 1 < this.rc && super.minefield.board[super.minefield.XYto1D(y+1,x+1)].getRevealed())
                correct |= super.minefield.guess(y + 1, x + 1, false);
            if (y - 1 >= 0) {
                correct |= super.minefield.guess(y - 1, x + 1, false);
            }
            correct |= super.minefield.guess(y, x + 1, false);
        }
        if (x - 1 >= 0) {
            if (y + 1 < this.rc)
                correct |= super.minefield.guess(y + 1, x - 1, false);
            if (y - 1 >= 0){
                correct |= super.minefield.guess(y - 1, x - 1, false);}

                correct |= super.minefield.guess(y, x - 1, false);
        }
        if (y + 1 < this.rc)
            correct |= super.minefield.guess(y + 1, x, false);
        if (y - 1 >= 0)
            correct |= super.minefield.guess(y - 1, x, false);
        return correct;
}
    /**
     * Guesses all the surrounding cells of the given position on the Minefield. The cells must be revealed first and places a flag
     * This method checks all the cells surrounding the given position (up, down, left, right, and diagonal)
     * and calls the guess() method for each of them. It returns true if any of the guessed cells contains a mine,
     * otherwise it returns false.
     *
     * @param x The x coordinate of the position
     * @param y The y coordinate of the position
     * @return boolean Returns true if any of the surrounding cells contains a mine, otherwise false.
     */
    public boolean flagAllSurrondingGuess(int x, int y) {
        boolean correct = false;
        if (x + 1 < this.rc) {
            if (y + 1 < this.rc && !super.minefield.board[super.minefield.XYto1D(y + 1, x + 1)].getRevealed())
                correct |= super.minefield.guess(y + 1, x + 1, true);
            if (y - 1 >= 0 && !super.minefield.board[super.minefield.XYto1D(y - 1, x + 1)].getRevealed())
                correct |= super.minefield.guess(y - 1, x + 1, true);
            if (!super.minefield.board[super.minefield.XYto1D(y, x + 1)].getRevealed())
                correct |= super.minefield.guess(y, x + 1, true);
        }
        if (x - 1 >= 0) {
            if (y + 1 < this.rc && !super.minefield.board[super.minefield.XYto1D(y + 1, x - 1)].getRevealed())
                correct |= super.minefield.guess(y + 1, x - 1, true);
            if (y - 1 >= 0 && !super.minefield.board[super.minefield.XYto1D(y - 1, x - 1)].getRevealed())
                correct |= super.minefield.guess(y - 1, x - 1, true);
            if (!super.minefield.board[super.minefield.XYto1D(y, x - 1)].getRevealed())
                correct |= super.minefield.guess(y, x - 1, true);
        }
        if (y + 1 < this.rc && !super.minefield.board[super.minefield.XYto1D(y + 1, x)].getRevealed())
            correct |= super.minefield.guess(y + 1, x, true);
        if (y - 1 >= 0 && !super.minefield.board[super.minefield.XYto1D(y - 1, x)].getRevealed())
            correct |= super.minefield.guess(y - 1, x, true);
        return correct;
    }

    /**
     * Counts the number of revealed, total surrounding, and flagged mine cells surrounding the given position.
     *
     * @param x The x coordinate of the position
     * @param y The y coordinate of the position
     * @return An integer array containing the following values:
     *         - Index 0: The number of surrounding cells that are revealed
     *         - Index 1: The total number of surrounding cells
     *         - Index 2: The number of cells surrounding that are flagged as mines
     */
    public int[] surrondingsCount(int x, int y) {
//        ctr represents total # of surrounding cells that are revealed
//        surr represents total # of surrounding cells
//        mineRey represents total # of cells surrounding that are flagged mines
        int ctr = 0;
        int surr = 0;
        int mineRev = 0;
        if (x + 1 < this.rc) {
            surr++;
            if (y + 1 < this.rc) {
                surr++;
                if (super.minefield.board[super.minefield.XYto1D(y + 1, x + 1)].getRevealed()) {
                    ctr += 1;
                    if (super.minefield.board[super.minefield.XYto1D(y + 1, x + 1)].symbol.toLowerCase().contains("m"))
                        mineRev++;

                }
            }

            if (y - 1 >= 0) {
                surr++;
                if (super.minefield.board[super.minefield.XYto1D(y - 1, x + 1)].getRevealed()) {
                    ctr += 1;
                    if (super.minefield.board[super.minefield.XYto1D(y - 1, x + 1)].symbol.toLowerCase().contains("m"))
                        mineRev++;

                }
                if (super.minefield.board[super.minefield.XYto1D(y, x + 1)].getRevealed()) {
                    ctr += 1;
                    if (super.minefield.board[super.minefield.XYto1D(y, x + 1)].symbol.toLowerCase().contains("m"))
                        mineRev++;

                }
            }
        }
            if (x - 1 >= 0) {
                surr++;
                if (y + 1 < this.rc) {
                    surr++;
                    if (super.minefield.board[super.minefield.XYto1D(y + 1, x - 1)].getRevealed()) {
                        ctr += 1;
                        if (super.minefield.board[super.minefield.XYto1D(y + 1, x - 1)].symbol.toLowerCase().contains("m"))
                            mineRev++;

                    }
                }
                if (y - 1 >= 0) {
                    surr++;
                    if (super.minefield.board[super.minefield.XYto1D(y - 1, x - 1)].getRevealed()) {
                        ctr += 1;
                        if (super.minefield.board[super.minefield.XYto1D(y - 1, x - 1)].symbol.toLowerCase().contains("m"))
                            mineRev++;

                    }
                }
                if (super.minefield.board[super.minefield.XYto1D(y, x - 1)].getRevealed()) {
                    ctr += 1;
                    if (super.minefield.board[super.minefield.XYto1D(y, x - 1)].symbol.toLowerCase().contains("m"))
                        mineRev++;

                }
            }
            if (y + 1 < this.rc) {
                surr++;
                if (super.minefield.board[super.minefield.XYto1D(y + 1, x)].getRevealed()) {
                    ctr += 1;
                    if (super.minefield.board[super.minefield.XYto1D(y + 1, x)].symbol.toLowerCase().contains("m"))
                        mineRev++;

                }
            }
            if (y - 1 >= 0) {
                surr++;
                if (super.minefield.board[super.minefield.XYto1D(y - 1, x)].getRevealed()) {
                    ctr++;
                    if (super.minefield.board[super.minefield.XYto1D(y - 1, x)].symbol.toLowerCase().contains("m"))
                        mineRev++;
                }
            }


        return new int[]{ctr, surr, mineRev};
    }


    public void refresh() {

        eval();
        super.minefield.printMinefield();
    }


    public static void main(String[] args) {
        MinesweeperAI ai = new MinesweeperAI();
    }


}
