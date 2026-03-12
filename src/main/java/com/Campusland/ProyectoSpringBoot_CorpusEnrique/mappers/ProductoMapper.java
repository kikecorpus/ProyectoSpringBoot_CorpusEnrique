package com.Campusland.ProyectoSpringBoot_CorpusEnrique.mappers;

import com.Campusland.ProyectoSpringBoot_CorpusEnrique.dto.request.ProductoRequest;
import com.Campusland.ProyectoSpringBoot_CorpusEnrique.dto.response.ProductoResponse;
import com.Campusland.ProyectoSpringBoot_CorpusEnrique.model.Categoria;
import com.Campusland.ProyectoSpringBoot_CorpusEnrique.model.Producto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductoMapper {

    private final CategoriaMapper categoriaMapper;

    public ProductoResponse entidadADto(Producto entidad) {
        if (entidad == null) { return null; }
        return new ProductoResponse(entidad.getIdProducto(),
                                    entidad.getNombre(),
                                    entidad.getCodigo(),
                                    entidad.getUnidadMedida(),
                                    categoriaMapper.entidadADto(entidad.getCategoria()));
    }

    public Producto dtoAEntidad(ProductoRequest dto, Categoria categoria) {
        if (dto == null) { return null; }
        Producto p = new Producto();
        p.setNombre(dto.nombre());
        p.setCodigo(dto.codigo());
        p.setUnidadMedida(dto.unidadMedida());
        p.setCategoria(categoria);
        return p;
    }

    public void actualizarEntidadDesdeDTO(Producto entidad, ProductoRequest dto, Categoria categoria) {
        if (entidad == null || dto == null) { return; }
        entidad.setNombre(dto.nombre());
        entidad.setCodigo(dto.codigo());
        entidad.setUnidadMedida(dto.unidadMedida());
        entidad.setCategoria(categoria);
    }
}
