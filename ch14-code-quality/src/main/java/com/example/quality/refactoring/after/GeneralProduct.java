package com.example.quality.refactoring.after;

/** GeneralProduct (no adjustment). */
public class GeneralProduct implements Product {

  private final double basePrice;

  /** Creates the product. */
  public GeneralProduct(double basePrice) {
    this.basePrice = basePrice;
  }

  @Override
  public double getPrice() {
    return basePrice;
  }

  @Override
  public double getBasePrice() {
    return basePrice;
  }
}
