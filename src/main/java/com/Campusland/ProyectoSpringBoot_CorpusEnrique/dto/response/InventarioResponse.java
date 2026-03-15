
package com.Campusland.ProyectoSpringBoot_CorpusEnrique.dto.response;

import com.Campusland.ProyectoSpringBoot_CorpusEnrique.model.Inventario.Estado;

import java.time.LocalDateTime;

public record InventarioResponse(     Long idInventario,
                                      ProductoResponse producto,
                                      BodegaResponse bodega,
                                      Integer cantidadActual,
                                      Integer stockMinimo,
                                      Integer stockMaximo,
                                      Estado estado,
                                      LocalDateTime ultimaActualizacion,
                                       Boolean stockCritico) {
}
