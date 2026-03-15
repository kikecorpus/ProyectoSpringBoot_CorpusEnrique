package com.Campusland.ProyectoSpringBoot_CorpusEnrique.service;

import com.Campusland.ProyectoSpringBoot_CorpusEnrique.dto.request.CategoriaRequest;
import com.Campusland.ProyectoSpringBoot_CorpusEnrique.dto.response.CategoriaResponse;
import java.util.List;

public interface CategoriaService {
    CategoriaResponse guardarCategoria(CategoriaRequest dto);
    List<CategoriaResponse> listarCategorias();
    CategoriaResponse obtenerCategoriaPorId(Long id);
    CategoriaResponse actualizarCategoria(Long id, CategoriaRequest dto);
    void eliminarCategoria(Long id);
}
