package com.orderbook.exchange.models;

public interface IOrder extends Comparable<IOrder> {
    long getOrderId();
    String getTicker();
    OrderType getType();
    double getPrice();
    int getQuantity();
    void reduceQuantity(int amount);
    long getTimestamp();
}
