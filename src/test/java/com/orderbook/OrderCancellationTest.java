package com.orderbook;

import com.orderbook.exchange.exchange.Exchange;
import com.orderbook.exchange.models.IOrder;
import com.orderbook.exchange.models.Order;
import com.orderbook.exchange.models.OrderType;
import com.orderbook.exchange.orderbook.IOrderBook;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class OrderCancellationTest {

    @Test
    void testOrderCancellation() throws InterruptedException {
        Exchange exchange = new Exchange();
        IOrder order = new Order("AAPL", OrderType.BUY, 150.0, 10);

        // Add the order to the exchange
        exchange.addOrder(order);

        // Start a thread to cancel the order
        Thread cancelThread = new Thread(() -> exchange.cancelOrder("AAPL", order.getOrderId(), OrderType.BUY));
        cancelThread.start();

        // Simulate other operations happening concurrently
        Thread.sleep(100);
        cancelThread.join(); // Wait for the cancellation thread to complete

        // Verify that the order book is empty after cancellation
        IOrderBook orderBook = exchange.getOrderBook("AAPL");
        assertTrue(orderBook.isEmpty(), "Order book should be empty after cancellation.");
    }
}
