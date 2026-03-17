package com.Campusland.ProyectoSpringBoot_CorpusEnrique.service.impl;

import com.Campusland.ProyectoSpringBoot_CorpusEnrique.dto.request.MovimientoInventarioRequest;
import com.Campusland.ProyectoSpringBoot_CorpusEnrique.dto.response.MovimientoInventarioResponse;
import com.Campusland.ProyectoSpringBoot_CorpusEnrique.exception.BusinessRuleException;
import com.Campusland.ProyectoSpringBoot_CorpusEnrique.mappers.MovimientoInventarioMapper;
import com.Campusland.ProyectoSpringBoot_CorpusEnrique.model.Inventario;
import com.Campusland.ProyectoSpringBoot_CorpusEnrique.model.MovimientoInventario;
import com.Campusland.ProyectoSpringBoot_CorpusEnrique.model.Usuario;
import com.Campusland.ProyectoSpringBoot_CorpusEnrique.repository.InventarioRepository;
import com.Campusland.ProyectoSpringBoot_CorpusEnrique.repository.MovimientoInventarioRepository;
import com.Campusland.ProyectoSpringBoot_CorpusEnrique.repository.UsuarioRepository;
import com.Campusland.ProyectoSpringBoot_CorpusEnrique.service.MovimientoInventarioService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MovimientoInventarioServiceImpl implements MovimientoInventarioService {

    private final MovimientoInventarioMapper movimientoMapper;
    private final MovimientoInventarioRepository movimientoRepository;
    private final InventarioRepository inventarioRepository;
    private final UsuarioRepository usuarioRepository;
    private final AuditoriaManualService auditoriaManualService;

    @Override
    @Transactional
    public MovimientoInventarioResponse registrarMovimiento(MovimientoInventarioRequest dto, String username) {
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con id: " + username));

        boolean esOperario = usuario.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("OPERARIO"));
        if (esOperario) {
            if (usuario.getBodega() == null) {
                throw new BusinessRuleException(
                        "El operario no tiene una bodega asignada. Contacte al administrador.");
            }
            validarBodegaOperario(dto, usuario.getBodega().getIdBodega());
        }

        MovimientoInventario movimiento = switch (dto.tipoMovimiento()) {
            case ENTRADA  -> registrarEntrada(dto, usuario);
            case SALIDA   -> registrarSalida(dto, usuario);
            case TRASLADO -> registrarTraslado(dto, usuario);
            case AJUSTE   -> registrarAjuste(dto, usuario);
        };

        movimientoRepository.save(movimiento);

// ── Audita el impacto en inventario
        auditoriaManualService.registrarMovimiento(
                dto.tipoMovimiento().name(),
                movimiento.getInventarioOrigen() != null
                        ? movimiento.getInventarioOrigen().getIdInventario()
                        : movimiento.getInventarioDestino().getIdInventario(),
                movimiento.getInventarioOrigen() != null
                        ? movimiento.getInventarioOrigen().getProducto().getNombre()
                        : movimiento.getInventarioDestino().getProducto().getNombre(),
                movimiento.getInventarioOrigen() != null
                        ? movimiento.getInventarioOrigen().getBodega().getNombre()
                        : movimiento.getInventarioDestino().getBodega().getNombre(),
                movimiento.getCantidadAnterior(),
                movimiento.getCantidadPosterior(),
                usuario
        );

        return movimientoMapper.entidadADto(movimiento);
    }

    // Operaciones privadas por tipo
    @Transactional
    private MovimientoInventario registrarEntrada(MovimientoInventarioRequest dto, Usuario usuario) {
        if (dto.inventarioDestinoId() == null) {
            throw new BusinessRuleException("Una ENTRADA requiere un inventario destino");
        }
        Inventario destino = obtenerInventario(dto.inventarioDestinoId());
        int cantidadAnterior = destino.getCantidadActual();
        int cantidadPosterior = cantidadAnterior + dto.cantidad();

        if (cantidadPosterior > destino.getStockMaximo()) {
            throw new BusinessRuleException("La entrada supera el stock máximo del inventario destino");
        }
        destino.setCantidadActual(cantidadPosterior);
        inventarioRepository.save(destino);

        return MovimientoInventario.builder()
                .inventarioOrigen(null)
                .inventarioDestino(destino)
                .usuario(usuario)
                .tipoMovimiento(dto.tipoMovimiento())
                .cantidad(dto.cantidad())
                .cantidadAnterior(cantidadAnterior)
                .cantidadPosterior(cantidadPosterior)
                .referencia(dto.referencia())
                .observacion(dto.observacion())
                .build();
    }
    @Transactional
    private MovimientoInventario registrarSalida(MovimientoInventarioRequest dto, Usuario usuario) {
        if (dto.inventarioOrigenId() == null) {
            throw new BusinessRuleException("Una SALIDA requiere un inventario origen");
        }
        Inventario origen = obtenerInventario(dto.inventarioOrigenId());
        int cantidadAnterior = origen.getCantidadActual();
        int cantidadPosterior = cantidadAnterior - dto.cantidad();

        if (cantidadPosterior < 0) {
            throw new BusinessRuleException("Stock insuficiente en el inventario origen");
        }
        origen.setCantidadActual(cantidadPosterior);
        inventarioRepository.save(origen);

        return MovimientoInventario.builder()
                .inventarioOrigen(origen)
                .inventarioDestino(null)
                .usuario(usuario)
                .tipoMovimiento(dto.tipoMovimiento())
                .cantidad(dto.cantidad())
                .cantidadAnterior(cantidadAnterior)
                .cantidadPosterior(cantidadPosterior)
                .referencia(dto.referencia())
                .observacion(dto.observacion())
                .build();
    }
    @Transactional
    private MovimientoInventario registrarTraslado(MovimientoInventarioRequest dto, Usuario usuario) {
        if (dto.inventarioOrigenId() == null || dto.inventarioDestinoId() == null) {
            throw new BusinessRuleException("Un TRASLADO requiere inventario origen y destino");
        }
        if (dto.inventarioOrigenId().equals(dto.inventarioDestinoId())) {
            throw new BusinessRuleException("El inventario origen y destino no pueden ser el mismo");
        }
        Inventario origen  = obtenerInventario(dto.inventarioOrigenId());
        Inventario destino = obtenerInventario(dto.inventarioDestinoId());

        int cantidadAnteriorOrigen = origen.getCantidadActual();
        int cantidadPosteriorOrigen = cantidadAnteriorOrigen - dto.cantidad();

        if (cantidadPosteriorOrigen < 0) {
            throw new BusinessRuleException("Stock insuficiente en el inventario origen para el traslado");
        }
        if (destino.getCantidadActual() + dto.cantidad() > destino.getStockMaximo()) {
            throw new BusinessRuleException("El traslado supera el stock máximo del inventario destino");
        }

        origen.setCantidadActual(cantidadPosteriorOrigen);
        destino.setCantidadActual(destino.getCantidadActual() + dto.cantidad());
        inventarioRepository.save(origen);
        inventarioRepository.save(destino);

        return MovimientoInventario.builder()
                .inventarioOrigen(origen)
                .inventarioDestino(destino)
                .usuario(usuario)
                .tipoMovimiento(dto.tipoMovimiento())
                .cantidad(dto.cantidad())
                .cantidadAnterior(cantidadAnteriorOrigen)
                .cantidadPosterior(cantidadPosteriorOrigen)
                .referencia(dto.referencia())
                .observacion(dto.observacion())
                .build();
    }
    @Transactional
    private MovimientoInventario registrarAjuste(MovimientoInventarioRequest dto, Usuario usuario) {
        if (dto.inventarioOrigenId() == null) {
            throw new BusinessRuleException("Un AJUSTE requiere un inventario origen");
        }
        Inventario inventario = obtenerInventario(dto.inventarioOrigenId());
        int cantidadAnterior = inventario.getCantidadActual();

        // En un ajuste la cantidad del dto representa el nuevo valor absoluto
        if (dto.cantidad() > inventario.getStockMaximo()) {
            throw new BusinessRuleException("La cantidad del ajuste supera el stock máximo");
        }
        inventario.setCantidadActual(dto.cantidad());
        inventarioRepository.save(inventario);

        return MovimientoInventario.builder()
                .inventarioOrigen(inventario)
                .inventarioDestino(null)
                .usuario(usuario)
                .tipoMovimiento(dto.tipoMovimiento())
                .cantidad(dto.cantidad())
                .cantidadAnterior(cantidadAnterior)
                .cantidadPosterior(dto.cantidad())
                .referencia(dto.referencia())
                .observacion(dto.observacion())
                .build();
    }

    // Consultas

    @Override
    public List<MovimientoInventarioResponse> listarMovimientos() {
        return movimientoRepository.findAll()
                .stream()
                .map(movimientoMapper::entidadADto)
                .toList();
    }

    @Override
    public MovimientoInventarioResponse obtenerMovimientoPorId(Long id) {
        MovimientoInventario m = movimientoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Movimiento no encontrado con id: " + id));
        return movimientoMapper.entidadADto(m);
    }

    @Override
    public List<MovimientoInventarioResponse> listarMovimientosPorInventario(Long inventarioId) {
        if (!inventarioRepository.existsById(inventarioId)) {
            throw new EntityNotFoundException("Inventario no encontrado con id: " + inventarioId);
        }
        return movimientoRepository.findByInventarioId(inventarioId)
                .stream()
                .map(movimientoMapper::entidadADto)
                .toList();
    }

    // Utilitario

    private Inventario obtenerInventario(Long id) {
        return inventarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Inventario no encontrado con id: " + id));
    }

    @Override
    public List<MovimientoInventarioResponse> listarMovimientosPorRangoFechas(
            LocalDateTime desde, LocalDateTime hasta) {
        if (desde.isAfter(hasta)) {
            throw new BusinessRuleException("La fecha inicio no puede ser mayor a la fecha fin");
        }
        return movimientoRepository.findByFechaBetween(desde, hasta)
                .stream()
                .map(movimientoMapper::entidadADto)
                .toList();
    }
    private void validarBodegaOperario(MovimientoInventarioRequest dto, Long bodegaAsignada) {

        switch (dto.tipoMovimiento()) {

            case ENTRADA -> {
                // Solo valida que el DESTINO sea su bodega
                if (dto.inventarioDestinoId() != null) {
                    Inventario destino = inventarioRepository.findById(dto.inventarioDestinoId())
                            .orElseThrow(() -> new EntityNotFoundException(
                                    "Inventario no encontrado con id: " + dto.inventarioDestinoId()));
                    if (!destino.getBodega().getIdBodega().equals(bodegaAsignada)) {
                        throw new BusinessRuleException(
                                "Solo puedes recibir mercancía en tu bodega asignada.");
                    }
                }
            }

            case SALIDA, AJUSTE -> {
                if (dto.inventarioOrigenId() != null) {
                    Inventario origen = inventarioRepository.findById(dto.inventarioOrigenId())
                            .orElseThrow(() -> new EntityNotFoundException(
                                    "Inventario no encontrado con id: " + dto.inventarioOrigenId()));
                    if (!origen.getBodega().getIdBodega().equals(bodegaAsignada)) {
                        throw new BusinessRuleException(
                                "Solo puedes operar inventarios de tu bodega asignada.");
                    }
                }
            }

            case TRASLADO -> {
                if (dto.inventarioOrigenId() != null) {
                    Inventario origen = inventarioRepository.findById(dto.inventarioOrigenId())
                            .orElseThrow(() -> new EntityNotFoundException(
                                    "Inventario no encontrado con id: " + dto.inventarioOrigenId()));
                    if (!origen.getBodega().getIdBodega().equals(bodegaAsignada)) {
                        throw new BusinessRuleException(
                                "Solo puedes trasladar desde tu bodega asignada.");
                    }
                }
            }
        }
    }
}
