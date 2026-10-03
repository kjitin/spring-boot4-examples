package com.example.quality.refactoring;

import com.example.quality.refactoring.after.Book;
import com.example.quality.refactoring.after.Electronics;
import com.example.quality.refactoring.after.GeneralProduct;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

// Refactorings must not change behaviour: each "after" version is checked against its "before" version.
@ExtendWith(OutputCaptureExtension.class)
class RefactoringBehaviourTests {

    @Test
    void extractMethodPrintsTheSameOutput(CapturedOutput output) {
        Order order = new Order(List.of(new OrderItem("Pen", 1.5), new OrderItem("Book", 10.0)));

        new com.example.quality.refactoring.before.OrderPrinter().printOrderDetails(order);
        String before = output.getOut();
        new com.example.quality.refactoring.after.OrderPrinter().printOrderDetails(order);
        String after = output.getOut().substring(before.length());

        assertThat(after).isEqualTo(before).contains("Total: 11.5");
    }

    @Test
    void explainingVariablesKeepTheSameLogic() {
        var before = new com.example.quality.refactoring.before.BrowserCheck(true);
        var after = new com.example.quality.refactoring.after.BrowserCheck(true);
        for (String platform : List.of("MacOS", "Windows")) {
            for (String browser : List.of("IE11", "Firefox")) {
                for (int resize : new int[] {0, 1}) {
                    assertThat(after.shouldApplyWorkaround(platform, browser, resize))
                            .isEqualTo(before.shouldApplyWorkaround(platform, browser, resize));
                }
            }
        }
        assertThat(after.shouldApplyWorkaround("MacOS", "IE11", 1)).isTrue();
    }

    @Test
    void polymorphismReplacesTheTypeSwitch() {
        var before = new com.example.quality.refactoring.before.PriceCalculator();
        var after = new com.example.quality.refactoring.after.PriceCalculator();

        assertThat(after.calculatePrice(new Book(100)))
                .isCloseTo(before.getPrice(new com.example.quality.refactoring.before.PriceCalculator.Product("BOOK", 100)), within(1e-9));
        assertThat(after.calculatePrice(new Electronics(100)))
                .isCloseTo(before.getPrice(new com.example.quality.refactoring.before.PriceCalculator.Product("ELECTRONICS", 100)), within(1e-9));
        assertThat(after.calculatePrice(new GeneralProduct(100)))
                .isEqualTo(before.getPrice(new com.example.quality.refactoring.before.PriceCalculator.Product("TOY", 100)));
    }
}
