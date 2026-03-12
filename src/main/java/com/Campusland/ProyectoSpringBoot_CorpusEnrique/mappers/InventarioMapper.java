package com.Campusland.ProyectoSpringBoot_CorpusEnrique.mappers;

import com.Campusland.ProyectoSpringBoot_CorpusEnrique.dto.request.InventarioRequest;
import com.Campusland.ProyectoSpringBoot_CorpusEnrique.dto.response.InventarioResponse;
import com.Campusland.ProyectoSpringBoot_CorpusEnrique.dto.response.InventarioResumenResponse;
import com.Campusland.ProyectoSpringBoot_CorpusEnrique.model.Bodega;
import com.Campusland.ProyectoSpringBoot_CorpusEnrique.model.Inventario;
import com.Campusland.ProyectoSpringBoot_CorpusEnrique.model.Producto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InventarioMapper {

    private final ProductoMapper productoMapper;
    private final BodegaMapper bodegaMapper;

    public InventarioResponse entidadADto(Inventario entidad) {
        if (entidad == null) { return null; }
        boolean stockCritico = entidad.getCantidadActual() <= entidad.getStockMinimo();
        return new InventarioResponse(entidad.getIdInventario(),
                                      productoMapper.entidadADto(entidad.getProducto()),
                                      bodegaMapper.entidadADto(entidad.getBodega()),
                                      entidad.getCantidadActual(),
                                      entidad.getStockMinimo(),
                                      entidad.getStockMaximo(),
                                      entidad.getEstado(),
                                      entidad.getUltimaActualizacion(),
                                      stockCritico);
    }

    public InventarioResumenResponse entidadAResumen(Inventario entidad) {
        if (entidad == null) { return null; }
        return InventarioResumenResponse.builder()
                .idInventario(entidad.getIdInventario())
                .nombreProducto(entidad.getProducto().getNombre())
                .codigoProducto(entidad.getProducto().getCodigo())
                .nombreBodega(entidad.getBodega().getNombre())
                .build();
    }

    public Inventario dtoAEntidad(InventarioRequest dto, Producto producto, Bodega bodega) {
        if (dto == null) { return null; }
        Inventario i = new Inventario();
        i.setProducto(producto);
        i.setBodega(bodega);
        i.setCantidadActual(dto.cantidadActual());
        i.setStockMinimo(dto.stockMinimo());
        i.setStockMaximo(dto.stockMaximo());
        i.setEstado(dto.estado());
        return i;
    }

    public void actualizarEntidadDesdeDTO(Inventario entidad, InventarioRequest dto, Producto producto, Bodega bodega) {
        if (entidad == null || dto == null) { return; }
        entidad.setProducto(producto);
        entidad.setBodega(bodega);
        entidad.setCantidadActual(dto.cantidadActual());
        entidad.setStockMinimo(dto.stockMinimo());
        entidad.setStockMaximo(dto.stockMaximo());
        entidad.setEstado(dto.estado());
    }
}
