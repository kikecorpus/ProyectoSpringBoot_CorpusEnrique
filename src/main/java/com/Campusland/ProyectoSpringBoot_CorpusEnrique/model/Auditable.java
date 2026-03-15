package com.Campusland.ProyectoSpringBoot_CorpusEnrique.model;

// ============================================================
// Anotacion personalizada para marcar entidades auditables
// Uso: @Auditable sobre cualquier @Entity que quieras auditar
// ============================================================

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Auditable {
}
