package com.Campusland.ProyectoSpringBoot_CorpusEnrique.dto.request;

import com.Campusland.ProyectoSpringBoot_CorpusEnrique.model.Persona.TipoDocumento;
import jakarta.validation.constraints.*;


public record PersonaRequest( @NotBlank(message = "El nombre es obligatorio")
                              @Size(max = 100, message = "El nombre no puede superar 100 caracteres")
                              String nombre ,

                              @NotBlank(message = "El apellido es obligatorio")
                              @Size(max = 100, message = "El apellido no puede superar 100 caracteres")
                              String apellido ,

                              @NotNull(message = "El tipo de documento es obligatorio")
                              TipoDocumento tipoDocumento ,

                              @NotBlank(message = "El numero de documento es obligatorio")
                              @Size(max = 20, message = "El numero de documento no puede superar 20 caracteres")
                              String numeroDocumento ) {
}
