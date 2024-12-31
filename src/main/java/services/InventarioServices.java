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
import models.ViewModels.VistaInventario;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.NativeQuery;
import org.hibernate.type.StandardBasicTypes;

/**
 *
 * @author jason
 */
public class InventarioServices {

    public InventarioServices() {

    }

    public List<VistaInventario> getInventario() {
        List<VistaInventario> resultado = new ArrayList<>();

        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction;

        String sql = "CALL ObtenerInventario()";

        try {
            transaction = session.beginTransaction();
            NativeQuery<VistaInventario> query = session.createNativeQuery(sql, VistaInventario.class)
                    .addScalar("id_producto", StandardBasicTypes.INTEGER)
                    .addScalar("nombre_producto", StandardBasicTypes.STRING)
                    .addScalar("indicaciones", StandardBasicTypes.STRING)
                    .addScalar("laboratorio", StandardBasicTypes.STRING)
                    .addScalar("codigo_lote", StandardBasicTypes.STRING)
                    .addScalar("fecha_vencimiento", StandardBasicTypes.DATE)
                    .addScalar("categoria", StandardBasicTypes.STRING)
                    .addScalar("presentacion", StandardBasicTypes.STRING)
                    .addScalar("precio", StandardBasicTypes.DOUBLE)
                    .addScalar("cantidad", StandardBasicTypes.INTEGER)
                    .addScalar("precio_costo", StandardBasicTypes.DOUBLE)
                    .addScalar("costo_total", StandardBasicTypes.DOUBLE)
                    .addScalar("utilidad", StandardBasicTypes.DOUBLE)
                    .addScalar("unidad_mayor", StandardBasicTypes.STRING)
                    .addScalar("conversion", StandardBasicTypes.INTEGER)
                    .addScalar("precio_mayor", StandardBasicTypes.DOUBLE);

            resultado = query.getResultList();

            transaction.commit();

        } catch (Exception e) {
            throw new RuntimeException("no se pudo obtener el invetario: " + e.getMessage());
        } finally {
            session.close();
        }

        return resultado;
    }

    public List<VistaInventario> obtenerProductoByName(String clave) {
        List<VistaInventario> resultado = new ArrayList<>();

        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction;

        String sql = "CALL ObtenerProductosByName(:clave)";

        try {
            transaction = session.beginTransaction();
            NativeQuery<VistaInventario> query = session.createNativeQuery(sql, VistaInventario.class)
                    .setParameter("clave", clave+'%')
                    .addScalar("id_producto", StandardBasicTypes.INTEGER)
                    .addScalar("nombre_producto", StandardBasicTypes.STRING)
                    .addScalar("indicaciones", StandardBasicTypes.STRING)
                    .addScalar("laboratorio", StandardBasicTypes.STRING)
                    .addScalar("codigo_lote", StandardBasicTypes.STRING)
                    .addScalar("fecha_vencimiento", StandardBasicTypes.DATE)
                    .addScalar("categoria", StandardBasicTypes.STRING)
                    .addScalar("presentacion", StandardBasicTypes.STRING)
                    .addScalar("precio", StandardBasicTypes.DOUBLE)
                    .addScalar("cantidad", StandardBasicTypes.INTEGER)
                    .addScalar("precio_costo", StandardBasicTypes.DOUBLE)
                    .addScalar("costo_total", StandardBasicTypes.DOUBLE)
                    .addScalar("utilidad", StandardBasicTypes.DOUBLE)
                    .addScalar("unidad_mayor", StandardBasicTypes.STRING)
                    .addScalar("conversion", StandardBasicTypes.INTEGER)
                    .addScalar("precio_mayor", StandardBasicTypes.DOUBLE);

            resultado = query.getResultList();

            transaction.commit();

        } catch (Exception e) {
            throw new RuntimeException("no se pudo obtener el invetario: " + e.getMessage());
        } finally {
            session.close();
        }

        return resultado;
    }

    public String guardarFromExcel(List<Object[]> objetos) {
        Session session = HibernateUtil.getSessionFactory().openSession();

        Transaction transaction = null;

        String procedureName = "InsertarProductosExcel";
        try {
            transaction = session.beginTransaction();

            for (Object[] objeto : objetos) {
                StoredProcedureQuery query = session.createStoredProcedureQuery(procedureName);

                query.registerStoredProcedureParameter("p_nombre", String.class, ParameterMode.IN);
                query.registerStoredProcedureParameter("p_indicaciones", String.class, ParameterMode.IN);
                query.registerStoredProcedureParameter("p_laboratorio", String.class, ParameterMode.IN);
                query.registerStoredProcedureParameter("codigo_lote", String.class, ParameterMode.IN);
                query.registerStoredProcedureParameter("p_categoria", String.class, ParameterMode.IN);
                query.registerStoredProcedureParameter("p_presentacion", String.class, ParameterMode.IN);
                query.registerStoredProcedureParameter("p_precio", Double.class, ParameterMode.IN);
                query.registerStoredProcedureParameter("p_cantidad", Integer.class, ParameterMode.IN);
                query.registerStoredProcedureParameter("p_precioCosto", Double.class, ParameterMode.IN);
                query.registerStoredProcedureParameter("p_total", Double.class, ParameterMode.IN);
                query.registerStoredProcedureParameter("p_utilidad", Double.class, ParameterMode.IN);
                query.registerStoredProcedureParameter("unidadMayor", String.class, ParameterMode.IN);
                query.registerStoredProcedureParameter("precioMayor", Double.class, ParameterMode.IN);
                query.registerStoredProcedureParameter("conversion", Integer.class, ParameterMode.IN);
                query.registerStoredProcedureParameter("l_fecha_vencimiento", Date.class, ParameterMode.IN);

                //Establecemos los valores de los parametros
                query.setParameter("p_nombre", objeto[0]);
                query.setParameter("p_indicaciones", objeto[1]);
                query.setParameter("p_laboratorio", objeto[2]);
                query.setParameter("codigo_lote", objeto[3]);
                query.setParameter("p_categoria", objeto[4]);
                query.setParameter("p_presentacion", objeto[5]);
                query.setParameter("p_precio", objeto[6]);
                query.setParameter("p_cantidad", objeto[7]);
                query.setParameter("p_precioCosto", objeto[8]);
                query.setParameter("p_total", objeto[9]);
                query.setParameter("p_utilidad", objeto[10]);
                query.setParameter("unidadMayor", objeto[11]);
                query.setParameter("precioMayor", objeto[12]);
                query.setParameter("conversion", objeto[13]);
                query.setParameter("l_fecha_vencimiento", objeto[14]);

                query.execute();

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

        return "Se guardaron los datos exitosamente";
    }

    public double obtenerGanancias() {
        Session session = HibernateUtil.getSessionFactory().openSession();

        String procedureName = "ObtenerGananciaInventario";

        double resultado = 0.0;

        try {
            StoredProcedureQuery query = session.createStoredProcedureQuery(procedureName);

            // Registrar el parámetro de salida
            query.registerStoredProcedureParameter("ganancia", Double.class, ParameterMode.OUT);

            query.execute();

            resultado = (Double) query.getOutputParameterValue("ganancia");

        } catch (Exception e) {
            throw new RuntimeException("No se obtuvo ningun valor: " + e.getMessage());
        } finally {
            session.close();
        }

        return resultado;
    }

    public List<VistaInventario> obtenerProductoByCategoria(String clave) {
        List<VistaInventario> resultado = new ArrayList<>();

        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction;

        String sql = "CALL ObtenerProductosByCategoria(:clave)";

        try {
            transaction = session.beginTransaction();
            NativeQuery<VistaInventario> query = session.createNativeQuery(sql, VistaInventario.class)
                    .setParameter("clave", clave+'%')
                    .addScalar("id_producto", StandardBasicTypes.INTEGER)
                    .addScalar("nombre_producto", StandardBasicTypes.STRING)
                    .addScalar("indicaciones", StandardBasicTypes.STRING)
                    .addScalar("laboratorio", StandardBasicTypes.STRING)
                    .addScalar("codigo_lote", StandardBasicTypes.STRING)
                    .addScalar("fecha_vencimiento", StandardBasicTypes.DATE)
                    .addScalar("categoria", StandardBasicTypes.STRING)
                    .addScalar("presentacion", StandardBasicTypes.STRING)
                    .addScalar("precio", StandardBasicTypes.DOUBLE)
                    .addScalar("cantidad", StandardBasicTypes.INTEGER)
                    .addScalar("precio_costo", StandardBasicTypes.DOUBLE)
                    .addScalar("costo_total", StandardBasicTypes.DOUBLE)
                    .addScalar("utilidad", StandardBasicTypes.DOUBLE)
                    .addScalar("unidad_mayor", StandardBasicTypes.STRING)
                    .addScalar("conversion", StandardBasicTypes.INTEGER)
                    .addScalar("precio_mayor", StandardBasicTypes.DOUBLE);

            resultado = query.getResultList();

            transaction.commit();

        } catch (Exception e) {
            throw new RuntimeException("no se pudo obtener el invetario: " + e.getMessage());
        } finally {
            session.close();
        }

        return resultado;
    }
}
