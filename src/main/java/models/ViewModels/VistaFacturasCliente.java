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
@Getter @Setter 
public class VistaFacturasCliente {
    
    private int factura_id;
    private Date fecha_factura;
    private String nombre_cliente;
    private String telefono_cliente;
    private String nombre_producto;
    private int cantidad_producto;
    private double precio_producto;
    private double subtotal;
    
    public VistaFacturasCliente(){
        
    }

    public VistaFacturasCliente(int factura_id, Date fecha_factura, String nombre_cliente, String telefono_cliente, String nombre_producto, int cantidad_producto, double precio_producto, double subtotal) {
        this.factura_id = factura_id;
        this.fecha_factura = fecha_factura;
        this.nombre_cliente = nombre_cliente;
        this.telefono_cliente = telefono_cliente;
        this.nombre_producto = nombre_producto;
        this.cantidad_producto = cantidad_producto;
        this.precio_producto = precio_producto;
        this.subtotal = subtotal;
    }

    @Override
    public String toString() {
        return "factura_id=" + factura_id + ", fecha=" + fecha_factura + ", producto=" + nombre_producto + ", cantidad=" + cantidad_producto + ", precio=" + precio_producto + ", subtotal=" + subtotal ;
    }

    
}
