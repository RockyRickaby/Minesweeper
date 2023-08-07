package minesweeper;

public enum Difficulties {
    EASY(9, 10, 40),
    INTERMEDIATE(16, 40, 35),
    ADVANCED(24, 99, 26);
	
    public final int numOfCells, numOfBombs, cellSize;

    Difficulties(int cells, int bombs, int cellSize) {
        this.numOfCells = cells;
        this.numOfBombs = bombs;
        this.cellSize = cellSize;
    }
}
