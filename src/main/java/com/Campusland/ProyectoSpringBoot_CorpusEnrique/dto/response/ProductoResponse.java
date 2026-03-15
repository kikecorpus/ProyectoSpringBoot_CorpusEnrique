package com.Campusland.ProyectoSpringBoot_CorpusEnrique.dto.response;

public record ProductoResponse(
    Long idProducto,
    String nombre,
    String codigo,
    String unidadMedida,
    CategoriaResponse categoria
) {}
