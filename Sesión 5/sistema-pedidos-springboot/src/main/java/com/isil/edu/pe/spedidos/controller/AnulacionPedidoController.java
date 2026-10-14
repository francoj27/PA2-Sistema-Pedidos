package com.isil.edu.pe.spedidos.controller;

import com.isil.edu.pe.spedidos.entity.AnulacionPedido;
import com.isil.edu.pe.spedidos.repository.AnulacionPedidoRepository;
import com.isil.edu.pe.spedidos.service.AnulacionPedidoService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/anulaciones")
public class AnulacionPedidoController {

    private final AnulacionPedidoService anulacionPedidoService;
    private final AnulacionPedidoRepository anulacionPedidoRepository;

    public AnulacionPedidoController(
            AnulacionPedidoService anulacionPedidoService,
            AnulacionPedidoRepository anulacionPedidoRepository) {
        this.anulacionPedidoService = anulacionPedidoService;
        this.anulacionPedidoRepository = anulacionPedidoRepository;
    }

    @PostMapping
    public ResponseEntity<?> anularPedido(
            @RequestBody Map<String, Object> solicitud) {
        try {
            Object idRecibido = solicitud.get("pedidoId");
            if (idRecibido == null) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "El pedidoId es obligatorio."));
            }

            Long pedidoId = Long.valueOf(idRecibido.toString());
            String motivo = (String) solicitud.get("motivo");

            AnulacionPedido anulacion =
                    anulacionPedidoService.anularPedido(pedidoId, motivo);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(anulacion);

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error",
                            e.getMessage() == null
                                    ? "No se pudo anular el pedido."
                                    : e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<List<AnulacionPedido>> listarAnulaciones() {
        return ResponseEntity.ok(anulacionPedidoRepository.findAll());
    }
}
