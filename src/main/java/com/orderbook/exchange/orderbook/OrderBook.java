package com.orderbook.exchange.orderbook;

import java.util.PriorityQueue;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.orderbook.exchange.models.*;

public class OrderBook implements IOrderBook {
    private final String ticker;
    private final PriorityQueue<IOrder> buyOrders;
    private final PriorityQueue<IOrder> sellOrders;
    private final ReentrantReadWriteLock rwLock;

    public OrderBook(String ticker) {
        this.ticker = ticker;
        this.buyOrders = new PriorityQueue<>();
        this.sellOrders = new PriorityQueue<>();
        this.rwLock = new ReentrantReadWriteLock();
    }

    @Override
    public void addOrder(IOrder order) {
        rwLock.writeLock().lock();
        try {
            if (order.getType() == OrderType.BUY) {
                buyOrders.offer(order);
            } else if (order.getType() == OrderType.SELL) {
                sellOrders.offer(order);
            }
            matchOrders();
        } finally {
            rwLock.writeLock().unlock();
        }
    }

    private void matchOrders() {
        while (!buyOrders.isEmpty() && !sellOrders.isEmpty()) {
            IOrder buyOrder = buyOrders.peek();
            IOrder sellOrder = sellOrders.peek();

            if (buyOrder.getPrice() >= sellOrder.getPrice()) {
                int tradedQuantity = Math.min(buyOrder.getQuantity(), sellOrder.getQuantity());
                double tradePrice = sellOrder.getPrice();

                System.out.println("Matched Order: BUY " + tradedQuantity + " of " + ticker +
                        " at $" + tradePrice + " [Order IDs: BUY#" + buyOrder.getOrderId() +
                        " & SELL#" + sellOrder.getOrderId() + "]");

                buyOrder.reduceQuantity(tradedQuantity);
                sellOrder.reduceQuantity(tradedQuantity);

                if (buyOrder.getQuantity() == 0) {
                    buyOrders.poll();
                }
                if (sellOrder.getQuantity() == 0) {
                    sellOrders.poll();
                }
            } else {
                break;
            }
        }
    }

    @Override
    public boolean cancelOrder(long orderId, OrderType type) {
        rwLock.writeLock().lock();
        try {
            PriorityQueue<IOrder> orders = type == OrderType.BUY ? buyOrders : sellOrders;
            return orders.removeIf(order -> order.getOrderId() == orderId);
        } finally {
            rwLock.writeLock().unlock();
        }
    }

    @Override
    public void printOrderBook() {
        rwLock.readLock().lock();
        try {
            System.out.println("\nOrder Book for " + ticker);
            System.out.println("Buy Orders:");
            for (IOrder order : buyOrders) {
                System.out.println("BUY " + order.getQuantity() + " at $" + order.getPrice() +
                        " [Order ID: " + order.getOrderId() + "]");
            }
            System.out.println("Sell Orders:");
            for (IOrder order : sellOrders) {
                System.out.println("SELL " + order.getQuantity() + " at $" + order.getPrice() +
                        " [Order ID: " + order.getOrderId() + "]");
            }
        } finally {
            rwLock.readLock().unlock();
        }
    }

}