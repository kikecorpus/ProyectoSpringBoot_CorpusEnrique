package com.Campusland.ProyectoSpringBoot_CorpusEnrique.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;


public record CategoriaRequest (@NotBlank(message = "El nombre de la categoría es obligatorio")
                                @Size(max = 100, message = "El nombre no puede superar 100 caracteres")
                                String nombre){
}
