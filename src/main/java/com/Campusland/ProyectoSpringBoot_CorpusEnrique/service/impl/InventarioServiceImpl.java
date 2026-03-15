package com.Campusland.ProyectoSpringBoot_CorpusEnrique.service.impl;

import com.Campusland.ProyectoSpringBoot_CorpusEnrique.dto.request.InventarioRequest;
import com.Campusland.ProyectoSpringBoot_CorpusEnrique.dto.response.InventarioResponse;
import com.Campusland.ProyectoSpringBoot_CorpusEnrique.exception.BusinessRuleException;
import com.Campusland.ProyectoSpringBoot_CorpusEnrique.mappers.InventarioMapper;
import com.Campusland.ProyectoSpringBoot_CorpusEnrique.model.Bodega;
import com.Campusland.ProyectoSpringBoot_CorpusEnrique.model.Inventario;
import com.Campusland.ProyectoSpringBoot_CorpusEnrique.model.Producto;
import com.Campusland.ProyectoSpringBoot_CorpusEnrique.repository.BodegaRepository;
import com.Campusland.ProyectoSpringBoot_CorpusEnrique.repository.InventarioRepository;
import com.Campusland.ProyectoSpringBoot_CorpusEnrique.repository.ProductoRepository;
import com.Campusland.ProyectoSpringBoot_CorpusEnrique.service.InventarioService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InventarioServiceImpl implements InventarioService {

    private final InventarioMapper inventarioMapper;
    private final InventarioRepository inventarioRepository;
    private final ProductoRepository productoRepository;
    private final BodegaRepository bodegaRepository;
    @Transactional
    @Override
    public InventarioResponse guardarInventario(InventarioRequest dto) {
        if (inventarioRepository.existsByProductoIdProductoAndBodegaIdBodega(dto.productoId(), dto.bodegaId())) {
            throw new BusinessRuleException("Ya existe un inventario para ese producto en esa bodega");
        }
        if (dto.stockMinimo() >= dto.stockMaximo()) {
            throw new BusinessRuleException("El stock mínimo debe ser menor al stock máximo");
        }
        Producto producto = productoRepository.findById(dto.productoId())
                .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado con id: " + dto.productoId()));
        Bodega bodega = bodegaRepository.findById(dto.bodegaId())
                .orElseThrow(() -> new EntityNotFoundException("Bodega no encontrada con id: " + dto.bodegaId()));

        Inventario i = inventarioMapper.dtoAEntidad(dto, producto, bodega);
        inventarioRepository.save(i);
        return inventarioMapper.entidadADto(i);
    }

    @Override
    public List<InventarioResponse> listarInventarios() {
        return inventarioRepository.findAll()
                .stream()
                .map(inventarioMapper::entidadADto)
                .toList();
    }

    @Override
    public InventarioResponse obtenerInventarioPorId(Long id) {
        Inventario i = inventarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Inventario no encontrado con id: " + id));
        return inventarioMapper.entidadADto(i);
    }
    @Transactional
    @Override
    public InventarioResponse actualizarInventario(Long id, InventarioRequest dto) {
        Inventario i = inventarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Inventario no encontrado con id: " + id));
        if (dto.stockMinimo() >= dto.stockMaximo()) {
            throw new BusinessRuleException("El stock mínimo debe ser menor al stock máximo");
        }
        Producto producto = productoRepository.findById(dto.productoId())
                .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado con id: " + dto.productoId()));
        Bodega bodega = bodegaRepository.findById(dto.bodegaId())
                .orElseThrow(() -> new EntityNotFoundException("Bodega no encontrada con id: " + dto.bodegaId()));

        inventarioMapper.actualizarEntidadDesdeDTO(i, dto, producto, bodega);
        inventarioRepository.save(i);
        return inventarioMapper.entidadADto(i);
    }
    @Transactional
    @Override
    public void eliminarInventario(Long id) {
        if (!inventarioRepository.existsById(id)) {
            throw new EntityNotFoundException("Inventario no encontrado con id: " + id);
        }
        inventarioRepository.deleteById(id);
    }

    @Override
    public List<InventarioResponse> listarInventariosConStockCritico() {
        return inventarioRepository.findInventariosConStockCritico()
                .stream()
                .map(inventarioMapper::entidadADto)
                .toList();
    }

    @Override
    public List<InventarioResponse> listarInventariosConStockBajo() {
        return inventarioRepository.findInventariosConStockBajo()
                .stream()
                .map(inventarioMapper::entidadADto)
                .toList();
    }
}
