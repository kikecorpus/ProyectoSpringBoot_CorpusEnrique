package com.Campusland.ProyectoSpringBoot_CorpusEnrique.config;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtService {
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;


    private SecretKey getKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String generateToken(UserDetails user) {
        return Jwts.builder()
                // Información principal del token (quién es el usuario)
                .setSubject(user.getUsername())

                //  Rol del usuario para control de acceso en el frontend
                .claim("rol", user.getAuthorities().stream()
                        .findFirst()
                        .map(a -> a.getAuthority())
                        .orElse(""))

                // Fecha en que se crea el token
                .setIssuedAt(new Date())

                // Fecha en que expira
                .setExpiration(new Date(System.currentTimeMillis() + expiration))

                // Firma con algoritmo HS256 usando mi clave secreta
                .signWith(getKey(), SignatureAlgorithm.HS256)

                // Construye el token final en formato String
                .compact();

    }

    public String validateToken(String token) {

        try {
            return Jwts.parser()
                    .setSigningKey(getKey())
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        } catch (Exception e) {

            return null;
        }
    }

    public String getRolFromToken(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(getKey())
                    .parseClaimsJws(token)
                    .getBody()
                    .get("rol", String.class);
        } catch (Exception e) {
            return null;
        }
    }
}
