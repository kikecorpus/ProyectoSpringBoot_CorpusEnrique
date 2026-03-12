package com.Campusland.ProyectoSpringBoot_CorpusEnrique.dto.request;

import jakarta.validation.constraints.*;



public record CiudadRequest( @NotBlank(message = "El nombre de la ciudad es obligatorio")
                             @Size(max = 100, message = "El nombre no puede superar 100 caracteres")
                             String nombre,
                             @NotNull(message = "El id de departamento es obligatorio")
                             Integer departamentoId) {

}
