package com.example.quality.refactoring.after;

/** Usage after the refactoring: no type switch needed. */
public class PriceCalculator {

  /** Delegates to the product. */
  public double calculatePrice(Product product) {
    return product.getPrice();
  }
}
