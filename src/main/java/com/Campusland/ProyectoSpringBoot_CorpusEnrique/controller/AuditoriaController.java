package com.Campusland.ProyectoSpringBoot_CorpusEnrique.controller;

import com.Campusland.ProyectoSpringBoot_CorpusEnrique.dto.response.AuditoriaResponse;
import com.Campusland.ProyectoSpringBoot_CorpusEnrique.model.Auditoria;
import com.Campusland.ProyectoSpringBoot_CorpusEnrique.service.AuditoriaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auditoria")
@RequiredArgsConstructor
@Validated
@Tag(name = "Auditoria", description = "Operaciones relacionadas con Auditoria")
public class AuditoriaController {

    private final AuditoriaService auditoriaService;

    @Operation(summary = "Listar registros", description = "Retorna todos los registros disponibles")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping
    public ResponseEntity<List<AuditoriaResponse>> listar() {
        return ResponseEntity.ok(auditoriaService.listarAuditorias());
    }
    @Operation(summary = "Obtener registro por ID", description = "Retorna un registro según su identificador")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Registro encontrado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Registro no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/{id}")
    public ResponseEntity<AuditoriaResponse> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(auditoriaService.obtenerAuditoriaPorId(id));
    }

    @Operation(summary = "Filtrar auditoria por entidad")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Informacón encontrada"),
            @ApiResponse(responseCode = "404", description = "Informacón no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/entidad/{entidad}")
    public ResponseEntity<List<AuditoriaResponse>> listarPorEntidad(@PathVariable String entidad) {
        return ResponseEntity.ok(auditoriaService.listarPorEntidad(entidad));
    }

    @Operation(summary = "Filtrar auditoria por usuario")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Informacón encontrada"),
            @ApiResponse(responseCode = "404", description = "Informacón no encontrado"),
            @ApiResponse(responseCode = "500", description = "Informacón no encontrado") })
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<AuditoriaResponse>> listarPorUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(auditoriaService.listarPorUsuario(usuarioId));
    }
    @Operation(summary = "Filtrar auditoria por tipo de operacion",
            description = "Retorna registros de auditoria filtrados por tipo: INSERT, UPDATE o DELETE")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente"),
            @ApiResponse(responseCode = "400", description = "Tipo de operacion invalido"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/tipo/{tipoOperacion}")
    public ResponseEntity<List<AuditoriaResponse>> listarPorTipoOperacion(
            @PathVariable Auditoria.TipoOperacion tipoOperacion) {
        return ResponseEntity.ok(auditoriaService.listarPorTipoOperacion(tipoOperacion));
    }
}
