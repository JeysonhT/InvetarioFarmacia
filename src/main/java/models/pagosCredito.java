/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;
import models.CajaModels.Turno;

/**
 *
 * @author jason
 */
@Entity
@Table(name = "pagosCreditos")
@Getter
@Setter
public class pagosCredito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_pagosCreditos;

    private int id_creditos;

    private Date fecha_pago;

    private Double monto_pago;

    @ManyToOne
    @JoinColumn(name = "id_turno")
    private Turno turno;

    public pagosCredito() {
    }

    public pagosCredito(int id_pagosCreditos, int id_creditos, Date fecha_pago, Double monto_pago, Turno turno) {
        this.id_pagosCreditos = id_pagosCreditos;
        this.id_creditos = id_creditos;
        this.fecha_pago = fecha_pago;
        this.monto_pago = monto_pago;
        this.turno = turno;
    }

    public pagosCredito(int id_creditos, Date fecha_pago, Double monto_pago, Turno turno) {
        this.id_creditos = id_creditos;
        this.fecha_pago = fecha_pago;
        this.monto_pago = monto_pago;
        this.turno = turno;
    }

}
