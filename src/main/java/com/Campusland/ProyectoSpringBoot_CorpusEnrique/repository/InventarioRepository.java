package com.Campusland.ProyectoSpringBoot_CorpusEnrique.repository;

import com.Campusland.ProyectoSpringBoot_CorpusEnrique.model.Inventario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface InventarioRepository extends JpaRepository<Inventario, Long> {
    boolean existsByProductoIdProductoAndBodegaIdBodega(Long productoId, Long bodegaId);
    Optional<Inventario> findByProductoIdProductoAndBodegaIdBodega(Long productoId, Long bodegaId);
    List<Inventario> findByBodegaIdBodega(Long bodegaId);
    List<Inventario> findByProductoIdProducto(Long productoId);

    // stock minimo
    @Query(value = "SELECT * FROM Inventario WHERE cantidad_actual <= stock_minimo", nativeQuery = true)
    List<Inventario> findInventariosConStockCritico();

    //stock Bajo de 10 unidades
    @Query(value = "SELECT * FROM Inventario WHERE cantidad_actual < 10", nativeQuery = true)
    List<Inventario> findInventariosConStockBajo();

    //reporte
    @Query(value = """
        SELECT b.nombre as nombreBodega,
               c.nombre as ciudad,
               COUNT(i.id_inventario) as totalProductos,
               SUM(i.cantidad_actual) as stockTotal
        FROM Inventario i
        JOIN Bodega b ON i.bodega_id = b.id_bodega
        JOIN Ciudad c ON b.ciudad_id = c.id_ciudad
        GROUP BY b.id_bodega, b.nombre, c.nombre
        """, nativeQuery = true)
    List<Object[]> findStockPorBodega();
}
