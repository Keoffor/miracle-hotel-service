package com.miracle.keoffor.config.jwt;

import com.miracle.keoffor.config.user_auth.HotelUserDetailsService;
import com.miracle.keoffor.constants.JwtCredConstant;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
@Component
public class JwtAuthFilter extends OncePerRequestFilter {
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private HotelUserDetailsService hotelUserDetailsService;
    private static final Logger logger = LoggerFactory.getLogger(JwtAuthFilter.class);
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        try{
            String authHeader = request.getHeader(JwtCredConstant.AUTHORIZATION.getJwtCred());
            String userName = null;
            if(StringUtils.hasText(authHeader) && authHeader.startsWith(JwtCredConstant.BEARER.getJwtCred())){
                String jwtToken = authHeader.substring(7);
                userName = jwtUtils.extractUserNameFromToken(jwtToken);
            }
            if(userName !=null && SecurityContextHolder.getContext().getAuthentication()==null){
                UserDetails userDetails = hotelUserDetailsService.loadUserByUsername(userName);
                var authToken = new UsernamePasswordAuthenticationToken
                        (userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        } catch (Exception e){
            logger.error("Cannot set user authntication: {} ",e.getMessage());
        }

        filterChain.doFilter(request,response);
    }
}
