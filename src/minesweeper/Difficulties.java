package minesweeper;

public enum Difficulties {
    BEGINNER(9, 9, 10, 50),
    INTERMEDIATE(16, 16, 40, 40),
    ADVANCED(16, 30, 99, 36);
	
    public final int numOfCellsX, numOfCellsY, numOfBombs, cellSize;

    Difficulties(int cellsX, int cellsY, int bombs, int cellSize) {
        this.numOfCellsX = cellsX;
        this.numOfCellsY = cellsY;
        this.numOfBombs = bombs;
        this.cellSize = cellSize;
    }
}
