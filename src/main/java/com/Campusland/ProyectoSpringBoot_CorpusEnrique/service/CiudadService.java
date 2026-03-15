package com.Campusland.ProyectoSpringBoot_CorpusEnrique.service;

import com.Campusland.ProyectoSpringBoot_CorpusEnrique.dto.request.CiudadRequest;
import com.Campusland.ProyectoSpringBoot_CorpusEnrique.dto.response.CiudadResponse;
import java.util.List;

public interface CiudadService {
    CiudadResponse guardarCiudad(CiudadRequest dto);
    List<CiudadResponse> listarCiudades();
    CiudadResponse obtenerCiudadPorId(Long id);
    CiudadResponse actualizarCiudad(Long id, CiudadRequest dto);
    void eliminarCiudad(Long id);
}
