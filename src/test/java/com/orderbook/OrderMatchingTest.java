package com.orderbook;

import com.orderbook.exchange.exchange.Exchange;
import com.orderbook.exchange.models.IOrder;
import com.orderbook.exchange.models.Order;
import com.orderbook.exchange.models.OrderType;
import com.orderbook.exchange.orderbook.IOrderBook;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class OrderMatchingTest {

    @Test
    void testOrderMatching() throws InterruptedException {
        Exchange exchange = new Exchange();

        // Add matching buy and sell orders
        exchange.addOrder(new Order("AAPL", OrderType.BUY, 150.0, 10));
        exchange.addOrder(new Order("AAPL", OrderType.SELL, 150.0, 10));

        // Wait for the matching service to process the orders
        Thread.sleep(1000);

        // Check that the order book is empty after matching
        IOrderBook orderBook = exchange.getOrderBook("AAPL");
        assertTrue(orderBook.isEmpty(), "Order book should be empty after matching.");
    }
}
