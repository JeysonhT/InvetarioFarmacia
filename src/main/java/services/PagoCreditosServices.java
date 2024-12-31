/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package services;

import Configurations.HibernateUtil;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.StoredProcedureQuery;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import models.pagosCredito;
import org.hibernate.NonUniqueResultException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.NativeQuery;

/**
 *
 * @author jason
 */
public class PagoCreditosServices {

    public PagoCreditosServices() {

    }

    public String efectuarPago(pagosCredito pCreditos) throws NonUniqueResultException {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;

        String resultado;

        int p_id;

        String procedureName = "crearPagoCredito";

        try {
            transaction = session.beginTransaction();
            //Creacion de la consulta con ayuda de la interfaz StoredProcedureQuery
            StoredProcedureQuery query = session.createStoredProcedureQuery(procedureName);

            query.registerStoredProcedureParameter("id_creditoP", Integer.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("fecha_pagoP", Date.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("monto_pagoP", Double.class, ParameterMode.IN);

            // Registrar el parámetro de salida
            query.registerStoredProcedureParameter("p_id", Integer.class, ParameterMode.OUT);

            //Establecemos los valores de los parametros
            query.setParameter("id_creditoP", pCreditos.getId_creditos());
            query.setParameter("fecha_pagoP", pCreditos.getFecha_pago());
            query.setParameter("monto_pagoP", pCreditos.getMonto_pago());

            query.execute();

            p_id = (Integer) query.getOutputParameterValue("p_id");

            transaction.commit();

            resultado = "Id del pago creado: " + p_id;

        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException("No se pudo efectuar el pago");
        } finally {
            session.close();
        }
        return resultado;
    }
    
    public List<Object[]> ObtenerPagosCreditos(Date fechaInicio, Date fechaFin){
        List<Object[]> resultado = new ArrayList<>();
        Session session = HibernateUtil.getSessionFactory().openSession();

        Transaction transaction;

        String sql = "CALL obtenerPagosFecha(:fechaInicio, :fechaFin)";

        try {
            transaction = session.beginTransaction();

            NativeQuery<Object[]> query = session.createNativeQuery(sql, Object[].class)
                    .setParameter("fechaInicio", fechaInicio)
                    .setParameter("fechaFin", fechaFin);

            resultado = query.getResultList();

            transaction.commit();
        } catch (Exception e) {
            throw new RuntimeException("error al obtener los pagos: " + e.getMessage());
        } finally {
            session.close();
        }

        return resultado;
    }
}
