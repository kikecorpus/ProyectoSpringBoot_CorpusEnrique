package com.Campusland.ProyectoSpringBoot_CorpusEnrique.service.impl;

import com.Campusland.ProyectoSpringBoot_CorpusEnrique.dto.request.RolRequest;
import com.Campusland.ProyectoSpringBoot_CorpusEnrique.dto.response.RolResponse;
import com.Campusland.ProyectoSpringBoot_CorpusEnrique.exception.BusinessRuleException;
import com.Campusland.ProyectoSpringBoot_CorpusEnrique.mappers.RolMapper;
import com.Campusland.ProyectoSpringBoot_CorpusEnrique.model.Rol;
import com.Campusland.ProyectoSpringBoot_CorpusEnrique.repository.RolRepository;
import com.Campusland.ProyectoSpringBoot_CorpusEnrique.service.RolService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RolServiceImpl implements RolService {

    private final RolMapper rolMapper;
    private final RolRepository rolRepository;

    @Override
    public RolResponse guardarRol(RolRequest dto) {
        if (rolRepository.existsByNombre(dto.nombre())) {
            throw new BusinessRuleException("Ya existe un rol con el nombre: " + dto.nombre());
        }
        Rol r = rolMapper.dtoAEntidad(dto);
        rolRepository.save(r);
        return rolMapper.entidadADto(r);
    }

    @Override
    public List<RolResponse> listarRoles() {
        return rolRepository.findAll()
                .stream()
                .map(rolMapper::entidadADto)
                .toList();
    }

    @Override
    public RolResponse obtenerRolPorId(Long id) {
        Rol r = rolRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Rol no encontrado con id: " + id));
        return rolMapper.entidadADto(r);
    }

    @Override
    public RolResponse actualizarRol(Long id, RolRequest dto) {
        Rol r = rolRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Rol no encontrado con id: " + id));
        if (rolRepository.existsByNombre(dto.nombre()) && !r.getNombre().equals(dto.nombre())) {
            throw new BusinessRuleException("Ya existe un rol con el nombre: " + dto.nombre());
        }
        rolMapper.actualizarEntidadDesdeDTO(r, dto);
        rolRepository.save(r);
        return rolMapper.entidadADto(r);
    }

    @Override
    public void eliminarRol(Long id) {
        if (!rolRepository.existsById(id)) {
            throw new EntityNotFoundException("Rol no encontrado con id: " + id);
        }
        rolRepository.deleteById(id);
    }
}
