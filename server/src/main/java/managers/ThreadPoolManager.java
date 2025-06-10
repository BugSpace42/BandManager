package main.java.managers;

import java.util.concurrent.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ThreadPoolManager {
    private static final int FIXED_POOL_SIZE = 10;
    private static final ThreadPoolManager instance = new ThreadPoolManager();

    private final ExecutorService requestReaderPool;
    private final ForkJoinPool requestProcessorPool;
    private final ExecutorService responseSenderPool;

    private ThreadPoolManager() {
        // Cached thread pool for reading requests
        this.requestReaderPool = Executors.newCachedThreadPool();

        // ForkJoinPool for processing requests
        this.requestProcessorPool = new ForkJoinPool();

        // Fixed thread pool for sending responses
        this.responseSenderPool = Executors.newFixedThreadPool(FIXED_POOL_SIZE);
    }

    public static ThreadPoolManager getInstance() {
        return instance;
    }

    public ExecutorService getRequestReaderPool() {
        return requestReaderPool;
    }

    public ForkJoinPool getRequestProcessorPool() {
        return requestProcessorPool;
    }

    public ExecutorService getResponseSenderPool() {
        return responseSenderPool;
    }

    public void shutdown() {
        requestReaderPool.shutdown();
        requestProcessorPool.shutdown();
        responseSenderPool.shutdown();

        try {
            if (!requestReaderPool.awaitTermination(60, TimeUnit.SECONDS)) {
                requestReaderPool.shutdownNow();
            }
            if (!requestProcessorPool.awaitTermination(60, TimeUnit.SECONDS)) {
                requestProcessorPool.shutdownNow();
            }
            if (!responseSenderPool.awaitTermination(60, TimeUnit.SECONDS)) {
                responseSenderPool.shutdownNow();
            }
        } catch (InterruptedException e) {
            requestReaderPool.shutdownNow();
            requestProcessorPool.shutdownNow();
            responseSenderPool.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
