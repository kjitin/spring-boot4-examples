package com.example.validation;

import com.example.api.product.dto.ProductRequest;
import com.example.validation.groups.OnCreate;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ValidProductNameTests {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void composedConstraintAppliesNotBlankAndSize() {
        assertThat(validator.validate(new ProductRequest(null, "OK name", 1, null), OnCreate.class)).isEmpty();
        assertThat(validator.validate(new ProductRequest(null, "x", 1, null), OnCreate.class)).isNotEmpty();
        assertThat(validator.validate(new ProductRequest(null, "x".repeat(101), 1, null), OnCreate.class)).isNotEmpty();
    }

    @Test
    void constraintsAreIgnoredForDefaultGroup() {
        // All constraints on ProductRequest are bound to OnCreate/OnUpdate
        assertThat(validator.validate(new ProductRequest(1L, "", -1, null))).isEmpty();
    }
}
