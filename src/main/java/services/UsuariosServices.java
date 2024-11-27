/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package services;

import Configurations.HibernateUtil;
import java.util.ArrayList;
import java.util.List;
import models.Usuarios;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.NativeQuery;

/**
 *
 * @author jason
 */
public class UsuariosServices {

    public UsuariosServices() {

    }
    
    public String crearUsuario(String nombre, String clave, String rol){
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        
        Usuarios usuario = new Usuarios(nombre, clave, rol);
        
        String response = null;
        try {
            transaction = session.beginTransaction();
            
            session.persist(usuario);
            
            transaction.commit();
            
            response = "Usuario guardado: " + usuario.getNombre() + "\n contraseña: " + usuario.getContraseña();
            
        } catch(Exception e){
            if(transaction != null){
                transaction.rollback();
            }
        } finally{
            session.close();
        }
        return response; 
    }
    
    public boolean compararContraseña(String name, String clave){
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        
        String sql = "SELECT * FROM Usuarios WHERE nombre = :name";
        
        Usuarios usuario;
        
        try {
            transaction = session.beginTransaction();
            NativeQuery<Usuarios> query = session.createNativeQuery(sql, Usuarios.class);
            query.setParameter("name", name);
            
            usuario = query.getSingleResult();
            
            transaction.commit();
        } catch (Exception e){
            if(transaction != null){
                transaction.rollback();
            }
            
            throw new RuntimeException("usuario No encontrado");
            
        } finally {
            session.close();
        }
        
        return usuario.getContraseña().equalsIgnoreCase(clave);
    }
    
    public List<String> getUsuarios(){
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<String> nombres = new ArrayList<>();
        String sql = "SELECT nombre FROM Usuarios";
        try {
            NativeQuery<String> query = session.createNativeQuery(sql, String.class);
            
            nombres = query.getResultList();
        } catch (Exception e) {
            throw new RuntimeException("No se encuentran usuarios");
            
        } finally {
            session.close();
        }
        
        return nombres;
    }
}
