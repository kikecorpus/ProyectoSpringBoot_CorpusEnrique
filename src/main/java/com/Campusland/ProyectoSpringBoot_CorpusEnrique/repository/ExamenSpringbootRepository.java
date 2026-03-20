package com.Campusland.ProyectoSpringBoot_CorpusEnrique.repository;

import com.Campusland.ProyectoSpringBoot_CorpusEnrique.model.MovimientoInventario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExamenSpringbootRepository extends JpaRepository<MovimientoInventario, Long> {

    List<MovimientoInventario> findTop10ByOrderByIdMovimientoDesc();

    //Cantidad total de movimientos registrados.
    //Número de movimientos por tipo (ENTRADA, SALIDA, TRANSFERENCIA).

    Long countByTipoMovimiento(MovimientoInventario.TipoMovimiento tp);

}
