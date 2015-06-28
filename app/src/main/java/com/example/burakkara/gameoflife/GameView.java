package com.example.burakkara.gameoflife;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Author: Burak Kara on 24/06/15.
 */
public class GameView extends View {

    private static final String TAG = GameView.class.getSimpleName();

    private float width;
    private float height;

    private float edge_length;

    public static final int GRID_EDGE_LENGTH = 10;
    public static final int BLOCK_EDGE_LENGTH = 3;
    public static final int BLOCK_COUNT = (int) Math.ceil(GRID_EDGE_LENGTH / BLOCK_EDGE_LENGTH);

    private Paint paintStroke;
    private Paint paintFill;

    private int[][] cells = new int[GRID_EDGE_LENGTH][GRID_EDGE_LENGTH];
    private boolean[][] blockState = new boolean[BLOCK_COUNT][BLOCK_COUNT];


    public GameView(Context context) {
        this(context, null);
    }

    public GameView(Context context, AttributeSet attrs) {

        this(context, attrs, 0);

    }

    public GameView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        setPaints();

    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        width = getWidth();
        height = getHeight();

        Log.e(TAG, String.valueOf(width));

        float xUnitLength = width / GRID_EDGE_LENGTH;
        float yUnitLength = height / GRID_EDGE_LENGTH;

        edge_length = Math.min(xUnitLength, yUnitLength);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //paintFill.setColor(getResources().getColor(R.color.primary_light));
        canvas.drawColor(getResources().getColor(R.color.primary_light));
        paintFill.setColor(getResources().getColor(R.color.primary));
        drawGrid(canvas);

    }

    private void drawGrid(Canvas canvas) {
        for (int i = 0; i < GRID_EDGE_LENGTH; i++) {
            for (int j = 0; j < GRID_EDGE_LENGTH; j++) {

                float left = j * edge_length;
                float top = i * edge_length;
                float right = (j + 1) * edge_length;
                float bottom = (i + 1) * edge_length;

                canvas.drawRect(left, top,
                        right, bottom, paintStroke);

                if (cells[i][j] == 1) {
                    canvas.drawRect(left, top,
                            right, bottom, paintFill);

                }

                int boxNo = (i + 1) + ((j) * GRID_EDGE_LENGTH);
                canvas.drawText(String.valueOf(boxNo), left, bottom, paintStroke);

                //canvas.drawText(String.valueOf(i) + "," + String.valueOf(j), left, bottom, paintStroke);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float screenX = event.getX();
        float screenY = event.getY();
        int viewX = (int) (screenX - getLeft());
        int viewY = (int) (screenY - getTop());

        //int boxNo = getBoxNo(viewX, viewY);
        //Toast.makeText(getContext(), String.valueOf(boxNo), Toast.LENGTH_SHORT).show();

        int rowIndex = getRowIndex(viewY);
        int columnIndex = getColumnIndex(viewX);

        if (cells[rowIndex][columnIndex] == 1) {
            cells[rowIndex][columnIndex] = 0;
        } else {
            cells[rowIndex][columnIndex] = 1;
        }
        invalidate();

        return super.onTouchEvent(event);
    }

    public void clearCells() {
        int zero = 0;
        for (int i = 0; i < GRID_EDGE_LENGTH; i++) {
            for (int j = 0; j < GRID_EDGE_LENGTH; j++) {
                cells[i][j] = 0;
            }
        }
        invalidate();
    }

    private int getBoxNo(int viewX, int viewY) {
        return (((int) (viewX / edge_length) + 1) +
                ((int) (viewY / edge_length)) * GRID_EDGE_LENGTH);
    }

    private int getRowIndex(int viewY) {
        return (int) (viewY / edge_length);
    }

    private int getColumnIndex(int viewX) {
        return (int) (viewX / edge_length);
    }

    public void setSeedData(int[][] seed, boolean[][] blockState) {
        cells = seed;
        this.blockState = blockState;//invalidate();
    }

    private void setPaints() {
        paintStroke = new Paint();
        paintStroke.setColor(Color.BLACK);
        paintStroke.setStyle(Paint.Style.STROKE);
        paintStroke.setTextSize(18);

        paintFill = new Paint();
        paintFill.setStyle(Paint.Style.FILL);
        paintFill.setColor(Color.BLACK);
    }

    public int[][] getCells() {
        return cells;
    }


}