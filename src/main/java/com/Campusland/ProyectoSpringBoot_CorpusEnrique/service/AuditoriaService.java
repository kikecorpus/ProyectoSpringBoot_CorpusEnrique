package com.Campusland.ProyectoSpringBoot_CorpusEnrique.service;

import com.Campusland.ProyectoSpringBoot_CorpusEnrique.dto.response.AuditoriaResponse;
import com.Campusland.ProyectoSpringBoot_CorpusEnrique.model.Auditoria.TipoOperacion;
import java.util.List;
import java.util.Map;

public interface AuditoriaService {
    List<AuditoriaResponse> listarAuditorias();
    AuditoriaResponse obtenerAuditoriaPorId(Long id);
    List<AuditoriaResponse> listarPorEntidad(String entidadAfectada);
    List<AuditoriaResponse> listarPorUsuario(Long usuarioId);


    void registrarAuditoria(Long usuarioId,
                            String entidadAfectada,
                            Long registroId,
                            TipoOperacion tipoOperacion,
                            Map<String, Object> valoresAnteriores,
                            Map<String, Object> valoresNuevos);
}
