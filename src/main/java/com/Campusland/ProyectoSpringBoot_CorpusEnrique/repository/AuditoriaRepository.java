package com.Campusland.ProyectoSpringBoot_CorpusEnrique.repository;

import com.Campusland.ProyectoSpringBoot_CorpusEnrique.model.Auditoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AuditoriaRepository extends JpaRepository<Auditoria, Long> {
    List<Auditoria> findByEntidadAfectada(String entidadAfectada);
    List<Auditoria> findByUsuarioIdUsuario(Long usuarioId);
    List<Auditoria> findByTipoOperacion(Auditoria.TipoOperacion tipoOperacion);
}
