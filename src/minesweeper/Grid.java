package minesweeper;
import javax.swing.JPanel;

import java.awt.GridLayout;

import java.util.Random;

@SuppressWarnings("serial")
public class Grid extends JPanel {
    private final int MAXLIN, MAXCOL, MINES;
    private final Difficulties dif;
	
    private Cell[][] grid;
    private boolean marking;
	
    /**
     * Constructs a minesweeper grid whose size changes according to the game's
     * difficulty, which is defined by {@code dif}.
     * @param dif the game's difficulty.
     */
    public Grid(Difficulties dif) {
        super();
        this.dif = dif;
        this.MAXLIN  = dif.numOfCells;
        this.MAXCOL = dif.numOfCells;
        this.MINES = dif.numOfBombs;
        this.marking = false;
		
        grid = new Cell[MAXLIN][MAXCOL];
        this.setLayout(new GridLayout(MAXLIN, MAXCOL));
        for (int i = 0; i < MAXLIN; i++) {
            for (int j = 0; j < MAXCOL; j++) {
                grid[i][j] = new Cell(dif.cellSize, this, i, j);
            }
        }
        this.setVisible(true);
        this.setMines();
        this.updateScreen();
    }
	
    /**
     * Resets this grid, making it possible to play the game again.
     */
    public void reset() {
        this.marking = false;
        for (int i = 0; i < MAXLIN; i++) {
            for (int j = 0; j < MAXCOL; j++) {
                grid[i][j] = new Cell(this.dif.cellSize, this, i, j);
            }
        }
        this.setMines();
        this.updateScreen();
    }
	
    /**
     * This one just exists for the game to work. 
     * @param cell the cell from which a click was received.
     */
    public void action(Cell cell) {
        if (cell.isMarked() || cell.isDisabled()) {
            System.out.println("oi");
            return;
        }
		if (cell.isBomb()) {
            disableAll();
            return;
		}
        //TODO - ajustar isso aqui
        int c = this.countAdjacentMines(cell.getI(), cell.getJ());
        System.out.println(c);
	}
	
    /**
     * Counts and returns the amount of mines around a given cell.
     * @param i the I index of the cell.
     * @param j the J index of the cell.
     * @return the amount of mines around the cell.
     */
    private int countAdjacentMines(int i, int j) {			
        int count = 0;
        i -= 1;
        j -= 1;
        for (int c1 = 0; c1 < 3; c1++) {
            for (int c2 = 0; c2 < 3; c2++) {
                int idx1 = i + c1;
                int idx2 = j + c2;
                if (idx1 < 0 || idx1 >= MAXLIN || idx2 < 0 || idx2 >= MAXCOL) {
					continue;
                }
                if (grid[idx1][idx2].isBomb()) {
					count += 1;
                }
            }
        }
        return count;
    }

	
    /**
     * Toggles mark mode on and off.
     */
    public void toggleMarkMode() {
        this.marking = !this.marking;
    }
	
    /**
     * Checks if this grid is in "mark mode" or not.
     * @return {@code true} if it is.
     */
    public boolean inMarkMode() {
        return this.marking;
    }
	
    /**
     * Only used when the game is over. This method just
     * "disables" all the buttons and reveals all the bombs.
     * 
     */
    private void disableAll() {
        for (int i = 0; i < MAXLIN; i++) {
            for (int j = 0; j < MAXCOL; j++) {
                Cell c = grid[i][j];
                if (c.isBomb()) {
                    c.setText("B");
                }
                c.disable();
			}
		}
	}
	
    /**
     * Sets some of the grid's cells as mines. The amount of mines change
     * according to the game's difficulty.
     */
    private void setMines() {
        for (int i = 0; i < this.MINES; i++) {
            Random r = new Random();
            int x, y;
            do {
                x = r.nextInt(MAXLIN);
                y = r.nextInt(MAXCOL);
            } while(grid[x][y].isBomb());
            grid[x][y].makeBomb();
        }
    }
	
    /**
     * Updates the screen when big changes are made to the grid like 
     * resetting it.
     */
    private void updateScreen() {
        this.removeAll();
        this.revalidate();
        this.repaint();
        for (int i = 0; i < MAXLIN; i++) {
            for (int j = 0; j < MAXCOL; j++) {
                this.add(grid[i][j]);
            }
        }
    }
}
