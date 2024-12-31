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
@Getter @Setter
public class VistaCredito {
    private int id_credito;
    private int id_cliente;
    private String nombre;
    private String telefono;
    private String direccion;
    private int id_factura;
    private double monto_total;
    private double saldo_pendiente;
    private String estado;
    private double interes;
    private Date fecha_inicial;
    private Date fecha_vencimiento;

    public VistaCredito() {
    }

    public VistaCredito(int id_credito, int id_cliente, String nombre, String telefono, String direccion, int id_factura, double monto_total, double saldo_pendiente, String estado, double interes, Date fecha_inicial, Date fecha_vencimiento) {
        this.id_credito = id_credito;
        this.id_cliente = id_cliente;
        this.nombre = nombre;
        this.telefono = telefono;
        this.direccion = direccion;
        this.id_factura = id_factura;
        this.monto_total = monto_total;
        this.saldo_pendiente = saldo_pendiente;
        this.estado = estado;
        this.interes = interes;
        this.fecha_inicial = fecha_inicial;
        this.fecha_vencimiento = fecha_vencimiento;
    }
    
    
}
