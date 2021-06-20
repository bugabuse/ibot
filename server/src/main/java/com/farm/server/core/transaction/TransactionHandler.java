/*
 * Decompiled with CFR 0.148.
 */
package com.farm.server.core.transaction;

import com.farm.server.core.transaction.TransactionRunnable;
import com.farm.server.core.util.Time;
import java.util.ArrayList;
import java.util.Collection;

public class TransactionHandler {
    private static ArrayList<TransactionRunnable> runnables = new ArrayList();

    public static boolean executeAndWait(Runnable runnable) {
        TransactionRunnable transactionRunnable = new TransactionRunnable(runnable);
        runnables.add(transactionRunnable);
        return Time.sleep(15000, () -> transactionRunnable.isCompleted);
    }

    public static void executeInBackground(Runnable runnable) {
        TransactionRunnable transactionRunnable = new TransactionRunnable(runnable);
        runnables.add(transactionRunnable);
    }

    public static void startTransactionExecuter() {
        new Thread(() -> {
            do {
                if (runnables.size() > 0) {
                    try {
                        for (TransactionRunnable runnable : new ArrayList<TransactionRunnable>(runnables)) {
                            try {
                                runnable.execute();
                            }
                            catch (Exception e) {
                                e.printStackTrace();
                            }
                            runnables.remove(runnable);
                        }
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                Time.sleep(1);
            } while (true);
        }).start();
    }
}

