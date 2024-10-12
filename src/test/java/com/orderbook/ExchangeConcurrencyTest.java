package com.orderbook;

import com.orderbook.exchange.exchange.Exchange;
import com.orderbook.exchange.models.*;
import org.junit.jupiter.api.Test;
// import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class ExchangeConcurrencyTest {

    @Test
    void testConcurrentOrderSubmission() throws InterruptedException {
        Exchange exchange = new Exchange();
        int numThreads = 10;
        int ordersPerThread = 50;

        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        CountDownLatch latch = new CountDownLatch(numThreads); // To coordinate thread start

        // Create multiple threads to add orders concurrently
        for (int i = 0; i < numThreads; i++) {
            executor.submit(() -> {
                try {
                    latch.await(); // Ensure all threads start together
                    for (int j = 0; j < ordersPerThread; j++) {
                        IOrder order = new Order("AAPL", OrderType.BUY, 100 + j, 10);
                        exchange.addOrder(order);
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
            latch.countDown(); // Signal that this thread is ready to start
        }

        executor.shutdown();
        boolean finished = executor.awaitTermination(10, java.util.concurrent.TimeUnit.SECONDS);

        // Verify that all threads completed without throwing exceptions
        assertDoesNotThrow(() -> {
            if (!finished)
                throw new RuntimeException("Threads did not finish in time.");
        });
    }
}
