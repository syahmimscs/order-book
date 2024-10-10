package com.orderbook;

public class OrderBookService {
    private final OrderBook orderBook;

    public OrderBookService(OrderBook orderBook) {
        this.orderBook = orderBook;
    }

    public void placeOrder(String stockSymbol, int quantity, boolean isBuy) {
        try {
            orderBook.placeOrder(stockSymbol, quantity, isBuy);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}