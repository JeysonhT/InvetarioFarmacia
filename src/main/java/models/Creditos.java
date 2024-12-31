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
@Table(name = "creditos")
@Getter
@Setter
public class Creditos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_credito;

    private int id_cliente;
    private int id_factura;

    private double monto_total;
    private double saldo_pendiente;
    private double interes;
    private int plazos;

    private Date fecha_inicial;
    private Date fecha_vencimiento;
    private String estado;

    public Creditos() {
    }

    public Creditos(int id_credito, int id_cliente, int id_factura, double monto_total, double saldo_pendiente, double interes, int plazos, Date fecha_inicial, Date fecha_vencimiento, String estado) {
        this.id_credito = id_credito;
        this.id_cliente = id_cliente;
        this.id_factura = id_factura;
        this.monto_total = monto_total;
        this.saldo_pendiente = saldo_pendiente;
        this.interes = interes;
        this.plazos = plazos;
        this.fecha_inicial = fecha_inicial;
        this.fecha_vencimiento = fecha_vencimiento;
        this.estado = estado;
    }

    public Creditos(int id_cliente, int id_factura, double monto_total, double saldo_pendiente, double interes, int plazos, Date fecha_inicial, Date fecha_vencimiento, String estado) {
        this.id_cliente = id_cliente;
        this.id_factura = id_factura;
        this.monto_total = monto_total;
        this.saldo_pendiente = saldo_pendiente;
        this.interes = interes;
        this.plazos = plazos;
        this.fecha_inicial = fecha_inicial;
        this.fecha_vencimiento = fecha_vencimiento;
        this.estado = estado;
    }

    

}
