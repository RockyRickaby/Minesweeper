package minesweeper;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;

@SuppressWarnings("serial")
public class Cell extends JButton implements ActionListener {
    private boolean isMine, marked, disabled;
    public final int i, j;
	
    /**
     * Constructs a cell that is not a mine.
     * @param cellSize the size of the cell, dictated by the difficulty of the game.
     * @param grid the grid from which this cell is part of.
     * @param i the I index of the cell.
     * @param j the J index of the cell.
     */
    public Cell(int cellSize, int i, int j) {
        super();
        this.i = i;
        this.j = j;
        this.isMine = this.marked = this.disabled = false;
        super.setPreferredSize(new Dimension(cellSize, cellSize));
        super.addActionListener(this);
        this.addMouseListener(new MouseAdapter() {
            @Override
	        public void mousePressed(MouseEvent e) {
        		if (disabled || marked) {
	        		return;
		        }
    		    if (Grid.getInstance(null).inMarkMode()) {
	    		    return;
    		    }
        		setIcon(Grid.getImage("cellpressed"));
	        }

        	@Override
	        public void mouseReleased(MouseEvent e) {
		        if (disabled || marked) {
			        return;
        		}
	        	setIcon(Grid.getImage("cell"));
    	    }
        });
    }
	
    /**
     * Checks if this Cell is a bomb or not.
     * @return {@code true} if it is.
     */
    public boolean isMine() {
        return this.isMine;
    }

    /**
     * Turns this cell into a mine. To avoid creating unnecessary
     * instances of cells. Better reuse the already existing ones
     * while and if we can!
     * <p>A mine cell will always be a mine until the end of the game.
     */
    public void makeMine() {
        this.isMine = true;
    }
    
    /**
     * Toggle method. Marks this cell as a possible mine. The player will
     * not be able to interact much with this cell while it is marked.
     */
    public void mark() { //hello, everybody, my name is Markiplier
    	if (this.marked) {
            //System.out.printf("Unmarked the cell at [%d, %d]\n", this.i, this.j);
            super.setIcon(Grid.getImage("cell"));
    	} else {
            //System.out.printf("Marked the cell at [%d, %d]\n", this.i, this.j);
            super.setIcon(Grid.getImage("marked"));
    	}
    	this.marked = !this.marked;
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
     * Disables this cell. This method should only be used when the player touches the cell.
     * <p>It is not possible to enable a cell after disabling it without resetting
     * the entire grid.
     */
    public void disableCell() {
        this.disabled = true;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
    	Grid grid = Grid.getInstance(null);
        if (grid.inMarkMode() && !this.disabled) {
            this.mark();
        } else {
            grid.action(this);
        }
    }
}
