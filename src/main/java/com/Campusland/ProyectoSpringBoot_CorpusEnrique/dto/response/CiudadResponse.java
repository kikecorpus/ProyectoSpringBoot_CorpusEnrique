package com.Campusland.ProyectoSpringBoot_CorpusEnrique.dto.response;


public record CiudadResponse(     Long idCiudad,
                                 String nombre,
                                 DepartamentoResponse departamento) {

}
