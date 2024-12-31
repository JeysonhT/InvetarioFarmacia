/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package services;

import Configurations.HibernateUtil;
import java.util.ArrayList;
import java.util.List;
import models.Creditos;
import models.ViewModels.VistaCredito;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.NativeQuery;
import org.hibernate.type.StandardBasicTypes;

/**
 *
 * @author jason
 */
public class CreditoServices {

    public CreditoServices() {

    }

    public String guardarCredito(Creditos c) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();
            session.persist(c);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }

            throw new RuntimeException("Error a guardar el credito: " + e.getMessage());
        } finally {
            session.close();
        }

        return "Credito creado";
    }

    public List<VistaCredito> obtenerCreditos() {
        List<VistaCredito> resultado = new ArrayList<>();

        Session session = HibernateUtil.getSessionFactory().openSession();

        String sql = "SELECT * FROM ObtenerCreditos";

        try {
            NativeQuery<VistaCredito> query = session.createNativeQuery(sql, VistaCredito.class)
                    .addScalar("id_credito", StandardBasicTypes.INTEGER)
                    .addScalar("id_cliente", StandardBasicTypes.INTEGER)
                    .addScalar("nombre_cliente", StandardBasicTypes.STRING)
                    .addScalar("telefono", StandardBasicTypes.STRING)
                    .addScalar("direccion", StandardBasicTypes.STRING)
                    .addScalar("id_factura", StandardBasicTypes.INTEGER)
                    .addScalar("monto_total", StandardBasicTypes.DOUBLE)
                    .addScalar("saldo_pendiente", StandardBasicTypes.DOUBLE)
                    .addScalar("estado", StandardBasicTypes.STRING)
                    .addScalar("interes", StandardBasicTypes.DOUBLE)
                    .addScalar("fecha_inicial", StandardBasicTypes.DATE)
                    .addScalar("fecha_vencimiento", StandardBasicTypes.DATE);

            resultado = query.getResultList();

        } catch (Exception e) {
            throw new RuntimeException("Error: " + e.getMessage());
        } finally {
            session.close();
        }
        return resultado;
    }
    
    public List<VistaCredito> buscarCreditoByName(String clave){
        List<VistaCredito> resultado = new ArrayList<>();

        Session session = HibernateUtil.getSessionFactory().openSession();

        String sql = "SELECT * FROM ObtenerCreditos WHERE nombre_cliente LIKE :clave";

        try {
            NativeQuery<VistaCredito> query = session.createNativeQuery(sql, VistaCredito.class)
                    .setParameter("clave", clave + '%')
                    .addScalar("id_credito", StandardBasicTypes.INTEGER)
                    .addScalar("id_cliente", StandardBasicTypes.INTEGER)
                    .addScalar("nombre_cliente", StandardBasicTypes.STRING)
                    .addScalar("telefono", StandardBasicTypes.STRING)
                    .addScalar("direccion", StandardBasicTypes.STRING)
                    .addScalar("id_factura", StandardBasicTypes.INTEGER)
                    .addScalar("monto_total", StandardBasicTypes.DOUBLE)
                    .addScalar("saldo_pendiente", StandardBasicTypes.DOUBLE)
                    .addScalar("estado", StandardBasicTypes.STRING)
                    .addScalar("interes", StandardBasicTypes.DOUBLE)
                    .addScalar("fecha_inicial", StandardBasicTypes.DATE)
                    .addScalar("fecha_vencimiento", StandardBasicTypes.DATE);

            resultado = query.getResultList();

        } catch (Exception e) {
            throw new RuntimeException("Error: " + e.getMessage());
        } finally {
            session.close();
        }
        return resultado;
    }
}
