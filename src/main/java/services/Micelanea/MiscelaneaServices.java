/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package services.Micelanea;

import Configurations.HibernateUtil;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import models.Micelanea.Micelanea;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.NativeQuery;

/**
 *
 * @author jason
 */
public class MiscelaneaServices {

    public MiscelaneaServices() {

    }

    public String guardarMicelanea(Micelanea micelanea) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(micelanea);
            transaction.commit();
        } catch (Exception e) {
            if(transaction != null){
                transaction.commit();
            }
            throw new RuntimeException("No se puede guardar el producto, por datos infaltantes");
        }
        return "Producto guardado";
    }
    
    public String eliminarMicelanea(int id) {
        Session session = HibernateUtil.getSessionFactory().openSession();

        Transaction transanction;

        try {
            transanction = session.beginTransaction();
            Micelanea entity = session.find(Micelanea.class, id);

            if (entity != null) {
                session.remove(entity);
            }

            transanction.commit();

        } catch (Exception e) {
            return "Existe una venta asociada a este producto " + e.getMessage();
        } finally {
            session.close();
        }

        return "Producto eliminado correctamente";
    }
    
    public Micelanea actualizarMicelanea(Micelanea pp) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction;
        Micelanea mm = null;
        try {
            transaction = session.beginTransaction();

            mm = session.find(Micelanea.class, pp.getIdMicelanea());

            if (mm != null) {
                
                mm.setNombre(pp.getNombre());
                mm.setTipo(pp.getTipo());
                mm.setPrecio(pp.getPrecio());
                mm.setPrecioCosto(pp.getPrecioCosto());
                mm.setCostoTotal(pp.getCostoTotal());
                mm.setUtilidad(pp.getUtilidad());
                mm.setCantidad(pp.getCantidad());

                session.merge(mm);
            } else {
                throw new SQLException("El producto no existe");
                //throw new RuntimeException("El Producto no Existe");
            }

            transaction.commit();
        } catch (SQLException e) {
            throw new RuntimeException("No se puede actualizar el producto" + e.getMessage());
        } finally {
            session.close();
        }
        return mm;
    }
    
    public List<Micelanea> obtenerProductos() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction;

        List<Micelanea> productos = new ArrayList<>();
        String sql = "CALL obtenerMicelaneas()";
        try {
            transaction = session.beginTransaction();
            NativeQuery<Micelanea> query = session.
                    createNativeQuery(sql, Micelanea.class);

            productos = query.getResultList();
            transaction.commit();
        } catch (Exception e) {

            throw new RuntimeException("No se pudo obtener la lista de productos: " + e.getMessage());
        } finally {
            session.close();
        }
        return productos;
    }
    
    
}
