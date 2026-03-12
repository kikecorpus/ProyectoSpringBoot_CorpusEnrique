package com.Campusland.ProyectoSpringBoot_CorpusEnrique.mappers;

import com.Campusland.ProyectoSpringBoot_CorpusEnrique.dto.request.CategoriaRequest;
import com.Campusland.ProyectoSpringBoot_CorpusEnrique.dto.response.CategoriaResponse;
import com.Campusland.ProyectoSpringBoot_CorpusEnrique.model.Categoria;
import org.springframework.stereotype.Component;

@Component
public class CategoriaMapper {

    public CategoriaResponse entidadADto(Categoria entidad) {
        if (entidad == null) { return null; }
        return new CategoriaResponse(entidad.getIdCategoria(),
                                     entidad.getNombre());
    }

    public Categoria dtoAEntidad(CategoriaRequest dto) {
        if (dto == null) { return null; }
        Categoria c = new Categoria();
        c.setNombre(dto.nombre());
        return c;
    }

    public void actualizarEntidadDesdeDTO(Categoria entidad, CategoriaRequest dto) {
        if (entidad == null || dto == null) { return; }
        entidad.setNombre(dto.nombre());
    }
}
