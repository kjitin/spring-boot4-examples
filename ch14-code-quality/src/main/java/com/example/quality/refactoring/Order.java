package com.example.quality.refactoring;

import java.util.List;

/** Order used by the Extract Method example. */
public record Order(List<OrderItem> items) {

  // Defensive copy: SpotBugs (EI_EXPOSE_REP) flags records that store and expose mutable lists
  public Order {
    items = List.copyOf(items);
  }

  /** Returns the order items. */
  public List<OrderItem> getItems() {
    return items;
  }
}
