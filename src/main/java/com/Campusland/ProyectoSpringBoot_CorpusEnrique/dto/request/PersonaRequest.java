package com.Campusland.ProyectoSpringBoot_CorpusEnrique.dto.request;

import com.Campusland.ProyectoSpringBoot_CorpusEnrique.model.Persona.TipoDocumento;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

@Schema(description = "Datos para crear o actualizar una persona")
public record PersonaRequest(
        @Schema(description = "Nombre de la persona", example = "Juan")
        @NotBlank(message = "El nombre es obligatorio")
        @Size(max = 100, message = "El nombre no puede superar 100 caracteres")
        String nombre,

        @Schema(description = "Apellido de la persona", example = "Pérez")
        @NotBlank(message = "El apellido es obligatorio")
        @Size(max = 100, message = "El apellido no puede superar 100 caracteres")
        String apellido,

        @Schema(description = "Tipo de documento de identidad", example = "CC")
        @NotNull(message = "El tipo de documento es obligatorio")
        TipoDocumento tipoDocumento,

        @Schema(description = "Número de documento de identidad", example = "1234567890")
        @NotBlank(message = "El numero de documento es obligatorio")
        @Size(max = 20, message = "El numero de documento no puede superar 20 caracteres")
        String numeroDocumento
) {}
