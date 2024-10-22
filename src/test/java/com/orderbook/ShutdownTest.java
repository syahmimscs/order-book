package com.orderbook;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import org.junit.jupiter.api.Test;

import com.orderbook.exchange.exchange.Exchange;
import com.orderbook.exchange.models.Order;
import com.orderbook.exchange.models.OrderType;

public class ShutdownTest {

    @Test
    void testShutdown() {
        Exchange exchange = new Exchange();

        // Add some orders to trigger the matching service
        exchange.addOrder(new Order("AAPL", OrderType.BUY, 150.0, 10));
        exchange.addOrder(new Order("AAPL", OrderType.SELL, 150.0, 10));

        // Ensure that the matching service stops without issues
        assertDoesNotThrow(() -> exchange.stopAllMatchingServices());
    }
}
