package com.Campusland.ProyectoSpringBoot_CorpusEnrique.dto.request;

import com.Campusland.ProyectoSpringBoot_CorpusEnrique.model.Usuario.Estado;
import jakarta.validation.constraints.*;

public record UsuarioRequest( @NotBlank(message = "El username es obligatorio")
                             @Size(max = 50, message = "El username no puede superar 50 caracteres")
                             String username,

                            @NotBlank(message = "La contraseña es obligatoria")
                            @Size(min = 8, message = "La contraseña debe tener mínimo 8 caracteres")
                            String contrasena,

                            @NotNull(message = "El estado es obligatorio")
                            Estado estado,

                            @NotNull(message = "El id de persona es obligatorio")
                            Integer personaId,

                            @NotNull(message = "El id de rol es obligatorio")
                            Integer rolId){
}
