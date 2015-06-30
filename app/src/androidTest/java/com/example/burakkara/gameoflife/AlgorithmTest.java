package com.example.burakkara.gameoflife;

import junit.framework.TestCase;

import org.junit.Test;

/**
 * Author: karab on 30/06/15.
 */
public class AlgorithmTest extends TestCase {
    private int[][] cells;
    private int[][] temp;

    private int row;
    private int column;

    private CalculationListener listener;


    @Test
    public void testSumOfBlock() {
        initData();
        cells[0][1] = 1;
        cells[1][2] = 1;
        cells[2][0] = 1;
        cells[2][1] = 1;
        cells[2][2] = 1;
        CalculationTask task = new CalculationTask(listener, row, column, cells, temp);
        assertEquals(1, task.getSumOfBlock(0, 0));
        assertEquals(2, task.getSumOfBlock(0, 1));
        assertEquals(2, task.getSumOfBlock(0, 2));
        assertEquals(3, task.getSumOfBlock(1, 0));
        assertEquals(5, task.getSumOfBlock(1, 1));
        assertEquals(4, task.getSumOfBlock(1, 2));
        assertEquals(2, task.getSumOfBlock(2, 0));
        assertEquals(4, task.getSumOfBlock(2, 1));
        assertEquals(3, task.getSumOfBlock(2, 2));
    }

    @Test
    public void testCreateNextGenerationOfBlock() {
        initData();
        cells[0][1] = 1;
        cells[1][2] = 1;
        cells[2][0] = 1;
        cells[2][1] = 1;
        cells[2][2] = 1;
        CalculationTask task = new CalculationTask(listener, row, column, cells, temp);
        task.createNextGenerationOfBlock();

        assertEquals(0, temp[0][0]);
        assertEquals(0, temp[0][1]);
        assertEquals(0, temp[0][2]);
        assertEquals(1, temp[1][0]);
        assertEquals(0, temp[1][1]);
        assertEquals(1, temp[1][2]);
        assertEquals(0, temp[2][0]);
        assertEquals(1, temp[2][1]);
        assertEquals(1, temp[2][2]);
        assertEquals(1, temp[3][1]);
    }

    private void initData() {
        this.row = 0;
        this.column = 0;
        this.cells = new int[20][20];
        this.temp = new int[20][20];
    }
}
