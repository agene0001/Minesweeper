import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Sentence {
    Set<List<Integer>> cells;
    int count;


    /**
     * Constructs a Sentence object with the given cells and count.
     *
     * @param cells an array of arrays representing the cells
     * @param count the count of the cells
     */
    public Sentence(List<Integer>[] cells, int count) {
        this.cells = new HashSet<>();
        for (List<Integer> cell : cells) {
            this.cells.add(cell);
        }
        this.count = count;
    }

    /**
     * Constructs a Sentence object with the given cells and count.
     *
     * @param cells a set of lists representing the cells
     * @param count the count of the cells
     */
    public Sentence(Set<List<Integer>> cells, int count) {
        this.cells = cells;
        this.count = count;
    }


    /**
     * Returns a set of lists representing the cells that are known to be safe.
     *
     * @return the set of safe cells, or null if there are no safe cells
     */
    public Set<List<Integer>> knownSafes() {
        if (count == 0) return cells;
        return null;
    }

    /**
     * Returns a set of lists representing the cells that are known to be mines.
     *
     * @return the set of mine cells, or null if there are no mine cells
     */
    public Set<List<Integer>> knownMines() {
        if (count == cells.size() && count!=0) return cells;
        return null;
    }

    /**
     * Marks a cell as a mine and updates the knowledge base accordingly.
     *
     * @param cell the coordinates of the cell to mark as a mine
     */
    public void markMine(List<Integer> cell) {
        if (cells.contains(cell)) {
            cells.remove(cell);
            count--;
        }
    }
@Override
public String toString() {
        String s = "Value="+count+"\n{\n";
        for (List<Integer> cell : cells) {
            s+= "("+cell.get(0)+","+cell.get(1)+")\n";
        }
        s+="}\n";
        return s;

}
@Override
public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null || getClass() != obj.getClass()) return false;
    Sentence sentence = (Sentence) obj;
    return cells.equals(sentence.cells);
}

    public void markSafe(List<Integer> cell) {
        if (cells.contains(cell)) {
            cells.remove(cell);
        }
    }
}
