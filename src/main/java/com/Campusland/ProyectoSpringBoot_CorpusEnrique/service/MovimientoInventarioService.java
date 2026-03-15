package com.Campusland.ProyectoSpringBoot_CorpusEnrique.service;

import com.Campusland.ProyectoSpringBoot_CorpusEnrique.dto.request.MovimientoInventarioRequest;
import com.Campusland.ProyectoSpringBoot_CorpusEnrique.dto.response.MovimientoInventarioResponse;
import java.util.List;

public interface MovimientoInventarioService {
    MovimientoInventarioResponse registrarMovimiento(MovimientoInventarioRequest dto, String username);
    List<MovimientoInventarioResponse> listarMovimientos();
    MovimientoInventarioResponse obtenerMovimientoPorId(Long id);
    List<MovimientoInventarioResponse> listarMovimientosPorInventario(Long inventarioId);
}
