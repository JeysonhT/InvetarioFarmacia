/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package services;

import Configurations.HibernateUtil;
import java.util.ArrayList;
import java.util.List;
import models.UnidadesMultiples;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.NativeQuery;

/**
 *
 * @author jason
 */
public class UnidadesMayoresServices {

    public UnidadesMayoresServices() {
    }

    // metodos para la unidades multiples de los productos
    public String guardarUnidadMayor(UnidadesMultiples um) {
        Session session = HibernateUtil.getSessionFactory().openSession();

        Transaction transaction;

        try {
            transaction = session.beginTransaction();
            session.persist(um);
            transaction.commit();
        } catch (Exception e) {
            throw new RuntimeException("Error al guardar la unidades: " + e.getMessage());
        } finally {
            session.close();
        }

        return "Unidades multiples guardadas exitosamente";
    }

    public List<UnidadesMultiples> obtenerUnidades() {
        Session session = HibernateUtil.getSessionFactory().openSession();

        String sql = "SELECT * FROM UnidadesMultiples";

        List<UnidadesMultiples> resultado = new ArrayList<>();
        try {
            NativeQuery<UnidadesMultiples> query = session.createNativeQuery(sql,
                    UnidadesMultiples.class);

            resultado = query.getResultList();

        } catch (Exception e) {
            throw new RuntimeException("Error en la base de datos");
        } finally {
            session.close();
        }

        return resultado;
    }

    public String actualizarUnidadMayor(UnidadesMultiples um) {

        Session session = HibernateUtil.getSessionFactory().openSession();

        Transaction transaction = null;

        String sql = "SELECT * FROM UnidadesMultiples WHERE id_producto = :id_producto";

        try {
            transaction = session.beginTransaction();

            NativeQuery<UnidadesMultiples> query = session.createNativeQuery(sql, UnidadesMultiples.class)
                    .setParameter("id_producto", um.getId_Producto());

            UnidadesMultiples unidadMultiple = query.uniqueResult();

            if (unidadMultiple != null) {
                unidadMultiple.setDescripcion(um.getDescripcion());
                unidadMultiple.setFactor_Conversion(um.getFactor_Conversion());
                unidadMultiple.setPrecio_Unitario(um.getPrecio_Unitario());

                session.merge(unidadMultiple);
            } else {
                throw new RuntimeException("El producto no tiene una unidad multplie o mayor");
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

        return "Unidad Multiple actualizada";
    }

    public UnidadesMultiples ObtenerUnidadByid(int id) {
        Session session = HibernateUtil.getSessionFactory().openSession();

        Transaction transaction = null;

        String sql = "SELECT * FROM UnidadesMultiples WHERE id_producto = :id_producto";
        UnidadesMultiples unidadMultiple;

        try {

            NativeQuery<UnidadesMultiples> query = session.createNativeQuery(sql, UnidadesMultiples.class)
                    .setParameter("id_producto", id);

            unidadMultiple = query.uniqueResult();

            if (unidadMultiple != null) {
                return unidadMultiple;
            } else {
                throw new RuntimeException("El producto no tiene una unidad multplie o mayor");
            }

        } catch (RuntimeException e) {

            if (transaction != null) {
                transaction.rollback();
            }

        } finally {
            session.close();
        }
        return null;
    }
}
