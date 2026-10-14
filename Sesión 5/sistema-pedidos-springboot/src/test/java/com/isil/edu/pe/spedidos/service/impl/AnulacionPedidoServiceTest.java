package com.isil.edu.pe.spedidos.service.impl;

import com.isil.edu.pe.spedidos.client.DevolucionStockClient;
import com.isil.edu.pe.spedidos.entity.AnulacionPedido;
import com.isil.edu.pe.spedidos.entity.Pedido;
import com.isil.edu.pe.spedidos.entity.PedidoEstado;
import com.isil.edu.pe.spedidos.exception.PedidoException;
import com.isil.edu.pe.spedidos.repository.AnulacionPedidoRepository;
import com.isil.edu.pe.spedidos.repository.PedidoRepository;
import com.isil.edu.pe.spedidos.service.AnulacionPedidoService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AnulacionPedidoServiceTest {

    @Mock
    private PedidoRepository pedidoRepository;

    @Mock
    private AnulacionPedidoRepository anulacionRepository;

    @Mock
    private DevolucionStockClient devolucionStockClient;

    @InjectMocks
    private AnulacionPedidoService service;

    private Pedido pedido;

    @BeforeEach
    void preparar() {
        pedido = new Pedido(
                "Ana Torres",
                "LAP-001",
                "Laptop",
                new BigDecimal("2500.00"),
                1,
                new BigDecimal("2500.00")
        );

        pedido.setId(1L);
        pedido.setEstado(PedidoEstado.REGISTRADO);
    }

    @Test
    void anularPedido_debeDevolverStockCambiarEstadoYGuardarAnulacion() {
        when(pedidoRepository.findById(1L))
                .thenReturn(Optional.of(pedido));

        when(anulacionRepository.save(any(AnulacionPedido.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        AnulacionPedido resultado =
                service.anularPedido(1L, "Prueba de anulación");

        assertNotNull(resultado);
        assertEquals(1L, resultado.getPedidoId());
        assertEquals("Prueba de anulación", resultado.getMotivo());
        assertEquals(PedidoEstado.ANULADO, pedido.getEstado());

        verify(devolucionStockClient).devolverStock(
                eq("LAP-001"),
                argThat(request -> request.getCantidad() == 1)
        );

        verify(pedidoRepository).save(pedido);
        verify(anulacionRepository).save(any(AnulacionPedido.class));
    }

    @Test
    void anularPedido_debeRechazarPedidoInexistente() {
        when(pedidoRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                PedidoException.class,
                () -> service.anularPedido(99L, "Motivo de prueba")
        );

        verifyNoInteractions(devolucionStockClient);
        verify(anulacionRepository, never()).save(any());
    }

    @Test
    void anularPedido_debeRechazarMotivoVacio() {
        assertThrows(
                PedidoException.class,
                () -> service.anularPedido(1L, "   ")
        );

        verifyNoInteractions(pedidoRepository);
        verifyNoInteractions(devolucionStockClient);
        verifyNoInteractions(anulacionRepository);
    }

    @Test
    void anularPedido_debeRechazarPedidoYaAnulado() {
        pedido.setEstado(PedidoEstado.ANULADO);

        when(pedidoRepository.findById(1L))
                .thenReturn(Optional.of(pedido));

        assertThrows(
                PedidoException.class,
                () -> service.anularPedido(1L, "Segundo intento")
        );

        verifyNoInteractions(devolucionStockClient);
        verify(anulacionRepository, never()).save(any());
    }

    @Test
    void anularPedido_debeRechazarMotivoMayorA500Caracteres() {
        String motivo = "A".repeat(501);

        assertThrows(
                PedidoException.class,
                () -> service.anularPedido(1L, motivo)
        );

        verifyNoInteractions(pedidoRepository);
        verifyNoInteractions(devolucionStockClient);
        verifyNoInteractions(anulacionRepository);
    }

    @Test
    void anularPedido_siFallaInventario_noDebeAnularNiGuardarRegistro() {
        when(pedidoRepository.findById(1L))
                .thenReturn(Optional.of(pedido));

        when(devolucionStockClient.devolverStock(
                eq("LAP-001"),
                argThat(request -> request.getCantidad() == 1)
        )).thenThrow(
                new RuntimeException("Inventario no disponible")
        );

        assertThrows(
                RuntimeException.class,
                () -> service.anularPedido(
                        1L,
                        "Prueba de fallo de inventario"
                )
        );

        assertEquals(
                PedidoEstado.REGISTRADO,
                pedido.getEstado(),
                "El pedido debe continuar registrado si falla el inventario"
        );

        verify(devolucionStockClient).devolverStock(
                eq("LAP-001"),
                argThat(request -> request.getCantidad() == 1)
        );

        verify(pedidoRepository, never()).save(any(Pedido.class));
        verify(anulacionRepository, never()).save(any(AnulacionPedido.class));
    }
}
