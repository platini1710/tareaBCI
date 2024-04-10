package com.auth.core.security;

import java.util.Base64;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class JWTEncodeTest {
    public static void main(String[] args) {
        try {

        	String secretkey="qwertypassword";
            byte[] decodedSecret = Base64.getDecoder().decode(secretkey);
            //The JWT signature algorithm we will be using to sign the token
            String jwtToken = Jwts.builder()
                .setSubject("admin")
                .setAudience("Solr")
                .signWith(SignatureAlgorithm.HS256,decodedSecret).compact();
            System.out.println("jwtToken=");
            System.out.println(jwtToken);
        } catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }
}
