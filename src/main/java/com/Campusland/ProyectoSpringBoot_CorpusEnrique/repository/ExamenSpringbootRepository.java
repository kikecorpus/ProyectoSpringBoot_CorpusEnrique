package com.Campusland.ProyectoSpringBoot_CorpusEnrique.repository;

import com.Campusland.ProyectoSpringBoot_CorpusEnrique.dto.response.RecientesDTO;
import com.Campusland.ProyectoSpringBoot_CorpusEnrique.model.MovimientoInventario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExamenSpringbootRepository extends JpaRepository<MovimientoInventario, Long> {

    @Query(value = """
        SELECT *
        FROM MovimientoInventario m
        ORDER BY id_movimiento DESC
        LIMIT 10
        """, nativeQuery = true)
    List<MovimientoInventario> FindRecientes();

    @Query(value = """
            SELECT COUNT(id_movimiento) as cantidadMovimientos  FROM MovimientoInventario m
                    GROUP BY tipo_movimiento;
        """, nativeQuery = true)

    RecientesDTO FindReporteExamen();
}
