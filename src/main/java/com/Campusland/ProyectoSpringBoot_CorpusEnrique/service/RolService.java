package com.Campusland.ProyectoSpringBoot_CorpusEnrique.service;

import com.Campusland.ProyectoSpringBoot_CorpusEnrique.dto.request.RolRequest;
import com.Campusland.ProyectoSpringBoot_CorpusEnrique.dto.response.RolResponse;
import java.util.List;

public interface RolService {
    RolResponse guardarRol(RolRequest dto);
    List<RolResponse> listarRoles();
    RolResponse obtenerRolPorId(Long id);
    RolResponse actualizarRol(Long id, RolRequest dto);
    void eliminarRol(Long id);
}
