package com.example.quality.refactoring.after;

/** After "Replace Conditional with Polymorphism": each type knows its own price. */
public interface Product {

  /** Final price for this kind of product. */
  double getPrice();

  /** Price before discounts or markups. */
  double getBasePrice();
}
