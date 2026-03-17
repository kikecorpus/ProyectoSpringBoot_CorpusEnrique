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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class BodegaServiceImpl implements BodegaService {

    private final BodegaMapper bodegaMapper;
    private final BodegaRepository bodegaRepository;
    private final CiudadRepository ciudadRepository;
    private final UsuarioRepository usuarioRepository;
    private final AuditoriaManualService auditoriaManualService;
    @Override
    @Transactional
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

    @Transactional
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

        Map<String, Object> anterior = Map.of(
                "nombre",    b.getNombre(),
                "direccion", b.getDireccion(),
                "capacidad", b.getCapacidad(),
                "estado",    b.getEstado().name(),
                "ciudadId",  b.getCiudad().getIdCiudad()
        );

        bodegaMapper.actualizarEntidadDesdeDTO(b, dto, ciudad, encargado);
        bodegaRepository.save(b);

        Map<String, Object> nuevo = Map.of(
                "nombre",    b.getNombre(),
                "direccion", b.getDireccion(),
                "capacidad", b.getCapacidad(),
                "estado",    b.getEstado().name(),
                "ciudadId",  b.getCiudad().getIdCiudad()
        );

        Usuario usuarioActivo = (Usuario) SecurityContextHolder
                .getContext().getAuthentication().getPrincipal();
        auditoriaManualService.registrarUpdate("Bodega", id, anterior, nuevo, usuarioActivo);

        return bodegaMapper.entidadADto(b);
    }

    @Transactional
    @Override
    public void eliminarBodega(Long id) {
        if (!bodegaRepository.existsById(id)) {
            throw new EntityNotFoundException("Bodega no encontrada con id: " + id);
        }
        bodegaRepository.deleteById(id);
    }
}
