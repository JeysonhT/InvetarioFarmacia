/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package services;

import Configurations.HibernateUtil;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import models.Productos;
import models.VentasProducto;
import models.ViewModels.VentasMes;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.NativeQuery;
import org.hibernate.type.StandardBasicTypes;

/**
 *
 * @author jason
 */
public class VentasProductosServices {

    public VentasProductosServices() {

    }

    public String crearVentasProductos(List<VentasProducto> listVenta) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();
            // se guarda el objeto de venta producto 1 por uno en la lista
            for (VentasProducto vp : listVenta) {
                session.persist(vp);
                
                //se busca el producto vendido 
                Productos producto = session.find(Productos.class, vp.getProducto_id());
                if(producto != null){
                    // se resta la cantidad vendida de la cantidad en stock del producto
                    int nuevaCantidad = producto.getCantidad() - vp.getCantidad();
                    if(nuevaCantidad < 0){
                        // si se vende mas de lo que hay se lanza una excepcion
                        throw new IllegalArgumentException("No hay suficiente stock para el producto: " + producto.getNombre());
                    }
                    // se esyablece la nueva cantidad al producto vendido
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
    
    public List<VentasMes> obtenerVentaMes(Date fechaInicio, Date fechaFin) {
        List<VentasMes> resultado = new ArrayList<>();
        Session session = HibernateUtil.getSessionFactory().openSession();
        
        Transaction transaction;
        
        String sql = "CALL ObtenerVentasPorFechas(:fechaInicio, :fechaFin)";
        
        try{
            transaction = session.beginTransaction();
            
            NativeQuery<VentasMes> query = session.createNativeQuery(sql, VentasMes.class)
                    .setParameter("fechaInicio", fechaInicio)
                    .setParameter("fechaFin", fechaFin);
            query.addScalar("producto_id", StandardBasicTypes.INTEGER);
            query.addScalar("nombre_producto", StandardBasicTypes.STRING);
            query.addScalar("totalCantidad", StandardBasicTypes.INTEGER);
            query.addScalar("total_venta", StandardBasicTypes.DOUBLE);
            
            resultado = query.getResultList();
            
            transaction.commit();
        } catch (Exception e){
            throw new RuntimeException("error al obtener las ventas: " + e.getMessage());
        } finally {
            session.close();
        }
        
        return resultado;
    }
}
