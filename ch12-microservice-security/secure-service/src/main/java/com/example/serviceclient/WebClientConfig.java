package com.example.serviceclient;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.AuthorizedClientServiceOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProviderBuilder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    WebClient webClient(OAuth2AuthorizedClientManager authorizedClientManager) {
        ServletOAuth2AuthorizedClientExchangeFilterFunction oauth2Client = new ServletOAuth2AuthorizedClientExchangeFilterFunction(authorizedClientManager);
        oauth2Client.setDefaultClientRegistrationId("internal-service-client"); // Client registration ID for service-to-service calls
        return WebClient.builder()
                .apply(oauth2Client.oauth2Configuration())
                .build();
    }

    /*
     * Changed from the book: the book builds a DefaultOAuth2AuthorizedClientManager and fakes an
     * OAuth2AuthenticationToken in the SecurityContextHolder from the context-attributes mapper.
     * DefaultOAuth2AuthorizedClientManager only works inside an HTTP request, and overwriting the
     * SecurityContext replaces the current user's authentication. For the client_credentials grant,
     * AuthorizedClientServiceOAuth2AuthorizedClientManager is the intended manager: it needs no request
     * and no user, so it also works from scheduled jobs and message listeners.
     */
    @Bean
    public OAuth2AuthorizedClientManager authorizedClientManager(
            ClientRegistrationRepository clientRegistrationRepository,
            OAuth2AuthorizedClientService authorizedClientService) {

        AuthorizedClientServiceOAuth2AuthorizedClientManager authorizedClientManager =
                new AuthorizedClientServiceOAuth2AuthorizedClientManager(clientRegistrationRepository, authorizedClientService);
        authorizedClientManager.setAuthorizedClientProvider(OAuth2AuthorizedClientProviderBuilder.builder()
                .clientCredentials()
                .build());
        return authorizedClientManager;
    }
}
