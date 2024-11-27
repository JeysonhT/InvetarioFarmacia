/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package services;

import Configurations.HibernateUtil;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import models.Productos;
import models.UnidadesMultiples;
import models.ViewModels.Presentacion;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.NativeQuery;

/**
 *
 * @author jason
 */
public class ProductoService {

    public ProductoService() {
    }

    public String crearProducto(Productos pp) {

        Session session = HibernateUtil.getSessionFactory().openSession();

        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();
            session.persist(pp);
            transaction.commit();

        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }

            return "No se pudo guardar el producto: " + e.getMessage();
        } finally {
            session.close();
        }

        return "producto guardado";

    }

    public String eliminarProducto(int id) {
        Session session = HibernateUtil.getSessionFactory().openSession();

        Transaction transanction;

        try {
            transanction = session.beginTransaction();
            Productos entity = session.find(Productos.class, id);

            if (entity != null) {
                session.remove(entity);
            }

            transanction.commit();

        } catch (Exception e) {
            return "Existe una venta asociada a este producto " + e.getMessage();
        } finally {
            session.close();
        }

        return "Producto eliminado correctamente";
    }

    public Productos actualizarProducto(Productos pp) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction;
        Productos producto = null;
        try {
            transaction = session.beginTransaction();

            producto = session.find(Productos.class, pp.getId());

            if (producto != null) {
                producto.setNombre(pp.getNombre());
                producto.setIndicaciones(pp.getIndicaciones());
                producto.setPresentacion(pp.getPresentacion());
                producto.setMarca(pp.getMarca());

                producto.setCategoria_id(pp.getCategoria_id());
                producto.setPrecio(pp.getPrecio());
                producto.setCantidad(pp.getCantidad());

                session.merge(producto);
            } else {
                throw new SQLException("El producto no existe");
                //throw new RuntimeException("El Producto no Existe");
            }

            transaction.commit();
        } catch (SQLException e) {
            throw new RuntimeException("No se puede actualizar el producto" + e.getMessage());
        } finally {
            session.close();
        }
        return producto;
    }

    public List<Productos> obtenerProductos() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction;

        List<Productos> productos = new ArrayList<>();
        String sql = "CALL ObtenerProductos()";
        try {
            transaction = session.beginTransaction();
            NativeQuery<Productos> query = session.
                    createNativeQuery(sql, Productos.class);

            productos = query.getResultList();
            transaction.commit();
        } catch (Exception e) {

            throw new RuntimeException("No se pudo obtener la lista de productos: " + e.getMessage());
        } finally {
            session.close();
        }

        return productos;
    }

    public List<Productos> obtenerProductoByName(String clave) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction;
        String sql = "SELECT * FROM Productos WHERE nombre LIKE :clave";

        try {
            transaction = session.beginTransaction();
            NativeQuery<Productos> query = session.createNativeQuery(sql,
                    Productos.class).setParameter("clave", clave + "%");

            List<Productos> result = query.getResultList();

            if (result.isEmpty()) {
                throw new RuntimeException("El producto no existe o escribio mal el nombre");
            }

            transaction.commit();
            return result;

        } catch (RuntimeException e) {
            throw new RuntimeException("Producto no encontrado: " + e.getMessage());
        } finally {
            session.close();
        }
    }

    //metodos para obtener datos importantes sobre el inventario de productos
    public int obtenerCantidadProductosTotal() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction;
        try {
            transaction = session.beginTransaction();
            NativeQuery<Integer> query = session.createNativeQuery("Call obtenerCantidadProductos()", Integer.TYPE);
            Integer resultado = query.getResultList().get(0);
            transaction.commit();

            return resultado;
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener la cantidad de productos");
        } finally {
            session.close();
        }
    }

    public double totalCordobasInvetario() {

        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction;

        String sql = "CALL totalCordobasInventario()";
        try {

            transaction = session.beginTransaction();
            NativeQuery<Integer> query = session.createNativeQuery(sql, Integer.TYPE);
            Integer resultado = query.getResultList().get(0);

            transaction.commit();

            return resultado;
        } catch (Exception e) {
            throw new RuntimeException("Error en la consulta: " + e.getMessage());
        } finally {
            session.close();
        }
    }

    public List<String> getPresentación() {
        Session session = HibernateUtil.getSessionFactory().openSession();

        Transaction transaction = null;

        String sql = "CALL obtenerPresentación()";

        List<Presentacion> presentaciones = new ArrayList<>();

        List<String> texto = new ArrayList<>();

        try {
            transaction = session.beginTransaction();

            NativeQuery<Presentacion> query = session.createNativeQuery(sql, Presentacion.class);

            presentaciones = query.getResultList();

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException("Error al obtener las presentaciones");
        } finally {
            session.close();
        }

        for (Presentacion p : presentaciones) {
            texto.add(p.getNombre());
        }

        return texto;

    }

    public void ingresarPresentacion(Presentacion presentacion) {
        Session session = HibernateUtil.getSessionFactory().openSession();

        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();

            session.persist(presentacion);

            transaction.commit();
        } catch (Exception e) {

            if (transaction != null) {
                transaction.rollback();
            }

        } finally {
            session.close();
        }
    }

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

}
