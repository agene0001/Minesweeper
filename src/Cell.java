public class Cell {
    boolean revealed;
    String symbol;
    int nearbyRevealed;
    int nearbyClosed;

    public int getNearbyRevealed() {
        return nearbyRevealed;
    }

    public void setNearbyRevealed(int nearbyRevealed) {
        this.nearbyRevealed = nearbyRevealed;
    }

    public int getNearbyClosed() {
        return nearbyClosed;
    }

    public void setNearbyClosed(int nearbyClosed) {
        this.nearbyClosed = nearbyClosed;
    }

    /**
     * Represents a cell in a minefield.
     *
     * @param revealed Indicates whether the cell is revealed or not.
     * @param symbol   The symbol representing the cell.
     */
    //"M"
    public Cell(boolean revealed, String symbol){
        this.revealed = revealed;
        this.symbol = symbol;
    }

    /**
     * Retrieves the status of the cell.
     *
     * @return The symbol representing the cell's status.
     */
    public String getStatus() {
        return symbol;
    }

    /**
     * Gets the revealed status of the cell.
     *
     * @return true if the cell is revealed, false otherwise.
     */
    public boolean getRevealed() {
        return revealed;
    }

    /**
     * Sets the revealed status of a cell.
     *
     * @param revealed The new revealed status of the cell.
     */
    public void setRevealed(boolean revealed) {
        this.revealed = revealed;
    }

    /**
     * Sets the status of the cell.
     *
     * @param status The new status of the cell.
     */
    public void setStatus(String status) {
        this.symbol = status;
    }
}
