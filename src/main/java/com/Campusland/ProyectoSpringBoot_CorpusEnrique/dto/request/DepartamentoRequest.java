package com.Campusland.ProyectoSpringBoot_CorpusEnrique.dto.request;

import jakarta.validation.constraints.*;


public record DepartamentoRequest ( @NotBlank(message = "El nombre del departamento es obligatorio")
                                    @Size(max = 100, message = "El nombre no puede superar 100 caracteres")
                                    String nombre){

}
