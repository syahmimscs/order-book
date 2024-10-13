package com.orderbook.exchange.service;

import com.orderbook.exchange.orderbook.OrderBook;

public class OrderMatchingService implements Runnable {
    private final OrderBook orderBook;
    private volatile boolean running = true; // control the matching thread

    public OrderMatchingService(OrderBook orderBook) {
        this.orderBook = orderBook;
    }

    @Override
    public void run() {
        while (running) {
            orderBook.matchOrders(); // Continuously attempt to match orders
            try {
                Thread.sleep(100); 
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    // Method to stop the matching service gracefully
    public void stop() {
        running = false;
    }
}
