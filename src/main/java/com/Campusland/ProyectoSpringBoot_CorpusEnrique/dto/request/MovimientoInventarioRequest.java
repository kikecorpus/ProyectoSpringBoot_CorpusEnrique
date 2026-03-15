package com.Campusland.ProyectoSpringBoot_CorpusEnrique.dto.request;

import com.Campusland.ProyectoSpringBoot_CorpusEnrique.model.MovimientoInventario.TipoMovimiento;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

@Schema(description = "Datos para registrar un movimiento de inventario")
public record MovimientoInventarioRequest(
        @Schema(description = "ID del inventario origen (requerido en SALIDA, TRASLADO y AJUSTE)", example = "1")
        Long inventarioOrigenId,

        @Schema(description = "ID del inventario destino (requerido en ENTRADA y TRASLADO)", example = "2")
        Long inventarioDestinoId,

        @Schema(description = "Tipo de movimiento a registrar", example = "ENTRADA")
        @NotNull(message = "El tipo de movimiento es obligatorio")
        TipoMovimiento tipoMovimiento,

        @Schema(description = "Cantidad de unidades a mover", example = "10")
        @NotNull(message = "La cantidad es obligatoria")
        @Positive(message = "La cantidad debe ser mayor a 0")
        Integer cantidad,

        @Schema(description = "Referencia del movimiento (ej. número de orden)", example = "ORD-2024-001")
        @Size(max = 100, message = "La referencia no puede superar 100 caracteres")
        String referencia,

        @Schema(description = "Observación adicional sobre el movimiento", example = "Ingreso por compra a proveedor")
        String observacion
) {
        // El usuario_id se obtiene del token JWT en el service
        // No se recibe en el request para evitar suplantacion
}
