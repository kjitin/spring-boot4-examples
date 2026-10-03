package com.example.quality.refactoring.after;

/** Electronics (10% markup for electronics). */
public class Electronics implements Product {

  private final double basePrice;

  /** Creates the product. */
  public Electronics(double basePrice) {
    this.basePrice = basePrice;
  }

  @Override
  public double getPrice() {
    return basePrice * 1.1;
  }

  @Override
  public double getBasePrice() {
    return basePrice;
  }
}
