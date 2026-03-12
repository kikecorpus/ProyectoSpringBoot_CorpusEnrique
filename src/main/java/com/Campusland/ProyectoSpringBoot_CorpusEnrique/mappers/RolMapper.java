package com.Campusland.ProyectoSpringBoot_CorpusEnrique.mappers;

import com.Campusland.ProyectoSpringBoot_CorpusEnrique.dto.request.RolRequest;
import com.Campusland.ProyectoSpringBoot_CorpusEnrique.dto.response.RolResponse;
import com.Campusland.ProyectoSpringBoot_CorpusEnrique.model.Rol;
import org.springframework.stereotype.Component;

@Component
public class RolMapper {

    public RolResponse entidadADto(Rol entidad) {
        if (entidad == null) { return null; }
        return new RolResponse(entidad.getIdRol(),
                               entidad.getNombre(),
                               entidad.getDescripcion());
    }

    public Rol dtoAEntidad(RolRequest dto) {
        if (dto == null) { return null; }
        Rol r = new Rol();
        r.setNombre(dto.nombre());
        r.setDescripcion(dto.descripcion());
        return r;
    }

    public void actualizarEntidadDesdeDTO(Rol entidad, RolRequest dto) {
        if (entidad == null || dto == null) { return; }
        entidad.setNombre(dto.nombre());
        entidad.setDescripcion(dto.descripcion());
    }
}
