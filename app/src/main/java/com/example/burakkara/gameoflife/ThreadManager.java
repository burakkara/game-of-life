package com.example.burakkara.gameoflife;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Author: karab on 27/06/15.
 */
public class ThreadManager {

    private static final ThreadManager instance;

    // Sets the amount of time an idle thread will wait for a task before terminating
    private static final int KEEP_ALIVE_TIME = 1;

    // Sets the Time Unit to seconds
    private static final TimeUnit KEEP_ALIVE_TIME_UNIT;

    public static int CORE_POOL_SIZE = (GameView.GRID_EDGE_LENGTH / 3) ^ 2;

    public static int MAXIMUM_POOL_SIZE = (GameView.GRID_EDGE_LENGTH / 3) ^ 2;

    // A queue of Runnables for the next generation of a block

    private final BlockingQueue<Runnable> calculationQueue;

    private static int NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors();

    /*  // A queue of PhotoManager tasks. Tasks are handed to a ThreadPool.
      private final Queue<PhotoTask> mPhotoTaskWorkQueue;
  */
    private ThreadPoolExecutor calculationThreadPool;

    private Handler handler;

    // A static block that sets class fields
    static {

        // The time unit for "keep alive" is in seconds
        KEEP_ALIVE_TIME_UNIT = TimeUnit.SECONDS;

        // Creates a single static instance of PhotoManager
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

        handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                //super.handleMessage(msg);
                //CalculationThread thre
                //Random rand = new Random();
                //MainActivityFragment.cells1[0][rand.nextInt(20)] = true;
            }
        };
    }

    public void sendResponse(CalculationThread thread) {
        Message completeMessage =
                handler.obtainMessage(0, thread);
        completeMessage.sendToTarget();

    }

    public static ThreadManager getInstance() {

        return instance;
    }

    public void waitForAll() {
        calculationThreadPool.shutdown();
        try {
            while (calculationThreadPool.awaitTermination(1000, TimeUnit.SECONDS)) {
               //® Log.e("Still waiting", "...");
            }
        } catch (InterruptedException e) {
            //todo
        }
    }

    public void reInitializePool() {
        calculationThreadPool = new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE,
                KEEP_ALIVE_TIME, KEEP_ALIVE_TIME_UNIT, calculationQueue);

    }

}
