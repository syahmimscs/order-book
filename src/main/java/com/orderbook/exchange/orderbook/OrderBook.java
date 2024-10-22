package com.orderbook.exchange.orderbook;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.orderbook.exchange.models.IOrder;
import com.orderbook.exchange.models.OrderType;
import com.orderbook.exchange.service.OrderMatchingService;

public class OrderBook implements IOrderBook {
    private final String ticker;
    private final PriorityQueue<IOrder> buyOrders;
    private final PriorityQueue<IOrder> sellOrders;
    private final ReentrantReadWriteLock rwLock;
    
    // Matching service components
    private final Thread matchingThread;  // Thread for the matching service
    private final OrderMatchingService matchingService;  // Background matching service

    // Comparator for Buy Orders: Higher prices come first
    private static final Comparator<IOrder> buyComparator = (o1, o2) -> Double.compare(o2.getPrice(), o1.getPrice());

    // Comparator for Sell Orders: Lower prices come first
    private static final Comparator<IOrder> sellComparator = Comparator.comparingDouble(IOrder::getPrice);

    public OrderBook(String ticker) {
        this.ticker = ticker;
        this.buyOrders = new PriorityQueue<>(buyComparator);
        this.sellOrders = new PriorityQueue<>(sellComparator);
        this.rwLock = new ReentrantReadWriteLock();

        this.matchingService = new OrderMatchingService(this); // Create the matching service
        this.matchingThread = new Thread(matchingService); // Create a thread for the service
        matchingThread.start(); // Start the matching service thread
    }

    @Override
    public void addOrder(IOrder order) {
        rwLock.writeLock().lock();
        try {
            if (order.getType() == OrderType.BUY) {
                buyOrders.offer(order);
            } else {
                sellOrders.offer(order);
            }
        } finally {
            rwLock.writeLock().unlock();
        }
    }

    public void matchOrders() {
        rwLock.writeLock().lock();
        try {
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
                        buyOrders.poll(); // Remove fully matched buy order
                    }
                    if (sellOrder.getQuantity() == 0) {
                        sellOrders.poll(); // Remove fully matched sell order
                    }
                } else {
                    break; // No more matching orders
                }
            }
        } finally {
            rwLock.writeLock().unlock();
        }
    }
    @Override
    public boolean cancelOrder(long orderId, OrderType type) {
        rwLock.writeLock().lock();
        try {
            PriorityQueue<IOrder> orders = (type == OrderType.BUY) ? buyOrders : sellOrders;
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
            buyOrders.forEach(order -> System.out.println(
                    "BUY " + order.getQuantity() + " at $" + order.getPrice() +
                            " [Order ID: " + order.getOrderId() + "]"));

            System.out.println("Sell Orders:");
            sellOrders.forEach(order -> System.out.println(
                    "SELL " + order.getQuantity() + " at $" + order.getPrice() +
                            " [Order ID: " + order.getOrderId() + "]"));
        } finally {
            rwLock.readLock().unlock();
        }
    }

    @Override
    public boolean isEmpty() {
        rwLock.readLock().lock();
        try {
            return buyOrders.isEmpty() && sellOrders.isEmpty();
        } finally {
            rwLock.readLock().unlock();
        }
    }

    @Override
    public void stopMatchingService() {
        matchingService.stop();
        try {
            matchingThread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    @Override
    public int getTotalOrderCount() {
        rwLock.readLock().lock(); // Use read lock to ensure thread safety
        try {
            return buyOrders.size() + sellOrders.size(); // Count the total number of buy and sell orders
        } finally {
            rwLock.readLock().unlock();
        }
    }

}