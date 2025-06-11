package main.java.managers;

import java.util.concurrent.*;

public class ThreadPoolManager {
    private static final int FIXED_POOL_SIZE = 10;
    private static final ThreadPoolManager instance = new ThreadPoolManager();

    // Для многопоточного чтения запросов использовать Cached thread pool
    private final ExecutorService requestReaderPool;
    // Для многопоточной обработки полученного запроса использовать ForkJoinPool
    private final ForkJoinPool requestProcessorPool;
    // Для многопоточной отправки ответа использовать Fixed thread pool
    private final ExecutorService responseSenderPool;

    private ThreadPoolManager() {
        this.requestReaderPool = Executors.newCachedThreadPool();
        this.requestProcessorPool = new ForkJoinPool();
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
