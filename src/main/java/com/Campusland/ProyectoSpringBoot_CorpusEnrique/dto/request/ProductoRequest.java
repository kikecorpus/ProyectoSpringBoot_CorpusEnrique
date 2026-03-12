package com.Campusland.ProyectoSpringBoot_CorpusEnrique.dto.request ;

import jakarta.validation.constraints.* ;

public record ProductoRequest(    @NotBlank(message = "El nombre del producto es obligatorio")
                                 @Size(max = 100, message = "El nombre no puede superar 100 caracteres")
                                 String nombre ,

                                 @NotBlank(message = "El código es obligatorio")
                                 @Size(max = 50, message = "El código no puede superar 50 caracteres")
                                 String codigo ,

                                 @NotBlank(message = "La unidad de medida es obligatoria")
                                 @Size(max = 30, message = "La unidad de medida no puede superar 30 caracteres")
                                 String unidadMedida ,

                                 @NotNull(message = "El id de categoría es obligatorio")
                                 Integer categoriaId) {
}
