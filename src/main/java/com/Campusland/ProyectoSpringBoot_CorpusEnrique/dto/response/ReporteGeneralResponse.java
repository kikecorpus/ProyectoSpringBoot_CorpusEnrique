package com.Campusland.ProyectoSpringBoot_CorpusEnrique.dto.response;

import java.util.List;

public record ReporteGeneralResponse(List<StockPorBodegaResponse> stockPorBodega,
                                     List<ProductoMasMovidoResponse> productosMasMovidos) {
}
