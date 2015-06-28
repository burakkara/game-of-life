package com.example.burakkara.gameoflife;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment implements View.OnClickListener {

    private static final int MILLISECONDS_TILL_UPDATE = 400;
    private static final long STEP_DELAY = 200;
    private GameView gameView;
    private Handler periodicGameUpdateHandler;

    int[][] cells1;
    int[][] cells2;

    private boolean isFirstArrayBuffer = false;

    private boolean[][] blockState;//if there is change true
    private boolean[][] blockStateTemp;//if there is change true

    private ThreadManager manager;

    private Button buttonRun;
    private Button buttonStep;
    private Button buttonClear;

    private boolean isRunning = false;

    public MainActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        periodicGameUpdateHandler = new Handler();

        manager = ThreadManager.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        findViews(view);
        setListeners();

        initArrays();

        gameView.setSeedData(cells1, blockState);
        gameView.invalidate();
        return view;
    }

    private void initArrays() {
        cells1 = new int[GameView.GRID_EDGE_LENGTH][GameView.GRID_EDGE_LENGTH];
        cells2 = new int[GameView.GRID_EDGE_LENGTH][GameView.GRID_EDGE_LENGTH];
        blockState = new boolean[GameView.BLOCK_COUNT][GameView.BLOCK_COUNT];
        blockStateTemp = new boolean[GameView.BLOCK_COUNT][GameView.BLOCK_COUNT];
    }

    private void setListeners() {
        buttonClear.setOnClickListener(this);
        buttonRun.setOnClickListener(this);
        buttonStep.setOnClickListener(this);
    }

    private void findViews(View view) {
        gameView = (GameView) view.findViewById(R.id.gameview);
        buttonRun = (Button) view.findViewById(R.id.run);
        buttonStep = (Button) view.findViewById(R.id.step);
        buttonClear = (Button) view.findViewById(R.id.clear);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.run:
                if (isRunning) {
                    buttonRun.setText(getString(R.string.run));
                    stop();
                } else {
                    buttonRun.setText(getString(R.string.stop));
                    run();
                }
                break;
            case R.id.step:
                step();
                break;
            case R.id.clear:
                clear();
                break;
            default:
                break;

        }
    }

    private void step() {
        cells1 = gameView.getCells();
        calculateNextStep();
        periodicGameUpdateHandler.postDelayed(gameUpdateRunnable, STEP_DELAY);
    }

    private void clear() {

        gameView.clearCells();
    }

    private void run() {
        cells1 = gameView.getCells();
        isFirstArrayBuffer = false;
        isRunning = true;
        periodicGameUpdateHandler.post(periodicGameUpdateRunnable);
    }

    private void stop() {
        isRunning = false;
        periodicGameUpdateHandler.removeCallbacks(periodicGameUpdateRunnable);
    }

    final Runnable periodicGameUpdateRunnable = new Runnable() {
        @Override
        public void run() {
            gameView.setSeedData(isFirstArrayBuffer ? cells2 : cells1, blockState);
            gameView.invalidate();
            periodicGameUpdateHandler.postDelayed(periodicGameUpdateRunnable, MILLISECONDS_TILL_UPDATE);
            calculateNextStep();
        }
    };

    final Runnable gameUpdateRunnable = new Runnable() {
        @Override
        public void run() {
            gameView.setSeedData(isFirstArrayBuffer ? cells2 : cells1, blockState);
            gameView.invalidate();
        }
    };

    private void calculateNextStep() {

        if (isFirstArrayBuffer) {
            cells1 = Util.deepCopy(cells2);
            //System.arraycopy(cells2, 0, cells1, 0, cells1.length);
            //cells1 = cells2;
        } else {
            cells2 = Util.deepCopy(cells1);
            //System.arraycopy(cells1, 0, cells2, 0, cells1.length);
            //cells2 = cells1;
        }

        blockStateTemp = blockState;
        blockState = new boolean[GameView.BLOCK_COUNT][GameView.BLOCK_COUNT];

        for (int i = 0; i < GameView.BLOCK_COUNT; i++) {
            for (int j = 0; j < GameView.BLOCK_COUNT; j++) {
                // if (blockStateTemp[i][j]) {
                ThreadManager.startCalculation(!isFirstArrayBuffer
                        ? new CalculationThread(i, j, cells1, cells2, blockState)
                        : new CalculationThread(i, j, cells2, cells1, blockState));
                //}
            }
        }
        isFirstArrayBuffer = !isFirstArrayBuffer;

        //manager.waitForAll();
    }

    @Override
    public void onPause() {
        super.onPause();
        stop();
    }

}
