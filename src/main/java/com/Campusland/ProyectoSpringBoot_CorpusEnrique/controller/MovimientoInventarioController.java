package com.Campusland.ProyectoSpringBoot_CorpusEnrique.controller;

import com.Campusland.ProyectoSpringBoot_CorpusEnrique.dto.request.MovimientoInventarioRequest;
import com.Campusland.ProyectoSpringBoot_CorpusEnrique.dto.response.MovimientoInventarioResponse;
import com.Campusland.ProyectoSpringBoot_CorpusEnrique.service.MovimientoInventarioService;
import com.Campusland.ProyectoSpringBoot_CorpusEnrique.service.impl.ExamenSpringbootImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
@Tag(name = "MovimientoInventario", description = "Operaciones relacionadas con el Movimiento de Inventario")
@RestController
@RequestMapping("/api/movimiento")
@RequiredArgsConstructor
@Validated
public class MovimientoInventarioController {

    private final MovimientoInventarioService movimientoService;
    private final ExamenSpringbootImpl examen;
    @Operation(summary = "Crear registro", description = "Guarda un nuevo registro en el sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Registro creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos en la solicitud"),
            @ApiResponse(responseCode = "404", description = "Recurso relacionado no encontrado"),
            @ApiResponse(responseCode = "409", description = "Ya existe un registro con esos datos"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPERVISOR', 'OPERARIO')")
    public ResponseEntity<MovimientoInventarioResponse> registrar(
            @Valid @RequestBody MovimientoInventarioRequest dto) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(movimientoService.registrarMovimiento(dto, username));
    }
    @Operation(summary = "Listar registros", description = "Retorna todos los registros disponibles")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPERVISOR', 'OPERARIO')")
    public ResponseEntity<List<MovimientoInventarioResponse>> listar() {
        return ResponseEntity.ok(movimientoService.listarMovimientos());
    }
    @Operation(summary = "Obtener registro por ID", description = "Retorna un registro según su identificador")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Registro encontrado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Registro no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPERVISOR', 'OPERARIO')")
    public ResponseEntity<MovimientoInventarioResponse> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(movimientoService.obtenerMovimientoPorId(id));
    }

    @Operation(summary = "Obtener registro por inventario", description = "Retorna un registro según su inventario referenciado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Registro encontrado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Registro no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/inventario/{inventarioId}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPERVISOR', 'OPERARIO')")
    public ResponseEntity<List<MovimientoInventarioResponse>> listarPorInventario(
            @PathVariable Long inventarioId) {
        return ResponseEntity.ok(movimientoService.listarMovimientosPorInventario(inventarioId));
    }
    @Operation(summary = "Listar movimientos por rango de fechas",
            description = "Retorna movimientos registrados entre dos fechas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente"),
            @ApiResponse(responseCode = "400", description = "Rango de fechas inválido"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/rango")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPERVISOR', 'OPERARIO')")
    public ResponseEntity<List<MovimientoInventarioResponse>> listarPorRangoFechas(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta) {
        LocalDateTime desdeDateTime = desde.atStartOfDay();
        LocalDateTime hastaDateTime = hasta.atTime(23, 59, 59);
        return ResponseEntity.ok(movimientoService.listarMovimientosPorRangoFechas(desdeDateTime, hastaDateTime));
    }

    @GetMapping("/recientes")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPERVISOR')")
    public ResponseEntity<List<MovimientoInventarioResponse>> listarRecientes(){
        return ResponseEntity.ok(examen.topRecientes());
    }

}
