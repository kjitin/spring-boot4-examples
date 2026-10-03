package com.example.oauth2;

import com.example.oauth2.config.OAuth2LoginConfig;
import com.example.oauth2.controller.UserInfoController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.oauth2.core.oidc.StandardClaimNames;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oidcLogin;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// Uses explicit provider endpoints instead of issuer-uri so no identity provider is needed for the test
@WebMvcTest(controllers = UserInfoController.class)
@Import({OAuth2LoginConfig.class, UserInfoController.class})
@TestPropertySource(properties = {
        "spring.security.oauth2.client.registration.keycloak.client-id=spring-client",
        "spring.security.oauth2.client.registration.keycloak.client-secret=secret",
        "spring.security.oauth2.client.registration.keycloak.client-authentication-method=client_secret_post",
        "spring.security.oauth2.client.registration.keycloak.authorization-grant-type=authorization_code",
        "spring.security.oauth2.client.registration.keycloak.redirect-uri={baseUrl}/login/oauth2/code/{registrationId}",
        "spring.security.oauth2.client.registration.keycloak.scope=openid,profile,email",
        "spring.security.oauth2.client.provider.keycloak.authorization-uri=http://localhost:8080/realms/master/protocol/openid-connect/auth",
        "spring.security.oauth2.client.provider.keycloak.token-uri=http://localhost:8080/realms/master/protocol/openid-connect/token",
        "spring.security.oauth2.client.provider.keycloak.jwk-set-uri=http://localhost:8080/realms/master/protocol/openid-connect/certs",
        "spring.security.oauth2.client.provider.keycloak.user-name-attribute=preferred_username"
})
class OAuth2LoginTests {

    @Autowired
    MockMvc mockMvc;

    @Test
    void unauthenticatedUsersAreSentToTheIdentityProvider() throws Exception {
        mockMvc.perform(get("/userinfo"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/oauth2/authorization/keycloak"));
    }

    @Test
    void readsOidcUserClaims() throws Exception {
        mockMvc.perform(get("/userinfo").with(oidcLogin().idToken(token -> token
                        .claim(StandardClaimNames.NAME, "Ada Lovelace")
                        .claim(StandardClaimNames.EMAIL, "ada@example.com")
                        .claim(StandardClaimNames.PREFERRED_USERNAME, "ada"))))
                .andExpect(status().isOk())
                .andExpect(content().string("Hello, Ada Lovelace! Email: ada@example.com, Username: ada"));
    }
}
