package com.Campusland.ProyectoSpringBoot_CorpusEnrique.service;

import com.Campusland.ProyectoSpringBoot_CorpusEnrique.dto.request.ProductoRequest;
import com.Campusland.ProyectoSpringBoot_CorpusEnrique.dto.response.ProductoResponse;
import java.util.List;

public interface ProductoService {
    ProductoResponse guardarProducto(ProductoRequest dto);
    List<ProductoResponse> listarProductos();
    ProductoResponse obtenerProductoPorId(Long id);
    ProductoResponse actualizarProducto(Long id, ProductoRequest dto);
    void eliminarProducto(Long id);
}
