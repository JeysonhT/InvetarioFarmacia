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
@Table(name = "Productos")
@Getter
@Setter
public class Productos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String nombre;
    private String indicaciones;
    private String presentacion;
    private String marca;
    private int categoria_id;
    private double precioCosto;
    private double costoTotal;
    private double utilidad;
    private Double precio;
    private int cantidad;

    public Productos() {
    }

    public Productos(String nombre, int cantidad, Double precio) {
        this.nombre = nombre;
        this.cantidad = cantidad;
        this.precio = precio;
    }

    public Productos(String nombre, String indicaciones, String presentacion, String marca, int categoria_id, double precioCosto, double costoTotal, double utilidad, Double precio, int cantidad) {
        this.nombre = nombre;
        this.indicaciones = indicaciones;
        this.presentacion = presentacion;
        this.marca = marca;
        this.categoria_id = categoria_id;
        this.precioCosto = precioCosto;
        this.costoTotal = costoTotal;
        this.utilidad = utilidad;
        this.precio = precio;
        this.cantidad = cantidad;
    }

    public Productos(int id, String nombre, String indicaciones, String presentacion, String marca, int categoria_id, double precioCosto, double costoTotal, double utilidad, Double precio, int cantidad) {
        this.id = id;
        this.nombre = nombre;
        this.indicaciones = indicaciones;
        this.presentacion = presentacion;
        this.marca = marca;
        this.categoria_id = categoria_id;
        this.precioCosto = precioCosto;
        this.costoTotal = costoTotal;
        this.utilidad = utilidad;
        this.precio = precio;
        this.cantidad = cantidad;
    }

    

    @Override
    public String toString() {
        return "Producto: " + nombre + ", Cantidad: " + cantidad + ", C$: " + precio;
    }

}
