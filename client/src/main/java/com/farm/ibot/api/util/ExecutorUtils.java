package com.farm.ibot.api.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

public class ExecutorUtils {
    public static void wait(ExecutorService executorService) {
        try {
            executorService.shutdown();
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
        } catch (Exception var2) {
            var2.printStackTrace();
        }

    }
}
