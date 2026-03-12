package com.Campusland.ProyectoSpringBoot_CorpusEnrique.dto.response;

import com.Campusland.ProyectoSpringBoot_CorpusEnrique.model.Auditoria.TipoOperacion;
import java.time.LocalDateTime;
import java.util.Map;

public record AuditoriaResponse ( Integer idAuditoria,
        UsuarioResponse usuario,
        String entidadAfectada,
        Integer registroId,
        TipoOperacion tipoOperacion,
        Map<String, Object> valoresAnteriores,
        Map<String, Object> valoresNuevos,
        LocalDateTime fechaHora){


}
