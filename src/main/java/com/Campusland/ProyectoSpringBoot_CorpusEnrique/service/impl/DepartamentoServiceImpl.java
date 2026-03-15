package com.Campusland.ProyectoSpringBoot_CorpusEnrique.service.impl;

import com.Campusland.ProyectoSpringBoot_CorpusEnrique.dto.request.DepartamentoRequest;
import com.Campusland.ProyectoSpringBoot_CorpusEnrique.dto.response.DepartamentoResponse;
import com.Campusland.ProyectoSpringBoot_CorpusEnrique.exception.BusinessRuleException;
import com.Campusland.ProyectoSpringBoot_CorpusEnrique.mappers.DepartamentoMapper;
import com.Campusland.ProyectoSpringBoot_CorpusEnrique.model.Departamento;
import com.Campusland.ProyectoSpringBoot_CorpusEnrique.repository.DepartamentoRepository;
import com.Campusland.ProyectoSpringBoot_CorpusEnrique.service.DepartamentoService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DepartamentoServiceImpl implements DepartamentoService {

    private final DepartamentoMapper departamentoMapper;
    private final DepartamentoRepository departamentoRepository;

    @Override
    public DepartamentoResponse guardarDepartamento(DepartamentoRequest dto) {
        if (departamentoRepository.existsByNombre(dto.nombre())) {
            throw new BusinessRuleException("Ya existe un departamento con el nombre: " + dto.nombre());
        }
        Departamento d = departamentoMapper.dtoAEntidad(dto);
        departamentoRepository.save(d);
        return departamentoMapper.entidadADto(d);
    }

    @Override
    public List<DepartamentoResponse> listarDepartamentos() {
        return departamentoRepository.findAll()
                .stream()
                .map(departamentoMapper::entidadADto)
                .toList();
    }

    @Override
    public DepartamentoResponse obtenerDepartamentoPorId(Long id) {
        Departamento d = departamentoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Departamento no encontrado con id: " + id));
        return departamentoMapper.entidadADto(d);
    }

    @Override
    public DepartamentoResponse actualizarDepartamento(Long id, DepartamentoRequest dto) {
        Departamento d = departamentoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Departamento no encontrado con id: " + id));
        if (departamentoRepository.existsByNombre(dto.nombre()) && !d.getNombre().equals(dto.nombre())) {
            throw new BusinessRuleException("Ya existe un departamento con el nombre: " + dto.nombre());
        }
        departamentoMapper.actualizarEntidadDesdeDTO(d, dto);
        departamentoRepository.save(d);
        return departamentoMapper.entidadADto(d);
    }

    @Override
    public void eliminarDepartamento(Long id) {
        if (!departamentoRepository.existsById(id)) {
            throw new EntityNotFoundException("Departamento no encontrado con id: " + id);
        }
        departamentoRepository.deleteById(id);
    }
}
