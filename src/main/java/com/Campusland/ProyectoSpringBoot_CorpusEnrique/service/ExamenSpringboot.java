package com.Campusland.ProyectoSpringBoot_CorpusEnrique.service;

import com.Campusland.ProyectoSpringBoot_CorpusEnrique.dto.response.MovimientoInventarioResponse;
import com.Campusland.ProyectoSpringBoot_CorpusEnrique.dto.response.reporteExamenDTO;

import java.util.List;

public interface ExamenSpringboot {

    List<MovimientoInventarioResponse> topRecientes();

    reporteExamenDTO reportes();

}
