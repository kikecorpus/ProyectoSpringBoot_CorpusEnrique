package com.Campusland.ProyectoSpringBoot_CorpusEnrique.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Ciudad")
@Data
@Auditable
@EntityListeners(com.Campusland.ProyectoSpringBoot_CorpusEnrique.model.AuditoriaListener.class)

public class Ciudad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ciudad")
    private Long idCiudad;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "departamento_id", nullable = false)
    private Departamento departamento;
}
