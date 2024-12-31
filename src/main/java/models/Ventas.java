/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;
import models.CajaModels.Turno;

/**
 *
 * @author jason
 */
@Entity
@Table(name = "Ventas")
@Getter
@Setter
public class Ventas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private Date fecha;
    private String tipoVenta;
    private String tipo_pago;

    @ManyToOne
    @JoinColumn(name = "id_turno")
    private Turno turno;
    
    private double subtotal;
    private double total;

    public Ventas() {
    }

    public Ventas(int id, Date fecha, String tipoVenta, String tipo_pago, Turno turno, double subtotal, double total) {
        this.id = id;
        this.fecha = fecha;
        this.tipoVenta = tipoVenta;
        this.tipo_pago = tipo_pago;
        this.turno = turno;
        this.subtotal = subtotal;
        this.total = total;
    }

    public Ventas(Date fecha, String tipoVenta, String tipo_pago, Turno turno, double subtotal, double total) {
        this.fecha = fecha;
        this.tipoVenta = tipoVenta;
        this.tipo_pago = tipo_pago;
        this.turno = turno;
        this.subtotal = subtotal;
        this.total = total;
    }

    @Override
    public String toString() {
        return "Ventas{" + "fecha=" + fecha + ", total=" + total + '}';
    }

}
