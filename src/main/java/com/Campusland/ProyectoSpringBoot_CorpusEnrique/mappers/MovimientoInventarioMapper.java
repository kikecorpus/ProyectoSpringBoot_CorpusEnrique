package com.Campusland.ProyectoSpringBoot_CorpusEnrique.mappers;

import com.Campusland.ProyectoSpringBoot_CorpusEnrique.dto.response.MovimientoInventarioResponse;
import com.Campusland.ProyectoSpringBoot_CorpusEnrique.model.MovimientoInventario;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MovimientoInventarioMapper {

    private final InventarioMapper inventarioMapper;
    private final UsuarioMapper usuarioMapper;

    public MovimientoInventarioResponse entidadADto(MovimientoInventario entidad) {
        if (entidad == null) { return null; }
        return new MovimientoInventarioResponse(
                entidad.getIdMovimiento(),
                inventarioMapper.entidadAResumen(entidad.getInventarioOrigen()),
                inventarioMapper.entidadAResumen(entidad.getInventarioDestino()),
                usuarioMapper.entidadADto(entidad.getUsuario()),
                entidad.getTipoMovimiento(),
                entidad.getCantidad(),
                entidad.getCantidadAnterior(),
                entidad.getCantidadPosterior(),
                entidad.getReferencia(),
                entidad.getObservacion(),
                entidad.getFecha());
    }

}
