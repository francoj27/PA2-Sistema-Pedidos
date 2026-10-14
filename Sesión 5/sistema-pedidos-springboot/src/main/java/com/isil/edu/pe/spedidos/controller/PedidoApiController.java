
        package com.isil.edu.pe.spedidos.controller;

import com.isil.edu.pe.spedidos.client.dto.ProductoInventarioResponse;
import com.isil.edu.pe.spedidos.controller.dto.PedidoResponse;
import com.isil.edu.pe.spedidos.entity.Pedido;
import com.isil.edu.pe.spedidos.service.PedidoService;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/orders")
public class PedidoApiController {

    private final PedidoService pedidoService;

    public PedidoApiController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @GetMapping
    public List<PedidoResponse> listarPedidos() {
        return pedidoService.listarPedidos()
                .stream()
                .map(PedidoResponse::from)
                .toList();
    }

    @PostMapping
    public ResponseEntity<?> registrarPedido(
            @RequestBody Map<String, Object> solicitud) {
        try {
            String cliente = (String) solicitud.get("cliente");
            String productoCodigo =
                    (String) solicitud.get("productoCodigo");

            Object cantidadRecibida = solicitud.get("cantidad");

            if (cantidadRecibida == null) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error",
                                "La cantidad es obligatoria."));
            }

            int cantidad = Integer.parseInt(
                    cantidadRecibida.toString());

            Pedido pedido = pedidoService.registrarPedido(
                    cliente, productoCodigo, cantidad);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(PedidoResponse.from(pedido));

        } catch (Exception e) {
            String mensaje = e.getMessage() == null
                    ? "No se pudo registrar el pedido."
                    : e.getMessage();

            return ResponseEntity.badRequest()
                    .body(Map.of("error", mensaje));
        }
    }
}

