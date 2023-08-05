

import javax.swing.JButton;

class Cell extends JButton {
    private int row;
    private int col;
    private boolean mine;
    private boolean revealed;
    private boolean flagged;
    private int numMines;
    private int whoRevealed;


    public Cell(int row, int col) {
        this.row = row;
        this.col = col;
        mine = false;
        revealed = false;
        flagged = false;
        numMines = 0;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public boolean isMine() {
        return mine;
    }

    public void setMine(boolean mine) {
        this.mine = mine;
    }

    public boolean isRevealed() {
        return revealed;
    }

    public void setRevealed(boolean revealed) {
        this.revealed = revealed;
    }

    public boolean isFlagged() {
        return flagged;
    }

    public void setFlagged(boolean flagged) {
        this.flagged = flagged;
    }

    public int getNumMines() {
        return numMines;
    }

    public void setNumMines(int numMines) {
        this.numMines = numMines;
    }
    public int getWhoRevealed() {
        return whoRevealed;
    }

    public void setWhoRevealed(int whoRevealed) {
        this.whoRevealed = whoRevealed;
    }

    // Resets the cell to its initial state
    public void reset() {
        mine = false;
        revealed = false;
        flagged = false;
        this.setEnabled(true);
        this.setIcon(null);
        this.setBackground(null);
        this.setText(null);
    }

    public void addActionListener(Object addMouseListener) {
    }
}