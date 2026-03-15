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
}
