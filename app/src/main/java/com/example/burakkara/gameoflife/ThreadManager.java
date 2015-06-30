package com.example.burakkara.gameoflife;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Author: karab on 27/06/15.
 */
public class ThreadManager {

    private static final ThreadManager instance;

    private static final int KEEP_ALIVE_TIME = 1;

    private static final TimeUnit KEEP_ALIVE_TIME_UNIT;

    public static int CORE_POOL_SIZE = (GameView.GRID_EDGE_LENGTH / 3) ^ 2;

    public static int MAXIMUM_POOL_SIZE = (GameView.GRID_EDGE_LENGTH / 3) ^ 2;

    private final BlockingQueue<Runnable> calculationQueue;

    private ThreadPoolExecutor calculationThreadPool;

    static {
        KEEP_ALIVE_TIME_UNIT = TimeUnit.SECONDS;
        instance = new ThreadManager();
    }

    static public CalculationThread startCalculation(CalculationThread thread) {
        instance.calculationThreadPool.execute(thread);
        return thread;
    }


    private ThreadManager() {
        calculationQueue = new LinkedBlockingQueue<Runnable>();

        calculationThreadPool = new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE,
                KEEP_ALIVE_TIME, KEEP_ALIVE_TIME_UNIT, calculationQueue);

    }

    public static ThreadManager getInstance() {

        return instance;
    }

    public void waitForAll() {

    }

    public void reInitializePool() {
        calculationThreadPool = new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE,
                KEEP_ALIVE_TIME, KEEP_ALIVE_TIME_UNIT, calculationQueue);

    }

}
