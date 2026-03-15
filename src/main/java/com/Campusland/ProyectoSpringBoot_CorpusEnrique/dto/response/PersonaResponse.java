package com.Campusland.ProyectoSpringBoot_CorpusEnrique.dto.response;

import com.Campusland.ProyectoSpringBoot_CorpusEnrique.model.Persona.TipoDocumento;

public record PersonaResponse(
    Long idPersona,
    String nombre,
    String apellido,
    TipoDocumento tipoDocumento,
    String numeroDocumento
) {}
