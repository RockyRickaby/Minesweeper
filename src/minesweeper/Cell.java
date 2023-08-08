package minesweeper;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;

@SuppressWarnings("serial")
public class Cell extends JButton implements ActionListener {
    private boolean isBomb, marked, disabled;
    private int i, j;
    private String label;
    private Grid grid;
	
    /**
     * Constructs a cell that is not a bomb.
     * @param cellSize the size of the cell, dictated by the difficulty of the game.
     * @param grid the grid from which this cell is part of.
     * @param i the I index of the cell.
     * @param j the J index of the cell.
     */
    public Cell(int cellSize, Grid grid, int i, int j) {
        super();
        this.i = i;
        this.j = j;
        this.grid = grid;
        this.label = "";
        this.isBomb = this.marked = this.disabled = false;
        super.setPreferredSize(new Dimension(cellSize,cellSize));
        super.addActionListener(this);
    }    
	
    /**
     * Sets this Cell's label to be the number of adjacent bombs. 
     * @param num the number of adjacent bombs.
     */
    public void setNumber(int num) {
        super.setText(String.valueOf(num));
    }
	
    /**
     * Checks if this Cell is a bomb or not.
     * @return {@code true} if it is.
     */
    public boolean isBomb() {
        return this.isBomb;
    }

    /**
     * Turns this cell into a bomb. To avoid creating unnecessary
     * instances of cells..
     */
    public void makeBomb() {
        this.isBomb = true;
    }
	
    /**
     * Checks if this cell is marked or not.
     * @return {@code true} if it is.
     */
    public boolean isMarked() {
        return this.marked;
    }
	
    /**
     * Checks if this cell is disabled or not.
     * @return {@code true} if it is.
     */
    public boolean isDisabled() {
        return this.disabled;
    }
	
    /**
     * Disables this cell. This method should only be used when the player touches a bomb
     * or if this cell has at least one adjacent mine.
     * <p>It is not possible to enable a cell after disabling it without resetting
     * the entire grid.
     */
    public void disable() {
        this.disabled = true;
	}
	
    public int getI() {
        return this.i;
    }
	
    public int getJ() {
        return this.j;
    }
	

    @Override
    public void actionPerformed(ActionEvent e) {
        if (this.grid.inMarkMode() && !this.disabled) {
            this.marked = !this.marked;
        } else {
            this.grid.action(this);
        }
    }
}
