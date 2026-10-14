package com.isil.edu.pe.spedidos.client;

import com.isil.edu.pe.spedidos.client.dto.ProductoInventarioResponse;
import com.isil.edu.pe.spedidos.client.dto.ReservaStockResponse;
import java.util.List;

/**
 * Contrato para acceder a las operaciones del servicio de Inventario.
 */
public interface InventarioClient {

    /**
     * Obtiene la lista de productos del inventario.
     *
     * @return lista de productos
     */
    List<ProductoInventarioResponse> listarProductos();

    /**
     * Reserva unidades de un producto por su código.
     *
     * @param codigo código del producto
     * @param cantidad cantidad que se desea reservar
     * @return resultado de la reserva
     */
    ReservaStockResponse reservarStock(String codigo, int cantidad);

    /**
     * Devuelve unidades al inventario por su código.
     *
     * @param codigo código del producto
     * @param cantidad cantidad que se devolverá
     */
    void devolverStock(String codigo, int cantidad);
}
