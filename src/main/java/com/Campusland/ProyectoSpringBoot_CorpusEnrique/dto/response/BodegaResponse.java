package com.Campusland.ProyectoSpringBoot_CorpusEnrique.dto.response;

import com.Campusland.ProyectoSpringBoot_CorpusEnrique.model.Bodega.Estado;

public record BodegaResponse (  Integer idBodega,
        String nombre,
        String direccion,
        Integer capacidad,
        Estado estado,
        CiudadResponse ciudad,
        UsuarioResponse encargado){
}
