package com.Campusland.ProyectoSpringBoot_CorpusEnrique.service.impl;

import com.Campusland.ProyectoSpringBoot_CorpusEnrique.dto.response.AuditoriaResponse;
import com.Campusland.ProyectoSpringBoot_CorpusEnrique.mappers.AuditoriaMapper;
import com.Campusland.ProyectoSpringBoot_CorpusEnrique.model.Auditoria;
import com.Campusland.ProyectoSpringBoot_CorpusEnrique.repository.AuditoriaRepository;
import com.Campusland.ProyectoSpringBoot_CorpusEnrique.repository.UsuarioRepository;
import com.Campusland.ProyectoSpringBoot_CorpusEnrique.service.AuditoriaService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuditoriaServiceImpl implements AuditoriaService {

    private final AuditoriaMapper auditoriaMapper;
    private final AuditoriaRepository auditoriaRepository;
    private final UsuarioRepository usuarioRepository;

    @Override
    public List<AuditoriaResponse> listarAuditorias() {
        return auditoriaRepository.findAll()
                .stream()
                .map(auditoriaMapper::entidadADto)
                .toList();
    }

    @Override
    public AuditoriaResponse obtenerAuditoriaPorId(Long id) {
        Auditoria a = auditoriaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Auditoría no encontrada con id: " + id));
        return auditoriaMapper.entidadADto(a);
    }

    @Override
    public List<AuditoriaResponse> listarPorEntidad(String entidadAfectada) {
        return auditoriaRepository.findByEntidadAfectada(entidadAfectada)
                .stream()
                .map(auditoriaMapper::entidadADto)
                .toList();
    }

    @Override
    public List<AuditoriaResponse> listarPorUsuario(Long usuarioId) {
        if (!usuarioRepository.existsById(usuarioId)) {
            throw new EntityNotFoundException("Usuario no encontrado con id: " + usuarioId);
        }
        return auditoriaRepository.findByUsuarioIdUsuario(usuarioId)
                .stream()
                .map(auditoriaMapper::entidadADto)
                .toList();
    }
    @Override
    public List<AuditoriaResponse> listarPorTipoOperacion(Auditoria.TipoOperacion tipoOperacion) {
        return auditoriaRepository.findByTipoOperacion(tipoOperacion)
                .stream()
                .map(auditoriaMapper::entidadADto)
                .toList();
    }
}
