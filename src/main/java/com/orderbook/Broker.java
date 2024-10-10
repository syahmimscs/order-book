package com.orderbook;

public class Broker implements Runnable {
    private final OrderBookService orderBookService;
    private final String stockSymbol;
    private final int quantity;
    private final boolean isBuy;

    public Broker(OrderBookService orderBookService, String stockSymbol, int quantity, boolean isBuy) {
        this.orderBookService = orderBookService;
        this.stockSymbol = stockSymbol;
        this.quantity = quantity;
        this.isBuy = isBuy;
    }

    @Override
    public void run() {
        try {
            orderBookService.placeOrder(stockSymbol, quantity, isBuy); // Use the service to place the order
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}