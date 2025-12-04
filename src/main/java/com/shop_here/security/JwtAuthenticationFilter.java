package com.shop_here.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");

        System.out.println("authHeader: " + authHeader);
        if(authHeader !=null && authHeader.startsWith("Bearer ")){
            String token = authHeader.substring(7);
            System.out.println("token: " + token);
            String username = jwtUtil.extractUsername(token);

            System.out.println("username: " + username);

            if(username != null && SecurityContextHolder.getContext().getAuthentication() == null){
                System.out.println("Inside getAuthentication: ");
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                System.out.println("Authorities: " + userDetails.getAuthorities());

                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,null,userDetails.getAuthorities());

                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
            System.out.println("getDetails: "
                    + SecurityContextHolder.getContext().getAuthentication().getDetails());
            System.out.println("getAuthorities: "
                    + SecurityContextHolder.getContext().getAuthentication().getAuthorities());
        }

        filterChain.doFilter(request,response);
    }
}
