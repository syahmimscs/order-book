package com.orderbook.exchange.orderbook;

import com.orderbook.exchange.models.*;

public interface IOrderBook {
    void addOrder(IOrder order);
    boolean cancelOrder(long orderId, OrderType type);
    void printOrderBook();
}
