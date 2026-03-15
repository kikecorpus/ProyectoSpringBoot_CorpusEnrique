package com.Campusland.ProyectoSpringBoot_CorpusEnrique.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Rol")
@Data
@Auditable
@EntityListeners(com.Campusland.ProyectoSpringBoot_CorpusEnrique.model.AuditoriaListener.class)

public class Rol {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_rol")
    private Long idRol;

    @Column(name = "nombre", nullable = false, unique = true, length = 50)
    private String nombre;

    @Column(name = "descripcion", length = 255)
    private String descripcion;
}
