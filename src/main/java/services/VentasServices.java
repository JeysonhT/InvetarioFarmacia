/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package services;

import Configurations.HibernateUtil;
import models.Ventas;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author jason
 */
public class VentasServices {

    public VentasServices() {

    }

    public int guardarVentasProducto(Ventas ventas) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try{
            transaction = session.beginTransaction();
            session.persist(ventas);
            int key = ventas.getId();
            
            transaction.commit();
            
            return key;
                    
        } catch (Exception e){
            if(transaction != null){
                transaction.rollback();
            }
            
            throw new RuntimeException("Error en la consulta: " + e.getMessage());
        } finally{
            session.close();
        }
    }
    
    
}
