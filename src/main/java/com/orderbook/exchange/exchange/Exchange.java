package com.orderbook.exchange.exchange;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.orderbook.exchange.models.IOrder;
import com.orderbook.exchange.models.OrderType;
import com.orderbook.exchange.orderbook.IOrderBook;
import com.orderbook.exchange.orderbook.OrderBook;

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

    @Override
    public IOrderBook getOrderBook(String ticker) {
        return orderBooks.get(ticker);
    }

    // Stop all matching services when shutting down
    public void stopAllMatchingServices() {
        orderBooks.values().forEach(IOrderBook::stopMatchingService);
    }
}
