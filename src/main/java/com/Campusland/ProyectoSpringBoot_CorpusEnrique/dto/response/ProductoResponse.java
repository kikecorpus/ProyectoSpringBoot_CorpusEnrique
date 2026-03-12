package com.Campusland.ProyectoSpringBoot_CorpusEnrique.dto.response;

public record ProductoResponse(
    Integer idProducto,
    String nombre,
    String codigo,
    String unidadMedida,
    CategoriaResponse categoria
) {}
