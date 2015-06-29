package com.example.burakkara.gameoflife;

/**
 * Author: karab on 27/06/15.
 */
public class CalculationThread extends Thread {

    private int[][] cells;
    private int[][] temp;

    private int row;
    private int column;

    private int blockRowIndex;
    private int blockColumnIndex;

    private boolean[][] blockState;

    private static ThreadManager manager;

    public CalculationThread(int blockRowIndex, int blockColumnIndex, int[][] cells,
                             int[][] temp, boolean[][] blockState) {
        this.blockRowIndex = blockRowIndex;
        this.blockColumnIndex = blockRowIndex;
        this.row = blockRowIndex * GameView.BLOCK_EDGE_LENGTH;
        this.column = blockColumnIndex * GameView.BLOCK_EDGE_LENGTH;
        this.cells = cells;
        this.temp = temp;
        this.blockState = blockState;
    }

    @Override
    public void run() {

        blockState[blockRowIndex][blockColumnIndex] = false;

        for (int i = row; i < row + GameView.BLOCK_EDGE_LENGTH; i++) {
            for (int j = column; j < column + GameView.BLOCK_EDGE_LENGTH; j++) {

                int sum = getSumOfBlock(i, j);
                if (sum == 3) {
                    if (cells[i][j] == 0) {
                        temp[i][j] = 1;
                        blockState[blockRowIndex][blockColumnIndex] = true;
                    }/*else already alive*/
                } else if (sum == 4) {
                    //keep state
                } else {
                    if (cells[i][j] == 1) {
                        temp[i][j] = 0;
                        blockState[blockRowIndex][blockColumnIndex] = true;
                    }/*else already dead*/

                }
            }
        }
    }

    /**
     * Gets sum of all the cells in a block.
     *
     * @param rowIndex    Row index of central cell.
     * @param columnIndex Column index of central cell.
     * @return Sum of block.
     */
    public int getSumOfBlock(int rowIndex, int columnIndex) {

        int left = columnIndex - 1;
        int right = columnIndex + 1;
        int up = rowIndex - 1;
        int down = rowIndex + 1;

        boolean isCellOnLeftEdge = columnIndex == 0;
        boolean isCellOnRightEdge = columnIndex == GameView.GRID_EDGE_LENGTH - 1;
        boolean isCellOnCeilEdge = rowIndex == 0;
        boolean isCellOnBottomEdge = rowIndex == GameView.GRID_EDGE_LENGTH - 1;

        if (isCellOnLeftEdge && isCellOnCeilEdge) {
            return cells[rowIndex][columnIndex] + cells[rowIndex][right]//middle row
                    + cells[down][(columnIndex)] + cells[(down)][(right)];//bottom row

        } else if (isCellOnLeftEdge && isCellOnBottomEdge) {
            return cells[rowIndex][columnIndex] + cells[rowIndex][right]//middle row
                    + cells[up][rowIndex] + cells[(up)][(right)];//upper row

        } else if (isCellOnRightEdge && isCellOnCeilEdge) {
            return cells[(rowIndex)][left] + cells[rowIndex][columnIndex] //middle row
                    + cells[(down)][(left)] + cells[down][(columnIndex)];//bottom row

        } else if (isCellOnRightEdge && isCellOnBottomEdge) {
            return cells[(rowIndex)][left] + cells[rowIndex][columnIndex] //middle row
                    + cells[(up)][(left)] + cells[up][rowIndex];//upper row

        } else if (isCellOnLeftEdge) {
            return cells[rowIndex][columnIndex] + cells[rowIndex][right]//middle row
                    + cells[up][columnIndex] + cells[(up)][(right)]//upper row
                    + cells[down][(columnIndex)] + cells[(down)][(right)];//bottom row
        } else if (isCellOnRightEdge) {
            return cells[(rowIndex)][left] + cells[rowIndex][columnIndex] //middle row
                    + cells[(up)][(left)] + cells[up][rowIndex]//upper row
                    + cells[(down)][(left)] + cells[down][(columnIndex)];//bottom row
        } else if (isCellOnCeilEdge) {
            return cells[(rowIndex)][left] + cells[rowIndex][columnIndex] + cells[rowIndex][right]//middle row
                    + cells[(down)][(left)] + cells[down][(columnIndex)] + cells[(down)][(right)];//bottom row
        } else if (isCellOnBottomEdge) {
            return cells[(rowIndex)][left] + cells[rowIndex][columnIndex] + cells[rowIndex][right]//middle row
                    + cells[(up)][(left)] + cells[up][rowIndex] + cells[(up)][(right)];//upper row

        } else {
            return cells[(rowIndex)][left] + cells[rowIndex][columnIndex] + cells[rowIndex][right]//middle row
                    + cells[(up)][(left)] + cells[up][columnIndex] + cells[(up)][(right)]//upper row
                    + cells[(down)][(left)] + cells[down][(columnIndex)] + cells[(down)][(right)];//bottom row

        }
    }
}
