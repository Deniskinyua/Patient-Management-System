package com.pms.apigateway.filters;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * {@code JwtValidationGatewayFilterFactory} is a custom Spring Cloud Gateway filter
 * that performs JWT token validation by forwarding the token to an external authentication service.
 * <p>
 * This filter is applied to routes defined in `application.yml` or `application.properties` by using the filter name {@code JwtValidation}.
 * It extracts the Authorization token from incoming requests, validates it via the authentication service,
 * and only allows the request to continue if the token is valid.
 * </p>
 *
 * <p><strong>Usage in application.yml:</strong></p
 *
 * @author DenisKinyua
 * @since 1.0
 */
@Component
public class JwtValidationGatewayFilterFactory extends AbstractGatewayFilterFactory<Object> {

    /**
     * Reactive WebClient used to call the external authentication service.
     */
    public final WebClient webClient;

    /**
     * Constructs the JWT Validation filter with a WebClient configured for the authentication service.
     *
     * @param webClientBuilder WebClient builder injected by Spring
     * @param authenticationServiceUrl Base URL of the authentication service (e.g., http://auth-service:4005)
     */
    public JwtValidationGatewayFilterFactory(
            WebClient.Builder webClientBuilder,
            @Value("${auth.service.url}") String authenticationServiceUrl) {
        this.webClient = webClientBuilder.baseUrl(authenticationServiceUrl).build();
    }

    /**
     * Applies the JWT validation logic. This method will:
     * <ul>
     *     <li>Extract the Authorization header</li>
     *     <li>Check if it exists and starts with "Bearer "</li>
     *     <li>Send a validation request to /auth/validate on the auth service</li>
     *     <li>Allow the request to proceed only if validation passes</li>
     *     <li>Return 401 Unauthorized if the token is missing or invalid</li>
     * </ul>
     *
     * @param config Not used currently (can be enhanced to accept configuration in future)
     * @return the GatewayFilter instance
     */
    @Override
    public GatewayFilter apply(Object config) {
        return (exchange, chain) -> {
            String token = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

            // Reject the request if the token is missing or malformed
            if (token == null || !token.startsWith("Bearer ")) {
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }

            // Validate token with auth service before forwarding the request
            return webClient.get()
                    .uri("/auth/validate")
                    .header(HttpHeaders.AUTHORIZATION, token)
                    .retrieve()
                    .toBodilessEntity()
                    .then(chain.filter(exchange));
        };
    }
}
