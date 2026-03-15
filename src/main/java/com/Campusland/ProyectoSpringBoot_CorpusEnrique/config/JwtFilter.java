package com.Campusland.ProyectoSpringBoot_CorpusEnrique.config;

import com.Campusland.ProyectoSpringBoot_CorpusEnrique.repository.UsuarioRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter { // Cambiamos a OncePerRequestFilter

    private final JwtService jwtService;
    private final UsuarioRepository usuarioRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {


        String header = request.getHeader("Authorization");

        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            String username = jwtService.validateToken(token);

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails usuario = usuarioRepository.findByUsername(username).orElse(null);

                if (usuario != null) {
                    // Creamos la autenticación con el objeto Usuario completo
                    UsernamePasswordAuthenticationToken auth =
                            new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());

                    // Seteamos la seguridad
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            }

            // Continuamos la ejecución
            filterChain.doFilter(request, response);
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getServletPath();
        return path.startsWith("/swagger-ui") || path.startsWith("/v3/api-docs") || path.startsWith("/api-docs")|| path.equals("/auth/login")  || path.equals("/auth/hash");  // ← agregar

    }
}