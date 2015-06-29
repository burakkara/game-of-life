package com.example.burakkara.gameoflife;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.Arrays;

/**
 * Author: Burak Kara on 24/06/15.
 */
public class GameView extends View {

    private static final String TAG = GameView.class.getSimpleName();

    private static final int GRID_EDGE_DEFAULT_LENGTH = 20;
    private static final int BLOCK_EDGE_DEFAULT_LENGTH = 3;

    private float unitEdgeLengthInPixels;

    public static int GRID_EDGE_LENGTH;
    public static int BLOCK_EDGE_LENGTH;

    public static int BLOCK_COUNT;

    private Paint paintStroke;
    private Paint paintFill;

    private int[][] cells;
    private boolean[][] blockState;

    public GameView(Context context, AttributeSet attrs) {

        this(context, attrs, 0);

    }

    public GameView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        init(attrs);

        BLOCK_COUNT = (int) Math.ceil(GRID_EDGE_LENGTH / BLOCK_EDGE_LENGTH);

        cells = new int[GRID_EDGE_LENGTH][GRID_EDGE_LENGTH];
        blockState = new boolean[BLOCK_COUNT][BLOCK_COUNT];

        setPaints();

    }

    private void init(AttributeSet attrs) {
        if (attrs != null && !isInEditMode()) {

            final TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.GameView);
            try {
                BLOCK_EDGE_LENGTH = a.getInt(R.styleable.GameView_blockEdgeLength,
                        BLOCK_EDGE_DEFAULT_LENGTH);
                GRID_EDGE_LENGTH = a.getInt(R.styleable.GameView_gridEdgeLength,
                        GRID_EDGE_DEFAULT_LENGTH);
            } finally {
                a.recycle();
            }

        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        float xUnitLength = getWidth() / GRID_EDGE_LENGTH;
        float yUnitLength = getHeight() / GRID_EDGE_LENGTH;

        unitEdgeLengthInPixels = Math.min(xUnitLength, yUnitLength);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawColor(getResources().getColor(R.color.accent));
        paintFill.setColor(getResources().getColor(R.color.primary));
        drawGrid(canvas);

    }

    private void drawGrid(Canvas canvas) {
        for (int i = 0; i < GRID_EDGE_LENGTH; i++) {
            for (int j = 0; j < GRID_EDGE_LENGTH; j++) {

                float left = j * unitEdgeLengthInPixels;
                float top = i * unitEdgeLengthInPixels;
                float right = (j + 1) * unitEdgeLengthInPixels;
                float bottom = (i + 1) * unitEdgeLengthInPixels;

                canvas.drawRect(left, top,
                        right, bottom, paintStroke);

                if (cells[i][j] == 1) {
                    canvas.drawRect(left, top,
                            right, bottom, paintFill);

                }

            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float screenX = event.getX();
        float screenY = event.getY();
        int viewX = (int) (screenX - getLeft());
        int viewY = (int) (screenY - getTop());

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
        for (int i = 0; i < GRID_EDGE_LENGTH; i++) {
            Arrays.fill(cells[i], 0);
        }
        invalidate();
    }

    private int getRowIndex(int viewY) {
        return (int) (viewY / unitEdgeLengthInPixels);
    }

    private int getColumnIndex(int viewX) {
        return (int) (viewX / unitEdgeLengthInPixels);
    }

    public void setSeedData(int[][] seed, boolean[][] blockState) {
        cells = seed;
        this.blockState = blockState;//invalidate();
    }

    private void setPaints() {
        paintStroke = new Paint();
        paintStroke.setColor(getResources().getColor(R.color.primary_light));
        paintStroke.setStyle(Paint.Style.STROKE);
        paintStroke.setTextSize(18);

        paintFill = new Paint();
        paintFill.setStyle(Paint.Style.FILL);
        paintFill.setColor(Color.BLACK);
    }

    public int[][] getCells() {
        return cells;
    }

    private void drawBoxNumbers(Canvas canvas, int i, int j, float left, float bottom) {
        if (BuildConfig.DEBUG) {
            int boxNo = (i + 1) + ((j) * GRID_EDGE_LENGTH);
            canvas.drawText(String.valueOf(boxNo), left, bottom, paintStroke);

            //canvas.drawText(String.valueOf(i) + "," + String.valueOf(j), left, bottom, paintStroke);

        }
    }

    private int getBoxNo(int viewX, int viewY) {
        return (((int) (viewX / unitEdgeLengthInPixels) + 1) +
                ((int) (viewY / unitEdgeLengthInPixels)) * GRID_EDGE_LENGTH);
    }

}