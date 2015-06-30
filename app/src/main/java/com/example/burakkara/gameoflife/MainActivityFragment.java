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
public class MainActivityFragment extends Fragment implements View.OnClickListener,
        CalculationListener {

    private static final int UPDATE_PERIOD = 300;
    private static final String KEY_BUNDLE = "key.bundle";

    private GameView gameView;
    private Handler gameUpdateHandler;

    int[][] cells1;
    int[][] cells2;

    private boolean isFirstArrayBuffer = false;

    private Button buttonRun;
    private Button buttonStep;
    private Button buttonClear;

    private boolean isRunning = false;

    private int callCount;

    public MainActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        gameUpdateHandler = new Handler();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        findViews(view);
        setListeners();

        if (savedInstanceState == null) {
            initArrays();
        } else {
            GameBundle bundle = (GameBundle) savedInstanceState.getSerializable(KEY_BUNDLE);
            cells1 = Util.deepCopy(bundle.getCells1());
            cells2 = Util.deepCopy(bundle.getCells1());
        }

        gameView.setSeedData(cells1);
        gameView.invalidate();
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(KEY_BUNDLE, new GameBundle(cells1));
    }

    private void initArrays() {
        cells1 = new int[GameView.GRID_EDGE_LENGTH][GameView.GRID_EDGE_LENGTH];
        cells2 = new int[GameView.GRID_EDGE_LENGTH][GameView.GRID_EDGE_LENGTH];
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

    private void run() {
        cells1 = gameView.getCells();
        isFirstArrayBuffer = false;
        isRunning = true;
        calculateNextStep();
    }

    private void step() {
        cells1 = gameView.getCells();
        isFirstArrayBuffer = false;
        calculateNextStep();
    }

    private void clear() {

        gameView.clearCells();
    }

    private void stop() {
        isRunning = false;
        gameUpdateHandler.removeCallbacks(gameUpdateRunnable);
    }

    final Runnable gameUpdateRunnable = new Runnable() {
        @Override
        public void run() {
            gameView.setSeedData(isFirstArrayBuffer ? cells1 : cells2);
            gameView.invalidate();
            isFirstArrayBuffer = !isFirstArrayBuffer;
            if (isRunning) {
                calculateNextStep();
            }
        }
    };

    private void calculateNextStep() {

        callCount = (int) Math.pow(GameView.BLOCK_COUNT, 2);

        if (isFirstArrayBuffer) {
            cells1 = Util.deepCopy(cells2);
        } else {
            cells2 = Util.deepCopy(cells1);
        }

        for (int i = 0; i < GameView.BLOCK_COUNT; i++) {
            for (int j = 0; j < GameView.BLOCK_COUNT; j++) {
                if (isFirstArrayBuffer) {
                    new CalculationTask(this, i, j, cells2, cells1).execute();
                } else {
                    new CalculationTask(this, i, j, cells1, cells2).execute();
                }
            }
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        stop();
    }

    @Override
    public void onCalculationComplete() {
        if (--callCount == 0) {
            gameUpdateHandler.postDelayed(gameUpdateRunnable, UPDATE_PERIOD);
        }
    }


}
