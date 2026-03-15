package com.Campusland.ProyectoSpringBoot_CorpusEnrique.dto.request;

import com.Campusland.ProyectoSpringBoot_CorpusEnrique.model.Inventario.Estado;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

@Schema(description = "Datos para crear o actualizar un inventario")
public record InventarioRequest(
        @Schema(description = "ID del producto", example = "1")
        @NotNull(message = "El id del producto es obligatorio")
        Long productoId,

        @Schema(description = "ID de la bodega", example = "1")
        @NotNull(message = "El id de la bodega es obligatorio")
        Long bodegaId,

        @Schema(description = "Cantidad actual disponible en inventario", example = "50")
        @NotNull(message = "La cantidad actual es obligatoria")
        @PositiveOrZero(message = "La cantidad actual no puede ser negativa")
        Integer cantidadActual,

        @Schema(description = "Stock mínimo permitido antes de alertar", example = "10")
        @NotNull(message = "El stock minimo es obligatorio")
        @PositiveOrZero(message = "El stock minimo no puede ser negativo")
        Integer stockMinimo,

        @Schema(description = "Stock máximo permitido en la bodega", example = "200")
        @NotNull(message = "El stock maximo es obligatorio")
        @Positive(message = "El stock maximo debe ser mayor a 0")
        Integer stockMaximo,

        @Schema(description = "Estado del inventario", example = "ACTIVO")
        @NotNull(message = "El estado es obligatorio")
        Estado estado
) {}
