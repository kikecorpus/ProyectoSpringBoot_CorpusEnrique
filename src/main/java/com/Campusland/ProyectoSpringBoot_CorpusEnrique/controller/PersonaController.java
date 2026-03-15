package com.Campusland.ProyectoSpringBoot_CorpusEnrique.controller;

import com.Campusland.ProyectoSpringBoot_CorpusEnrique.dto.request.PersonaRequest;
import com.Campusland.ProyectoSpringBoot_CorpusEnrique.dto.response.PersonaResponse;
import com.Campusland.ProyectoSpringBoot_CorpusEnrique.service.PersonaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Tag(name = "Persona", description = "Operaciones relacionadas con la Persona")
@RestController
@RequestMapping("/api/persona")
@RequiredArgsConstructor
@Validated
public class PersonaController {

    private final PersonaService personaService;
    @Operation(summary = "Crear registro", description = "Guarda un nuevo registro en el sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Registro creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos en la solicitud"),
            @ApiResponse(responseCode = "404", description = "Recurso relacionado no encontrado"),
            @ApiResponse(responseCode = "409", description = "Ya existe un registro con esos datos"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping
    public ResponseEntity<PersonaResponse> guardar(@Valid @RequestBody PersonaRequest dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(personaService.guardarPersona(dto));
    }
    @Operation(summary = "Listar registros", description = "Retorna todos los registros disponibles")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping
    public ResponseEntity<List<PersonaResponse>> listar() {
        return ResponseEntity.ok(personaService.listarPersonas());
    }
    @Operation(summary = "Obtener registro por ID", description = "Retorna un registro según su identificador")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Registro encontrado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Registro no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/{id}")
    public ResponseEntity<PersonaResponse> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(personaService.obtenerPersonaPorId(id));
    }
    @Operation(summary = "Actualizar registro", description = "Actualiza los datos de un registro existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Registro actualizado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos en la solicitud"),
            @ApiResponse(responseCode = "404", description = "Registro o recurso relacionado no encontrado"),
            @ApiResponse(responseCode = "409", description = "Ya existe un registro con esos datos"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PutMapping("/{id}")
    public ResponseEntity<PersonaResponse> actualizar(@PathVariable Long id,
                                                      @Valid @RequestBody PersonaRequest dto) {
        return ResponseEntity.ok(personaService.actualizarPersona(id, dto));
    }
    @Operation(summary = "Eliminar registro", description = "Elimina un registro según su identificador")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Registro eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Registro no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        personaService.eliminarPersona(id);
        return ResponseEntity.noContent().build();
    }
}
