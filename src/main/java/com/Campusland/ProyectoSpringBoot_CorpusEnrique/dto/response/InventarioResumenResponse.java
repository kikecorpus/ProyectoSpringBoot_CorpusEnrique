package com.Campusland.ProyectoSpringBoot_CorpusEnrique.dto.response;

import lombok.*;

// DTO liviano para referenciar un inventario dentro de un movimiento
// Evita anidar objetos completos y previene referencias circulares
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class InventarioResumenResponse {

    private Long idInventario;
    private String nombreProducto;
    private String codigoProducto;
    private String nombreBodega;
}
