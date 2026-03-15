package com.Campusland.ProyectoSpringBoot_CorpusEnrique.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Categoria")
@Data@Auditable
@EntityListeners(com.Campusland.ProyectoSpringBoot_CorpusEnrique.model.AuditoriaListener.class)

public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_categoria")
    private Long idCategoria;

    @Column(name = "nombre", nullable = false, unique = true, length = 100)
    private String nombre;
}
