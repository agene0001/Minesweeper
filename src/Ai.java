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
    public Set<List<Integer>>knownMines() {
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

public void markMine(int x,int y) {

       List<Integer> temp = Arrays.asList(x,y);
    mines.add(temp);

    // Temporary list for Sentences that need to be marked
    List<Sentence> sentencesToMark = new ArrayList<>(knowledge);

    for (Sentence sentence : sentencesToMark) {
        sentence.markMine(temp);
    }
}

public void markSafe(int x,int y) {
       List<Integer> temp = Arrays.asList(x,y);
    safes.add(temp);

    // Temporary list for Sentences that need to be marked
    List<Sentence> sentencesToMark = new ArrayList<>(knowledge);

    for (Sentence sentence : sentencesToMark) {
        sentence.markSafe(temp);
    }
}

    public ArrayList<List<Integer>> closeCells(Integer[] cell) {
        ArrayList<List<Integer>> closedCells = new ArrayList<>();
        for (int i = cell[0] - 1; i < cell[0] + 2; i++) {
            for (int j = cell[1] - 1; j < cell[1] + 2; j++) {
                if (cell[0] != i || cell[1] != j) {
                    if(i>=0&&i<height&&j>=0&&j<width) {
                    closedCells.add(Arrays.asList(i, j));}
                }
            }
        }
        return closedCells;
    }
    public boolean moveMade(List<Integer> cell) {
        return movesMade.contains(cell);
    }

//    public void addKnowledge(Integer[] cell,ArrayList<List<Integer>> cells, int count) {
//
////        for(List<Integer> mines : this.mines)System.out.printf("mines->(%d,%d)\n",mines.get(0),mines.get(1));
////        for(List<Integer> safes : this.safes)System.out.printf("safes->(%d,%d)\n",safes.get(0),safes.get(1));
////        for(Sentence sentence : this.knowledge)System.out.println("Knowledge sentence "+sentence.toString());
//    List<Integer> cellAsList = Arrays.asList(cell);
//        if (!moveMade(cellAsList)) {
//
//        Sentence cent = new Sentence(new HashSet(cells),count);
//           knowledge.add(cent);
//            List<Integer> list = Arrays.stream(cell).collect(Collectors.toList());
//            movesMade.add(list);
//            markSafe(cell[0], cell[1]);
//            Set<List<Integer>> cellCpy = new HashSet<>(cells);
//
//            if (cells.size() == count) {
//                for (List<Integer> cell1 : cellCpy) markMine(cell1.get(0), cell1.get(1));
//
//            }
//            if (count == 0) {
//                for (List<Integer> cell1 : cellCpy) markSafe(cell1.get(0), cell1.get(1));
//            }
//            Set<Integer[]> cellCpyArray = cellCpy.stream().map(u -> u.toArray(new Integer[0])).collect(Collectors.toSet());
//
//
//
//            for (Sentence sentence : knowledge) {
////                System.out.println(sentence.toString());
//                sentence.markSafe(Arrays.asList(cell));
//                if (!cellCpy.equals(sentence.cells))
//                    if (cellCpy.containsAll(sentence.cells)) {
//                        Set<Integer[]> diff = setDifference(new HashSet(sentence.cells), new HashSet(cellCpyArray));
//                        Set<List<Integer>> diffCpy = new HashSet<>();
//                        for (Integer[] cell2 : diff) {
//                            diffCpy.add(Arrays.asList(cell2));
//                        }
//                        if (!diff.isEmpty()) {
//                            knowledge.add(new Sentence(diffCpy, sentence.count - cellCpyArray.size()));
//                        }
//                    } else if (sentence.cells.containsAll(cellCpy)) {
//                        Set<Integer[]> diff = setDifference(new HashSet(cellCpyArray), new HashSet(sentence.cells));
//                        Set<List<Integer>> diffCpy = new HashSet<>();
//                        for (Integer[] cell2 : diff) {
//                            diffCpy.add(Arrays.asList(cell2));
//                        }
//                        if (!diff.isEmpty()) {
//                            knowledge.add(new Sentence(diffCpy, cellCpy.size() - sentence.count));
//                        }
//
//                    }
//            }
//            extraInference();
//            checkKnowledge();
//        }
//    }
    public void addKnowledge(Integer[] cell, int count) {

//        for(List<Integer> mines : this.mines)System.out.printf("mines->(%d,%d)\n",mines.get(0),mines.get(1));
//        for(List<Integer> safes : this.safes)System.out.printf("safes->(%d,%d)\n",safes.get(0),safes.get(1));
//        for(Sentence sentence : this.knowledge)System.out.println("Knowledge sentence "+sentence.toString());
    List<Integer> cellAsList = Arrays.asList(cell);
    safes.add(cellAsList);
        if (!moveMade(cellAsList)) {
        HashSet<List<Integer>> cells = new HashSet();
        for(List<Integer> close: closeCells(cell)) {
            if(!safes.contains(close) && !mines.contains(close)) {
                cells.add(close);
            }
            else if(mines.contains(close)) {
                count--;
            }
        }
        Sentence cent = new Sentence(cells,count);
           knowledge.add(cent);
            List<Integer> list = Arrays.stream(cell).collect(Collectors.toList());
            movesMade.add(list);
            markSafe(cell[0], cell[1]);
            Set<List<Integer>> cellCpy = new HashSet<>(cells);

            if (cells.size() == count) {
                for (List<Integer> cell1 : cellCpy) markMine(cell1.get(0), cell1.get(1));

            }
            if (count == 0) {
                for (List<Integer> cell1 : cellCpy) markSafe(cell1.get(0), cell1.get(1));
            }
//            Set<Integer[]> cellCpyArray = cellCpy.stream().map(u -> u.toArray(new Integer[0])).collect(Collectors.toSet());



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
                        newSentences.add(new Sentence(diffCpy, count-sentence.count));
                    }
                } else if (sentence.cells.containsAll(cellCpy)) {
                    Set<List<Integer>> diff = setDifference(new HashSet(cellCpy), new HashSet(sentence.cells));
                    Set<List<Integer>> diffCpy = new HashSet<>();
                    for (List<Integer> cell2 : diff) {
                        diffCpy.add(cell2);
                    }
                    if (!diff.isEmpty()) {
                        newSentences.add(new Sentence(diffCpy,sentence.count-count));
                    }
                }
            }
        }
         knowledge.addAll(newSentences);
            while(extraInference())
            checkKnowledge();
        }
    }







//    public void checkKnowledge() {
//        boolean update = true;
//
//        while (update) {
//            update = false;
//
//            Iterator<Sentence> iterator = knowledge.iterator();
//            while (iterator.hasNext()) {
//                Sentence sentence = iterator.next();
//
//                    if (sentence.cells.size() == 0) {
//                        iterator.remove();
//                    }
//                    Set<List<Integer>> mines = sentence.knownMines();
//                    Set<List<Integer>> safes = sentence.knownSafes();
//
//                    if (mines != null) {
//                        for (List<Integer> mine : mines) {
//                            markMine(mine.get(0), mine.get(1));
////                    checkKnowledge();
//                        }
//                    }
//                    if (safes != null) {
//                        for (List<Integer> safe : safes) {
//                            markSafe(safe.get(0), safe.get(1));
////                    checkKnowledge();
//
//                            }
//                    }
//                }
//
//        }
//    }
/* You can follow the bellow approach
    1. First collect all items that need to be removed/added without modifying the list.
    2. Then remove/add all those items after the loop.
*/

    public void checkKnowledge() {
        // Variables to track the mines and safes
        List<List<Integer>> minesToMark = new ArrayList<>();
        List<List<Integer>> safesToMark = new ArrayList<>();
        List<Sentence> sentencesToRemove = new ArrayList<>();

        boolean update = true;

        while (update) {
            update = false;
            Iterator<Sentence> iterator = knowledge.iterator();

            while (iterator.hasNext()) {
                Sentence sentence = iterator.next();
                if (sentence.cells.size() == 0) {
                    sentencesToRemove.add(sentence);
                }

                Set<List<Integer>> mines = sentence.knownMines();
                Set<List<Integer>> safes = sentence.knownSafes();
                if (mines != null) minesToMark.addAll(mines);
                if (safes != null) safesToMark.addAll(safes);
            }

            // Make updates after iteration
            for (Sentence s : sentencesToRemove) {
                knowledge.remove(s);
            }
            for (List<Integer> mine : minesToMark) {
                markMine(mine.get(0), mine.get(1));
//                checkKnowledge();
            }
            for (List<Integer> safe : safesToMark) {
                markSafe(safe.get(0), safe.get(1));
//                checkKnowledge();
            }

            // Check if there were any updates this round
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

    public boolean extraInference() {
        boolean change = false;
        for (Sentence sentence : knowledge) {
            for (Sentence sentence1 : knowledge) {
                if (sentence.cells.containsAll(sentence1.cells)&&!sentence.equals(sentence1)) {
                    Set<List<Integer>> newCells = setDifference(new HashSet(sentence1.cells), new HashSet(sentence.cells));

                    //Subtract counts
                    int newCount = sentence.count - sentence1.count;

                    Sentence newSentence = new Sentence(newCells, newCount);
                    if (!knowledge.contains(newSentence)&&newCells.size()!=0) {
                        Set<List<Integer>> mines = newSentence.knownMines();
                        Set<List<Integer>> safes = newSentence.knownSafes();
                        change = true;
                        if (mines != null) {
                            for (List<Integer> mine : mines) {
                                markMine(mine.get(0), mine.get(1));
//                                change = false;

                            }
                        }
                        if (safes != null) {
                            for (List<Integer> safe : safes) {
                                markSafe(safe.get(0), safe.get(1));
//                                change = false;
                            }
                        }
                    }
                }
            }
        }
        return change;
    }

    public List<Integer> makeSafeMove() {
        for (List<Integer> arr : safes) {
            if (!movesMade.contains(arr)) {
                return arr;
            }
        }
        return null;
    }

    public List<Integer> makeRandomMove() {
        for (int i = 0; i < this.height; i++) {
            for (int j = 0; j < this.width; j++) {
                if (!this.movesMade.contains(Arrays.asList(i, j)) && !this.mines.contains(Arrays.asList(i, j))) {
                    return Arrays.asList(i, j);
                }
            }
        }
        return null;
    }
}
