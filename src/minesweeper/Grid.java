package minesweeper;
import javax.swing.JPanel;
import javax.swing.JButton;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Insets;

import java.util.Random;

public class Grid extends JPanel {
	private final int MAXLIN, MAXCOL, MINES;
	private JButton[][] grid;
	
	public Grid(Difficulties dif) {
		super();
		
		this.MAXLIN = this.MAXCOL = dif.numOfCells;
		this.MINES = dif.numOfBombs;
		grid = new JButton[MAXLIN][MAXCOL];
		this.setLayout(new GridLayout(MAXLIN, MAXCOL));
		for (int i = 0; i < MAXLIN; i++) {
			for (int j = 0; j < MAXCOL; j++) {
				grid[i][j] = new JButton();
				//grid[i][j].setMargin(new Insets(20,20,20,20));
				grid[i][j].setPreferredSize(new Dimension(37,37));
				this.add(grid[i][j]);
			}
		}
		this.setVisible(true);
		this.setMines();
	}
	
	private void setMines() {
		for (int i = 0; i < this.MINES; i++) {
			Random r = new Random();
			int x = r.nextInt(MAXLIN);
			int y = r.nextInt(MAXCOL);
			grid[x][y] = new JButton("B");
		}
		this.updateScreen();
	}
	
	public void updateScreen() {
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
