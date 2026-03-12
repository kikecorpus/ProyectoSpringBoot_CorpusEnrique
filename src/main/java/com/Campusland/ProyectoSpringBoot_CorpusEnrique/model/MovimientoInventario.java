package com.Campusland.ProyectoSpringBoot_CorpusEnrique.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "MovimientoInventario")
@Getter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class MovimientoInventario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_movimiento")
    private Integer idMovimiento;

    // Nullable: ENTRADA no tiene origen
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inventario_origen_id")
    private Inventario inventarioOrigen;

    // Nullable: SALIDA y AJUSTE no tienen destino
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inventario_destino_id")
    private Inventario inventarioDestino;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_movimiento", nullable = false)
    private TipoMovimiento tipoMovimiento;

    @Column(name = "cantidad", nullable = false)
    private Integer cantidad;

    @Column(name = "cantidad_anterior", nullable = false)
    private Integer cantidadAnterior;

    @Column(name = "cantidad_posterior", nullable = false)
    private Integer cantidadPosterior;

    @Column(name = "referencia", length = 100)
    private String referencia;

    @Column(name = "observacion", columnDefinition = "TEXT")
    private String observacion;

    @Column(name = "fecha")
    private LocalDateTime fecha;

    @PrePersist
    public void prePersist() {
        this.fecha = LocalDateTime.now();
    }

    // Entidad inmutable: sin setters publicos
    // La validacion de origen/destino se hace en MovimientoService

    public enum TipoMovimiento {
        ENTRADA, SALIDA, TRASLADO, AJUSTE
    }
}
