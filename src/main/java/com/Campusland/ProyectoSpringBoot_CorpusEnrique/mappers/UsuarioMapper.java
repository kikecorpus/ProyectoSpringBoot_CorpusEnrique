package com.Campusland.ProyectoSpringBoot_CorpusEnrique.mappers;

import com.Campusland.ProyectoSpringBoot_CorpusEnrique.dto.request.UsuarioRequest;
import com.Campusland.ProyectoSpringBoot_CorpusEnrique.dto.response.UsuarioResponse;
import com.Campusland.ProyectoSpringBoot_CorpusEnrique.model.Persona;
import com.Campusland.ProyectoSpringBoot_CorpusEnrique.model.Rol;
import com.Campusland.ProyectoSpringBoot_CorpusEnrique.model.Usuario;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UsuarioMapper {

    private final PersonaMapper personaMapper;
    private final RolMapper rolMapper;

    public UsuarioResponse entidadADto(Usuario entidad) {
        if (entidad == null) { return null; }
        return new UsuarioResponse(entidad.getIdUsuario(),
                                   entidad.getUsername(),
                                   entidad.getEstado(),
                                   personaMapper.entidadADto(entidad.getPersona()),
                                   rolMapper.entidadADto(entidad.getRol()));
    }

    public Usuario dtoAEntidad(UsuarioRequest dto, Persona persona, Rol rol) {
        if (dto == null) { return null; }
        Usuario u = new Usuario();
        u.setUsername(dto.username());
        u.setContrasena(dto.contrasena()); // cifrado se hace en el servicio
        u.setEstado(dto.estado());
        u.setPersona(persona);
        u.setRol(rol);
        return u;
    }

    public void actualizarEntidadDesdeDTO(Usuario entidad, UsuarioRequest dto, Persona persona, Rol rol) {
        if (entidad == null || dto == null) { return; }
        entidad.setUsername(dto.username());
        entidad.setContrasena(dto.contrasena()); // cifrado se hace en el servicio
        entidad.setEstado(dto.estado());
        entidad.setPersona(persona);
        entidad.setRol(rol);
    }
}
