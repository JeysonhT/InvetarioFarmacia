/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models.ViewModels;

import java.util.Date;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author jason
 */
@Getter
@Setter
public class VistaFacturasProductos {

    private int factura_id;
    private int ventaId;
    private String tipoVenta;
    private int productoId;
    private String nombre;
    private String marca;
    private int cantidad;
    private double precio;
    private double subtotal;

    public VistaFacturasProductos() {

    }

    public VistaFacturasProductos(int factura_id, int ventaId, String tipoVenta, int productoId, String nombre, String marca, int cantidad, double precio, double subtotal) {
        this.factura_id = factura_id;
        this.ventaId = ventaId;
        this.tipoVenta = tipoVenta;
        this.productoId = productoId;
        this.nombre = nombre;
        this.marca = marca;
        this.cantidad = cantidad;
        this.precio = precio;
        this.subtotal = subtotal;
    }

}
