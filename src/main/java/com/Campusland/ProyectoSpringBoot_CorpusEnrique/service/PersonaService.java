package com.Campusland.ProyectoSpringBoot_CorpusEnrique.service;

import com.Campusland.ProyectoSpringBoot_CorpusEnrique.dto.request.PersonaRequest;
import com.Campusland.ProyectoSpringBoot_CorpusEnrique.dto.response.PersonaResponse;
import java.util.List;

public interface PersonaService {
    PersonaResponse guardarPersona(PersonaRequest dto);
    List<PersonaResponse> listarPersonas();
    PersonaResponse obtenerPersonaPorId(Long id);
    PersonaResponse actualizarPersona(Long id, PersonaRequest dto);
    void eliminarPersona(Long id);
}
