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
@Getter @Setter
@Table(name = "Usuarios")
public class Usuarios {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String nombre;
    private String contraseña;
    private String rol;

    public Usuarios(String nombre, String contraseña, String rol) {
        this.nombre = nombre;
        this.contraseña = contraseña;
        this.rol = rol;
    }

    public Usuarios(int id, String nombre, String contraseña, String rol) {
        this.id = id;
        this.nombre = nombre;
        this.contraseña = contraseña;
        this.rol = rol;
    }
    
    public Usuarios() {
    }
    
    
}
