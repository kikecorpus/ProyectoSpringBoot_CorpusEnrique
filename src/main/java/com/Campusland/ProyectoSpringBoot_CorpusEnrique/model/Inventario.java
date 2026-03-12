package com.Campusland.ProyectoSpringBoot_CorpusEnrique.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(
    name = "Inventario",
    uniqueConstraints = @UniqueConstraint(columnNames = {"producto_id", "bodega_id"})
)
@Data
public class Inventario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_inventario")
    private Integer idInventario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bodega_id", nullable = false)
    private Bodega bodega;

    @Column(name = "cantidad_actual", nullable = false)
    private Integer cantidadActual;

    @Column(name = "stock_minimo", nullable = false)
    private Integer stockMinimo;

    @Column(name = "stock_maximo", nullable = false)
    private Integer stockMaximo;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private Estado estado;

    @Column(name = "ultima_actualizacion")
    private LocalDateTime ultimaActualizacion;

    // Se actualiza en el servicio antes de guardar en db
    @PreUpdate
    public void preUpdate() {
        this.ultimaActualizacion = LocalDateTime.now();
    }

    @PrePersist
    public void prePersist() {
        this.ultimaActualizacion = LocalDateTime.now();
    }

    public enum Estado {
        ACTIVO, INACTIVO
    }
}
