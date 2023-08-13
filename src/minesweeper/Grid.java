package minesweeper;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import java.awt.Color;
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
    private static final List<String> imageNamesList = Arrays.asList("mine", "cell", "cellopened", "cellpressed", "marked", "1", "2", "3", "4", "5", "6", "7", "8");
    private static final List<String> buttonsImages = Arrays.asList("smiley", "marking", "shovel");
    private static final HashMap<String, ImageIcon> images = new HashMap<>();
    //this one is for the clearCells method.
    private static final int[][] directions = {{1,0}, {0,1}, {-1,0}, {0,-1}};
    
    private static Grid instance = null;
	
    private final int MAXLIN, MAXCOL, MINES;
    private final Difficulties dif;
	
    private Cell[][] grid;
    private boolean marking;
	
    /**
     * Constructs a minesweeper grid whose size changes according to the game's
     * difficulty, which is defined by {@code dif}.
     * @param dif the game's difficulty.
     */
    private Grid(Difficulties dif) {
        super();
        super.setBackground(new Color(173, 173, 173));
        super.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createRaisedSoftBevelBorder(), BorderFactory.createLoweredSoftBevelBorder()));
        this.dif = dif;
        this.MAXLIN = dif.numOfCellsX;
        this.MAXCOL = dif.numOfCellsY;
        this.MINES = dif.numOfBombs;
        this.marking = false;
		
        grid = new Cell[MAXLIN][MAXCOL];
        loadImages(dif.cellSize - 2);
        this.setLayout(new GridLayout(MAXLIN, MAXCOL));
        this.reset();
        this.setVisible(true);
    }
    
    /**
     * Just to make sure that there are no more than one current instances
     * of Grid. You may use {@code null} for the Difficulty
     * if you just want the current instance. This method will return null if
     * {@code dif} is null in a situation where there's no current instance of grid.
     * @param dif the game's difficulty.
     * @return the Grid instance.
     */
    public static Grid getInstance(Difficulties dif) {
    	if (instance == null && dif != null) {
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
                grid[i][j] = new Cell(this.dif.cellSize, i, j);
                grid[i][j].setIcon(images.get("cell"));
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
            disableAll(true);
            return;
        }
        this.clearCells(cell);
        if (this.cleared()) {
            System.out.println("You won!");
            disableAll(false);
            JOptionPane.showMessageDialog(null, "You won!", "Congrats", JOptionPane.INFORMATION_MESSAGE);
    	}
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
                if (!validIndex(idx1, idx2)) {
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
            //we're probably not going to need this one since this loop
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
                c.setIcon(images.get(String.format("%d", numMines)));
                c.disableCell();
                continue;
    		}
            c.disableCell();
            c.setIcon(images.get("cellopened"));
            for (int[] dir : directions) {
                idx1 = c.i + dir[0];
                idx2 = c.j + dir[1];
                if (!validIndex(idx1, idx2)) {
                    continue;
                }
                cells.offer(grid[idx1][idx2]);
            }
        }
    }
    
    /**
     * Auxiliary method. Checks if an index is not valid or if it is.
     * @param i an index.
     * @param j another index.
     * @return {@code true} if the inded {@code ij} is invalid. 
     */
    private boolean validIndex(int i, int j) {
    	return (i >= 0 && i < MAXLIN) && (j >= 0 && j < MAXCOL);
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
     * Only used when the game is over. This method just does a
     * soft disable on all the buttons. It will also reveal all the 
     * bombs if the flag {@code showBombs} is set to {@code true}.
     * 
     * @param showBombs the boolean flag that tells if this method 
     * should show all the bombs or not.
     * 
     */
    private void disableAll(boolean showBombs) {
        for (int i = 0; i < MAXLIN; i++) {
            for (int j = 0; j < MAXCOL; j++) {
                Cell c = grid[i][j];
                if (c.isBomb() && showBombs) {
                    c.setIcon(images.get("mine"));
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
    
    //This methos is not static because it deppends on a non-static
    //private attribute (the difficulty) of an instance of Grid for properly resizing the images.
    /**
     * Loads all the necessary images and saves them in a HashMap for
     * ease of access. An image may be accessed by its filename.
     * 
     * @param dif the difficulty. used to scale the images to the correct size.
     */
    private static void loadImages(int scale) {
    	for (String s : imageNamesList) {
            java.net.URL imgurl = App.class.getResource(String.format("images/%s.png", s));
            if (imgurl == null) {
                System.out.printf("It was not possible to load the image %s\n", s);
                JOptionPane.showMessageDialog(null, String.format("It was not possible to load the image %s", s), "Failed to load image", JOptionPane.ERROR_MESSAGE);
                System.exit(2);
            }
            ImageIcon img = new ImageIcon(imgurl);
            images.put(s, new ImageIcon(img.getImage().getScaledInstance(scale, scale, Image.SCALE_SMOOTH)));
    	}
    	
    	int nscale = Difficulties.BEGINNER.cellSize - 2;
    	for (String s : buttonsImages) {
    		java.net.URL imgurl = App.class.getResource(String.format("images/%s.png", s));
            if (imgurl == null) {
                System.out.printf("It was not possible to load the image %s\n", s);
                JOptionPane.showMessageDialog(null, String.format("It was not possible to load the image %s", s), "Failed to load image", JOptionPane.ERROR_MESSAGE);
                System.exit(2);
            }
            ImageIcon img = new ImageIcon(imgurl);
            images.put(s, new ImageIcon(img.getImage().getScaledInstance(nscale, nscale, Image.SCALE_SMOOTH)));
    	}
    }
    
    /**
     * Returns the ImageIcon image associated with the string {@code image}.
     * @param image the image name.
     * @return an image or null if there's no image associated with the string.
     */
    public static ImageIcon getImage(String image) {
    	return images.get(image);
    }
}
