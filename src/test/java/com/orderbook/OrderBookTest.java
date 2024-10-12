package com.orderbook;

import com.orderbook.exchange.models.IOrder;
import com.orderbook.exchange.models.Order;
import com.orderbook.exchange.models.OrderType;
import com.orderbook.exchange.orderbook.IOrderBook;
import com.orderbook.exchange.orderbook.OrderBook;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;



public class OrderBookTest {

    private IOrderBook orderBook;

    @Before
    public void setup() {
        orderBook = new OrderBook("AAPL");
    }

    @Test
    public void testAddBuyOrder() {
        IOrder buyOrder = new Order("AAPL", OrderType.BUY, 150.00, 100);
        orderBook.addOrder(buyOrder);

        // Test if the order is added to the buyOrders queue
        orderBook.printOrderBook();
    }

    @Test
    public void testAddSellOrder() {
        IOrder sellOrder = new Order("AAPL", OrderType.SELL, 155.00, 50);
        orderBook.addOrder(sellOrder);

        // Test if the order is added to the sellOrders queue
        orderBook.printOrderBook();
    }

    @Test
    public void testMatchOrders() {
        IOrder buyOrder = new Order("AAPL", OrderType.BUY, 150.00, 100);
        IOrder sellOrder = new Order("AAPL", OrderType.SELL, 145.00, 50);
        
        orderBook.addOrder(buyOrder);
        orderBook.addOrder(sellOrder);

        assertEquals(50, buyOrder.getQuantity());
        assertEquals(0, sellOrder.getQuantity()); // sellOrder should be fully fulfilled and removed

    }

    @Test
    public void testMultipleOrderMatching() {
        // Add multiple buy and sell orders
        IOrder buyOrder1 = new Order("AAPL", OrderType.BUY, 150.00, 100);
        IOrder buyOrder2 = new Order("AAPL", OrderType.BUY, 155.00, 200);
        IOrder sellOrder1 = new Order("AAPL", OrderType.SELL, 140.00, 50);
        IOrder sellOrder2 = new Order("AAPL", OrderType.SELL, 145.00, 150);

        orderBook.addOrder(buyOrder1);
        orderBook.addOrder(buyOrder2);
        orderBook.addOrder(sellOrder1);
        orderBook.addOrder(sellOrder2);

        // Expectation: buyOrder2 should be prioritized because its price is higher
        // sellOrder1 should match with buyOrder2
        // sellOrder2 should match partially with buyOrder2 and buyOrder1

        orderBook.printOrderBook();

        // Further assertions (e.g., using reflection or extending IOrderBook to expose internal state)
        assertEquals(100, buyOrder1.getQuantity());
        assertEquals(0, sellOrder1.getQuantity());
        assertEquals(0, buyOrder2.getQuantity());
        assertEquals(0, sellOrder2.getQuantity());


    }
}