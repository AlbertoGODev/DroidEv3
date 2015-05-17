package com.guerritastudio.albertogarcia.droidev3.command;

import android.util.Log;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class CommandExecutor implements Executor {

    private static final String TAG = CommandExecutor.class.getSimpleName();

    private static final int CORE_POOL_SIZE = 1;
    private static final int MAX_POOL_SIZE = 2;//1
    private static final int KEEP_ALIVE_TIME = 10;//120
    private static final TimeUnit TIME_UNIT = TimeUnit.SECONDS;
    private static final BlockingQueue<Runnable> WORK_QUEUE = new LinkedBlockingQueue<Runnable>();
    private ThreadPoolExecutor threadPoolExecutor;

    private static CommandExecutor executor = null;

    public static CommandExecutor getInstance() {
        Log.e(TAG, "getInstance");
        if (executor == null) {
            executor = new CommandExecutor();
        }
        return executor;
    }

    private CommandExecutor() {
        int corePoolSize = CORE_POOL_SIZE;
        int maxPoolSize = MAX_POOL_SIZE;
        int keepAliveTime = KEEP_ALIVE_TIME;
        TimeUnit timeUnit = TIME_UNIT;
        BlockingQueue<Runnable> workQueue = WORK_QUEUE;
        threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maxPoolSize, keepAliveTime, timeUnit, workQueue);
    }

    @Override
    public void run(final Command command) {
        try {
            Log.e(TAG, "run()");
            if (command == null) {
                throw new IllegalArgumentException("Interactor to execute can't be null");
            }
            threadPoolExecutor.submit(new Runnable() {
                @Override
                public void run() {
                    Log.e(TAG, "threadPoolExecutor.submit run()");
                    command.run();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}