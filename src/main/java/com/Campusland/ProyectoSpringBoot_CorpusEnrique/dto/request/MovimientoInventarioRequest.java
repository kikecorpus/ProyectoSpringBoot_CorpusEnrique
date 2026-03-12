package com.Campusland.ProyectoSpringBoot_CorpusEnrique.dto.request ;

import com.Campusland.ProyectoSpringBoot_CorpusEnrique.model.MovimientoInventario.TipoMovimiento ;
import jakarta.validation.constraints.* ;



public record MovimientoInventarioRequest (
                                            Integer inventarioOrigenId ,
                                            Integer inventarioDestinoId ,
                                            @NotNull(message = "El tipo de movimiento es obligatorio")
                                            TipoMovimiento tipoMovimiento ,
                                            @NotNull(message = "La cantidad es obligatoria")
                                            @Positive(message = "La cantidad debe ser mayor a 0")
                                            Integer cantidad ,
                                            @Size(max = 100, message = "La referencia no puede superar 100 caracteres")
                                            String referencia ,
                                            String observacion ){



    // El usuario_id se obtiene del token JWT en el service
    // No se recibe en el request para evitar suplantacion
}
