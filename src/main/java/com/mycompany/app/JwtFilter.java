package com.mycompany.app;

import org.jose4j.jwa.AlgorithmConstraints;
import org.jose4j.jwa.AlgorithmConstraints.ConstraintType;
import org.jose4j.jwk.HttpsJwks;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.jose4j.keys.resolvers.HttpsJwksVerificationKeyResolver;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class JwtFilter implements Filter {

    public static JwtClaims verifyJwt(String token) throws InvalidJwtException {
        String JWKS_URL = "https://auth.nblocks.cloud/.well-known/jwks.json";

        try {
            // Verify the tokens result using public keys from Nblocks JWKS
            HttpsJwks httpsJkws = new HttpsJwks(JWKS_URL);
            HttpsJwksVerificationKeyResolver httpsJwksKeyResolver = new HttpsJwksVerificationKeyResolver(httpsJkws);

            JwtConsumer jwtConsumer = new JwtConsumerBuilder()
                    .setVerificationKeyResolver(httpsJwksKeyResolver)
                    .setExpectedIssuer("https://auth.nblocks.cloud")
                    .setExpectedAudience(App.APP_ID)
                    .setJwsAlgorithmConstraints(new AlgorithmConstraints(ConstraintType.PERMIT,
                            AlgorithmIdentifiers.RSA_PSS_USING_SHA256)) 
                    .build();

            JwtClaims jwtClaims = jwtConsumer.processToClaims(token);
            return jwtClaims;
        } catch (InvalidJwtException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String authHeader = httpRequest.getHeader("Authorization");
        // Only run verficiation if Authorization header is present
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring("Bearer ".length());
            try {
                JwtClaims jwtClaims = verifyJwt(token);
                httpRequest.setAttribute("user", jwtClaims.getClaimsMap());
            } catch (Exception e) {
                ((HttpServletResponse) response).sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token");
                return;
            }
        }
        chain.doFilter(request, response);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Initialization logic here
    }

    @Override
    public void destroy() {
        // Cleanup logic here
    }
}