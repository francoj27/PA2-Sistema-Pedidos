package com.isil.edu.pe.spedidos.service;

import com.isil.edu.pe.spedidos.client.DevolucionStockClient;
import com.isil.edu.pe.spedidos.client.dto.DevolucionStockRequest;
import com.isil.edu.pe.spedidos.entity.AnulacionPedido;
import com.isil.edu.pe.spedidos.entity.Pedido;
import com.isil.edu.pe.spedidos.entity.PedidoEstado;
import com.isil.edu.pe.spedidos.exception.PedidoException;
import com.isil.edu.pe.spedidos.repository.AnulacionPedidoRepository;
import com.isil.edu.pe.spedidos.repository.PedidoRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AnulacionPedidoService {

    private final PedidoRepository pedidoRepository;
    private final AnulacionPedidoRepository anulacionRepository;
    private final DevolucionStockClient devolucionStockClient;

    public AnulacionPedidoService(
            PedidoRepository pedidoRepository,
            AnulacionPedidoRepository anulacionRepository,
            DevolucionStockClient devolucionStockClient) {

        this.pedidoRepository = pedidoRepository;
        this.anulacionRepository = anulacionRepository;
        this.devolucionStockClient = devolucionStockClient;
    }

    @Transactional
    public AnulacionPedido anularPedido(Long pedidoId, String motivo) {

        if (pedidoId == null) {
            throw new PedidoException(
                    "El identificador del pedido es obligatorio.");
        }

        if (motivo == null || motivo.isBlank()) {
            throw new PedidoException(
                    "El motivo de anulación es obligatorio.");
        }

        String motivoValidado = motivo.trim();

        if (motivoValidado.length() > 500) {
            throw new PedidoException(
                    "El motivo no puede superar los 500 caracteres.");
        }

        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new PedidoException(
                        "El pedido no existe."));

        if (pedido.getEstado() != PedidoEstado.REGISTRADO) {
            throw new PedidoException(
                    "Solo se pueden anular pedidos registrados.");
        }

        if (anulacionRepository.existsByPedidoId(pedidoId)) {
            throw new PedidoException(
                    "El pedido ya tiene una anulación registrada.");
        }

        DevolucionStockRequest solicitud =
                new DevolucionStockRequest(pedido.getCantidad());

        // Si la devolución remota falla, no se continúa con la anulación local.
        devolucionStockClient.devolverStock(
                pedido.getProductoCodigo(),
                solicitud);

        AnulacionPedido anulacion = new AnulacionPedido(
                pedido.getId(),
                pedido.getProductoCodigo(),
                pedido.getCantidad(),
                motivoValidado);

        pedido.setEstado(PedidoEstado.ANULADO);
        pedidoRepository.save(pedido);

        return anulacionRepository.save(anulacion);
    }
}
