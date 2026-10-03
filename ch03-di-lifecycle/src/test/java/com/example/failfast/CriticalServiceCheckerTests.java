package com.example.failfast;

import org.junit.jupiter.api.Test;
import org.springframework.boot.DefaultApplicationArguments;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatIllegalStateException;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CriticalServiceCheckerTests {

    private final ExternalService externalService = mock(ExternalService.class);
    private final CriticalServiceChecker checker = new CriticalServiceChecker(externalService);

    @Test
    void passesWhenServiceAvailable() {
        when(externalService.isAvailable()).thenReturn(true);
        assertThatCode(() -> checker.run(new DefaultApplicationArguments())).doesNotThrowAnyException();
    }

    @Test
    void failsFastWhenServiceUnavailable() {
        when(externalService.isAvailable()).thenReturn(false);
        assertThatIllegalStateException().isThrownBy(() -> checker.run(new DefaultApplicationArguments()))
                .withMessageContaining("not available");
    }
}
