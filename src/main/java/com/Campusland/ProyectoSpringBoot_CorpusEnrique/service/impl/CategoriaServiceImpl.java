package com.Campusland.ProyectoSpringBoot_CorpusEnrique.service.impl;

import com.Campusland.ProyectoSpringBoot_CorpusEnrique.dto.request.CategoriaRequest;
import com.Campusland.ProyectoSpringBoot_CorpusEnrique.dto.response.CategoriaResponse;
import com.Campusland.ProyectoSpringBoot_CorpusEnrique.exception.BusinessRuleException;
import com.Campusland.ProyectoSpringBoot_CorpusEnrique.mappers.CategoriaMapper;
import com.Campusland.ProyectoSpringBoot_CorpusEnrique.model.Categoria;
import com.Campusland.ProyectoSpringBoot_CorpusEnrique.repository.CategoriaRepository;
import com.Campusland.ProyectoSpringBoot_CorpusEnrique.service.CategoriaService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoriaServiceImpl implements CategoriaService {

    private final CategoriaMapper categoriaMapper;
    private final CategoriaRepository categoriaRepository;

    @Transactional
    @Override
    public CategoriaResponse guardarCategoria(CategoriaRequest dto) {
        if (categoriaRepository.existsByNombre(dto.nombre())) {
            throw new BusinessRuleException("Ya existe una categoría con el nombre: " + dto.nombre());
        }
        Categoria c = categoriaMapper.dtoAEntidad(dto);
        categoriaRepository.save(c);
        return categoriaMapper.entidadADto(c);
    }

    @Override
    public List<CategoriaResponse> listarCategorias() {
        return categoriaRepository.findAll()
                .stream()
                .map(categoriaMapper::entidadADto)
                .toList();
    }

    @Override
    public CategoriaResponse obtenerCategoriaPorId(Long id) {
        Categoria c = categoriaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Categoría no encontrada con id: " + id));
        return categoriaMapper.entidadADto(c);
    }

    @Transactional
    @Override
    public CategoriaResponse actualizarCategoria(Long id, CategoriaRequest dto) {
        Categoria c = categoriaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Categoría no encontrada con id: " + id));
        if (categoriaRepository.existsByNombre(dto.nombre()) && !c.getNombre().equals(dto.nombre())) {
            throw new BusinessRuleException("Ya existe una categoría con el nombre: " + dto.nombre());
        }
        categoriaMapper.actualizarEntidadDesdeDTO(c, dto);
        categoriaRepository.save(c);
        return categoriaMapper.entidadADto(c);
    }

    @Transactional
    @Override
    public void eliminarCategoria(Long id) {
        if (!categoriaRepository.existsById(id)) {
            throw new EntityNotFoundException("Categoría no encontrada con id: " + id);
        }
        categoriaRepository.deleteById(id);
    }
}
