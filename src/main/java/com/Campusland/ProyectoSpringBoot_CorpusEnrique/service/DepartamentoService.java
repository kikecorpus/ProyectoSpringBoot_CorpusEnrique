package com.Campusland.ProyectoSpringBoot_CorpusEnrique.service;

import com.Campusland.ProyectoSpringBoot_CorpusEnrique.dto.request.DepartamentoRequest;
import com.Campusland.ProyectoSpringBoot_CorpusEnrique.dto.response.DepartamentoResponse;
import java.util.List;

public interface DepartamentoService {
    DepartamentoResponse guardarDepartamento(DepartamentoRequest dto);
    List<DepartamentoResponse> listarDepartamentos();
    DepartamentoResponse obtenerDepartamentoPorId(Long id);
    DepartamentoResponse actualizarDepartamento(Long id, DepartamentoRequest dto);
    void eliminarDepartamento(Long id);
}
