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
@Table(name = "UnidadesMultiples")
@Getter
@Setter
public class UnidadesMultiples {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_Unidad;

    private int id_Producto;

    private String descripcion;

    private int factor_Conversion;

    private double precio_Unitario;

    public UnidadesMultiples() {
    }

    public UnidadesMultiples(int id_Producto, String descripcion, int factor_Conversion, double precio_Unitario) {
        this.id_Producto = id_Producto;
        this.descripcion = descripcion;
        this.factor_Conversion = factor_Conversion;
        this.precio_Unitario = precio_Unitario;
    }

    public UnidadesMultiples(int id_Unidad, int id_Producto, String descripcion, int factor_Conversion, double precio_Unitario) {
        this.id_Unidad = id_Unidad;
        this.id_Producto = id_Producto;
        this.descripcion = descripcion;
        this.factor_Conversion = factor_Conversion;
        this.precio_Unitario = precio_Unitario;
    }

}
