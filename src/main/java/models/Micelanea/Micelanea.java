/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models.Micelanea;

import jakarta.persistence.Column;
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
@Table(name = "micelanea")
@Getter @Setter
public class Micelanea {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_micelanea")
    private Integer idMicelanea;

    @Column(name = "nombre", nullable = false, length = 50)
    private String nombre;

    @Column(name = "tipo", nullable = false, length = 20)
    private String tipo;

    @Column(name = "cantidad", nullable = false)
    private int cantidad;

    @Column(name = "precio", nullable = false)
    private double precio;

    @Column(name = "precioCosto", nullable = false)
    private double precioCosto;

    @Column(name = "costoTotal", nullable = false)
    private double costoTotal;

    @Column(name = "utilidad", nullable = false)
    private double utilidad;

    // Getters y Setters

    // Constructor por defecto
    public Micelanea() {
    }

    // Constructor con argumentos (opcional)
    public Micelanea(String nombre, String tipo, int cantidad, double precio, double precioCosto, double costoTotal, double utilidad) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.cantidad = cantidad;
        this.precio = precio;
        this.precioCosto = precioCosto;
        this.costoTotal = costoTotal;
        this.utilidad = utilidad;
    }

    public Micelanea(Integer idMicelanea, String nombre, String tipo, int cantidad, double precio, double precioCosto, double costoTotal, double utilidad) {
        this.idMicelanea = idMicelanea;
        this.nombre = nombre;
        this.tipo = tipo;
        this.cantidad = cantidad;
        this.precio = precio;
        this.precioCosto = precioCosto;
        this.costoTotal = costoTotal;
        this.utilidad = utilidad;
    }

    public Micelanea(Integer idMicelanea, String nombre, int cantidad, double precio) {
        this.idMicelanea = idMicelanea;
        this.nombre = nombre;
        this.cantidad = cantidad;
        this.precio = precio;
    }

    public Micelanea(Integer idMicelanea, int cantidad) {
        this.idMicelanea = idMicelanea;
        this.cantidad = cantidad;
    }

    

    
    
}
