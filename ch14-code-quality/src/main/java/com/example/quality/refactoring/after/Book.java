package com.example.quality.refactoring.after;

/** Book (10% discount for books). */
public class Book implements Product {

  private final double basePrice;

  /** Creates the product. */
  public Book(double basePrice) {
    this.basePrice = basePrice;
  }

  @Override
  public double getPrice() {
    return basePrice * 0.9;
  }

  @Override
  public double getBasePrice() {
    return basePrice;
  }
}
