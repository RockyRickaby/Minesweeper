package minesweeper;

public enum Difficulties {
	EASY(9, 10),
	INTERMEDIATE(16, 40),
	ADVANCED(24, 99);
	
	public final int numOfCells, numOfBombs;
	
	Difficulties(int cells, int bombs) {
		this.numOfCells = cells;
		this.numOfBombs = bombs;
	}
}
