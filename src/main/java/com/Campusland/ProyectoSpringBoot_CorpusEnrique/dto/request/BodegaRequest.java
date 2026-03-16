package com.Campusland.ProyectoSpringBoot_CorpusEnrique.dto.request;

import com.Campusland.ProyectoSpringBoot_CorpusEnrique.model.Bodega.Estado;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

@Schema(description = "Datos para crear o actualizar una bodega")
public record BodegaRequest(
        @Schema(description = "Nombre de la bodega", example = "Bodega Central")
        @NotBlank(message = "El nombre de la bodega es obligatorio")
        @Size(max = 100, message = "El nombre no puede superar 100 caracteres")
        String nombre,

        @Schema(description = "Dirección de la bodega", example = "Calle 50 # 30-20")
        @NotBlank(message = "La dirección es obligatoria")
        @Size(max = 255, message = "La dirección no puede superar 255 caracteres")
        String direccion,

        @Schema(description = "Capacidad máxima de la bodega en unidades", example = "1000")
        @NotNull(message = "La capacidad es obligatoria")
        @Positive(message = "La capacidad debe ser mayor a 0")
        Integer capacidad,

        @Schema(description = "Estado de la bodega", example = "ACTIVO")
        @NotNull(message = "El estado es obligatorio")
        Estado estado,

        @Schema(description = "ID de la ciudad donde se ubica la bodega", example = "1")
        @NotNull(message = "El id de ciudad es obligatorio")
        Long ciudadId,

        @Schema(description = "ID del usuario encargado de la bodega", example = "2")
        Long encargadoId
) {}
