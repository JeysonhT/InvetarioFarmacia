/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models.Micelanea;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import models.Ventas;

/**
 *
 * @author jason
 */
@Entity
@Table(name = "ventasMicelanea")
@Getter @Setter
public class VentasMicelanea {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ventaMicelanea")
    private Integer idVentaMicelanea;

    private int venta_id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_micelanea", referencedColumnName = "id_micelanea", nullable = false)
    private Micelanea micelanea;

    @Column(name = "cantidad", nullable = false)
    private int cantidad = 1;

    @Column(name = "precio", nullable = false)
    private double precio;

    // Getters y Setters

    // Constructor por defecto
    public VentasMicelanea() {
    }

    // Constructor con argumentos (opcional)
    public VentasMicelanea(int venta, Micelanea micelanea, int cantidad, double precio) {
        this.venta_id = venta;
        this.micelanea = micelanea;
        this.cantidad = cantidad;
        this.precio = precio;
    }
}
