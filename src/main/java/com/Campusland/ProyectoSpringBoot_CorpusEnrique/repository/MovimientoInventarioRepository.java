package com.Campusland.ProyectoSpringBoot_CorpusEnrique.repository;

import com.Campusland.ProyectoSpringBoot_CorpusEnrique.model.MovimientoInventario;
import com.Campusland.ProyectoSpringBoot_CorpusEnrique.model.MovimientoInventario.TipoMovimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
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
}
