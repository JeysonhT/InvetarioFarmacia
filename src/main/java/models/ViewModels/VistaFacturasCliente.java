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
public class VistaFacturasCliente {
    
    private String nombre_cliente;
    private int id;
    private Date fecha;
    private double total;
    private String Nombreproducto;
    private int Cantidad;
    private double precio;
    private double subtotal;
    
    public VistaFacturasCliente(){
        
    }

    public VistaFacturasCliente(String nombre_cliente, int id, Date fecha, double total, String Nombreproducto, int Cantidad, double precio, double subtotal) {
        this.nombre_cliente = nombre_cliente;
        this.id = id;
        this.fecha = fecha;
        this.total = total;
        this.Nombreproducto = Nombreproducto;
        this.Cantidad = Cantidad;
        this.precio = precio;
        this.subtotal = subtotal;
    }
    
}
