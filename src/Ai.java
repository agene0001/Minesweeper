import edu.princeton.cs.algs4.In;

import java.util.*;
import java.util.stream.Collectors;

public class Ai {
    int height = 8;
    int width = 8;
    Set<List<Integer>> movesMade = new HashSet<>();
    Set<List<Integer>> mines = new HashSet<>();

    public Set<List<Integer>> getMines() {
        return mines;
    }

    public Set<List<Integer>> knownMines() {
        return mines;
    }

    public void setMines(Set<List<Integer>> mines) {
        this.mines = mines;
    }

    public Set<List<Integer>> getSafes() {
        return safes;
    }

    public void setSafes(Set<List<Integer>> safes) {
        this.safes = safes;
    }

    Set<List<Integer>> safes = new HashSet<>();
    ArrayList<Sentence> knowledge = new ArrayList<>();

    public Ai(int height, int width) {
        this.height = height;
        this.width = width;


    }

    public Ai() {


    }

    public void markMine(int x, int y) {

        List<Integer> temp = Arrays.asList(x, y);
        mines.add(temp);

        // Temporary list for Sentences that need to be marked
        List<Sentence> sentencesToMark = new ArrayList<>(knowledge);

        for (Sentence sentence : sentencesToMark) {
            sentence.markMine(temp);
        }
    }

    public void markSafe(int x, int y) {
        List<Integer> temp = Arrays.asList(x, y);
        safes.add(temp);

        // Temporary list for Sentences that need to be marked
        List<Sentence> sentencesToMark = new ArrayList<>(knowledge);

        for (Sentence sentence : sentencesToMark) {
            sentence.markSafe(temp);
        }
    }

    /**
     * Returns the surrounding cells of a given cell as an ArrayList. A cell is
     * considered surrounding if it is present within one index away on
     * both the x and y directions from the given cell. It does not consider
     * the actual cell itself and checks if the cells are valid with respect
     * to the boundaries of the grid (height and width).
     *
     * @param cell The cell for which we want to find the surrounding cells.
     *             It contains x and y coordinates as first and second elements.
     * @return ArrayList of surrounding cells of the given "cell".
     **/
    public ArrayList<List<Integer>> closeCells(Integer[] cell) {

        // An ArrayList to store the determined surrounding cells.
        ArrayList<List<Integer>> closedCells = new ArrayList<>();

        // Iterating through the indexes around the given cell.
        // Starting from one index before to one index after the given cell.
        for (int i = cell[0] - 1; i < cell[0] + 2; i++) {
            for (int j = cell[1] - 1; j < cell[1] + 2; j++) {

                // Exclude the original cell itself.
                // Also ensure that the cells are within the grid boundaries.
                if (cell[0] != i || cell[1] != j) {
                    if (i >= 0 && i < height && j >= 0 && j < width) {

                        // Add the valid surrounding cell to the list.
                        closedCells.add(Arrays.asList(i, j));
                    }
                }
            }
        }

        return closedCells;
    }

    public boolean moveMade(List<Integer> cell) {
        return movesMade.contains(cell);
    }

    /**
     * This method evolves the agent's knowledge about the minefield.
     *
     * @param cell An array of Integer containing the x and y coordinates of the cell.
     * @param count The number of mines surrounding the cell.
     */
    public void addKnowledge(Integer[] cell, int count) {

        // Convert cell array to list.
        List<Integer> cellAsList = Arrays.asList(cell);

        // Add the cell to the safes list.
        safes.add(cellAsList);

        // Check if a move has been made on the cell.
        // If not, then process further, else do nothing.
        if (!moveMade(cellAsList)) {
            // Creates a set to hold cells.
            HashSet<List<Integer>> cells = new HashSet();

            // For all close cells
            for (List<Integer> close : closeCells(cell)) {
                // Add to cells set if not a safe or mine cell
                if (!safes.contains(close) && !mines.contains(close)) {
                    cells.add(close);
                }
                // If cell is a mine, decrement count
                else if (mines.contains(close)) {
                    count--;
                }
            }

            // Creates new Sentence with cells and updated count.
            Sentence cent = new Sentence(cells, count);

                // Add the sentence to knowledge.
                knowledge.add(cent);

                // Converts cell array to list and adds it to movesMade.
                List<Integer> list = Arrays.stream(cell).collect(Collectors.toList());
                movesMade.add(list);

                // Mark cell as safe.
                markSafe(cell[0], cell[1]);

                // If the size of cells and count is the same, mark all cells as mine.
                // If the count is 0, mark all cells as safe.
                Set<List<Integer>> cellCpy = new HashSet<>(cells);
                if (cells.size() == count) {
                    for (List<Integer> cell1 : cellCpy) markMine(cell1.get(0), cell1.get(1));
                }
                if (count == 0) {
                    for (List<Integer> cell1 : cellCpy) markSafe(cell1.get(0), cell1.get(1));
                }

                // Generate new Sentences based on current knowledge and add to knowledge.
                List<Sentence> newSentences = new ArrayList<>();
                for (Sentence sentence : knowledge) {
                    sentence.markSafe(Arrays.asList(cell));
                    if (!cellCpy.equals(sentence.cells)) {
                        if (cellCpy.containsAll(sentence.cells)) {
                            Set<List<Integer>> diff = setDifference(new HashSet(sentence.cells), new HashSet(cellCpy));
                            Set<List<Integer>> diffCpy = new HashSet<>();
                            for (List<Integer> cell2 : diff) {
                                diffCpy.add(cell2);
                            }
                            if (!diff.isEmpty()) {
                                newSentences.add(new Sentence(diffCpy, count - sentence.count));
                            }
                        } else if (sentence.cells.containsAll(cellCpy)) {
                            Set<List<Integer>> diff = setDifference(new HashSet(cellCpy), new HashSet(sentence.cells));
                            Set<List<Integer>> diffCpy = new HashSet<>();
                            for (List<Integer> cell2 : diff) {
                                diffCpy.add(cell2);
                            }
                            if (!diff.isEmpty()) {
                                newSentences.add(new Sentence(diffCpy, sentence.count - count));
                            }
                        }
                    }
                }
                // Add all new sentences to knowledge.
                for (Sentence sentence : newSentences) {
                    if (!knowledge.contains(sentence)) {
                        knowledge.add(sentence);
                    }
                }

                // Check if the knowledge has been extended after making an inference. If so, keep checking knowledge.
                int size = knowledge.size();
                extraInference();
                while (size != knowledge.size()) checkKnowledge();
                checkKnowledge();
            }


    }


    /**
     * This method is used to infer knowledge based on the current state of the minesweeper board.
     * The method constructs the knowledge in terms of sentences, where each sentence consists of a set of cells and a count.
     * The count is the number of mine-cells among the cells. A sentence is created for each unset (unclear) cell on the board
     * with a number, and the cells in the sentence are the surrounding unset cells.
     * The method iterates this process until no more knowledge can be inferred.
     */
    public void checkKnowledge() {
        // A list of indices for mine-cells and safe-cells to be marked accordingly as an update is made
        List<List<Integer>> minesToMark = new ArrayList<>();
        List<List<Integer>> safesToMark = new ArrayList<>();
        // A list of sentences to be removed from the knowledge base as an update is made
        List<Sentence> sentencesToRemove = new ArrayList<>();
        // A variable to control the update iteration loop
        boolean update = true;
        // Continuously update knowledge until no more updates can be made
        while (update) {
            // Initially no updates are to be made
            update = false;
            // Iterate through all the sentences in the knowledge base to check for mines and safes
            Iterator<Sentence> iterator = knowledge.iterator();
            while (iterator.hasNext()) {
                Sentence sentence = iterator.next();
                // If a sentence has no cells, add it to the sentencesToRemove list
                if (sentence.cells.size() == 0) {
                    sentencesToRemove.add(sentence);
                }
                // Known mines and safes in the sentence are added to the minesToMark and safesToMark lists respectively
                Set<List<Integer>> mines = sentence.knownMines();
                Set<List<Integer>> safes = sentence.knownSafes();
                if (mines != null) minesToMark.addAll(mines);
                if (safes != null) safesToMark.addAll(safes);
            }
            // After iteration through all sentences, update knowledge base
            // Update the knowledge base by removing sentences from sentencesToRemove
            for (Sentence s : sentencesToRemove) {
                knowledge.remove(s);
            }
            // Mark cells in minesToMark and safesToMark as mines and safes respectively
            for (List<Integer> mine : minesToMark) {
                markMine(mine.get(0), mine.get(1));
            }
            for (List<Integer> safe : safesToMark) {
                markSafe(safe.get(0), safe.get(1));
            }
            // After updating the knowledge base, check if any updates were made in this round
            // If updates were made, set update to true for the next round
            if (!sentencesToRemove.isEmpty() || !minesToMark.isEmpty() || !safesToMark.isEmpty()) {
                update = true;
            }
            // Clear lists for next iteration
            sentencesToRemove.clear();
            minesToMark.clear();
            safesToMark.clear();
        }
    }

    public <T> Set<List<T>> setDifference(Set<List<T>> set1, Set<List<T>> set2) {

        set2.removeAll(set1);
        return set2;
    }

    /**
     * This method carries out further inference of Minesweeper gameplay by comparing all pairs of sentence objects in the knowledge base.
     * It compares each pair of sentences (sentence and sentence1) and creates a new sentence if certain conditions are met.
     * If the cells of sentence are a superset of the cells of sentence1 and they are not equal, the code proceeds to generate new knowledge.
     *
     * The new sentence is created from the set difference of their cell sets and the difference of their counts.
     * It then checks if the newly formed sentence is already present in our knowledge base and the number of cells in the new sentence is not zero.
     * If they do not appear in the knowledge base and contain more than zero cells, the method attempts to discover known mines or safe cells from the new sentence.
     *
     * If mines are identified, it marks each of these cells as a mine and if safe cells are identified, they are marked as safe.
     * This constitutes a new piece of knowledge gained from the current state of the game.
     *
     * This method aims to deduce more information by comparing and contrasting the existing sentences in the knowledge base.
     */
    public void extraInference() {
        for (Sentence sentence : knowledge) {
            for (Sentence sentence1 : knowledge) {
                if (sentence.cells.containsAll(sentence1.cells) && !sentence.equals(sentence1)) {
                    Set<List<Integer>> newCells = setDifference(new HashSet(sentence1.cells), new HashSet(sentence.cells));

                    // Computing the difference of the counts of each pair of sentence
                    int newCount = sentence.count - sentence1.count;

                    // Create new sentence with the difference of cells and counts
                    Sentence newSentence = new Sentence(newCells, newCount);

                    // Check whether this new sentence already exists in our knowledge base & ensure it contains atleast 1 cell
                    if (!knowledge.contains(newSentence) && newCells.size() != 0) {

                        // Attempting to explicitly identify mines from the new sentence
                        Set<List<Integer>> mines = newSentence.knownMines();

                        // Attempting to explicitly identify safe cells from the new sentence
                        Set<List<Integer>> safes = newSentence.knownSafes();

                        // If we found any mines, mark them as mines
                        if (mines != null) {
                            for (List<Integer> mine : mines) {
                                markMine(mine.get(0), mine.get(1));
                            }
                        }

                        // If we found any safe cells, mark them as safe
                        if (safes != null) {
                            for (List<Integer> safe : safes) {
                                markSafe(safe.get(0), safe.get(1));
                            }
                        }
                    }
                }
            }
        }
    }

    public List<Integer> makeSafeMove() {
        for (List<Integer> arr : safes) {
            if (!movesMade.contains(arr)) {
                return arr;
            }
        }
        return null;
    }
public void printKnowledge(){
        for(Sentence sentence : knowledge){
            System.out.println(sentence);
        }
}
    /**
     * Generates a random move based on own knowledge.
     *
     * @return A list consisting of coordinates to make the move.
     */
    public List<Integer> makeRandomMove() {
        //check if knowledge set is not empty and if so find cell with the lowest probability of being a mine
        if (knowledge.size() != 0) {
            // initializing minimum probability to 1
            float minProb = 1;
            // variable to store the best move
            List<Integer> bestMove = null;

            // iterating over all sentences in the knowledge
            for (Sentence sentence : knowledge) {

                // check if there are cells in sentence and if the ratio of count to cells is less than minimum probability
                float prob = (float) sentence.count / sentence.cells.size();
                if (sentence.cells.size() != 0 && !mines.containsAll(sentence.cells)&&prob< minProb ) {
                    minProb = (float) sentence.count / sentence.cells.size();
                    // transforming sentence cells HashSet into ArrayList
                    ArrayList<List<Integer>> array = new ArrayList<>(sentence.cells);

                    // creating random number generator
                    Random rand = new Random();

                    // generating random index within the size of array
                    int randomIndex = rand.nextInt(array.size());

                    // selecting the cell from array based on random index
                    bestMove = array.get(randomIndex);
                    while(mines.contains(bestMove)) bestMove = array.get(randomIndex);
                }
            }

            // if best move exists, return it
            if (bestMove != null) {
                return bestMove;
            }
        }

        // If no best move detected from existing knowledge, make a random move
        // Iterate over the game grid
        for (int i = 0; i < this.height; i++) {
            for (int j = 0; j < this.width; j++) {
                // Check if specific cell has not been visited and is not a mine
                if (!this.movesMade.contains(Arrays.asList(i, j)) && !this.mines.contains(Arrays.asList(i, j))) {
                    // Return the first unvisited, safe cell found.
                    return Arrays.asList(i, j);
                }
            }
        }

        // If no move is possible, return null
        return null;
    }
    public List<Integer> makeRandomMove(int unopened,int minesCtr) {
        //check if knowledge set is not empty and if so find cell with the lowest probability of being a mine
        if (knowledge.size() != 0) {
            // initializing minimum probability to 1
            float minProb = (float)(minesCtr-mines.size())/unopened;
            // variable to store the best move
            List<Integer> bestMove = null;

            // iterating over all sentences in the knowledge
            for (Sentence sentence : knowledge) {

                // check if there are cells in sentence and if the ratio of count to cells is less than minimum probability
                float prob = (float) sentence.count / sentence.cells.size();
                if (sentence.cells.size() != 0 && !mines.containsAll(sentence.cells)&&(prob< minProb||prob<.25) ) {
                    minProb = (float) sentence.count / sentence.cells.size();
                    // transforming sentence cells HashSet into ArrayList
                    ArrayList<List<Integer>> array = new ArrayList<>(sentence.cells);

                    // creating random number generator
                    Random rand = new Random();

                    // generating random index within the size of array
                    int randomIndex = rand.nextInt(array.size());

                    // selecting the cell from array based on random index
                    bestMove = array.get(randomIndex);
                    while(mines.contains(bestMove)) bestMove = array.get(randomIndex);
                }
            }

            // if best move exists, return it
            if (bestMove != null) {
                return bestMove;
            }
        }

        // If no best move detected from existing knowledge, make a random move
        // Iterate over the game grid
        for (int i = 0; i < this.height; i++) {
            for (int j = 0; j < this.width; j++) {
                // Check if specific cell has not been visited and is not a mine
                if (!this.movesMade.contains(Arrays.asList(i, j)) && !this.mines.contains(Arrays.asList(i, j))) {
                    // Return the first unvisited, safe cell found.
                    boolean cont = false;
                    for(Sentence sentence : knowledge) {
                        if(sentence.cells.contains(Arrays.asList(i,j))) cont = true;
                    }
                    if(!cont) return Arrays.asList(i, j);
                }
            }
        }
        for (int i = 0; i < this.height; i++) {
            for (int j = 0; j < this.width; j++) {
                // Check if specific cell has not been visited and is not a mine
                if (!this.movesMade.contains(Arrays.asList(i, j)) && !this.mines.contains(Arrays.asList(i, j))) {
                    // Return the first unvisited, safe cell found.

                       return Arrays.asList(i, j);
                }
            }
        }

        // If no move is possible, return null
        return null;
    }
}
