/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models.ViewModels;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author jason
 */

@Getter @Setter
public class VistaInventario {
    private String nombreProducto;
    private String descripcionProducto;
    private String lote;
    private String fechaVencimiento;
    private Integer existencia;
    private String presentacion;
    private Double precioPorPresentacion;

    // Constructor
    public VistaInventario(String nombreProducto, String descripcionProducto, String lote,
                           String fechaVencimiento, Integer existencia, String presentacion, Double precioPorPresentacion) {
        this.nombreProducto = nombreProducto;
        this.descripcionProducto = descripcionProducto;
        this.lote = lote;
        this.fechaVencimiento = fechaVencimiento;
        this.existencia = existencia;
        this.presentacion = presentacion;
        this.precioPorPresentacion = precioPorPresentacion;
    }
}
