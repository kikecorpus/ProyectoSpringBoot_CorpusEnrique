package com.Campusland.ProyectoSpringBoot_CorpusEnrique.controller;

import com.Campusland.ProyectoSpringBoot_CorpusEnrique.dto.request.DepartamentoRequest;
import com.Campusland.ProyectoSpringBoot_CorpusEnrique.dto.response.DepartamentoResponse;
import com.Campusland.ProyectoSpringBoot_CorpusEnrique.service.DepartamentoService;
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
@Tag(name = "Departamento", description = "Operaciones relacionadas con el Departamento")

@RestController
@RequestMapping("/api/departamento")
@RequiredArgsConstructor
@Validated
public class DepartamentoController {

    private final DepartamentoService departamentoService;
    @Operation(summary = "Crear registro", description = "Guarda un nuevo registro en el sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Registro creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos en la solicitud"),
            @ApiResponse(responseCode = "404", description = "Recurso relacionado no encontrado"),
            @ApiResponse(responseCode = "409", description = "Ya existe un registro con esos datos"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping
    public ResponseEntity<DepartamentoResponse> guardar(@Valid @RequestBody DepartamentoRequest dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(departamentoService.guardarDepartamento(dto));
    }
    @Operation(summary = "Listar registros", description = "Retorna todos los registros disponibles")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping
    public ResponseEntity<List<DepartamentoResponse>> listar() {
        return ResponseEntity.ok(departamentoService.listarDepartamentos());
    }
    @Operation(summary = "Obtener registro por ID", description = "Retorna un registro según su identificador")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Registro encontrado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Registro no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/{id}")
    public ResponseEntity<DepartamentoResponse> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(departamentoService.obtenerDepartamentoPorId(id));
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
    public ResponseEntity<DepartamentoResponse> actualizar(@PathVariable Long id,
                                                           @Valid @RequestBody DepartamentoRequest dto) {
        return ResponseEntity.ok(departamentoService.actualizarDepartamento(id, dto));
    }
    @Operation(summary = "Eliminar registro", description = "Elimina un registro según su identificador")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Registro eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Registro no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        departamentoService.eliminarDepartamento(id);
        return ResponseEntity.noContent().build();
    }
}
