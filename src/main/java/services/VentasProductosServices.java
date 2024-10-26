/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package services;

import Configurations.HibernateUtil;
import models.VentasProducto;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author jason
 */
public class VentasProductosServices {
    
    public VentasProductosServices(){
        
    }
    
    public String crearVentasProductos(VentasProducto ventasProducto){
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null; 
        
        try {
            transaction = session.beginTransaction();
            session.persist(ventasProducto);
            transaction.commit();
            
            return "venta producto creada: " + ventasProducto.toString();
        } catch (Exception e) {
            if(transaction != null){
                transaction.rollback();
            }
            
            throw new RuntimeException(e.getMessage());
        } finally {
            session.close();
        }
    }
}
