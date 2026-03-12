package com.Campusland.ProyectoSpringBoot_CorpusEnrique.dto.request;

import com.Campusland.ProyectoSpringBoot_CorpusEnrique.model.Inventario.Estado;
import jakarta.validation.constraints.* ;


public record InventarioRequest( @NotNull(message = "El id del producto es obligatorio")
                                 Integer productoId ,

                                 @NotNull(message = "El id de la bodega es obligatorio")
                                 Integer bodegaId ,

                                 @NotNull(message = "La cantidad actual es obligatoria")
                                 @PositiveOrZero(message = "La cantidad actual no puede ser negativa")
                                 Integer cantidadActual ,

                                 @NotNull(message = "El stock minimo es obligatorio")
                                 @PositiveOrZero(message = "El stock minimo no puede ser negativo")
                                 Integer stockMinimo ,

                                 @NotNull(message = "El stock maximo es obligatorio")
                                 @Positive(message = "El stock maximo debe ser mayor a 0")
                                 Integer stockMaximo ,

                                 @NotNull(message = "El estado es obligatorio")
                                 Estado estado) {


}
