package com.orderbook;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class OrderBookTest {

    private OrderBook orderBook;

    @Before
    public void setUp() {
        orderBook = new OrderBookImpl();
    }

    @Test
    public void testInitialStockQuantities() {
        // Test initial stock quantities
        assertEquals(100, orderBook.getStockQuantity("AAPL"));
        assertEquals(200, orderBook.getStockQuantity("GOOGL"));
        assertEquals(150, orderBook.getStockQuantity("TSLA"));
    }

    @Test
    public void testBuyOrder() throws InterruptedException {
        // Buy 50 shares of AAPL
        orderBook.placeOrder("AAPL", 50, true);
        assertEquals(150, orderBook.getStockQuantity("AAPL"));
    }

    @Test
    public void testSellOrder() throws InterruptedException {
        // Sell 50 shares of GOOGL
        orderBook.placeOrder("GOOGL", 50, false);
        assertEquals(150, orderBook.getStockQuantity("GOOGL"));
    }

    @Test
    public void testConcurrentOrders() throws InterruptedException {
        // Simulate concurrent buy and sell orders using threads
        Thread buyOrder = new Thread(() -> {
            try {
                orderBook.placeOrder("AAPL", 30, true);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        Thread sellOrder = new Thread(() -> {
            try {
                orderBook.placeOrder("AAPL", 20, false);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        buyOrder.start();
        sellOrder.start();

        buyOrder.join();
        sellOrder.join();

        // Final quantity of AAPL should reflect both buy and sell operations
        assertEquals(110, orderBook.getStockQuantity("AAPL"));
    }
}