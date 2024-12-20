package com.example.SpringSecurity.filter;

import com.example.SpringSecurity.service.MyUserDetailService;
import com.example.SpringSecurity.util.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private ApplicationContext applicationContext;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        String username = null;
        String token = null;
        System.out.println("jwt filer line 38 Authenticated");

        // checking if request has valid bearer filter
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            // extracting token and username
            token = authHeader.substring(7);
            username = this.jwtService.extractUsername(token);
            System.out.println("Token " + token);
        }
        System.out.println("jwt filer line 46 Authenticated");
        // Valid token is found then uses extracted data to create authorized token which proceed to new filter
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // getting User from database by using service method
            UserDetails userDetails = applicationContext.getBean(MyUserDetailService.class).loadUserByUsername(username);
            System.out.println("like 52 := " + userDetails);
            if (jwtService.validateToken(token, userDetails)) {
                System.out.println("jwt filer line 52 Authenticated");
                // Token contains Validated user with details to authorize authorities based data
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                // This sets additional details, such as the remote IP address and session information,
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                // this updates current makes authToken as currently logged in and authenticated user
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
            System.out.println(" TOken validation failed");
        }

        // continues filter chain
        filterChain.doFilter(request, response);

    }
}
