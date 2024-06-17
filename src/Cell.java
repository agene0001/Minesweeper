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

    //"M"
    public Cell(boolean revealed, String symbol){
        this.revealed = revealed;
        this.symbol = symbol;
    }

    public String getStatus() {
        return symbol;
    }

    public boolean getRevealed() {
        return revealed;
    }

    public void setRevealed(boolean revealed) {
        this.revealed = revealed;
    }

    public void setStatus(String status) {
        this.symbol = status;
    }
}
