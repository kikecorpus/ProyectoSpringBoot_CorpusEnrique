package com.Campusland.ProyectoSpringBoot_CorpusEnrique.dto.response;

public record ProductoMasMovidoResponse(String nombreProducto,
                                        String codigoProducto,
                                        Long totalMovimientos,
                                        Long totalUnidadesMovidas) {
}
