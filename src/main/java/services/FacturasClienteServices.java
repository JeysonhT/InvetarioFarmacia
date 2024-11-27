/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package services;

import Configurations.HibernateUtil;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import models.FacturasCliente;
import models.ViewModels.VistaFacturas;
import models.ViewModels.VistaFacturasCliente;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.NativeQuery;

/**
 *
 * @author jason
 */
public class FacturasClienteServices {

    public FacturasClienteServices() {

    }

    public String createFacturasCliente(int facturaId, int clienteId) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();

            session.persist(new FacturasCliente(facturaId, clienteId));

            transaction.commit();

            return "Factura cliente guardada con exito";

        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException("Error en los datos de entrada: " + e.getMessage());
        } finally {
            session.close();
        }
    }

    /*
       Metodos dedicados a las vistas de las facturas
       metodos pertenecientes a esta seccion: 
       . obtenerFactrasCliente
       . obtenervistaFactura
       . ObtenerVistaFacturaByname
     */
    public List<FacturasCliente> obtenerFacturasCliente() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction;

        List<FacturasCliente> listFC = new ArrayList<>();

        String sql = "CALL obtenerFacturasCliente()";

        try {
            transaction = session.beginTransaction();
            NativeQuery<FacturasCliente> query = session.createNativeQuery(sql,
                    FacturasCliente.class);

            listFC = query.getResultList();

            transaction.commit();

        } catch (Exception e) {

            throw new RuntimeException("Error en la consulta: " + e.getMessage());
        } finally {
            session.close();
        }

        return listFC;
    }

    public List<VistaFacturas> obtenerVistaFactura() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction;

        String sql = "SELECT * FROM Vista_FacturaClientes";
        try {
            transaction = session.beginTransaction();
            NativeQuery<VistaFacturas> query = session.createNativeQuery(sql, VistaFacturas.class);

            List<VistaFacturas> list = query.getResultList();

            transaction.commit();

            return list;

        } catch (Exception e) {
            throw new RuntimeException("Error en la consulta: " + e.getMessage());
        } finally {
            session.close();
        }

    }

    public List<VistaFacturas> obtenerVistaFacturaByName(String clave) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction;

        List<VistaFacturas> resultado = new ArrayList<>();

        String sql = "SELECT * FROM Vista_FacturaClientes WHERE nombre_cliente = :clave";

        try {
            transaction = session.beginTransaction();

            NativeQuery<VistaFacturas> query = session.createNativeQuery(sql,
                    VistaFacturas.class).setParameter("clave", clave);

            transaction.commit();
            resultado = query.getResultList();

        } catch (Exception e) {
            throw new RuntimeException("Error en la consulta: " + e.getMessage());
        } finally {
            session.close();
        }
        return resultado;
    }
    
    public List<VistaFacturasCliente> obtenerVistaFacturaByFactura(int clave) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction;

        List<VistaFacturasCliente> resultado = new ArrayList<>();

        String sql = "SELECT * FROM vista_facturas_clientes_productos WHERE factura_id = :clave";

        try {
            transaction = session.beginTransaction();

            NativeQuery<VistaFacturasCliente> query = session.createNativeQuery(sql,
                    VistaFacturasCliente.class).setParameter("clave", clave);

            transaction.commit();
            resultado = query.getResultList();

        } catch (Exception e) {
            throw new RuntimeException("Error en la consulta: " + e.getMessage());
        } finally {
            session.close();
        }
        return resultado;
    }
}
