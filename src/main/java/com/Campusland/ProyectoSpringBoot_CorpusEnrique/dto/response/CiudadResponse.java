package com.Campusland.ProyectoSpringBoot_CorpusEnrique.dto.response;


public record CiudadResponse(     Integer idCiudad,
                                 String nombre,
                                 DepartamentoResponse departamento) {

}
