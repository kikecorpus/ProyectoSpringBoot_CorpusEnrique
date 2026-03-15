package com.Campusland.ProyectoSpringBoot_CorpusEnrique.dto.response;

public record StockPorBodegaResponse(
        String nombreBodega,
        String ciudad,
        Integer totalProductos,
        Integer stockTotal
) {}
