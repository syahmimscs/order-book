package com.orderbook.exchange.simulator;

import com.orderbook.exchange.exchange.*;
import com.orderbook.exchange.producers.*;

public class ExchangeSimulator {
    public static void main(String[] args) {
        IExchange exchange = new Exchange();

        int numberOfProducers = 5;
        for (int i = 0; i < numberOfProducers; i++) {
            Thread producerThread = new Thread(new OrderProducer(exchange));
            producerThread.start();
        }

        while (true) {
            try {
                Thread.sleep(5000);
                exchange.printOrderBooks();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}
