package com.example.quality.refactoring.before;

/** Before "Replace Conditional with Polymorphism". */
public class PriceCalculator {

  /** Simple product with a type code. */
  public record Product(String type, double basePrice) {

    /** Returns the product type. */
    public String getType() {
      return type;
    }

    /** Returns the base price. */
    public double getBasePrice() {
      return basePrice;
    }
  }

  /** Calculates the price based on the product type. */
  public double getPrice(Product product) {
    if (product.getType().equals("BOOK")) {
      return product.getBasePrice() * 0.9; // 10% discount for books
    } else if (product.getType().equals("ELECTRONICS")) {
      return product.getBasePrice() * 1.1; // 10% markup for electronics
    } else {
      return product.getBasePrice();
    }
  }
}
