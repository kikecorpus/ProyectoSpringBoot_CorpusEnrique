package com.Campusland.ProyectoSpringBoot_CorpusEnrique.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

@Schema(description = "Datos para crear o actualizar un producto")
public record ProductoRequest(
        @Schema(description = "Nombre del producto", example = "Laptop HP 15")
        @NotBlank(message = "El nombre del producto es obligatorio")
        @Size(max = 100, message = "El nombre no puede superar 100 caracteres")
        String nombre,

        @Schema(description = "Código único del producto", example = "PROD-001")
        @NotBlank(message = "El código es obligatorio")
        @Size(max = 50, message = "El código no puede superar 50 caracteres")
        String codigo,

        @Schema(description = "Unidad de medida del producto", example = "UNIDAD")
        @NotBlank(message = "La unidad de medida es obligatoria")
        @Size(max = 30, message = "La unidad de medida no puede superar 30 caracteres")
        String unidadMedida,

        @Schema(description = "ID de la categoría a la que pertenece el producto", example = "1")
        @NotNull(message = "El id de categoría es obligatorio")
        Long categoriaId
) {}
