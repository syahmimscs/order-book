package com.orderbook.exchange.exchange;

import com.orderbook.exchange.orderbook.*;
import com.orderbook.exchange.models.*;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Exchange implements IExchange {
    private final Map<String, IOrderBook> orderBooks;

    public Exchange() {
        this.orderBooks = new ConcurrentHashMap<>();
    }

    @Override
    public void addOrder(IOrder order) {
        IOrderBook orderBook = orderBooks.computeIfAbsent(order.getTicker(), OrderBook::new);
        orderBook.addOrder(order);
    }

    @Override
    public boolean cancelOrder(String ticker, long orderId, OrderType type) {
        IOrderBook orderBook = orderBooks.get(ticker);
        if (orderBook != null) {
            return orderBook.cancelOrder(orderId, type);
        }
        return false;
    }

    @Override
    public void printOrderBooks() {
        for (IOrderBook orderBook : orderBooks.values()) {
            orderBook.printOrderBook();
        }
    }

    // Stop all matching services when shutting down
    public void stopAllMatchingServices() {
        orderBooks.values().forEach(orderBook -> {
            if (orderBook instanceof OrderBook) { // Ensure it's the correct type
                ((OrderBook) orderBook).stopMatchingService(); // Stop the matching service
            }
        });
    }
}
