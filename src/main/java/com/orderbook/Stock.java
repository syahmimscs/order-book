package com.orderbook;

public class Stock {
    private final String symbol;
    private int quantity;

    public Stock(String symbol, int quantity) {
        this.symbol = symbol;
        this.quantity = quantity;
    }

    public int getQuantity() {
        return quantity;
    }

    public void increaseQuantity(int amount) {
        this.quantity += amount;
    }

    public void decreaseQuantity(int amount) {
        this.quantity -= amount;
    }

    public String getSymbol() {
        return symbol;
    }
}