package com.Campusland.ProyectoSpringBoot_CorpusEnrique.service.impl;

import com.Campusland.ProyectoSpringBoot_CorpusEnrique.dto.request.ProductoRequest;
import com.Campusland.ProyectoSpringBoot_CorpusEnrique.dto.response.ProductoResponse;
import com.Campusland.ProyectoSpringBoot_CorpusEnrique.exception.BusinessRuleException;
import com.Campusland.ProyectoSpringBoot_CorpusEnrique.mappers.ProductoMapper;
import com.Campusland.ProyectoSpringBoot_CorpusEnrique.model.Categoria;
import com.Campusland.ProyectoSpringBoot_CorpusEnrique.model.Producto;
import com.Campusland.ProyectoSpringBoot_CorpusEnrique.repository.CategoriaRepository;
import com.Campusland.ProyectoSpringBoot_CorpusEnrique.repository.ProductoRepository;
import com.Campusland.ProyectoSpringBoot_CorpusEnrique.service.ProductoService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductoServiceImpl implements ProductoService {

    private final ProductoMapper productoMapper;
    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;

    @Override
    public ProductoResponse guardarProducto(ProductoRequest dto) {
        if (productoRepository.existsByCodigo(dto.codigo())) {
            throw new BusinessRuleException("Ya existe un producto con el código: " + dto.codigo());
        }
        Categoria categoria = categoriaRepository.findById(dto.categoriaId())
                .orElseThrow(() -> new EntityNotFoundException("Categoría no encontrada con id: " + dto.categoriaId()));

        Producto p = productoMapper.dtoAEntidad(dto, categoria);
        productoRepository.save(p);
        return productoMapper.entidadADto(p);
    }

    @Override
    public List<ProductoResponse> listarProductos() {
        return productoRepository.findAll()
                .stream()
                .map(productoMapper::entidadADto)
                .toList();
    }

    @Override
    public ProductoResponse obtenerProductoPorId(Long id) {
        Producto p = productoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado con id: " + id));
        return productoMapper.entidadADto(p);
    }

    @Override
    public ProductoResponse actualizarProducto(Long id, ProductoRequest dto) {
        Producto p = productoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado con id: " + id));
        if (productoRepository.existsByCodigo(dto.codigo()) && !p.getCodigo().equals(dto.codigo())) {
            throw new BusinessRuleException("Ya existe un producto con el código: " + dto.codigo());
        }
        Categoria categoria = categoriaRepository.findById(dto.categoriaId())
                .orElseThrow(() -> new EntityNotFoundException("Categoría no encontrada con id: " + dto.categoriaId()));

        productoMapper.actualizarEntidadDesdeDTO(p, dto, categoria);
        productoRepository.save(p);
        return productoMapper.entidadADto(p);
    }

    @Override
    public void eliminarProducto(Long id) {
        if (!productoRepository.existsById(id)) {
            throw new EntityNotFoundException("Producto no encontrado con id: " + id);
        }
        productoRepository.deleteById(id);
    }
}
