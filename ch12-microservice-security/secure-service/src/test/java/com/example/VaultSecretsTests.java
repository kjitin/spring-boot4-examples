package com.example;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.vault.VaultContainer;

import static org.assertj.core.api.Assertions.assertThat;

// Requires Docker (skipped otherwise): a real Vault server provides the datasource credentials
// through spring.config.import=vault://
@Testcontainers(disabledWithoutDocker = true)
@SpringBootTest
@ActiveProfiles("vault")
class VaultSecretsTests {

    @Container
    static final VaultContainer<?> vault = new VaultContainer<>("hashicorp/vault:1.20")
            .withVaultToken("root-token")
            .withInitCommand("kv put secret/application datasource.username=vault-user datasource.password=vault-pass");

    // Config data (spring.config.import) is resolved before @DynamicPropertySource values are applied, so the
    // container coordinates are passed as system properties. This runs after the container has started and
    // before Spring creates the application context.
    @BeforeAll
    static void vaultCoordinates() {
        System.setProperty("spring.cloud.vault.host", vault.getHost());
        System.setProperty("spring.cloud.vault.port", String.valueOf(vault.getFirstMappedPort()));
        System.setProperty("spring.cloud.vault.token", "root-token");
    }

    @AfterAll
    static void clearCoordinates() {
        System.clearProperty("spring.cloud.vault.host");
        System.clearProperty("spring.cloud.vault.port");
        System.clearProperty("spring.cloud.vault.token");
    }

    @Value("${spring.datasource.username}")
    String username;

    @Value("${spring.datasource.password}")
    String password;

    @Test
    void datasourceCredentialsComeFromVault() {
        assertThat(username).isEqualTo("vault-user");
        assertThat(password).isEqualTo("vault-pass");
    }
}
