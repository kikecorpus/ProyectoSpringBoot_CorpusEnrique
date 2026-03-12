package com.Campusland.ProyectoSpringBoot_CorpusEnrique.mappers;

import com.Campusland.ProyectoSpringBoot_CorpusEnrique.dto.request.CiudadRequest;
import com.Campusland.ProyectoSpringBoot_CorpusEnrique.dto.response.CiudadResponse;
import com.Campusland.ProyectoSpringBoot_CorpusEnrique.model.Ciudad;
import com.Campusland.ProyectoSpringBoot_CorpusEnrique.model.Departamento;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CiudadMapper {

    private final DepartamentoMapper departamentoMapper;

    public CiudadResponse entidadADto(Ciudad entidad) {
        if (entidad == null) { return null; }
        return new CiudadResponse(entidad.getIdCiudad(),
                                  entidad.getNombre(),
                                  departamentoMapper.entidadADto(entidad.getDepartamento()));
    }

    public Ciudad dtoAEntidad(CiudadRequest dto, Departamento departamento) {
        if (dto == null) { return null; }
        Ciudad c = new Ciudad();
        c.setNombre(dto.nombre());
        c.setDepartamento(departamento);
        return c;
    }

    public void actualizarEntidadDesdeDTO(Ciudad entidad, CiudadRequest dto, Departamento departamento) {
        if (entidad == null || dto == null) { return; }
        entidad.setNombre(dto.nombre());
        entidad.setDepartamento(departamento);
    }
}
