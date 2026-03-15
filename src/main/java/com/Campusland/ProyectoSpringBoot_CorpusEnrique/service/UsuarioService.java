package com.Campusland.ProyectoSpringBoot_CorpusEnrique.service;

import com.Campusland.ProyectoSpringBoot_CorpusEnrique.dto.request.UsuarioRequest;
import com.Campusland.ProyectoSpringBoot_CorpusEnrique.dto.response.UsuarioResponse;
import java.util.List;

public interface UsuarioService {
    UsuarioResponse guardarUsuario(UsuarioRequest dto);
    List<UsuarioResponse> listarUsuarios();
    UsuarioResponse obtenerUsuarioPorId(Long id);
    UsuarioResponse actualizarUsuario(Long id, UsuarioRequest dto);
    void eliminarUsuario(Long id);
}
