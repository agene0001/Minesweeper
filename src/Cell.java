public class Cell {
    boolean revealed;
    String symbol;
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
