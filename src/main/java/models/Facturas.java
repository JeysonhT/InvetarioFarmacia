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
@Table(name = "Facturas")
@Getter @Setter
public class Facturas {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    private Date fecha;
    private int VentasId;

    public Facturas() {
    }

    public Facturas(Date fecha, int VentasId) {
        this.fecha = fecha;
        this.VentasId = VentasId;
    }

    public Facturas(int id, Date fecha, int VentasId) {
        this.id = id;
        this.fecha = fecha;
        this.VentasId = VentasId;
    }
    
    
}
