package com.Campusland.ProyectoSpringBoot_CorpusEnrique.repository;

import com.Campusland.ProyectoSpringBoot_CorpusEnrique.model.MovimientoInventario;
import com.Campusland.ProyectoSpringBoot_CorpusEnrique.model.MovimientoInventario.TipoMovimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MovimientoInventarioRepository extends JpaRepository<MovimientoInventario, Long> {

    // consultar inventario origen o destino
    @Query(value = "SELECT * FROM MovimientoInventario " +
                   "WHERE inventario_origen_id = :inventarioId " +
                   "OR inventario_destino_id = :inventarioId", nativeQuery = true)
    List<MovimientoInventario> findByInventarioId(@Param("inventarioId") Long inventarioId);

    List<MovimientoInventario> findByUsuarioIdUsuario(Long usuarioId);
    List<MovimientoInventario> findByTipoMovimiento(TipoMovimiento tipoMovimiento);
    @Query(value = "SELECT * FROM MovimientoInventario WHERE fecha BETWEEN :desde AND :hasta",
            nativeQuery = true)
    List<MovimientoInventario> findByFechaBetween(
            @Param("desde") LocalDateTime desde,
            @Param("hasta") LocalDateTime hasta);

    //reporte
    @Query(value = """
        SELECT p.nombre as nombreProducto,
               p.codigo as codigoProducto,
               COUNT(m.id_movimiento) as totalMovimientos,
               SUM(m.cantidad) as totalUnidadesMovidas
        FROM MovimientoInventario m
        JOIN Inventario i ON m.inventario_origen_id = i.id_inventario
                          OR m.inventario_destino_id = i.id_inventario
        JOIN Producto p ON i.producto_id = p.id_producto
        GROUP BY p.id_producto, p.nombre, p.codigo
        ORDER BY totalMovimientos DESC
        LIMIT 10
        """, nativeQuery = true)
    List<Object[]> findProductosMasMovidos();
}
