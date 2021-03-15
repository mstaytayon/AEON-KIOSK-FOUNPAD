package allcardtech.com.booking.app.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class ThreadUtils {
    private static ExecutorService executorService;

    public ThreadUtils() {
    }

    public static ExecutorService getExecutorService() {
        if (null == executorService) {
            executorService = Executors.newCachedThreadPool();
        }

        return executorService;
    }
}
