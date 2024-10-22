package com.orderbook.exchange.exchange;

import com.orderbook.exchange.models.IOrder;
import com.orderbook.exchange.models.OrderType;
import com.orderbook.exchange.orderbook.IOrderBook;

public interface IExchange {
    void addOrder(IOrder order);
    boolean cancelOrder(String ticker, long orderId, OrderType type);
    void printOrderBooks();
    IOrderBook getOrderBook(String ticker);
}
