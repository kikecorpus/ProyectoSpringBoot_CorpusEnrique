package com.Campusland.ProyectoSpringBoot_CorpusEnrique.service;

import com.Campusland.ProyectoSpringBoot_CorpusEnrique.dto.request.InventarioRequest;
import com.Campusland.ProyectoSpringBoot_CorpusEnrique.dto.response.InventarioResponse;
import java.util.List;

public interface InventarioService {
    InventarioResponse guardarInventario(InventarioRequest dto);
    List<InventarioResponse> listarInventarios();
    InventarioResponse obtenerInventarioPorId(Long id);
    InventarioResponse actualizarInventario(Long id, InventarioRequest dto);
    void eliminarInventario(Long id);
    List<InventarioResponse> listarInventariosConStockCritico();
}
