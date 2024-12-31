/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models.CajaModels;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author jason
 */
@Entity
@Table(name = "movimientosCaja")
@Getter @Setter
public class MovimientosCaja {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_movimiento;
    private LocalDateTime fecha;
    private BigDecimal monto;
    private String tipoMovimiento; // VENTA, INGRESO, RETIRO
    private String descripcion;

    @ManyToOne
    @JoinColumn(name = "id_caja")
    private Caja caja;

    public MovimientosCaja(Long id_movimiento, LocalDateTime fecha, BigDecimal monto, String tipoMovimiento, Caja caja) {
        this.id_movimiento = id_movimiento;
        this.fecha = fecha;
        this.monto = monto;
        this.tipoMovimiento = tipoMovimiento;
        this.caja = caja;
    }

    public MovimientosCaja(LocalDateTime fecha, BigDecimal monto, String tipoMovimiento, Caja caja) {
        this.fecha = fecha;
        this.monto = monto;
        this.tipoMovimiento = tipoMovimiento;
        this.caja = caja;
    }

    public MovimientosCaja(LocalDateTime fecha, BigDecimal monto, String tipoMovimiento, String descripcion, Caja caja) {
        this.fecha = fecha;
        this.monto = monto;
        this.tipoMovimiento = tipoMovimiento;
        this.descripcion = descripcion;
        this.caja = caja;
    }
    
    

    public MovimientosCaja() {
    }

}
