package com.Campusland.ProyectoSpringBoot_CorpusEnrique.model;

import jakarta.persistence.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Component
public class AuditoriaListener {

    private static EntityManager entityManager;

    @Autowired
    public void setEntityManager(EntityManager em) {
        AuditoriaListener.entityManager = em;
    }

    @PostPersist
    public void onPostPersist(Object entidad) {
        if (entidad.getClass().isAnnotationPresent(Auditable.class)) {
            registrar(entidad, Auditoria.TipoOperacion.INSERT, null, toMap(entidad));
        }
    }


    @PostRemove
    public void onPostRemove(Object entidad) {
        if (entidad.getClass().isAnnotationPresent(Auditable.class)) {
            registrar(entidad, Auditoria.TipoOperacion.DELETE, toMap(entidad), null);
        }
    }

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
            System.err.println("Error al registrar auditoria: " + e.getMessage());
        }
    }

    private Usuario getUsuarioActivo() {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.getPrincipal() instanceof Usuario) {
                return (Usuario) auth.getPrincipal();
            }
        } catch (Exception ignored) {}
        return null;
    }

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

    private Map<String, Object> toMap(Object entidad) {
        Map<String, Object> result = new HashMap<>();
        try {
            for (Field field : entidad.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                Class<?> type = field.getType();
                if (type.isPrimitive()
                        || type == String.class
                        || type == Long.class
                        || type == Integer.class
                        || type == Boolean.class
                        || type.isEnum()
                        || type == LocalDateTime.class) {
                    result.put(field.getName(), field.get(entidad));
                }
            }
        } catch (Exception e) {
            result.put("error", "No se pudo serializar: " + e.getMessage());
        }
        return result;
    }
}