package com.ims.auth;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

// OncePerRequestFilter = guaranteed to run exactly ONCE per HTTP request.
// The FLOW for every request:
//
//   Browser sends: GET /api/products
//                  Authorization: Bearer eyJhbGci...
//          |
//          v
//   [JwtAuthFilter] ← YOU ARE HERE
//     1. Extract token from header
//     2. Validate token
//     3. Set SecurityContext (tell Spring "this user is authenticated")
//          |
//          v
//   [SecurityConfig] checks if user's role is allowed for this endpoint
//          |
//          v
//   [ProductController.getAll()] runs and returns data

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // STEP 1: Get the Authorization header
        final String authHeader = request.getHeader("Authorization");

        // STEP 2: No header or doesn't start with "Bearer " → skip (not authenticated)
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // STEP 3: Extract token (everything after "Bearer ")
        final String token = authHeader.substring(7);

        // STEP 4: Extract username from the token
        final String username = jwtService.extractUsername(token);

        // STEP 5: If username found AND no authentication set yet
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // STEP 6: Look up user in database
            User user = userRepository.findByUsername(username).orElse(null);

            // STEP 7: Validate token
            if (user != null && jwtService.isTokenValid(token, username)) {

                // STEP 8: Create auth token with user's role
                // "ROLE_" prefix is a Spring Security convention — ROLE_ADMIN, ROLE_MANAGER, ROLE_STAFF
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                user,           // principal (the logged-in user object)
                                null,           // credentials (not needed — already validated via JWT)
                                List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()))
                        );

                // STEP 9: Tell Spring: "this request is from 'admin' with role 'ROLE_ADMIN'"
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // STEP 10: Continue to the next filter → eventually reaches the controller
        filterChain.doFilter(request, response);
    }
}