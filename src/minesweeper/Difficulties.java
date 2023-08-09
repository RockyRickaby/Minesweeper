package minesweeper;

public enum Difficulties {
    EASY(9, 9, 10, 50),
    INTERMEDIATE(16, 18, 40, 42),
    ADVANCED(16, 30, 99, 32);
	
    public final int numOfCellsX, numOfCellsY, numOfBombs, cellSize;

    Difficulties(int cellsX, int cellsY, int bombs, int cellSize) {
        this.numOfCellsX = cellsX;
        this.numOfCellsY = cellsY;
        this.numOfBombs = bombs;
        this.cellSize = cellSize;
    }
}
