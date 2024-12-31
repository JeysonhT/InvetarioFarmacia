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
public class VistaInventario {

    private int id_producto;
    private String nombre_Producto;
    private String indicaciones;
    private String laboratorio;
    private String codigo_lote;
    private Date fecha_Vencimiento;
    private String categoria;
    private String presentacion;
    private double precio;
    private int cantidad;
    private double precio_costo;
    private double costo_total;
    private double utilidad;
    private String unidad_mayor;
    private int conversion;
    private double precio_mayor;

    public VistaInventario(int id_producto, String nombre_Producto, String indicaciones, String laboratorio, String codigo_lote, Date fecha_Vencimiento, String categoria, String presentacion, double precio, int cantidad, double precio_costo, double costo_total, double utilidad, String unidad_mayor, int conversion, double precio_mayor) {
        this.id_producto = id_producto;
        this.nombre_Producto = nombre_Producto;
        this.indicaciones = indicaciones;
        this.laboratorio = laboratorio;
        this.codigo_lote = codigo_lote;
        this.fecha_Vencimiento = fecha_Vencimiento;
        this.categoria = categoria;
        this.presentacion = presentacion;
        this.precio = precio;
        this.cantidad = cantidad;
        this.precio_costo = precio_costo;
        this.costo_total = costo_total;
        this.utilidad = utilidad;
        this.unidad_mayor = unidad_mayor;
        this.conversion = conversion;
        this.precio_mayor = precio_mayor;
    }
    
    

}
