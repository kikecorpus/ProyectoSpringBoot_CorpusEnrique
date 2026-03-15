package com.Campusland.ProyectoSpringBoot_CorpusEnrique.service.impl;

import com.Campusland.ProyectoSpringBoot_CorpusEnrique.dto.request.BodegaRequest;
import com.Campusland.ProyectoSpringBoot_CorpusEnrique.dto.response.BodegaResponse;
import com.Campusland.ProyectoSpringBoot_CorpusEnrique.exception.BusinessRuleException;
import com.Campusland.ProyectoSpringBoot_CorpusEnrique.mappers.BodegaMapper;
import com.Campusland.ProyectoSpringBoot_CorpusEnrique.model.Bodega;
import com.Campusland.ProyectoSpringBoot_CorpusEnrique.model.Ciudad;
import com.Campusland.ProyectoSpringBoot_CorpusEnrique.model.Usuario;
import com.Campusland.ProyectoSpringBoot_CorpusEnrique.repository.BodegaRepository;
import com.Campusland.ProyectoSpringBoot_CorpusEnrique.repository.CiudadRepository;
import com.Campusland.ProyectoSpringBoot_CorpusEnrique.repository.UsuarioRepository;
import com.Campusland.ProyectoSpringBoot_CorpusEnrique.service.BodegaService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BodegaServiceImpl implements BodegaService {

    private final BodegaMapper bodegaMapper;
    private final BodegaRepository bodegaRepository;
    private final CiudadRepository ciudadRepository;
    private final UsuarioRepository usuarioRepository;

    @Override
    public BodegaResponse guardarBodega(BodegaRequest dto) {
        if (bodegaRepository.existsByNombre(dto.nombre())) {
            throw new BusinessRuleException("Ya existe una bodega con el nombre: " + dto.nombre());
        }
        Ciudad ciudad = ciudadRepository.findById(dto.ciudadId())
                .orElseThrow(() -> new EntityNotFoundException("Ciudad no encontrada con id: " + dto.ciudadId()));
        Usuario encargado = usuarioRepository.findById(dto.encargadoId())
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con id: " + dto.encargadoId()));

        Bodega b = bodegaMapper.dtoAEntidad(dto, ciudad, encargado);
        bodegaRepository.save(b);
        return bodegaMapper.entidadADto(b);
    }

    @Override
    public List<BodegaResponse> listarBodegas() {
        return bodegaRepository.findAll()
                .stream()
                .map(bodegaMapper::entidadADto)
                .toList();
    }

    @Override
    public BodegaResponse obtenerBodegaPorId(Long id) {
        Bodega b = bodegaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Bodega no encontrada con id: " + id));
        return bodegaMapper.entidadADto(b);
    }

    @Override
    public BodegaResponse actualizarBodega(Long id, BodegaRequest dto) {
        Bodega b = bodegaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Bodega no encontrada con id: " + id));
        if (bodegaRepository.existsByNombre(dto.nombre()) && !b.getNombre().equals(dto.nombre())) {
            throw new BusinessRuleException("Ya existe una bodega con el nombre: " + dto.nombre());
        }
        Ciudad ciudad = ciudadRepository.findById(dto.ciudadId())
                .orElseThrow(() -> new EntityNotFoundException("Ciudad no encontrada con id: " + dto.ciudadId()));
        Usuario encargado = usuarioRepository.findById(dto.encargadoId())
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con id: " + dto.encargadoId()));

        bodegaMapper.actualizarEntidadDesdeDTO(b, dto, ciudad, encargado);
        bodegaRepository.save(b);
        return bodegaMapper.entidadADto(b);
    }

    @Override
    public void eliminarBodega(Long id) {
        if (!bodegaRepository.existsById(id)) {
            throw new EntityNotFoundException("Bodega no encontrada con id: " + id);
        }
        bodegaRepository.deleteById(id);
    }
}
