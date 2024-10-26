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

    public String guardarVentasProducto(Ventas ventas) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();
            session.persist(ventas);
            transaction.commit();

            return "Venta creada: " + ventas.toString();
        } catch (Exception e) {

            if (transaction != null) {
                transaction.rollback();
            }

            throw new RuntimeException(e.getMessage());
        } finally {
            session.close();
        }
    }
}
