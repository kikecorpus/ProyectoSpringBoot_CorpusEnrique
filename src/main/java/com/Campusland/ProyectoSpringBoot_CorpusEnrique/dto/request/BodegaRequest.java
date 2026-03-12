package com.Campusland.ProyectoSpringBoot_CorpusEnrique.dto.request;

import com.Campusland.ProyectoSpringBoot_CorpusEnrique.model.Bodega.Estado;
import jakarta.validation.constraints.*;
import lombok.*;


public record BodegaRequest(@NotBlank(message = "El nombre de la bodega es obligatorio")
                           @Size(max = 100, message = "El nombre no puede superar 100 caracteres")
                           String nombre,
                           @NotBlank(message = "La dirección es obligatoria")
                           @Size(max = 255, message = "La dirección no puede superar 255 caracteres")
                           String direccion,
                           @NotNull(message = "La capacidad es obligatoria")
                           @Positive(message = "La capacidad debe ser mayor a 0")
                           Integer capacidad,
                           @NotNull(message = "El estado es obligatorio")
                           Estado estado,
                           @NotNull(message = "El id de ciudad es obligatorio")
                           Integer ciudadId,
                           @NotNull(message = "El id del encargado es obligatorio")
                           Integer encargadoId) {
}
