package com.Campusland.ProyectoSpringBoot_CorpusEnrique.dto.response;

import com.Campusland.ProyectoSpringBoot_CorpusEnrique.model.Usuario.Estado;

public record UsuarioResponse(
    Long idUsuario,
    String username,
    Estado estado,
    PersonaResponse persona,
    RolResponse rol,
    Long bodegaId
) {}
