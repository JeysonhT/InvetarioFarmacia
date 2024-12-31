/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package services.Micelanea;

import Configurations.HibernateUtil;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import models.CajaModels.Caja;
import models.CajaModels.Turno;
import models.Micelanea.Micelanea;
import models.Micelanea.VentasMicelanea;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.NativeQuery;

/**
 *
 * @author jason
 */
public class ventasMiscelaneaServices {

    public ventasMiscelaneaServices() {

    }


    public String GuardarVentaMicelanea(List<VentasMicelanea> listVenta) {

        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();
            // se guarda el objeto de venta producto 1 por uno en la lista
            for (VentasMicelanea vp : listVenta) {
                session.persist(vp);
                //se busca el producto vendido 
                Micelanea producto = session.find(Micelanea.class, vp.getMicelanea().getIdMicelanea());
                if (producto != null) {
                    // se resta la cantidad vendida de la cantidad en stock del producto
                    int nuevaCantidad = producto.getCantidad() - vp.getCantidad();
                    if (nuevaCantidad < 0) {
                        // si se vende mas de lo que hay se lanza una excepcion
                        throw new IllegalArgumentException("No hay suficiente stock para el producto: " + producto.getNombre());
                    }
                    // se establece la nueva cantidad al producto vendido
                    producto.setCantidad(nuevaCantidad);
                    session.persist(producto);
                }

            }
            transaction.commit();

            return "Venta realizada con exito";
        } catch (IllegalArgumentException e) {
            if (transaction != null) {
                transaction.rollback();
            }

            throw new RuntimeException(e.getMessage());
        } finally {
            session.close();
        }
    }

    public List<Object[]> obtenerVentaMes(Date fechaInicio, Date fechaFin) {
        List<Object[]> resultado = new ArrayList<>();
        Session session = HibernateUtil.getSessionFactory().openSession();

        Transaction transaction;

        String sql = "CALL ObtenerVentasMicelaneaPorFechas(:fechaInicio, :fechaFin)";

        try {
            transaction = session.beginTransaction();

            NativeQuery<Object[]> query = session.createNativeQuery(sql, Object[].class)
                    .setParameter("fechaInicio", fechaInicio)
                    .setParameter("fechaFin", fechaFin);

            resultado = query.getResultList();

            transaction.commit();
        } catch (Exception e) {
            throw new RuntimeException("error al obtener las ventas: " + e.getMessage());
        } finally {
            session.close();
        }

        return resultado;
    }
}
