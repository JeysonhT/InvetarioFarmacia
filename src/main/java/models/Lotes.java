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
@Table(name = "Lotes")
@Getter
@Setter
public class Lotes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_lote;

    private int id_producto;

    private String lote;

    private Date fecha_vencimiento;

    private double existencia_inicial;

    private double existencia_actual;
    
    private double cantidadMinima;

    public Lotes() {
    }

    public Lotes(int id_lote, int id_producto, String lote, Date fecha_vencimiento, double existencia_inicial, double existencia_actual, double cantidadMinima) {
        this.id_lote = id_lote;
        this.id_producto = id_producto;
        this.lote = lote;
        this.fecha_vencimiento = fecha_vencimiento;
        this.existencia_inicial = existencia_inicial;
        this.existencia_actual = existencia_actual;
        this.cantidadMinima = cantidadMinima;
    }

    public Lotes(int id_producto, String lote, Date fecha_vencimiento, double existencia_inicial, double existencia_actual, double cantidadMinima) {
        this.id_producto = id_producto;
        this.lote = lote;
        this.fecha_vencimiento = fecha_vencimiento;
        this.existencia_inicial = existencia_inicial;
        this.existencia_actual = existencia_actual;
        this.cantidadMinima = cantidadMinima;
    }

    public Lotes(int id_producto, String lote, Date fecha_vencimiento, double existencia_actual) {
        this.id_producto = id_producto;
        this.lote = lote;
        this.fecha_vencimiento = fecha_vencimiento;
        this.existencia_actual = existencia_actual;
    }
    
    

}
