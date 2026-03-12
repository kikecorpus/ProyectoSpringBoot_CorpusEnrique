package com.Campusland.ProyectoSpringBoot_CorpusEnrique.mappers;

import com.Campusland.ProyectoSpringBoot_CorpusEnrique.dto.response.AuditoriaResponse;
import com.Campusland.ProyectoSpringBoot_CorpusEnrique.model.Auditoria;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuditoriaMapper {

    private final UsuarioMapper usuarioMapper;

    public AuditoriaResponse entidadADto(Auditoria entidad) {
        if (entidad == null) { return null; }
        return new AuditoriaResponse(entidad.getIdAuditoria(),
                                     usuarioMapper.entidadADto(entidad.getUsuario()),
                                     entidad.getEntidadAfectada(),
                                     entidad.getRegistroId(),
                                     entidad.getTipoOperacion(),
                                     entidad.getValoresAnteriores(),
                                     entidad.getValoresNuevos(),
                                     entidad.getFechaHora());
    }

}
