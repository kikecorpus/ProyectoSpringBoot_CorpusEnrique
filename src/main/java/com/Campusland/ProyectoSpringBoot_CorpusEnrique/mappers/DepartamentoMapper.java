package com.Campusland.ProyectoSpringBoot_CorpusEnrique.mappers;

import com.Campusland.ProyectoSpringBoot_CorpusEnrique.dto.request.DepartamentoRequest;
import com.Campusland.ProyectoSpringBoot_CorpusEnrique.dto.response.DepartamentoResponse;
import com.Campusland.ProyectoSpringBoot_CorpusEnrique.model.Departamento;
import org.springframework.stereotype.Component;

@Component
public class DepartamentoMapper {

    public DepartamentoResponse entidadADto(Departamento entidad){
        if(entidad == null){ return null;}
        return new DepartamentoResponse(entidad.getIdDepartamento(),
                                        entidad.getNombre()
                                        );
    }

    public Departamento dtoAEntidad(DepartamentoRequest dto){
        if(dto == null){ return null;}
        Departamento d = new Departamento();
        d.setNombre(dto.nombre());
        return d;
    }

    public void  actualizarEntidadDesdeDTO(Departamento entidad, DepartamentoRequest dto){
        if(entidad == null || dto == null){ return;}
        entidad.setNombre(dto.nombre());
    }

}
