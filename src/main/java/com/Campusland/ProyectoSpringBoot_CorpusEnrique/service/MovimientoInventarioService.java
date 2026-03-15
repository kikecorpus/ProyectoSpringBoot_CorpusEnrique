package com.Campusland.ProyectoSpringBoot_CorpusEnrique.service;

import com.Campusland.ProyectoSpringBoot_CorpusEnrique.dto.request.MovimientoInventarioRequest;
import com.Campusland.ProyectoSpringBoot_CorpusEnrique.dto.response.MovimientoInventarioResponse;

import java.time.LocalDateTime;
import java.util.List;

public interface MovimientoInventarioService {
    MovimientoInventarioResponse registrarMovimiento(MovimientoInventarioRequest dto, String username);
    List<MovimientoInventarioResponse> listarMovimientos();
    MovimientoInventarioResponse obtenerMovimientoPorId(Long id);
    List<MovimientoInventarioResponse> listarMovimientosPorInventario(Long inventarioId);
    List<MovimientoInventarioResponse> listarMovimientosPorRangoFechas(
            LocalDateTime desde, LocalDateTime hasta);
}
