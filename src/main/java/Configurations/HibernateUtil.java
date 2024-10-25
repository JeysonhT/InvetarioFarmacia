/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Configurations;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 *
 * @author jason
 */
public class HibernateUtil {

    private static SessionFactory sessionFactory;

    private HibernateUtil() {
    }

    //obtener el creador de session de hibernate
    public static SessionFactory getSessionFactory() {
        synchronized (HibernateUtil.class) {
            if (sessionFactory == null) {
                try {
                    sessionFactory = new Configuration()
                            .configure()
                            .buildSessionFactory();
                } catch (HibernateException e) {
                    throw new RuntimeException("No se puede realizar la conexión: " + e.getMessage());
                }
            }
        }

        return sessionFactory;
    }

    public static void closeSessionFactory() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }
}
