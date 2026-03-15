package com.Campusland.ProyectoSpringBoot_CorpusEnrique.service;

import com.Campusland.ProyectoSpringBoot_CorpusEnrique.dto.request.BodegaRequest;
import com.Campusland.ProyectoSpringBoot_CorpusEnrique.dto.response.BodegaResponse;
import java.util.List;

public interface BodegaService {
    BodegaResponse guardarBodega(BodegaRequest dto);
    List<BodegaResponse> listarBodegas();
    BodegaResponse obtenerBodegaPorId(Long id);
    BodegaResponse actualizarBodega(Long id, BodegaRequest dto);
    void eliminarBodega(Long id);
}
