package com.Campusland.ProyectoSpringBoot_CorpusEnrique.dto.request;

import jakarta.validation.constraints.*;


public record RolRequest(  @NotBlank(message = "El nombre del rol es obligatorio")
                           @Size(max = 50, message = "El nombre no puede superar 50 caracteres")
                           String nombre,

                           @Size(max = 255, message = "La descripción no puede superar 255 caracteres")
                           String descripcion) {
}
