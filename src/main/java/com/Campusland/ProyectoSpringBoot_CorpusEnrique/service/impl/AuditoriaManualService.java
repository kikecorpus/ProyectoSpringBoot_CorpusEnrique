package com.Campusland.ProyectoSpringBoot_CorpusEnrique.service.impl;

import com.Campusland.ProyectoSpringBoot_CorpusEnrique.model.Auditoria;
import com.Campusland.ProyectoSpringBoot_CorpusEnrique.model.Usuario;
import com.Campusland.ProyectoSpringBoot_CorpusEnrique.repository.AuditoriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuditoriaManualService {

    private final AuditoriaRepository auditoriaRepository;

    public void registrarUpdate(
            String entidad,
            Long registroId,
            Map<String, Object> anterior,
            Map<String, Object> nuevo,
            Usuario usuario
    ) {
        Auditoria auditoria = Auditoria.builder()
                .entidadAfectada(entidad)
                .registroId(registroId)
                .tipoOperacion(Auditoria.TipoOperacion.UPDATE)
                .valoresAnteriores(anterior)
                .valoresNuevos(nuevo)
                .usuario(usuario)
                .build();

        auditoriaRepository.save(auditoria);
    }
    public void registrarMovimiento(
            String tipoMovimiento,
            Long inventarioId,
            String nombreProducto,
            String nombreBodega,
            int cantidadAnterior,
            int cantidadPosterior,
            Usuario usuario
    ) {
        Map<String, Object> anterior = Map.of(
                "inventarioId",  inventarioId,
                "producto",      nombreProducto,
                "bodega",        nombreBodega,
                "stock",         cantidadAnterior
        );

        Map<String, Object> nuevo = Map.of(
                "inventarioId",  inventarioId,
                "producto",      nombreProducto,
                "bodega",        nombreBodega,
                "stock",         cantidadPosterior,
                "tipoMovimiento", tipoMovimiento
        );

        Auditoria auditoria = Auditoria.builder()
                .entidadAfectada("Inventario por " + tipoMovimiento)
                .registroId(inventarioId)
                .tipoOperacion(Auditoria.TipoOperacion.UPDATE)
                .valoresAnteriores(anterior)
                .valoresNuevos(nuevo)
                .usuario(usuario)
                .build();

        auditoriaRepository.save(auditoria);
    }
}