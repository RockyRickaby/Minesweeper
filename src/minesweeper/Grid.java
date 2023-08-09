package minesweeper;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import java.awt.GridLayout;
import java.awt.Image;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

@SuppressWarnings("serial")
public class Grid extends JPanel {
	private static final List<String> imageNamesList = Arrays.asList("mine.png", "cell.png", "cellopened.png", "marked.png", "1.png", "2.png", "3.png", "4.png", "5.png", "6.png", "7.png", "8.png");
	private static final HashMap<String, ImageIcon> images = new HashMap<>();
	
	private static Grid instance = null;
	
    private final int MAXLIN, MAXCOL, MINES;
    private final Difficulties dif;
	
    private Cell[][] grid;
    private boolean marking;
    
    //this one is for the clearCells method.
    private static final int[][] directions = {{1,0},
    										   {0,1},
    										   {-1,0},
    										   {0,-1}};
	
    /**
     * Constructs a minesweeper grid whose size changes according to the game's
     * difficulty, which is defined by {@code dif}.
     * @param dif the game's difficulty.
     */
    private Grid(Difficulties dif) {
        super();
        this.dif = dif;
        this.MAXLIN  = dif.numOfCells;
        this.MAXCOL =  (int) (dif.numOfCells * 1.5);
        this.MINES = dif.numOfBombs;
        this.marking = false;
		
        grid = new Cell[MAXLIN][MAXCOL];
        this.loadImages();
        this.setLayout(new GridLayout(MAXLIN, MAXCOL));
        
        for (int i = 0; i < MAXLIN; i++) {
            for (int j = 0; j < MAXCOL; j++) {
                grid[i][j] = new Cell(dif.cellSize, this, i, j);
                grid[i][j].setIcon(images.get("cell.png"));
            }
        }
        this.setVisible(true);
        this.setMines();
        this.updateScreen();
    }
    
    /**
     * Just to make sure that there are no more than one current instances
     * of Grid.
     * @param dif the game's difficulty.
     * @return the Grid instance.
     */
    public static Grid getInstance(Difficulties dif) {
    	if (instance == null) {
    		instance = new Grid(dif);
    	}
    	return instance;
    }	
    /**
     * Resets this grid, making it possible to play the game again.
     */
    public void reset() {
        this.marking = false;
        for (int i = 0; i < MAXLIN; i++) {
            for (int j = 0; j < MAXCOL; j++) {
                grid[i][j] = new Cell(this.dif.cellSize, this, i, j);
                grid[i][j].setIcon(images.get("cell.png"));
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
            System.out.println("The cell is disabled/marked!");
            return;
        }
		if (cell.isBomb()) {
			System.out.println("Game over!");
            disableAll();
            return;
		}
		clearCells(cell);
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
                if (invalidIndex(idx1, idx2)) {
                    continue;
                }
                if (grid[idx1][idx2].isBomb()) {
					count += 1;
                }
            }
        }
        return count;
    }
    
    //maze-solving algorithms saving the day once more.
    /**
     * Opens this grid's cells starting at {@code cell}.
     * Every cell with no adjacent mines that are adjacent to {@code cell}
     * will be opened. This is also valid for the adjacent cells. If the cell
     * has at least one adjacent mine, only that cell will be opened.
     * @param cell the cell from which we start clearing the grid.
     */
    private void clearCells(Cell cell) {
    	Queue<Cell> cells = new LinkedList<>();
    	boolean[][] seen = new boolean[MAXLIN][MAXCOL];
    	cells.offer(cell);
    	
    	//walks through the grid in such a way that it looks like it flows through it like water.
    	while (!cells.isEmpty()) {
            Cell c = cells.poll();
            int idx1 = c.i, idx2 = c.j;
            if (seen[idx1][idx2]) {
                continue;
            }
            seen[idx1][idx2] = true;
            //we'll probably not need this one since this loop
            //should stop before reaching a bomb thanks to the if statement after the next,
            //the one that checks the number of adjacent mines.
            //if (c.isBomb()) { 
            //	continue;       
            //}
            if (c.isMarked() || c.isDisabled()) {
            	continue;
            }
            int numMines = countAdjacentMines(idx1, idx2);
            if (numMines > 0) {
                //c.setNumber(numMines);
                System.out.printf("Number of adjacent mines in [%d, %d]: %d\n", idx1, idx2, numMines);
                c.setIcon(images.get(String.format("%d.png", numMines)));
                c.disableCell();
                continue;
    		}
            c.disableCell();
            c.setIcon(images.get("cellopened.png"));
            for (int[] dir : directions) {
                idx1 = c.i + dir[0];
                idx2 = c.j + dir[1];
                if (invalidIndex(idx1, idx2)) {
                    continue;
                }
                cells.offer(grid[idx1][idx2]);
            }
        }
    	if (cleared()) {
    		System.out.println("You won!");
    		this.disableAll();
    		JOptionPane.showMessageDialog(null, "You won!", "Congrats", JOptionPane.INFORMATION_MESSAGE);
    	}
    }
    
    /**
     * Auxiliary method. Checks if an index is not valid or if it is.
     * @param i an index.
     * @param j another index.
     * @return {@code true} if the inded {@code ij} is invalid. 
     */
    private boolean invalidIndex(int i, int j) {
    	return (i < 0 || i >= MAXLIN || j < 0 || j >= MAXCOL);
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
                    c.setIcon(images.get("mine.png"));
                }
                c.disableCell();
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
            //grid[x][y].setText("v");
        }
    }
    
    private boolean cleared() {
    	int count = 0;
    	for (Cell[] cc : grid) {
    		for (Cell c : cc) {
    			if (c.isBomb()) {
    				continue;
    			}
    			if (!c.isEnabled() || c.isDisabled()) {
    				count++;
    			}
    		}
    	}
    	return count == (MAXLIN * MAXCOL - dif.numOfBombs);
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
    
    /**
     * Loads all the necessary images and saves them in a HashMap for
     * ease of access. An image may be accessed by its filename plus the
     * file extension. 
     */
    private void loadImages() {
    	for (String s : imageNamesList) {
    		java.net.URL imgurl = App.class.getResource("images/" + s);
    		if (imgurl == null) {
    			System.out.printf("It was not possible to load the image %s\n", s);
    			JOptionPane.showMessageDialog(null, String.format("It was not possible to load the image %s", s), "Failed to load image", JOptionPane.ERROR_MESSAGE);
    			System.exit(2);
    		}
    		ImageIcon img = new ImageIcon(imgurl);
    		images.put(s, new ImageIcon(img.getImage().getScaledInstance(dif.cellSize - 3, dif.cellSize - 3, Image.SCALE_SMOOTH)));
    	}
    }
    
    /**
     * Returns the ImageIcon image associated with the string {@code image}.
     * @param image the image name.
     * @return an image or null if there's no image associated with the string.
     */
    public ImageIcon getImage(String image) {
    	return this.images.get(image);
    }
}
