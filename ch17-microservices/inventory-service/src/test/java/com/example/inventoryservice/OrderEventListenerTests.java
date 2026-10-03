package com.example.inventoryservice;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.cloud.stream.binder.test.InputDestination;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.messaging.support.MessageBuilder;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(properties = "spring.cloud.stream.default-binder=integration")
@Import(TestChannelBinderConfiguration.class)
@ExtendWith(OutputCaptureExtension.class)
class OrderEventListenerTests {

    @Autowired
    InputDestination input;

    @Test
    void consumesOrderEvents(CapturedOutput output) {
        input.send(MessageBuilder.withPayload("{\"orderId\":\"order-123\", \"status\":\"PLACED\"}".getBytes()).build(),
                "order-events");

        assertThat(output).contains("Received order event: {\"orderId\":\"order-123\", \"status\":\"PLACED\"}");
    }
}
