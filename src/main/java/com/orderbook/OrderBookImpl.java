package com.orderbook;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class OrderBookImpl implements OrderBook {
    private final Map<String, Stock> stocks = new HashMap<>();
    private final Lock lock = new ReentrantLock();

    public OrderBookImpl() {
        stocks.put("AAPL", new Stock("AAPL", 100));
        stocks.put("GOOGL", new Stock("GOOGL", 200));
        stocks.put("TSLA", new Stock("TSLA", 150));
    }

    @Override
    public void placeOrder(String stockSymbol, int quantity, boolean isBuy) throws InterruptedException {
        lock.lock();
        try {
            Stock stock = stocks.get(stockSymbol);
            if (stock == null) {
                System.out.println("Stock " + stockSymbol + " not available.");
                return;
            }
            if (isBuy) {
                stock.increaseQuantity(quantity);
                System.out.println("Bought " + quantity + " shares of " + stockSymbol);
            } else {
                if (stock.getQuantity() >= quantity) {
                    stock.decreaseQuantity(quantity);
                    System.out.println("Sold " + quantity + " shares of " + stockSymbol);
                } else {
                    System.out.println("Not enough stock to sell " + quantity + " shares of " + stockSymbol);
                }
            }
        } finally {
            lock.unlock();
        }
    }

    @Override
    public int getStockQuantity(String stockSymbol) {
        Stock stock = stocks.get(stockSymbol);
        return stock != null ? stock.getQuantity() : 0;
    }
}