package com.Campusland.ProyectoSpringBoot_CorpusEnrique.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.Campusland.ProyectoSpringBoot_CorpusEnrique.model.Auditoria;
import com.Campusland.ProyectoSpringBoot_CorpusEnrique.model.Usuario;
import jakarta.persistence.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

@Component
public class AuditoriaListener {

    // EntityListeners necesitan acceso al contexto de Spring via holder estatico
    private static EntityManager entityManager;
    private static ObjectMapper objectMapper;

    @Autowired
    public void setEntityManager(EntityManager em) {
        AuditoriaListener.entityManager = em;
    }

    @Autowired
    public void setObjectMapper(ObjectMapper mapper) {
        AuditoriaListener.objectMapper = mapper;
    }


    @PostPersist
    public void onPostPersist(Object entidad) {
        if (entidad.getClass().isAnnotationPresent(Auditable.class)) {
            registrar(entidad, Auditoria.TipoOperacion.INSERT, null, toMap(entidad));
        }
    }

    @PostUpdate
    public void onPostUpdate(Object entidad) {
        if (entidad.getClass().isAnnotationPresent(Auditable.class)) {
            registrar(entidad, Auditoria.TipoOperacion.UPDATE, null, toMap(entidad));
        }
    }

    @PostRemove
    public void onPostRemove(Object entidad) {
        if (entidad.getClass().isAnnotationPresent(Auditable.class)) {
            registrar(entidad, Auditoria.TipoOperacion.DELETE, toMap(entidad), null);
        }
    }

   
    // Metodo central que guarda el registro de auditoria
    private void registrar(
            Object entidad,
            Auditoria.TipoOperacion operacion,
            Map<String, Object> valoresAnteriores,
            Map<String, Object> valoresNuevos
    ) {
        try {
            Auditoria auditoria = Auditoria.builder()
                    .usuario(getUsuarioActivo())
                    .entidadAfectada(entidad.getClass().getSimpleName())
                    .registroId(getId(entidad))
                    .tipoOperacion(operacion)
                    .valoresAnteriores(valoresAnteriores)
                    .valoresNuevos(valoresNuevos)
                    .build();

            entityManager.persist(auditoria);

        } catch (Exception e) {
            // No interrumpir la operacion principal si la auditoria falla
            System.err.println("Error al registrar auditoria: " + e.getMessage());
        }
    }

    // Obtiene el usuario autenticado desde Spring Security
    private Usuario getUsuarioActivo() {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.getPrincipal() instanceof Usuario) {
                return (Usuario) auth.getPrincipal();
            }
        } catch (Exception ignored) {}
        return null;
    }

   
    // Extrae el ID de la entidad via reflexion
    private Long getId(Object entidad) {
        try {
            for (Field field : entidad.getClass().getDeclaredFields()) {
                if (field.isAnnotationPresent(Id.class)) {
                    field.setAccessible(true);
                    return (Long) field.get(entidad);
                }
            }
        } catch (Exception ignored) {}
        return null;
    }

   
    // Convierte la entidad a Map<String, Object> para JSON
    @SuppressWarnings("unchecked")
    private Map<String, Object> toMap(Object entidad) {
        try {
            return objectMapper.convertValue(entidad, Map.class);
        } catch (Exception e) {
            Map<String, Object> fallback = new HashMap<>();
            fallback.put("error", "No se pudo serializar la entidad");
            return fallback;
        }
    }
}
