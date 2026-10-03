package com.example.quality.refactoring.after;

import com.example.quality.refactoring.Order;
import com.example.quality.refactoring.OrderItem;

/** After "Extract Method": small, intention-revealing methods. */
public class OrderPrinter {

  /** Prints the items and the total. */
  public void printOrderDetails(Order order) {
    printItems(order);
    printTotal(order);
  }

  private void printItems(Order order) {
    for (OrderItem item : order.getItems()) {
      System.out.println(item.getName() + " - " + item.getPrice());
    }
  }

  private void printTotal(Order order) {
    double total = calculateTotal(order);
    System.out.println("Total: " + total);
  }

  private double calculateTotal(Order order) {
    return order.getItems().stream().mapToDouble(OrderItem::getPrice).sum();
  }
}
