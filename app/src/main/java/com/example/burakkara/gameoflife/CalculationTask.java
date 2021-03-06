package com.example.burakkara.gameoflife;

import android.os.AsyncTask;

/**
 * Author: karab on 30/06/15.
 */
public class CalculationTask extends AsyncTask<Void, Void, Void> {
    private int[][] cells;
    private int[][] temp;

    private int row;
    private int column;

    private CalculationListener listener;

    public CalculationTask(CalculationListener listener, int blockRowIndex, int blockColumnIndex, int[][] cells,
                           int[][] temp) {
        this.listener = listener;
        this.row = blockRowIndex * GameView.BLOCK_EDGE_LENGTH;
        this.column = blockColumnIndex * GameView.BLOCK_EDGE_LENGTH;
        this.cells = cells;
        this.temp = temp;
    }



    @Override
    protected Void doInBackground(Void... params) {

        createNextGenerationOfBlock();
        return null;
    }

    public void createNextGenerationOfBlock() {
        for (int i = row; i < row + GameView.BLOCK_EDGE_LENGTH; i++) {
            for (int j = column; j < column + GameView.BLOCK_EDGE_LENGTH; j++) {

                int sum = getSumOfBlock(i, j);
                if (sum == 3) {
                    if (cells[i][j] == 0) {
                        temp[i][j] = 1;
                    }/*else already alive*/
                } else if (sum == 4) {
                    //keep state
                } else {
                    if (cells[i][j] == 1) {
                        temp[i][j] = 0;
                    }/*else already dead*/

                }
            }
        }
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        listener.onCalculationComplete();
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
