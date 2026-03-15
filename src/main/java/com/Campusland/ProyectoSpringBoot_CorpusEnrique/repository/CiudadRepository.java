package com.Campusland.ProyectoSpringBoot_CorpusEnrique.repository;

import com.Campusland.ProyectoSpringBoot_CorpusEnrique.model.Ciudad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CiudadRepository extends JpaRepository<Ciudad, Long> {
    List<Ciudad> findByDepartamentoIdDepartamento(Long departamentoId);
    boolean existsByNombreAndDepartamentoIdDepartamento(String nombre, Long departamentoId);
}
