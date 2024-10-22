package com.orderbook.exchange.models;

import java.util.concurrent.atomic.AtomicLong;

public class Order implements IOrder {
    private static final AtomicLong ID_GENERATOR = new AtomicLong(1);

    private final long orderId;
    private final String ticker;
    private final OrderType type;
    private final double price;
    private int quantity;
    private final long timestamp;

    public Order(String ticker, OrderType type, double price, int quantity) {
        this.orderId = ID_GENERATOR.getAndIncrement();
        this.ticker = ticker;
        this.type = type;
        this.price = price;
        this.quantity = quantity;
        this.timestamp = System.nanoTime();
    }

    @Override
    public long getOrderId() {
        return orderId;
    }

    @Override
    public String getTicker() {
        return ticker;
    }

    @Override
    public OrderType getType() {
        return type;
    }

    @Override
    public double getPrice() {
        return price;
    }

    @Override
    public int getQuantity() {
        return quantity;
    }

    @Override
    public void reduceQuantity(int amount) {
        this.quantity -= amount;
    }

    @Override
    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public int compareTo(IOrder o) {
        // For priority queue ordering
        if (this.price == o.getPrice()) {
            return Long.compare(this.timestamp, o.getTimestamp());
        }
        return this.type == OrderType.BUY
                ? Double.compare(o.getPrice(), this.price) // Higher price first for BUY
                : Double.compare(this.price, o.getPrice()); // Lower price first for SELL
    }
}
