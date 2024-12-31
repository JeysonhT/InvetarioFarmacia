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
public class VistaFacturas {

    private int cliente_id;
    private String nombre_Cliente;
    private int factura_ClienteId;
    private int facturaId;
    private int ventaId;
    private Date fecha;
    private double subtotal;
    private double total;
    private String tipoVenta;
    private String tipo_pago;

    public VistaFacturas() {
    }

    public VistaFacturas(int cliente_id, String nombre_Cliente, int factura_ClienteId, int facturaId, int ventaId, Date fecha, double subtotal, double total, String tipoVenta, String tipo_pago) {
        this.cliente_id = cliente_id;
        this.nombre_Cliente = nombre_Cliente;
        this.factura_ClienteId = factura_ClienteId;
        this.facturaId = facturaId;
        this.ventaId = ventaId;
        this.fecha = fecha;
        this.subtotal = subtotal;
        this.total = total;
        this.tipoVenta = tipoVenta;
        this.tipo_pago = tipo_pago;
    }

}
