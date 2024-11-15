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
@Table(name = "FacturasCliente")
@Getter @Setter
public class FacturasCliente {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int Id;
    
    private int facturaId;
    private int clienteId;
    
    public FacturasCliente(){
        
    }

    public FacturasCliente(int facturaId, int clienteId) {
        this.facturaId = facturaId;
        this.clienteId = clienteId;
    }
    
    public FacturasCliente(int Id, int facturaId, int clienteId) {
        this.Id = Id;
        this.facturaId = facturaId;
        this.clienteId = clienteId;
    }
       
}
