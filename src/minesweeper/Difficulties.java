package minesweeper;

public enum Difficulties {
    BEGINNER(9, 9, 10, 55),
    INTERMEDIATE(16, 16, 40, 50),
    ADVANCED(16, 30, 99, 45);
	
    public final int numOfCellsX, numOfCellsY, numOfBombs, cellSize;

    Difficulties(int cellsX, int cellsY, int bombs, int cellSize) {
        this.numOfCellsX = cellsX;
        this.numOfCellsY = cellsY;
        this.numOfBombs = bombs;
        this.cellSize = cellSize;
    }
}
