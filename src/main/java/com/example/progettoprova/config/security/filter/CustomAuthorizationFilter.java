package com.example.progettoprova.config.security.filter;

import com.example.progettoprova.config.security.SecurityConstants;
import com.example.progettoprova.config.security.TokenStoreJwt;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@Slf4j
//leggo request per cercare presenza token inviato dall'utente
public class CustomAuthorizationFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String uri = request.getRequestURI();
        String token = null;
        String authorizationHeader = request.getHeader(AUTHORIZATION);

        logger.info("Authroziation Request URI: " + uri);
        logger.info("Authorization Header : " + authorizationHeader);

        if (authorizationHeader == null || (!authorizationHeader.startsWith(SecurityConstants.BASIC_TOKEN_PREFIX) && !authorizationHeader.startsWith(SecurityConstants.BEARER_TOKEN_PREFIX))) {
            this.logger.trace("Header Authorization non trovato!");
            log.info("Header Authorization non trovato");
            filterChain.doFilter(request, response);
            return;
        }

        if ((authorizationHeader.startsWith(SecurityConstants.BASIC_TOKEN_PREFIX) && uri.endsWith(SecurityConstants.LOGIN_URI_ENDING))
            || uri.endsWith(SecurityConstants.REFRESH_TOKEN_URI_ENDING)) {
            log.info("if "+SecurityConstants.LOGIN_URI_ENDING);
            filterChain.doFilter(request, response);

        } else {

            if(authorizationHeader != null && authorizationHeader.startsWith(SecurityConstants.BEARER_TOKEN_PREFIX) && !uri.endsWith(SecurityConstants.LOGIN_URI_ENDING)) {
                log.info(SecurityConstants.BEARER_TOKEN_PREFIX);
                try {
                    token = authorizationHeader.substring("Bearer ".length());
                    UsernamePasswordAuthenticationToken authenticationToken = TokenStoreJwt.parseToken(token);
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    filterChain.doFilter(request, response);
                }
                catch (Exception e) {
                    log.error(String.format("Errore Token: %s", token), e);
                    response.setStatus(FORBIDDEN.value());
                    Map<String, String> error = new HashMap<>();
                    error.put("errorMessage", e.getMessage());
                    response.setContentType(APPLICATION_JSON_VALUE);
                    new ObjectMapper().writeValue(response.getOutputStream(), error);
                }
            } else {
                log.info("No bearer no basic");
                filterChain.doFilter(request, response);
            }
        }
    }
}