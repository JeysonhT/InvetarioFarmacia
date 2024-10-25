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
import java.util.Date;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author jason
 */
@Entity
@Table(name = "Productos")
@Getter
@Setter
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    private String nombre;
    private String indicaciones;
    private String marca;
    private int categoria_id;
    private Double precio;
    private int cantidad;
    private Date fecha_vencimiento;

    public Producto() {
    }

    public Producto(String nombre, Double precio) {
        this.nombre = nombre;
        this.precio = precio;
    }

    public Producto(String nombre, String indicaciones, String marca, int categoria_id, Double precio, int cantidad, Date fecha_vencimiento) {
        this.nombre = nombre;
        this.indicaciones = indicaciones;
        this.marca = marca;
        this.categoria_id = categoria_id;
        this.precio = precio;
        this.cantidad = cantidad;
        this.fecha_vencimiento = fecha_vencimiento;
    }

    public Producto(int id, String nombre, String indicaciones, String marca, int categoria_id, Double precio, int cantidad, Date fecha_vencimiento) {
        this.id = id;
        this.nombre = nombre;
        this.indicaciones = indicaciones;
        this.marca = marca;
        this.categoria_id = categoria_id;
        this.precio = precio;
        this.cantidad = cantidad;
        this.fecha_vencimiento = fecha_vencimiento;
    }

    @Override
    public String toString() {
        return nombre + ", indicaciones=" + indicaciones + ", marca=" + marca + ", precio=" + precio + ", cantidad=" + cantidad + ", fecha_vencimiento=" + fecha_vencimiento.toString();
    }


}
