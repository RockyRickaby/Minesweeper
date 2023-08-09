package minesweeper;

public enum Difficulties {
    EASY(9, 10, 50),
    INTERMEDIATE(16, 40, 42),
    ADVANCED(22, 99, 32);
	
    public final int numOfCells, numOfBombs, cellSize;

    Difficulties(int cells, int bombs, int cellSize) {
        this.numOfCells = cells;
        this.numOfBombs = bombs;
        this.cellSize = cellSize;
    }
}
