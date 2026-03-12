package com.Campusland.ProyectoSpringBoot_CorpusEnrique.mappers;

import com.Campusland.ProyectoSpringBoot_CorpusEnrique.dto.request.PersonaRequest;
import com.Campusland.ProyectoSpringBoot_CorpusEnrique.dto.response.PersonaResponse;
import com.Campusland.ProyectoSpringBoot_CorpusEnrique.model.Persona;
import org.springframework.stereotype.Component;

@Component
public class PersonaMapper {

    public PersonaResponse entidadADto(Persona entidad) {
        if (entidad == null) { return null; }
        return new PersonaResponse(entidad.getIdPersona(),
                                   entidad.getNombre(),
                                   entidad.getApellido(),
                                   entidad.getTipoDocumento(),
                                   entidad.getNumeroDocumento());
    }

    public Persona dtoAEntidad(PersonaRequest dto) {
        if (dto == null) { return null; }
        Persona p = new Persona();
        p.setNombre(dto.nombre());
        p.setApellido(dto.apellido());
        p.setTipoDocumento(dto.tipoDocumento());
        p.setNumeroDocumento(dto.numeroDocumento());
        return p;
    }

    public void actualizarEntidadDesdeDTO(Persona entidad, PersonaRequest dto) {
        if (entidad == null || dto == null) { return; }
        entidad.setNombre(dto.nombre());
        entidad.setApellido(dto.apellido());
        entidad.setTipoDocumento(dto.tipoDocumento());
        entidad.setNumeroDocumento(dto.numeroDocumento());
    }
}
