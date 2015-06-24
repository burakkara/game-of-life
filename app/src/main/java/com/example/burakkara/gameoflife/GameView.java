package com.example.burakkara.gameoflife;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

/**
 * Author: Burak Kara on 24/06/15.
 */
public class GameView extends View {

    private float width;
    private float height;

    private float edge_length;

    private final int GRID_COUNT = 10;

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

        float xUnitLength = width / GRID_COUNT;
        float yUnitLength = height / GRID_COUNT;

        edge_length = xUnitLength < yUnitLength ? xUnitLength : yUnitLength;


        for (int i = 0; i < GRID_COUNT; i++) {
            for (int y = 0; y < GRID_COUNT; y++) {

                float left = i * edge_length;
                float top = y * edge_length;
                float right = (i + 1) * edge_length;
                float bottom = (y + 1) * edge_length;

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
        float screenX = event.getX();
        float screenY = event.getY();
        int viewX = (int) (screenX - getLeft());
        int viewY = (int) (screenY - getTop());

        int boxNo = getBoxNo(viewX, viewY);
        Toast.makeText(getContext(), String.valueOf(boxNo), Toast.LENGTH_SHORT).show();
        return super.onTouchEvent(event);
    }

    private int getBoxNo(int viewX, int viewY) {
        return (((int)(viewX / edge_length) + 1) +
                ((int)(viewY / edge_length)) * GRID_COUNT);

    }
}