/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models.ViewModels;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author jason
 */
@Getter
@Setter
public class VentasMes {

    private int producto_id;
    private String nombre_producto;
    private int totalCantidad;
    private double total_venta;

    public VentasMes() {

    }

    public VentasMes(int producto_id, String nombre_producto, int totalCantidad, double total_venta) {
        this.producto_id = producto_id;
        this.nombre_producto = nombre_producto;
        this.totalCantidad = totalCantidad;
        this.total_venta = total_venta;
    }

}
