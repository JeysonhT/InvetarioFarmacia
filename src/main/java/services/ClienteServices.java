/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package services;

import Configurations.HibernateUtil;
import java.util.List;
import models.Cliente;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.NativeQuery;

/**
 *
 * @author jason
 */
public class ClienteServices {

    public ClienteServices() {
    }

    public String crearCliente(Cliente cliente) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();
            session.persist(cliente);
            transaction.commit();
            return "cliente guardado: " + cliente.toString();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            return "No se pudo guardar el cliente" + e.getMessage();

        } finally {
            session.close();
        }

    }

    public Cliente getIdClienteNoRegistrado() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;

        String sql = "SELECT * FROM Clientes where nombre = 'Cliente Anonimo'";
        
        Cliente cliente = new Cliente();
        try {
            transaction = session.beginTransaction();
            NativeQuery<Cliente> query = session.createNativeQuery(sql,
                    Cliente.class);
            cliente = query.getResultList().get(0);
           
            transaction.commit();
            
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException("El registro no existe: " + e.getMessage());
        } finally {
            session.close();
        }
        
        return cliente;

    }

    public List<Cliente> obtenerClientes() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction;
        String sql = "CALL obtenerClientes()";

        List<Cliente> clientes;

        try {
            transaction = session.beginTransaction();
            NativeQuery<Cliente> query = session.
                    createNativeQuery(sql, Cliente.class);
            clientes = query.getResultList();
            transaction.commit();
            return clientes;
        } catch (Exception e) {
            throw new RuntimeException("No se puede obtener la lista de Clientes: " + e.getMessage());
        } finally {
            session.close();
        }
    }

    public String eliminarCliente(int id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction;
        try {
            transaction = session.beginTransaction();
            Cliente entity = session.find(Cliente.class, id);

            if (entity != null) {
                session.remove(entity);
                transaction.commit();

                session.close();

                return "Cliente eliminado: " + entity.toString();
            } else {
                transaction.rollback();
                session.close();
            }

        } catch (Exception e) {
            return "El cliente no existe o : " + e.getMessage();
        }
        return "Operacion no realizada";
    }

}
