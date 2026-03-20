package com.Campusland.ProyectoSpringBoot_CorpusEnrique.service.impl;

import com.Campusland.ProyectoSpringBoot_CorpusEnrique.dto.response.MovimientoInventarioResponse;
import com.Campusland.ProyectoSpringBoot_CorpusEnrique.dto.response.reporteExamenDTO;
import com.Campusland.ProyectoSpringBoot_CorpusEnrique.mappers.MovimientoInventarioMapper;
import com.Campusland.ProyectoSpringBoot_CorpusEnrique.model.MovimientoInventario;
import com.Campusland.ProyectoSpringBoot_CorpusEnrique.repository.ExamenSpringbootRepository;
import com.Campusland.ProyectoSpringBoot_CorpusEnrique.service.ExamenSpringboot;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class  ExamenSpringbootImpl implements ExamenSpringboot {
    private final ExamenSpringbootRepository examenRepository;
    private final MovimientoInventarioMapper mapper;

    @Override
    public List<MovimientoInventarioResponse> topRecientes() {
        return examenRepository.findTop10ByOrderByIdMovimientoDesc().stream()
                .map(mapper::entidadADto).toList();
    }

    @Override
    public reporteExamenDTO reportes() {
       Long cantidaMovimiento=  examenRepository.count();
       Long cantidadEntrada =  examenRepository.countByTipoMovimiento(MovimientoInventario.TipoMovimiento.ENTRADA);
       Long cantidadSalida = examenRepository.countByTipoMovimiento(MovimientoInventario.TipoMovimiento.SALIDA);
       Long cantidadAjuste = examenRepository.countByTipoMovimiento(MovimientoInventario.TipoMovimiento.AJUSTE);
       Long cantidadTraslado = examenRepository.countByTipoMovimiento(MovimientoInventario.TipoMovimiento.TRASLADO);

       return new reporteExamenDTO(cantidaMovimiento, cantidadEntrada, cantidadSalida,cantidadTraslado, cantidadAjuste);
    }

}
