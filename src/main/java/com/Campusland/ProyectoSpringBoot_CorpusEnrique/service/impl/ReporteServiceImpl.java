package com.Campusland.ProyectoSpringBoot_CorpusEnrique.service.impl;

import com.Campusland.ProyectoSpringBoot_CorpusEnrique.dto.response.ProductoMasMovidoResponse;
import com.Campusland.ProyectoSpringBoot_CorpusEnrique.dto.response.ReporteGeneralResponse;
import com.Campusland.ProyectoSpringBoot_CorpusEnrique.dto.response.StockPorBodegaResponse;
import com.Campusland.ProyectoSpringBoot_CorpusEnrique.repository.InventarioRepository;
import com.Campusland.ProyectoSpringBoot_CorpusEnrique.repository.MovimientoInventarioRepository;
import com.Campusland.ProyectoSpringBoot_CorpusEnrique.service.ReporteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReporteServiceImpl implements ReporteService {

    private final InventarioRepository inventarioRepository;
    private final MovimientoInventarioRepository movimientoRepository;

    @Override
    public ReporteGeneralResponse generarReporteGeneral() {
        List<StockPorBodegaResponse> stockPorBodega = inventarioRepository
                .findStockPorBodega()
                .stream()
                .map(row -> new StockPorBodegaResponse(
                        (String) row[0],                           // nombreBodega
                        (String) row[1],                           // ciudad
                        ((Number) row[2]).intValue(),              // totalProductos
                        ((Number) row[3]).intValue()               // stockTotal
                ))
                .toList();

        List<ProductoMasMovidoResponse> productosMasMovidos = movimientoRepository
                .findProductosMasMovidos()
                .stream()
                .map(row -> new ProductoMasMovidoResponse(
                        (String) row[0],                           // nombreProducto
                        (String) row[1],                           // codigoProducto
                        ((Number) row[2]).longValue(),             // totalMovimientos
                        ((Number) row[3]).longValue()              // totalUnidadesMovidas
                ))
                .toList();

        return new ReporteGeneralResponse(stockPorBodega, productosMasMovidos);
    }

}