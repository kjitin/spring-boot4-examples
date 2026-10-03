package com.example.quality.refactoring;

/** Order line used by the Extract Method example. */
public record OrderItem(String name, double price) {

  /** Returns the item name. */
  public String getName() {
    return name;
  }

  /** Returns the item price. */
  public double getPrice() {
    return price;
  }
}
