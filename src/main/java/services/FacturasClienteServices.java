/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package services;

import Configurations.HibernateUtil;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import models.CajaModels.Caja;
import models.CajaModels.MovimientosCaja;
import models.FacturasCliente;
import models.Micelanea.Micelanea;
import models.Micelanea.VentasMicelanea;
import models.Productos;
import models.Ventas;
import models.VentasProducto;
import models.ViewModels.VistaFacturas;
import models.ViewModels.VistaFacturasProductos;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.NativeQuery;
import org.tentackle.misc.MiscCoreBundle;

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

        String sql = "SELECT * FROM Vista_FacturaClientes WHERE nombre_Cliente LIKE :clave";

        try {
            transaction = session.beginTransaction();

            NativeQuery<VistaFacturas> query = session.createNativeQuery(sql,
                    VistaFacturas.class).setParameter("clave", clave + '%');

            transaction.commit();
            resultado = query.getResultList();

        } catch (Exception e) {
            throw new RuntimeException("Error en la consulta: " + e.getMessage());
        } finally {
            session.close();
        }
        return resultado;
    }

    public List<VistaFacturasProductos> obtenerVistaFacturaByFactura(int clave) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction;

        List<VistaFacturasProductos> resultado = new ArrayList<>();

        String sql = "CALL ObtenerProductosDeFactura(:clave)";

        try {
            transaction = session.beginTransaction();

            NativeQuery<VistaFacturasProductos> query = session.createNativeQuery(sql,
                    VistaFacturasProductos.class).setParameter("clave", clave);

            transaction.commit();
            resultado = query.getResultList();

        } catch (Exception e) {
            throw new RuntimeException("Error en la consulta: " + e.getMessage());
        } finally {
            session.close();
        }
        return resultado;
    }

    public String reembolsoCompleto(List<Productos> productos, int idVenta, Caja caja) {

        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession();) {
            transaction = session.beginTransaction();
            for (Productos p : productos) {
                Productos producto = session.find(Productos.class, p.getId());

                if (producto != null) {
                    producto.setCantidad(producto.getCantidad() + p.getCantidad());

                    session.merge(producto);
                }
            }

            Ventas venta = session.find(Ventas.class, idVenta);

            MovimientosCaja mc = new MovimientosCaja();

            mc.setFecha(LocalDateTime.now());
            mc.setMonto(BigDecimal.valueOf(-venta.getTotal()));
            mc.setTipoMovimiento("RETIRO");
            mc.setCaja(caja);

            session.persist(mc);

            session.remove(venta);

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }

            throw new RuntimeException("No se puede realizar el reembolso");
        }
        return "Reembolso realizado con exito";
    }

    public String reembolsoParcial(Productos p, int idventa, Caja caja) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            // Obtener y actualizar el producto
            Productos producto = session.find(Productos.class, p.getId());
            if (producto != null) {
                producto.setCantidad(producto.getCantidad() + p.getCantidad());
                session.merge(producto);
            }

            // Obtener la venta y verificar su existencia
            Ventas venta = session.find(Ventas.class, idventa);
            if (venta != null) {
                String sql = "SELECT * FROM ventas_producto WHERE venta_id = :idVenta AND producto_id = :productoId";
                VentasProducto vp = session.createNativeQuery(sql, VentasProducto.class)
                        .setParameter("idVenta", idventa)
                        .setParameter("productoId", p.getId()) // Corregir nombre del parámetro
                        .getSingleResult();

                if (vp != null) {
                    // Actualizar el total de la venta
                    venta.setTotal(venta.getTotal() - vp.getPrecio());
                    //guardar el movimiento
                    MovimientosCaja mc = new MovimientosCaja();

                    mc.setFecha(LocalDateTime.now());
                    mc.setMonto(BigDecimal.valueOf(-vp.getPrecio()));
                    mc.setTipoMovimiento("RETIRO");
                    mc.setCaja(caja);

                    session.persist(mc);
                    session.merge(venta);

                    // Eliminar la relación VentasProducto
                    session.remove(vp);
                }
            }

            // Confirmar la transacción
            transaction.commit();
        } catch (Exception e) {
            // Revertir transacción si ocurre un error
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException("No se puede realizar el reembolso: " + e.getMessage(), e);
        }
        return "Reembolso realizado con éxito";
    }

    public List<Object[]> obtenerVistaMicelaneaByFactura(int clave) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction;

        List<Object[]> resultado = new ArrayList<>();

        String sql = "CALL obtenerMicelaneasdeFactura(:clave)";

        try {
            transaction = session.beginTransaction();

            NativeQuery<Object[]> query = session.createNativeQuery(sql,
                    Object[].class).setParameter("clave", clave);

            transaction.commit();
            resultado = query.getResultList();

        } catch (Exception e) {
            throw new RuntimeException("Error en la consulta: " + e.getMessage());
        } finally {
            session.close();
        }
        return resultado;
    }

    public String reembolsoCompletoMiscelanea(List<Micelanea> productos, int ventaId, Caja caja) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession();) {
            transaction = session.beginTransaction();
            for (Micelanea p : productos) {
                Micelanea micelanea = session.find(Micelanea.class, p.getIdMicelanea());

                if (micelanea != null) {
                    micelanea.setCantidad(micelanea.getCantidad() + p.getCantidad());

                    session.merge(micelanea);
                }
            }

            Ventas venta = session.find(Ventas.class, ventaId);

            MovimientosCaja mc = new MovimientosCaja();

            mc.setFecha(LocalDateTime.now());
            mc.setMonto(BigDecimal.valueOf(-venta.getTotal()));
            mc.setTipoMovimiento("RETIRO");
            mc.setCaja(caja);

            session.persist(mc);

            session.remove(venta);

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }

            throw new RuntimeException("No se puede realizar el reembolso");
        }
        return "Reembolso realizado con exito";
    }

    public String reembolsoParcialMiscelanea(Micelanea micelanea, int ventaId, Caja caja) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            // Obtener y actualizar el producto
            Micelanea producto = session.find(Micelanea.class, micelanea.getIdMicelanea());
            if (producto != null) {
                producto.setCantidad(producto.getCantidad() + micelanea.getCantidad());
                session.merge(producto);
            }

            // Obtener la venta y verificar su existencia
            Ventas venta = session.find(Ventas.class, ventaId);
            if (venta != null) {
                String sql = "SELECT * FROM ventasmicelanea WHERE venta_id = :idVenta AND id_micelanea = :productoId";
                VentasMicelanea vp = session.createNativeQuery(sql, VentasMicelanea.class)
                        .setParameter("idVenta", ventaId)
                        .setParameter("productoId", micelanea.getIdMicelanea()) // Corregir nombre del parámetro
                        .getSingleResult();

                if (vp != null) {
                    // Actualizar el total de la venta
                    venta.setTotal(venta.getTotal() - vp.getPrecio());
                    //guardar el movimiento
                    MovimientosCaja mc = new MovimientosCaja();

                    mc.setFecha(LocalDateTime.now());
                    mc.setMonto(BigDecimal.valueOf(-vp.getPrecio()));
                    mc.setTipoMovimiento("RETIRO");
                    mc.setCaja(caja);

                    session.persist(mc);
                    session.merge(venta);

                    // Eliminar la relación VentasProducto
                    session.remove(vp);
                }
            }

            // Confirmar la transacción
            transaction.commit();
        } catch (Exception e) {
            // Revertir transacción si ocurre un error
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException("No se puede realizar el reembolso: " + e.getMessage(), e);
        }
        return "Reembolso realizado con éxito";
    }

}
