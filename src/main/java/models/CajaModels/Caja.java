/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models.CajaModels;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author jason
 */
@Entity
@Table(name = "caja")
@Getter @Setter
public class Caja {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_caja;
    private LocalDateTime apertura;
    private LocalDateTime cierre;
    private BigDecimal monto_Inicial;
    private BigDecimal monto_Final;
    private Boolean abierta;
    
    @OneToMany(mappedBy = "caja")
    private List<MovimientosCaja> movimientos;

    public Caja(Long id_caja, LocalDateTime apertura, LocalDateTime cierre, BigDecimal monto_Inicial, BigDecimal monto_Final, Boolean abierta, List<MovimientosCaja> movimientos) {
        this.id_caja = id_caja;
        this.apertura = apertura;
        this.cierre = cierre;
        this.monto_Inicial = monto_Inicial;
        this.monto_Final = monto_Final;
        this.abierta = abierta;
        this.movimientos = movimientos;
    }

    public Caja(LocalDateTime apertura, LocalDateTime cierre, BigDecimal monto_Inicial, BigDecimal monto_Final, Boolean abierta, List<MovimientosCaja> movimientos) {
        this.apertura = apertura;
        this.cierre = cierre;
        this.monto_Inicial = monto_Inicial;
        this.monto_Final = monto_Final;
        this.abierta = abierta;
        this.movimientos = movimientos;
    }

    public Caja() {
    }

}
