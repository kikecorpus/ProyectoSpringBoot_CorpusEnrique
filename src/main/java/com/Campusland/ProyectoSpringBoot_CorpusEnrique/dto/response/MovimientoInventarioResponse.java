package com.Campusland.ProyectoSpringBoot_CorpusEnrique.dto.response;

import com.Campusland.ProyectoSpringBoot_CorpusEnrique.model.MovimientoInventario.TipoMovimiento;
import java.time.LocalDateTime;

public record MovimientoInventarioResponse(
    Integer idMovimiento,
    InventarioResumenResponse inventarioOrigen,   // null si es ENTRADA
    InventarioResumenResponse inventarioDestino,  // null si es SALIDA o AJUSTE
    UsuarioResponse usuario,
    TipoMovimiento tipoMovimiento,
    Integer cantidad,
    Integer cantidadAnterior,
    Integer cantidadPosterior,
    String referencia,
    String observacion,
    LocalDateTime fecha
) {}
