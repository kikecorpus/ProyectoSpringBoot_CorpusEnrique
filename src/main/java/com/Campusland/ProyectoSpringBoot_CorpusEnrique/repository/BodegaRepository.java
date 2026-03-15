package com.Campusland.ProyectoSpringBoot_CorpusEnrique.repository;

import com.Campusland.ProyectoSpringBoot_CorpusEnrique.model.Bodega;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BodegaRepository extends JpaRepository<Bodega, Long> {
    List<Bodega> findByCiudadIdCiudad(Long ciudadId);
    List<Bodega> findByEncargadoIdUsuario(Long usuarioId);
    boolean existsByNombre(String nombre);
}
