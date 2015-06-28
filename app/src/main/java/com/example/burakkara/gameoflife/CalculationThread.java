package com.example.burakkara.gameoflife;

/**
 * Author: karab on 27/06/15.
 */
public class CalculationThread extends Thread {

    private int[][] cells = new int[GameView.GRID_EDGE_LENGTH][GameView.GRID_EDGE_LENGTH];
    private int[][] temp = new int[GameView.GRID_EDGE_LENGTH][GameView.GRID_EDGE_LENGTH];

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

                int sum = getSumOfSurroundingCells(i, j);
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

    public int getSumOfSurroundingCells(int i, int j) {

        int left = j - 1;
        int right = j + 1;
        int up = i - 1;
        int down = i + 1;

        boolean leftEdge = j == 0;
        boolean rightEdge = j == GameView.GRID_EDGE_LENGTH - 1;
        boolean ceilEdge = i == 0;
        boolean bottomEdge = i == GameView.GRID_EDGE_LENGTH - 1;

        if (leftEdge && ceilEdge) {
            return cells[i][j] + cells[i][right]//middle row
                    + cells[down][(j)] + cells[(down)][(right)];//bottom row

        } else if (leftEdge && bottomEdge) {
            return cells[i][j] + cells[i][right]//middle row
                    + cells[up][i] + cells[(up)][(right)];//upper row

        } else if (rightEdge && ceilEdge) {
            return cells[(i)][left] + cells[i][j] //middle row
                    + cells[(down)][(left)] + cells[down][(j)];//bottom row

        } else if (rightEdge && bottomEdge) {
            return cells[(i)][left] + cells[i][j] //middle row
                    + cells[(up)][(left)] + cells[up][i];//upper row

        } else if (leftEdge) {//be careful
            return cells[i][j] + cells[i][right]//middle row
                    + cells[up][j] + cells[(up)][(right)]//upper row
                    + cells[down][(j)] + cells[(down)][(right)];//bottom row

        } else if (rightEdge) {
            return cells[(i)][left] + cells[i][j] //middle row
                    + cells[(up)][(left)] + cells[up][i]//upper row
                    + cells[(down)][(left)] + cells[down][(j)];//bottom row

        } else if (ceilEdge) {
            return cells[(i)][left] + cells[i][j] + cells[i][right]//middle row
                    + cells[(down)][(left)] + cells[down][(j)] + cells[(down)][(right)];//bottom row
        } else if (bottomEdge) {
            return cells[(i)][left] + cells[i][j] + cells[i][right]//middle row
                    + cells[(up)][(left)] + cells[up][i] + cells[(up)][(right)];//upper row
        } else {
            return cells[(i)][left] + cells[i][j] + cells[i][right]//middle row
                    + cells[(up)][(left)] + cells[up][j] + cells[(up)][(right)]//upper row
                    + cells[(down)][(left)] + cells[down][(j)] + cells[(down)][(right)];//bottom row

        }
    }
}
