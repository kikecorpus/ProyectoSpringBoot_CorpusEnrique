package com.Campusland.ProyectoSpringBoot_CorpusEnrique.service.impl;

import com.Campusland.ProyectoSpringBoot_CorpusEnrique.dto.request.CiudadRequest;
import com.Campusland.ProyectoSpringBoot_CorpusEnrique.dto.response.CiudadResponse;
import com.Campusland.ProyectoSpringBoot_CorpusEnrique.exception.BusinessRuleException;
import com.Campusland.ProyectoSpringBoot_CorpusEnrique.mappers.CiudadMapper;
import com.Campusland.ProyectoSpringBoot_CorpusEnrique.model.Ciudad;
import com.Campusland.ProyectoSpringBoot_CorpusEnrique.model.Departamento;
import com.Campusland.ProyectoSpringBoot_CorpusEnrique.repository.CiudadRepository;
import com.Campusland.ProyectoSpringBoot_CorpusEnrique.repository.DepartamentoRepository;
import com.Campusland.ProyectoSpringBoot_CorpusEnrique.service.CiudadService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CiudadServiceImpl implements CiudadService {

    private final CiudadMapper ciudadMapper;
    private final CiudadRepository ciudadRepository;
    private final DepartamentoRepository departamentoRepository;
    @Transactional
    @Override
    public CiudadResponse guardarCiudad(CiudadRequest dto) {
        Departamento departamento = departamentoRepository.findById(dto.departamentoId())
                .orElseThrow(() -> new EntityNotFoundException("Departamento no encontrado con id: " + dto.departamentoId()));
        if (ciudadRepository.existsByNombreAndDepartamentoIdDepartamento(dto.nombre(), dto.departamentoId())) {
            throw new BusinessRuleException("Ya existe la ciudad '" + dto.nombre() + "' en ese departamento");
        }
        Ciudad c = ciudadMapper.dtoAEntidad(dto, departamento);
        ciudadRepository.save(c);
        return ciudadMapper.entidadADto(c);
    }

    @Override
    public List<CiudadResponse> listarCiudades() {
        return ciudadRepository.findAll()
                .stream()
                .map(ciudadMapper::entidadADto)
                .toList();
    }

    @Override
    public CiudadResponse obtenerCiudadPorId(Long id) {
        Ciudad c = ciudadRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Ciudad no encontrada con id: " + id));
        return ciudadMapper.entidadADto(c);
    }
    @Transactional
    @Override
    public CiudadResponse actualizarCiudad(Long id, CiudadRequest dto) {
        Ciudad c = ciudadRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Ciudad no encontrada con id: " + id));
        Departamento departamento = departamentoRepository.findById(dto.departamentoId())
                .orElseThrow(() -> new EntityNotFoundException("Departamento no encontrado con id: " + dto.departamentoId()));
        if (ciudadRepository.existsByNombreAndDepartamentoIdDepartamento(dto.nombre(), dto.departamentoId())
                && !c.getNombre().equals(dto.nombre())) {
            throw new BusinessRuleException("Ya existe la ciudad '" + dto.nombre() + "' en ese departamento");
        }
        ciudadMapper.actualizarEntidadDesdeDTO(c, dto, departamento);
        ciudadRepository.save(c);
        return ciudadMapper.entidadADto(c);
    }
    @Transactional
    @Override
    public void eliminarCiudad(Long id) {
        if (!ciudadRepository.existsById(id)) {
            throw new EntityNotFoundException("Ciudad no encontrada con id: " + id);
        }
        ciudadRepository.deleteById(id);
    }
}
