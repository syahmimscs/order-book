package com.orderbook;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) {
        // Create the order book
        OrderBook orderBook = new OrderBookImpl();

        // Create the service to handle placing orders
        OrderBookService orderBookService = new OrderBookService(orderBook);

        // Use an ExecutorService to manage multiple broker threads
        ExecutorService executorService = Executors.newFixedThreadPool(5);

        // Simulate multiple brokers placing orders concurrently
        // Each broker places buy or sell orders for different stocks
        executorService.submit(new Broker(orderBookService, "AAPL", 50, true));  // Buy 50 shares of AAPL
        executorService.submit(new Broker(orderBookService, "AAPL", 30, false)); // Sell 30 shares of AAPL
        executorService.submit(new Broker(orderBookService, "TSLA", 40, true));  // Buy 40 shares of TSLA
        executorService.submit(new Broker(orderBookService, "GOOGL", 20, false)); // Sell 20 shares of GOOGL
        executorService.submit(new Broker(orderBookService, "TSLA", 50, false));  // Sell 50 shares of TSLA

        // Shut down the executor after the tasks are complete
        executorService.shutdown();
    }
}
