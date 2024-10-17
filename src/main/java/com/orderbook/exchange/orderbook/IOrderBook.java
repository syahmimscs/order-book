package com.orderbook.exchange.orderbook;

import com.orderbook.exchange.models.IOrder;
import com.orderbook.exchange.models.OrderType;

public interface IOrderBook {
    void addOrder(IOrder order);
    boolean cancelOrder(long orderId, OrderType type);
    void printOrderBook();
    void stopMatchingService();
    boolean isEmpty();
    
    int getTotalOrderCount();
}
