package com.Campusland.ProyectoSpringBoot_CorpusEnrique.service.impl;

import com.Campusland.ProyectoSpringBoot_CorpusEnrique.dto.request.PersonaRequest;
import com.Campusland.ProyectoSpringBoot_CorpusEnrique.dto.response.PersonaResponse;
import com.Campusland.ProyectoSpringBoot_CorpusEnrique.exception.BusinessRuleException;
import com.Campusland.ProyectoSpringBoot_CorpusEnrique.mappers.PersonaMapper;
import com.Campusland.ProyectoSpringBoot_CorpusEnrique.model.Persona;
import com.Campusland.ProyectoSpringBoot_CorpusEnrique.repository.PersonaRepository;
import com.Campusland.ProyectoSpringBoot_CorpusEnrique.service.PersonaService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PersonaServiceImpl implements PersonaService {

    private final PersonaMapper personaMapper;
    private final PersonaRepository personaRepository;
    @Transactional
    @Override
    public PersonaResponse guardarPersona(PersonaRequest dto) {
        if (personaRepository.existsByNumeroDocumento(dto.numeroDocumento())) {
            throw new BusinessRuleException("Ya existe una persona con el documento: " + dto.numeroDocumento());
        }
        Persona p = personaMapper.dtoAEntidad(dto);
        personaRepository.save(p);
        return personaMapper.entidadADto(p);
    }

    @Override
    public List<PersonaResponse> listarPersonas() {
        return personaRepository.findAll()
                .stream()
                .map(personaMapper::entidadADto)
                .toList();
    }

    @Override
    public PersonaResponse obtenerPersonaPorId(Long id) {
        Persona p = personaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Persona no encontrada con id: " + id));
        return personaMapper.entidadADto(p);
    }
    @Transactional
    @Override
    public PersonaResponse actualizarPersona(Long id, PersonaRequest dto) {
        Persona p = personaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Persona no encontrada con id: " + id));
        if (personaRepository.existsByNumeroDocumento(dto.numeroDocumento())
                && !p.getNumeroDocumento().equals(dto.numeroDocumento())) {
            throw new BusinessRuleException("Ya existe una persona con el documento: " + dto.numeroDocumento());
        }
        personaMapper.actualizarEntidadDesdeDTO(p, dto);
        personaRepository.save(p);
        return personaMapper.entidadADto(p);
    }
    @Transactional
    @Override
    public void eliminarPersona(Long id) {
        if (!personaRepository.existsById(id)) {
            throw new EntityNotFoundException("Persona no encontrada con id: " + id);
        }
        personaRepository.deleteById(id);
    }
}
