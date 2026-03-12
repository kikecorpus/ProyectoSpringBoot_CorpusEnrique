package com.Campusland.ProyectoSpringBoot_CorpusEnrique.mappers;

import com.Campusland.ProyectoSpringBoot_CorpusEnrique.dto.request.BodegaRequest;
import com.Campusland.ProyectoSpringBoot_CorpusEnrique.dto.response.BodegaResponse;
import com.Campusland.ProyectoSpringBoot_CorpusEnrique.model.Bodega;
import com.Campusland.ProyectoSpringBoot_CorpusEnrique.model.Ciudad;
import com.Campusland.ProyectoSpringBoot_CorpusEnrique.model.Usuario;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BodegaMapper {

    private final CiudadMapper ciudadMapper;
    private final UsuarioMapper usuarioMapper;

    public BodegaResponse entidadADto(Bodega entidad) {
        if (entidad == null) { return null; }
        return new BodegaResponse(entidad.getIdBodega(),
                                  entidad.getNombre(),
                                  entidad.getDireccion(),
                                  entidad.getCapacidad(),
                                  entidad.getEstado(),
                                  ciudadMapper.entidadADto(entidad.getCiudad()),
                                  usuarioMapper.entidadADto(entidad.getEncargado()));
    }

    public Bodega dtoAEntidad(BodegaRequest dto, Ciudad ciudad, Usuario encargado) {
        if (dto == null) { return null; }
        Bodega b = new Bodega();
        b.setNombre(dto.nombre());
        b.setDireccion(dto.direccion());
        b.setCapacidad(dto.capacidad());
        b.setEstado(dto.estado());
        b.setCiudad(ciudad);
        b.setEncargado(encargado);
        return b;
    }

    public void actualizarEntidadDesdeDTO(Bodega entidad, BodegaRequest dto, Ciudad ciudad, Usuario encargado) {
        if (entidad == null || dto == null) { return; }
        entidad.setNombre(dto.nombre());
        entidad.setDireccion(dto.direccion());
        entidad.setCapacidad(dto.capacidad());
        entidad.setEstado(dto.estado());
        entidad.setCiudad(ciudad);
        entidad.setEncargado(encargado);
    }
}
