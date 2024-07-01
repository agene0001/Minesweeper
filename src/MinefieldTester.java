
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;

import java.util.*;

import static org.junit.Assert.*;

public class MinefieldTester {

    Set<List<Integer>> mines = new HashSet<>();
    int rc;
    int flags;
    Minefield minefield;
    Ai solver;


    public void setUp(ArrayList<List<Integer>> mine) {
        for (List<Integer> list : mine) {
            mines.add(list);
        }
        minefield = new Minefield(rc, rc, flags, mines);

        solver = new Ai(rc, rc);
    }

    public ArrayList<List<Integer>> closeClosedCells(Integer[] cell) {
        ArrayList<List<Integer>> closedCells = new ArrayList<>();
        for (int i = cell[0] - 1; i < cell[0] + 2; i++) {
            for (int j = cell[1] - 1; j < cell[1] + 2; j++) {
                if (cell[0] != i || cell[1] != j) {
                    if (i >= 0 && i < rc && j >= 0 && j < rc) {
                        int x = minefield.XYto1D(i, j);
                        if (!minefield.getCell(minefield.XYto1D(i, j)).getRevealed())
                            closedCells.add(new ArrayList<>(Arrays.asList(i, j)));
                    }
                }
            }
        }
        return closedCells;
    }

    @Test
    public void singleMine() {
        ArrayList<List<Integer>> closedCells = new ArrayList<>();
        closedCells.add(Arrays.asList(2, 2));
        flags = 1;
        rc = 4;
        setUp(closedCells);
        minefield.revealStart(0, 0);
        minefield.printMinefield();
//        minefield.printDebugMinefield();
//        while(true){}
        for (int i = 0; i < rc; i++) {
            for (int j = 0; j < rc; j++) {
                MinefieldGUI.solverEval(j, i, minefield, solver);
            }
        }
//        field.printDebugMinefield();
        assertEquals(Arrays.asList(3, 2), solver.makeSafeMove());
        ArrayList<Integer> mine = new ArrayList<>();
        HashSet<List<Integer>> mines = new HashSet<>();

        HashSet<List<Integer>> safes = new HashSet<>();
        addCoord(2, 2, mine);
        mines.add(mine);

        safes.add(addCoord(0, 0, new ArrayList<>()));
        safes.add(addCoord(1, 0, new ArrayList<>()));
        safes.add(addCoord(2, 0, new ArrayList<>()));
        safes.add(addCoord(3, 0, new ArrayList<>()));
        safes.add(addCoord(0, 1, new ArrayList<>()));
        safes.add(addCoord(1, 1, new ArrayList<>()));
        safes.add(addCoord(2, 1, new ArrayList<>()));
        safes.add(addCoord(3, 1, new ArrayList<>()));
        safes.add(addCoord(0, 2, new ArrayList<>()));
        safes.add(addCoord(1, 2, new ArrayList<>()));
        safes.add(addCoord(3, 2, new ArrayList<>()));
        safes.add(addCoord(0, 3, new ArrayList<>()));
        safes.add(addCoord(1, 3, new ArrayList<>()));
        safes.add(addCoord(2, 3, new ArrayList<>()));
        assertEquals(mines, solver.getMines());
        assertEquals(safes, solver.getSafes());
    }

    public ArrayList<Integer> addCoord(int x, int y, ArrayList<Integer> lis) {
        lis.add(x);
        lis.add(y);
        return lis;

    }

    public void makeMove(int i, int j) {
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
                solver.addKnowledge(new Integer[]{j, i}, val);
            }
        }
    }

    @Test
    public void twoMines() {
        ArrayList<List<Integer>> closedCells = new ArrayList<>();
        closedCells.add(Arrays.asList(2, 2));
        closedCells.add(Arrays.asList(0, 2));
        flags = 2;
        rc = 4;
        setUp(closedCells);
        minefield.revealStart(0, 0);
        minefield.printMinefield();
        minefield.printDebugMinefield();
//        while(true){}
        for (int i = 0; i < rc; i++) {
            for (int j = 0; j < rc; j++) {
                if (minefield.getCell(minefield.XYto1D(i, j)).getRevealed()) {
                    makeMove(i, j);
                }
            }
        }
//        field.printDebugMinefield();

        List<Integer> safe = solver.makeSafeMove();
        assertEquals(Arrays.asList(2, 1), safe);


        minefield.guess(safe.get(1), safe.get(0), false);
        makeMove(safe.get(1), safe.get(0));
        safe = solver.makeSafeMove();
        assertEquals(Arrays.asList(3, 2), safe);


        minefield.guess(safe.get(1), safe.get(0), false);
        makeMove(safe.get(1), safe.get(0));
        safe = solver.makeSafeMove();
        assertEquals(Arrays.asList(3, 3), safe);

        minefield.guess(safe.get(1), safe.get(0), false);
        makeMove(safe.get(1), safe.get(0));
        safe = solver.makeSafeMove();
        assertEquals(Arrays.asList(2, 3), safe);

        minefield.guess(safe.get(1), safe.get(0), false);
        makeMove(safe.get(1), safe.get(0));
        safe = solver.makeSafeMove();
        assertEquals(Arrays.asList(3, 0), safe);
        minefield.guess(safe.get(1), safe.get(0), false);
        makeMove(safe.get(1), safe.get(0));
        safe = solver.makeSafeMove();
        assertEquals(Arrays.asList(3, 1), safe);
    }

    @Test
    public void twoMines1() {
        rc = 4;
        ArrayList<List<Integer>> closedCells = new ArrayList<>();
        closedCells.add(Arrays.asList(2, 0));
        closedCells.add(Arrays.asList(0, 2));
        flags = 2;
        setUp(closedCells);
        minefield.revealStart(0, 0);
        minefield.printMinefield();
        minefield.printDebugMinefield();
//        while(true){}
        for (int i = 0; i < rc; i++) {
            for (int j = 0; j < rc; j++) {
                if (minefield.getCell(minefield.XYto1D(i, j)).getRevealed()) {
                    makeMove(i, j);
                }
            }
        }
//        field.printDebugMinefield();

        List<Integer> safe = solver.makeSafeMove();
        assertEquals(Arrays.asList(2, 2), safe);
        minefield.guess(safe.get(1), safe.get(0), false);
        makeMove(safe.get(0), safe.get(1));
        safe = solver.makeSafeMove();
        assertEquals(Arrays.asList(2, 1), safe);
        minefield.guess(safe.get(1), safe.get(0), false);

    }

    @Test
    public void test1() {
        ArrayList<List<Integer>> closedCells = new ArrayList<>();
        closedCells.add(Arrays.asList(0, 2));
        closedCells.add(Arrays.asList(1, 2));
        closedCells.add(Arrays.asList(2, 2));
        closedCells.add(Arrays.asList(2, 1));
        closedCells.add(Arrays.asList(2, 0));
        closedCells.add(Arrays.asList(3, 2));
        closedCells.add(Arrays.asList(2, 3));
        closedCells.add(Arrays.asList(3, 3));
        closedCells.add(Arrays.asList(4, 1));
        rc = 5;
        flags = 9;
        setUp(closedCells);
        minefield.revealStart(0, 0);
        minefield.printMinefield();
        minefield.printDebugMinefield();
//        while(true){}
        for (int i = 0; i < rc; i++) {
            for (int j = 0; j < rc; j++) {
                if (minefield.getCell(minefield.XYto1D(i, j)).getRevealed()) {
                    makeMove(i, j);
                }
            }
        }
//        field.printDebugMinefield();

        List<Integer> safe = solver.makeSafeMove();
        assertNull(safe);

        safe = solver.makeRandomMove();
        minefield.guess(safe.get(0), safe.get(1), false);
        makeMove(safe.get(1), safe.get(0));
        assertEquals(Arrays.asList(0, 3), safe);
        minefield.printMinefield();
        safe = solver.makeSafeMove();
        assertNull(safe);
//        minefield.guess(safe.get(1), safe.get(0), false);
//        makeMove(safe.get(0), safe.get(1));
//        safe = solver.makeSafeMove();
//        assertNull(safe);

    }

    @Test
    public void test2() {
        ArrayList<List<Integer>> closedCells = new ArrayList<>();
        closedCells.add(Arrays.asList(0, 2));
        closedCells.add(Arrays.asList(2, 2));
        closedCells.add(Arrays.asList(2, 1));
        closedCells.add(Arrays.asList(3, 1));
        closedCells.add(Arrays.asList(4, 1));
        closedCells.add(Arrays.asList(4, 0));
        closedCells.add(Arrays.asList(2, 3));
        closedCells.add(Arrays.asList(1, 3));
        closedCells.add(Arrays.asList(0, 3));
        closedCells.add(Arrays.asList(0, 2));
        closedCells.add(Arrays.asList(0, 4));
        closedCells.add(Arrays.asList(2, 4));
        rc = 5;
        flags = 12;
        setUp(closedCells);
        minefield.revealStart(0, 0);
        minefield.printMinefield();
        minefield.printDebugMinefield();
//        while(true){}
        for (int i = 0; i < rc; i++) {
            for (int j = 0; j < rc; j++) {
                if (minefield.getCell(minefield.XYto1D(i, j)).getRevealed()) {
                    makeMove(i, j);
                }
            }
        }
//        field.printDebugMinefield();

        List<Integer> safe = solver.makeSafeMove();
        assertNull(safe);
        safe = solver.makeRandomMove();
        HashSet<List<Integer>> cells = new HashSet<>();
        cells.add(Arrays.asList(2, 2));
        assertEquals(cells, solver.knownMines());
        minefield.guess(safe.get(1), safe.get(0), false);
        makeMove(safe.get(1), safe.get(0));
        safe = solver.makeSafeMove();
        System.out.println();
        minefield.printMinefield();
        assertNull(safe);
        safe = solver.makeRandomMove();
        assertEquals(Arrays.asList(0, 3), safe);
        minefield.guess(safe.get(1), safe.get(0), false);
        makeMove(safe.get(1), safe.get(0));
//        minefield.guess(safe.get(1), safe.get(0), false);
//        makeMove(safe.get(0), safe.get(1));
//        safe = solver.makeSafeMove();
//        assertNull(safe);

    }
    @Test
    public void test3() {
        ArrayList<List<Integer>> closedCells = new ArrayList<>();
        closedCells.add(Arrays.asList(1, 6));
        closedCells.add(Arrays.asList(2, 6));
        closedCells.add(Arrays.asList(3, 6));
        closedCells.add(Arrays.asList(1, 7));
        closedCells.add(Arrays.asList(2, 7));
        closedCells.add(Arrays.asList(2, 4));
        closedCells.add(Arrays.asList(3, 4));
        closedCells.add(Arrays.asList(4, 2));
        closedCells.add(Arrays.asList(4, 0));
        closedCells.add(Arrays.asList(4, 7));
        closedCells.add(Arrays.asList(4, 8));
        closedCells.add(Arrays.asList(7, 4));
        closedCells.add(Arrays.asList(8, 4));
        closedCells.add(Arrays.asList(9, 4));
        closedCells.add(Arrays.asList(7, 7));
        closedCells.add(Arrays.asList(8, 8));
        closedCells.add(Arrays.asList(9, 8));
        closedCells.add(Arrays.asList(8, 9));
        rc = 10;
        flags = 18;
        setUp(closedCells);
        minefield.revealStart(0, 0);
        minefield.printMinefield();
        minefield.printDebugMinefield();
//        while(true){}
        for (int i = 0; i < rc; i++) {
            for (int j = 0; j < rc; j++) {
                if (minefield.getCell(minefield.XYto1D(i, j)).getRevealed()) {
                    makeMove(i, j);
                }
            }
        }
//        field.printDebugMinefield();

        List<Integer> safe = solver.makeSafeMove();
        assertEquals(Arrays.asList(5,2),safe);
        minefield.guess(safe.get(1), safe.get(0), false);
        makeMove(safe.get(1), safe.get(0));
        safe = solver.makeSafeMove();
        assertNull(safe);
        safe = solver.makeRandomMove();
        assertEquals(Arrays.asList(0, 3), safe);
        minefield.printMinefield();
        assertTrue(minefield.guess(safe.get(1), safe.get(0), false));
        makeMove(safe.get(1), safe.get(0));

        System.out.println();
        minefield.printMinefield();
//        minefield.guess(safe.get(1), safe.get(0), false);
//        makeMove(safe.get(0), safe.get(1));
//        safe = solver.makeSafeMove();
//        assertNull(safe);

    }
    @Test
    public void test4() {
        ArrayList<List<Integer>> closedCells = new ArrayList<>();
        closedCells.add(Arrays.asList(1, 2));
        closedCells.add(Arrays.asList(2, 2));
        closedCells.add(Arrays.asList(0, 2));
        closedCells.add(Arrays.asList(3, 0));
        rc = 14;
        flags = 4;
        setUp(closedCells);
        minefield.revealStart(0, 0);
//        minefield.printDebugMinefield();
//        while(true){}
        for (int i = 0; i < rc; i++) {
            for (int j = 0; j < rc; j++) {
                if (minefield.getCell(minefield.XYto1D(i, j)).getRevealed()) {
                    makeMove(i, j);
                }
            }
        }
//        field.printDebugMinefield();

        List<Integer> safe = solver.makeSafeMove();
        assertEquals(Arrays.asList(2,3),safe);
        minefield.guess(safe.get(1), safe.get(0), false);
        makeMove(safe.get(1), safe.get(0));
        safe = solver.makeSafeMove();

//        minefield.printMinefield();
        assertEquals(Arrays.asList(3,2),safe);
//        minefield.printMinefield();
        minefield.guess(safe.get(1), safe.get(0), false);
        makeMove(safe.get(1), safe.get(0));

        safe = solver.makeSafeMove();


        minefield.printMinefield();
        assertEquals(Arrays.asList(4,3),safe);
//        minefield.guess(safe.get(1), safe.get(0), false);
//        makeMove(safe.get(0), safe.get(1));
//        safe = solver.makeSafeMove();
//        assertNull(safe);

    }

}
