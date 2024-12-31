/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package services.Caja;

import Configurations.HibernateUtil;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import models.CajaModels.Caja;
import models.CajaModels.MovimientosCaja;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.NativeQuery;

/**
 *
 * @author jason
 */
public class MovimientoServices {

    public MovimientoServices() {

    }

    public String resgistrarMovimiento(Caja caja, double monto, String tipoMovimiento, String descripcion) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            MovimientosCaja mc = new MovimientosCaja();

            mc.setFecha(LocalDateTime.now());
            mc.setMonto(BigDecimal.valueOf(monto));
            mc.setTipoMovimiento(tipoMovimiento);
            mc.setDescripcion(descripcion);
            
            mc.setCaja(caja);

            session.persist(mc);

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }

            throw new RuntimeException("No se puede guardar el movimiento");
        }
        return "Movimiento registrado exitosamente como: " + tipoMovimiento;
    }

    public List<MovimientosCaja> obtenerMovimientos(Long id_caja) {

        List<MovimientosCaja> movimientos;
        String sql = "SELECT * FROM movimientosCaja WHERE id_caja = :id_caja";
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            NativeQuery<MovimientosCaja> query = session.
                    createNativeQuery(sql, MovimientosCaja.class)
                    .setParameter("id_caja", id_caja);

            movimientos = query.getResultList();

            return movimientos;

        } catch (Exception e) {
            throw new RuntimeException("La caja aun no tiene movimientos: " + e.getMessage());
        }

    }

    public List<Object[]> obtenerMovimientosCaja(Long id_caja) {
        List<Object[]> resultado = new ArrayList<>();
        String sql = "CALL obtenerMovimientosCaja(:id_caja)";
        
        try (Session session = HibernateUtil.getSessionFactory().openSession()){
            NativeQuery<Object[]> query = session.createNativeQuery(sql, Object[].class)
                    .setParameter("id_caja", id_caja);
            resultado = query.getResultList();
            
        } catch (Exception e) {
            throw new RuntimeException("No hay movimientos en la caja actual");
        }
        
        return resultado;
    }

}
