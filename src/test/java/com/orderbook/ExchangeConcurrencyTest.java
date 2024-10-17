package com.orderbook;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import org.junit.jupiter.api.Test;

import com.orderbook.exchange.exchange.Exchange;
import com.orderbook.exchange.models.IOrder;
import com.orderbook.exchange.models.Order;
import com.orderbook.exchange.models.OrderType;

class ExchangeConcurrencyTest {

    @Test
    void testConcurrentOrderSubmission() throws InterruptedException {
        Exchange exchange = new Exchange();
        int numThreads = 10;
        int ordersPerThread = 50;

        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        CountDownLatch latch = new CountDownLatch(numThreads); 

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

        
        assertDoesNotThrow(() -> {
            if (!finished)
                throw new RuntimeException("Threads did not finish in time.");
        });



        // Visual representation for sake of demo. 


        // Print the state of the order book after all threads have finished
        
        // System.out.println("Final state of the order book:");
        // exchange.printOrderBooks();  // Method to display all orders in the order book

        // // Validate the total number of orders placed in the AAPL order book
        // int totalExpectedOrders = numThreads * ordersPerThread;
        // int totalOrdersInOrderBook = exchange.getOrderBook("AAPL").getTotalOrderCount(); // Custom method to count total orders in the order book
        // System.out.println(totalExpectedOrders);
        // System.out.println(totalOrdersInOrderBook);
        // assertEquals(totalExpectedOrders, totalOrdersInOrderBook, 
        //     "The number of orders in the order book should match the total number of orders placed.");
    }
}
