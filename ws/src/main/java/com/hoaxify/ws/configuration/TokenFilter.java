package com.hoaxify.ws.configuration;

import java.io.IOException;

import org.aspectj.weaver.NewConstructorTypeMunger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import com.hoaxify.ws.auth.AuthService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Gelen tum request'leri bu filtreden gecirecegiz
 * @author mehmetfevziakdeniz
 *
 */

public class TokenFilter extends OncePerRequestFilter{
	
	@Autowired
	AuthService authService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		String authorization = request.getHeader("Authorization");
		if(authorization != null) {
			// token'in basindaki 'Bearer ' kismi atiyoruz
			String token = authorization.substring(7);
			
			// token'dan yola cikarak userDetail elde ediyoruz
			UserDetails user = authService.getUserDetails(token);
			// elimizdeki user ile spring security'i biraraya getirelim
			UsernamePasswordAuthenticationToken authenticationToken = new 
					UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
			authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
			SecurityContextHolder.getContext().setAuthentication(authenticationToken);
		}
		
		filterChain.doFilter(request, response);
		
	}

}
