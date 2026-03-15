package com.Campusland.ProyectoSpringBoot_CorpusEnrique.service.impl;

import com.Campusland.ProyectoSpringBoot_CorpusEnrique.dto.request.PersonaRequest;
import com.Campusland.ProyectoSpringBoot_CorpusEnrique.dto.request.RolRequest;
import com.Campusland.ProyectoSpringBoot_CorpusEnrique.dto.request.UsuarioRequest;
import com.Campusland.ProyectoSpringBoot_CorpusEnrique.dto.response.UsuarioResponse;
import com.Campusland.ProyectoSpringBoot_CorpusEnrique.exception.BusinessRuleException;
import com.Campusland.ProyectoSpringBoot_CorpusEnrique.mappers.PersonaMapper;
import com.Campusland.ProyectoSpringBoot_CorpusEnrique.mappers.RolMapper;
import com.Campusland.ProyectoSpringBoot_CorpusEnrique.mappers.UsuarioMapper;
import com.Campusland.ProyectoSpringBoot_CorpusEnrique.model.Persona;
import com.Campusland.ProyectoSpringBoot_CorpusEnrique.model.Rol;
import com.Campusland.ProyectoSpringBoot_CorpusEnrique.model.Usuario;
import com.Campusland.ProyectoSpringBoot_CorpusEnrique.repository.PersonaRepository;
import com.Campusland.ProyectoSpringBoot_CorpusEnrique.repository.RolRepository;
import com.Campusland.ProyectoSpringBoot_CorpusEnrique.repository.UsuarioRepository;
import com.Campusland.ProyectoSpringBoot_CorpusEnrique.service.UsuarioService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioMapper usuarioMapper;
    private final UsuarioRepository usuarioRepository;
    private final PersonaRepository personaRepository;
    private final PersonaMapper personaMapper;
    private final RolMapper rolMapper;
    private final RolRepository rolRepository;
   // private final PasswordEncoder passwordEncoder;

    @Override
    public UsuarioResponse guardarUsuario(UsuarioRequest dto) {
        if (usuarioRepository.existsByUsername(dto.username())) {
            throw new BusinessRuleException("Ya existe un usuario con el username: " + dto.username());
        }

        Persona persona = personaRepository.findById(dto.personaId())
                .orElseThrow(() -> new EntityNotFoundException("Persona no encontrada con id: " + dto.personaId()));
        Rol rol = rolRepository.findById(dto.rolId())
                .orElseThrow(() -> new EntityNotFoundException("Rol no encontrado con id: " + dto.rolId()));

        Usuario u = usuarioMapper.dtoAEntidad(dto, persona, rol);
        //u.setContrasena(passwordEncoder.encode(dto.contrasena()));
        usuarioRepository.save(u);
        return usuarioMapper.entidadADto(u);
    }

    @Override
    public List<UsuarioResponse> listarUsuarios() {
        return usuarioRepository.findAll()
                .stream()
                .map(usuarioMapper::entidadADto)
                .toList();
    }

    @Override
    public UsuarioResponse obtenerUsuarioPorId(Long id) {
        Usuario u = usuarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con id: " + id));
        return usuarioMapper.entidadADto(u);
    }

    @Override
    public UsuarioResponse actualizarUsuario(Long id, UsuarioRequest dto) {
        Usuario u = usuarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con id: " + id));
        if (usuarioRepository.existsByUsername(dto.username()) && !u.getUsername().equals(dto.username())) {
            throw new BusinessRuleException("Ya existe un usuario con el username: " + dto.username());
        }
        Persona persona = personaRepository.findById(dto.personaId())
                .orElseThrow(() -> new EntityNotFoundException("Persona no encontrada con id: " + dto.personaId()));
        Rol rol = rolRepository.findById(dto.rolId())
                .orElseThrow(() -> new EntityNotFoundException("Rol no encontrado con id: " + dto.rolId()));

        usuarioMapper.actualizarEntidadDesdeDTO(u, dto, persona, rol);
        //u.setContrasena(passwordEncoder.encode(dto.contrasena()));
        usuarioRepository.save(u);
        return usuarioMapper.entidadADto(u);
    }

    @Override
    public void eliminarUsuario(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new EntityNotFoundException("Usuario no encontrado con id: " + id);
        }
        usuarioRepository.deleteById(id);
    }
}
