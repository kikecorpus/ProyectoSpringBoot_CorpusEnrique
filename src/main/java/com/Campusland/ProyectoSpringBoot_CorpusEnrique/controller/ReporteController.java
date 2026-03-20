package com.Campusland.ProyectoSpringBoot_CorpusEnrique.controller;

import com.Campusland.ProyectoSpringBoot_CorpusEnrique.dto.response.MovimientoInventarioResponse;
import com.Campusland.ProyectoSpringBoot_CorpusEnrique.dto.response.ReporteGeneralResponse;
import com.Campusland.ProyectoSpringBoot_CorpusEnrique.service.ReporteService;
import com.Campusland.ProyectoSpringBoot_CorpusEnrique.service.impl.ExamenSpringbootImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Reporte", description = "Operaciones relacionadas con Reportes del sistema")
@RestController
@RequestMapping("/api/reporte")
@RequiredArgsConstructor
@Validated
public class ReporteController {

    private final ReporteService reporteService;
    private final ExamenSpringbootImpl examen;
    @Operation(
            summary = "Reporte general del sistema",
            description = "Retorna un resumen con stock total por bodega y los 10 productos más movidos"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reporte generado exitosamente"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/general")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPERVISOR')")
    public ResponseEntity<ReporteGeneralResponse> reporteGeneral() {
        return ResponseEntity.ok(reporteService.generarReporteGeneral());
    }

    @GetMapping("/reporte/basico")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPERVISOR')")

    public ResponseEntity<List<MovimientoInventarioResponse>> reporteGeneral() {
        return ResponseEntity.ok(examen.reportes);

    }
}

