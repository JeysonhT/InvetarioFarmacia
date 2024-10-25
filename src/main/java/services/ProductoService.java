/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package services;

import Configurations.HibernateUtil;
import java.util.ArrayList;
import java.util.List;
import models.Producto;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.NativeQuery;

/**
 *
 * @author jason
 */
public class ProductoService {

    public ProductoService() {
    }

    public String crearProducto(Producto pp) {
        Session session = HibernateUtil.getSessionFactory().openSession();

        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();
            session.persist(pp);
            transaction.commit();

        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }

            return "No se pudo guardar el producto: " + e.getMessage();
        } finally {
            session.close();
        }

        return "producto guardado";

    }

    public String eliminarProducto(int id) {
        Session session = HibernateUtil.getSessionFactory().openSession();

        Transaction transanction;

        try {
            transanction = session.beginTransaction();
            Producto entity = session.find(Producto.class, id);
            
            if (entity != null) {
                session.remove(entity);
            }
            
            transanction.commit();

        } catch (Exception e) {
            return "No se pudo eliminar el producto por que no existe: " + e.getMessage();
        } finally{
            session.close();
        }
        
        return "Producto eliminado correctamente";
    }

    public Producto actualizarProducto(Producto pp) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction;
        Producto producto = null;
        try {
            transaction = session.beginTransaction();
            producto = session.merge("Productos", pp);
            transaction.commit();
        } catch (Exception e) {
            throw new RuntimeException("No se puede actualizar el producto");
        } finally {
            session.close();
        }
        return producto;
    }

    public List<Producto> obtenerProductos() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction;

        List<Producto> productos = new ArrayList<>();
        String sql = "CALL ObtenerProductos()";
        try {
            transaction = session.beginTransaction();
            NativeQuery<Producto> query = session.
                    createNativeQuery(sql, Producto.class);
            productos = query.getResultList();
            transaction.commit();
        } catch (Exception e) {

            throw new RuntimeException("No se pudo obtener la lista de productos: " + e.getMessage());
        } finally {
            session.close();
        }
        
        return productos;
    }
    
    public int obtenerCantidadProductosTotal(){
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction;
        try{
            transaction = session.beginTransaction();
            NativeQuery<Integer> query = session.createNativeQuery("Call obtenerCantidadProductos()", Integer.TYPE);
            Integer resultado = query.getResultList().get(0);
            transaction.commit();
           
            return resultado;
        } catch(Exception e){
            throw new RuntimeException("Error al obtener la cantidad de productos");
        }
    }

}
