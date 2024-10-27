/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package services;

import Configurations.HibernateUtil;
import java.util.List;
import models.Producto;
import models.Ventas;
import models.VentasProducto;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author jason
 */
public class VentasProductosServices {

    public VentasProductosServices() {

    }

    public String crearVentasProductos(List<VentasProducto> listVenta) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();
            for (VentasProducto vp : listVenta) {
                session.persist(vp);
                
                Producto producto = session.find(Producto.class, vp.getProducto_id());
                if(producto != null){
                    int nuevaCantidad = producto.getCantidad() - vp.getCantidad();
                    if(nuevaCantidad < 0){
                        throw new IllegalArgumentException("No hay suficiente stock para el producto: " + producto.getNombre());
                    }
                    producto.setCantidad(nuevaCantidad);
                    session.persist(producto);
                }
            }
            transaction.commit();

            return "Venta realizada con exito";
        } catch (IllegalArgumentException e) {
            if (transaction != null) {
                transaction.rollback();
            }

            throw new RuntimeException(e.getMessage());
        } finally {
            session.close();
        }
    }
}
