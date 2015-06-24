package com.example.burakkara.gameoflife;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Author: Burak Kara on 24/06/15.
 */
public class GameView extends View {

    private int width;
    private int height;

    private int edge_length = 0;

    private final int GRID_COUNT = 20;

    private Paint paintStroke;
    private Paint paintFill;

    public GameView(Context context) {
        this(context, null);
    }


    /**
     * Constructor for MTSEditText.
     *
     * @param context Context
     * @param attrs   AttributeSet
     */
    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);

        paintStroke = new Paint();
        paintStroke.setColor(Color.BLACK);
        paintStroke.setStyle(Paint.Style.STROKE);
        paintStroke.setTextSize(18);

        paintFill = new Paint();
        paintFill.setStyle(Paint.Style.FILL);
        paintFill.setColor(Color.BLACK);

    }

    /**
     * Constructor for MTSEditText.
     *
     * @param context  Context
     * @param attrs    AttributeSet
     * @param defStyle int
     */
    public GameView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        width = getMeasuredWidth();
        height = getMeasuredHeight();

        int xUnitLength = width / GRID_COUNT;
        int yUnitLength = height / GRID_COUNT;

        edge_length = xUnitLength < yUnitLength ? xUnitLength : yUnitLength;


        for (int i = 0; i < GRID_COUNT; i++) {
            for (int y = 0; y < GRID_COUNT; y++) {

                int left = i * edge_length;
                int top = y * edge_length;
                int right = (i + 1) * edge_length;
                int bottom = (y + 1) * edge_length;

                canvas.drawRect(left, top,
                        right, bottom, paintStroke);

                int boxNo = (i + 1) + ((y) * GRID_COUNT);

                canvas.drawText(String.valueOf(boxNo),//todo for test, remove later
                        left, bottom, paintStroke);

            }
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }
}