import java.util.Scanner;
//agene001
public class Main {
    public static void main(String[] args) {
        //scanner and inital game loop variables
        Scanner s = new Scanner(System.in);
        boolean a = true;
        boolean b = true;
        while (b) {
            outer:
            while (a) {
                System.out.println("What difficulty would you like.");
                String difficult = s.nextLine();
                System.out.println("In debug mode (yes or no)?");
                String debug = s.nextLine();
                System.out.println("Would you like it the way minesweeper is supposed to be played or the way asked for by the project prompt?\nyes for right way\nno for project way");
                String res = s.nextLine();
                Minefield board1;
                if (difficult.toLowerCase().startsWith("e")) board1 = new Minefield(10, 10, 10);
                else if (difficult.toLowerCase().startsWith("m")) board1 = new Minefield(18, 18, 40);
                else if (difficult.toLowerCase().startsWith("h")) board1 = new Minefield(24, 24, 99);
                else if(difficult.toLowerCase().startsWith("n"))board1 = new Minefield(30, 30, 200);
                else if(difficult.toLowerCase().startsWith("c"))board1 = new Minefield(35, 35, 150);
                else {
                    System.out.println("Not valid difficulty");
                    break;
                }
                if (debug.toLowerCase().startsWith("y")) board1.debug();
                else if (!debug.toLowerCase().startsWith("n")) {
                    System.out.println("Not a valid response");
                    break;
                }
                board1.printMinefield();
                System.out.println("Please enter a starting guess(x,y)");
                String[] guess = s.nextLine().split(",");
                int w = Integer.parseInt(guess[0]);
                int c = Integer.parseInt(guess[1]);
                if(res.toLowerCase().startsWith("y")) {
                    if (w < board1.cols && w >= 0 && c < board1.rows && c >= 0) {
                        board1.createMines(c, w, board1.flags);
                        board1.evaluateField();
                        board1.revealStart(c, w);
                        //continues to ask for guess
                        while (true) {
                            board1.printMinefield();
                            System.out.println("Please enter a guess(x,y,flag) \nflag:T/F");
                            String[] guess1 = s.nextLine().split(",");
                            try {
                                int x = Integer.parseInt(guess1[0]);
                                int y = Integer.parseInt(guess1[1]);
                                boolean flag;
                                if (guess1[2].toLowerCase().startsWith("t")) flag = true;
                                else flag = false;
                                if (board1.guess(y, x, flag)) {
                                    System.out.println("Aww you hit a mine. Game Over!!!");
                                    break outer;
                                }
                                if(board1.gameOver()){
                                    System.out.println("Congratulations you have found all the mines!!!");
                                    break outer;
                                }
                            } catch (ArrayIndexOutOfBoundsException e) {
                                System.out.println("Not a valid guess. try again");
                            }
                            catch (NumberFormatException e){
                                System.out.println("Not a valid guess. try again");
                            }

                        }

                    } else System.out.println("Not a valid guess");
                }else if(res.toLowerCase().startsWith("n")){
                    if (w < board1.cols && w >= 0 && c < board1.rows && c >= 0) {
                        board1.createMines(c, w, board1.flags);
                        board1.evaluateField();
                        // board1.revealStart1(c, w);
                        //continues to ask for guess
                        while (true) {
                            board1.printMinefield();
                            System.out.println("Please enter a guess(x,y,flag) \nflag:T/F");
                            String[] guess1 = s.nextLine().split(",");
                            try {
                                int x = Integer.parseInt(guess1[0]);
                                int y = Integer.parseInt(guess1[1]);
                                boolean flag;
                                if (guess1[2].toLowerCase().startsWith("t")) flag = true;
                                else flag = false;
                                //    if (board1.guess1(y, x, flag)) {
                                System.out.println("Aww you hit a mine. Game Over!!!");
                                break outer;
                                //    }
                                //    if(board1.gameOver()){
//                                    System.out.println("Congratulations you have found all the mines!!!");
//                                    break outer;
//                                }
                            } catch (ArrayIndexOutOfBoundsException e) {
                                System.out.println("Not a valid guess. try again");
                            }


                        }

                    } else System.out.println("Not a valid guess");
                }
                else {
                    System.out.println("Not a valid response for minesweeper mode");
                    break;
                }
            }
            System.out.println("Play again?");
            String res = s.nextLine();
            if (res.toLowerCase().startsWith("y"))a= true;
            else b=false;
        }
    }
}