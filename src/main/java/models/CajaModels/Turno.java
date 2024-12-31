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
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import models.Ventas;

/**
 *
 * @author jason
 */
@Entity
@Table(name = "turno")
@Getter
@Setter
public class Turno {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_turno;

    private LocalDateTime inicio;
    private LocalDateTime fin;

    private int id_vendedor;

    @ManyToOne
    @JoinColumn(name = "id_caja")
    private Caja caja;

   @OneToMany(mappedBy = "turno")
    private List<Ventas> ventas;

    public Turno() {
    }

}
