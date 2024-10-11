package com.orderbook.exchange.exchange;

import com.orderbook.exchange.models.*;

public interface IExchange {
    void addOrder(IOrder order);
    boolean cancelOrder(String ticker, long orderId, OrderType type);
    void printOrderBooks();
}
