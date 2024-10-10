package com.orderbook;

public interface OrderBook {
    void placeOrder(String stockSymbol, int quantity, boolean isBuy) throws InterruptedException;
    int getStockQuantity(String stockSymbol);
}