package com.example.quality.refactoring.before;

import com.example.quality.refactoring.Order;
import com.example.quality.refactoring.OrderItem;

/** Before "Extract Method": one method does everything. */
public class OrderPrinter {

  /** Prints the items and the total. */
  public void printOrderDetails(Order order) {
    double total = 0;
    for (OrderItem item : order.getItems()) {
      System.out.println(item.getName() + " - " + item.getPrice());
      total += item.getPrice();
    }
    System.out.println("Total: " + total);
  }
}
