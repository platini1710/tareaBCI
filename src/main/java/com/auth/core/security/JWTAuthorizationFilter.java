package com.auth.core.security;


import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;

public class JWTAuthorizationFilter extends OncePerRequestFilter {

	private final String HEADER = "Authorization";
	private final String PREFIX = "Bearer ";
	private final String SECRET = "mySecretKey";
	private static final Logger logger = LoggerFactory.getLogger(JWTAuthorizationFilter.class);
	DateTimeFormatter ZDT_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss a z");
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
		try {
			if (existeJWTToken(request, response)) {
				Claims claims = validateToken(request);
				
				System.out.println("authorities" + claims.get("Oauthorities"));
				if (claims.get("authorities") != null) {
					setUpSpringAuthentication(claims);
				} else {
					SecurityContextHolder.clearContext();
				}
			} else {
					SecurityContextHolder.clearContext();
			}
			chain.doFilter(request, response);
		} catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException e) {
			DateTimeFormatter ZDT_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss a z");
			logger.info("error, token incorrecto, copie el token  que aparece cuando grabo un  usuario , debe copiarlo  en el Head en el valor Authorization:::"+ ZDT_FORMATTER.format(ZonedDateTime.now()) );
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
			
	            response.setHeader("Content-Type", "application/json");  
		       response.getWriter().write("{ \"mensaje\": \"Error, token incorrecto, debe ingresar el valor del token correcto que aparece en la pestaña Headers con el key Authorization\" }");
		//	((HttpServletResponse) response).sendError(HttpServletResponse.SC_FORBIDDEN, "mensaje:debe ingresar el token correcto que aparece en el Head Authorization");

			return;
		}
	}	

	private Claims validateToken(HttpServletRequest request) {
		String jwtToken = request.getHeader(HEADER).replace(PREFIX, "");
		return Jwts.parser().setSigningKey(SECRET.getBytes()).parseClaimsJws(jwtToken).getBody();
	}

	/**
	 * Metodo para autenticarnos dentro del flujo de Spring
	 * 
	 * @param claims
	 */
	private void setUpSpringAuthentication(Claims claims) {
		System.out.println("authorities   ******************************************************authorities *************************************");
		@SuppressWarnings("unchecked")

		List<String> authorities = (List) claims.get("authorities");
		authorities.forEach( System.out::println);
		
		UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(claims.getSubject(), null,
				authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));

		System.out.println("authorities   ******************************************************auth.getName() *************************************" +auth.getName());
	//	System.out.println("authorities   ******************************************************auth.getName() *************************************" +auth.);
		SecurityContextHolder.getContext().setAuthentication(auth);

	}

	private boolean existeJWTToken(HttpServletRequest request, HttpServletResponse res) {
		String authenticationHeader = request.getHeader(HEADER);
		if (authenticationHeader == null || !authenticationHeader.startsWith(PREFIX))
			return false;
		return true;
	}

}
