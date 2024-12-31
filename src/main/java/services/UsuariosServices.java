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

    public String crearUsuario(String nombre, String clave, String rol) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;

        Usuarios usuario = new Usuarios(nombre, clave, rol);

        String response = null;
        try {
            transaction = session.beginTransaction();

            session.persist(usuario);

            transaction.commit();

            response = "Usuario guardado: " + usuario.getNombre() + "\n contraseña: " + usuario.getContraseña();

        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
        } finally {
            session.close();
        }
        return response;
    }

    public boolean compararContraseña(String name, String clave) {
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
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }

            throw new RuntimeException("usuario No encontrado");

        } finally {
            session.close();
        }

        return usuario.getContraseña().equalsIgnoreCase(clave);
    }

    public List<Object[]> getUsuarios() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<Object[]> nombres = new ArrayList<>();
        String sql = "SELECT id, nombre, rol FROM Usuarios";
        try {
            NativeQuery<Object[]> query = session.createNativeQuery(sql, Object[].class);

            nombres = query.getResultList();
        } catch (Exception e) {
            throw new RuntimeException("No se encuentran usuarios");

        } finally {
            session.close();
        }

        return nombres;
    }

    public List<Usuarios> ObtenerVendedores() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<Usuarios> usuarios = new ArrayList<>();
        String sql = "SELECT * FROM Usuarios WHERE rol = 'VENDEDOR'";
        try {
            NativeQuery<Usuarios> query = session.createNativeQuery(sql, Usuarios.class);

            usuarios = query.getResultList();
        } catch (Exception e) {
            throw new RuntimeException("No se encuentran Vendedores");

        } finally {
            session.close();
        }

        return usuarios;
    }

    public String eliminarUsuario(int id) {

        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession();) {
            transaction = session.beginTransaction();
            Usuarios usuario = session.find(Usuarios.class, id);
            
            if(usuario != null){
                session.remove(usuario);
            }
            
            transaction.commit();
        } catch (Exception e) {
            if(transaction != null){
                transaction.rollback();
            }
            
            throw new RuntimeException("No se puede eliminar el usuario");
        }

        return "Usuario eliminado";
    }
}
