package com.Campusland.ProyectoSpringBoot_CorpusEnrique.auth;

import com.Campusland.ProyectoSpringBoot_CorpusEnrique.config.JwtService;
import com.Campusland.ProyectoSpringBoot_CorpusEnrique.exception.BusinessRuleException;
import com.Campusland.ProyectoSpringBoot_CorpusEnrique.model.Usuario;
import com.Campusland.ProyectoSpringBoot_CorpusEnrique.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final JwtService jwtService;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {

        // 1. Busca el usuario por username en la base de datos
        UserDetails usuario = usuarioRepository.findByUsername(request.username())
                .orElseThrow(() -> new BusinessRuleException("Credenciales inválidas"));

        // 2. Verifica que el usuario esté activo
        if (!usuario.isEnabled()) {
            throw new BusinessRuleException("El usuario se encuentra inactivo");
        }

        // 3. Compara la contraseña ingresada contra el hash guardado en la BD
        if (!passwordEncoder.matches(request.password(), usuario.getPassword())) {
            throw new BusinessRuleException("Credenciales inválidas");
        }

        // 4. Si todo está bien, genera y retorna el token
        String token = jwtService.generateToken(usuario);
        return ResponseEntity.ok(new LoginResponse(token));
    }

}