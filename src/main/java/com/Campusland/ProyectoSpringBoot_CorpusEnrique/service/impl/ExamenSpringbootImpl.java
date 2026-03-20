package com.Campusland.ProyectoSpringBoot_CorpusEnrique.service.impl;

import com.Campusland.ProyectoSpringBoot_CorpusEnrique.dto.response.MovimientoInventarioResponse;
import com.Campusland.ProyectoSpringBoot_CorpusEnrique.mappers.MovimientoInventarioMapper;
import com.Campusland.ProyectoSpringBoot_CorpusEnrique.repository.ExamenSpringbootRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class  ExamenSpringbootImpl {
    private final ExamenSpringbootRepository examenRepository;
    private  final MovimientoInventarioMapper mapper;


    public List<MovimientoInventarioResponse> listarRecientes (){

      return examenRepository.FindRecientes() .stream()
              .map(mapper::entidadADto).toList();
    }

    public List<MovimientoInventarioResponse> reportes(){


    }
}
