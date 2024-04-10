package com.bci.tareas.helper;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class Utils {
	public static String getJWTToken(String username) {
		String secretKey = "mySecretKey";
		List<GrantedAuthority> grantedAuthorities = AuthorityUtils
				.commaSeparatedStringToAuthorityList("ROLE_USER");
		
		String token = Jwts
				.builder()
				.setId("softtekJWT")
				.setSubject(username)
				.claim("authorities",
						grantedAuthorities.stream()
								.map(GrantedAuthority::getAuthority)
								.collect(Collectors.toList()))
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + 600000))
				.signWith(SignatureAlgorithm.HS256,
						secretKey.getBytes()).compact();

		return "Bearer " + token;
	}
	
	public static ResponseEntity<ErrorResp> getError(String str) {
	
	    HttpHeaders headers = new HttpHeaders();
	    headers.add("usuario", "no existe");
	    ErrorResp errorResp=new ErrorResp();
	    errorResp.setCodigo( HttpStatus.INTERNAL_SERVER_ERROR.value());
	    errorResp.setDetail("se ha producido un error, rut no encontrado ");
	    errorResp.setTimestamp(str);

		return new ResponseEntity<>(
				errorResp, headers, HttpStatus.INTERNAL_SERVER_ERROR);


}
}
