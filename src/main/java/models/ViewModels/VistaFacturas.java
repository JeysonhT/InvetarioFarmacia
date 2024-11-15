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
@Getter
@Setter
public class VistaFacturas {

    private String nombre_cliente;
    private int cliente_id;
    private int factura_id;

    public VistaFacturas() {
    }

    public VistaFacturas(String nombre_cliente, int cliente_id, int factura_id) {
        this.nombre_cliente = nombre_cliente;
        this.cliente_id = cliente_id;
        this.factura_id = factura_id;
    }
    
    
}
