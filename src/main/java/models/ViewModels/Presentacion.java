/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models.ViewModels;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author jason
 */
@Entity
@Table(name = "Presentación")
@Getter
@Setter
public class Presentacion {
    
    @Id
    private int id;
    private String nombre;

    public Presentacion(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public Presentacion(String nombre) {
        this.nombre = nombre;
    }

    public Presentacion() {
    }

}
