/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author jason
 */
@Entity
@Table(name = "Ventas_producto")
@Getter
@Setter
public class VentasProducto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int venta_id;

    private int producto_id;

    private int cantidad;

    private double precio;


    public VentasProducto() {
    }

    public VentasProducto(int venta_id, int producto_id, int cantidad, double precio) {
        this.venta_id = venta_id;
        this.producto_id = producto_id;
        this.cantidad = cantidad;
        this.precio = precio;
    }

    public VentasProducto(int id, int venta_id, int producto_id, int cantidad, double precio) {
        this.id = id;
        this.venta_id = venta_id;
        this.producto_id = producto_id;
        this.cantidad = cantidad;
        this.precio = precio;
    }

    @Override
    public String toString() {
        return "VentasProducto{" + "venta_id=" + venta_id + ", producto_id=" + producto_id + ", cantidad=" + cantidad + ", precio=" + precio + '}';
    }

}
