/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package services;

import Configurations.HibernateUtil;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.StoredProcedureQuery;
import java.util.ArrayList;
import java.util.List;
import models.Lotes;
import models.UnidadesMultiples;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.NativeQuery;

/**
 *
 * @author jason
 */
public class LoteServices {

    public LoteServices() {

    }

    public String guardarLote(Lotes lote) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction;
        try {
            transaction = session.beginTransaction();
            session.persist(lote);
            transaction.commit();
        } catch (Exception e) {
            throw new RuntimeException("error al guardar el lote");
        } finally {
            session.close();
        }

        return "Lote Guardado";
    }

    public List<Lotes> obtenerLotes() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        String sql = "SELECT * FROM Lotes";
        List<Lotes> resultado = new ArrayList<>();
        try {
            NativeQuery<Lotes> query = session.createNativeQuery(sql, Lotes.class);

            resultado = query.getResultList();
        } catch (Exception e) {
            throw new RuntimeException("No hay lotes registrados");
        }
        return resultado;
    }

    public String actualizarLote(Lotes ll, int cantidad_producto) {

        Session session = HibernateUtil.getSessionFactory().openSession();

        Transaction transaction = null;

        String sql = "SELECT * FROM Lotes WHERE id_producto = :id_producto";

        try {
            transaction = session.beginTransaction();

            NativeQuery<Lotes> query = session.createNativeQuery(sql, Lotes.class)
                    .setParameter("id_producto", ll.getId_producto());

            Lotes lote = query.uniqueResult();

            if (lote != null) {
                UnidadesMultiples um = new UnidadesMayoresServices().ObtenerUnidadByid(ll.getId_producto());
                if (um != null) {

                    double nuevaExistencia = (double) cantidad_producto / um.getFactor_Conversion();
                    lote.setExistencia_inicial(ll.getExistencia_inicial());
                    lote.setExistencia_actual(nuevaExistencia);

                    lote.setFecha_vencimiento(ll.getFecha_vencimiento());

                    lote.setLote(ll.getLote());

                    session.merge(lote);
                }

            } else {
                throw new RuntimeException("El producto no tiene un lote asociado");
            }

            transaction.commit();
        } catch (RuntimeException e) {

            if (transaction != null) {
                transaction.rollback();
            }

            return e.getMessage();

        } finally {
            session.close();
        }

        return "Lote actualizado";
    }

    public int lotesPorVencer() {
        Session session = HibernateUtil.getSessionFactory().openSession();

        String procedureName = "comprobarVencimientos";

        int resultado = 0;

        try {
            StoredProcedureQuery query = session.createStoredProcedureQuery(procedureName);

            // Registrar el parámetro de salida
            query.registerStoredProcedureParameter("p_num", Integer.class, ParameterMode.OUT);

            query.execute();

            resultado = (Integer) query.getOutputParameterValue("p_num");

        } catch (Exception e) {
            throw new RuntimeException("No se obtuvo ningun valor: " + e.getMessage());
        } finally {
            session.close();
        }

        return resultado;
    }

}
