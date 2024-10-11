package com.orderbook.exchange.producers;

import com.orderbook.exchange.models.*;
import com.orderbook.exchange.exchange.*;
import java.util.Random;

public class OrderProducer implements IOrderProducer {
    private final IExchange exchange;
    private final String[] tickers = {"AAPL", "GOOGL", "MSFT", "AMZN"};
    private final Random random = new Random();

    public OrderProducer(IExchange exchange) {
        this.exchange = exchange;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            String ticker = tickers[random.nextInt(tickers.length)];
            OrderType type = random.nextBoolean() ? OrderType.BUY : OrderType.SELL;
            double price = 100 + random.nextDouble() * 50;
            int quantity = 1 + random.nextInt(100);

            IOrder order = new Order(ticker, type, price, quantity);
            exchange.addOrder(order);

            try {
                Thread.sleep(100); // Adjust as needed
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}
