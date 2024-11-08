/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package services;

import Configurations.HibernateUtil;
import java.util.Date;
import models.Facturas;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author jason
 */
public class FacturaServices {

    public FacturaServices() {

    }

    public int createFactura(Facturas factura) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();
            session.persist(factura);
            transaction.commit();
            return factura.getId();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException("Error en los datos de entrada: " + e.getMessage());
        } finally {
            session.close();
        }
    }
}
