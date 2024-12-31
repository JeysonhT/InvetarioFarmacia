/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package services.Caja;

import Configurations.HibernateUtil;
import java.lang.annotation.Native;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import models.CajaModels.Caja;
import models.CajaModels.MovimientosCaja;
import models.Usuarios;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.NativeQuery;
import org.hibernate.type.StandardBasicTypes;

/**
 *
 * @author jason
 */
public class CajaServices {

    public CajaServices() {

    }

    public String abrirCaja(Caja caja) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;

        String response = "";
        try {
            transaction = session.beginTransaction();

            session.persist(caja);

            transaction.commit();

            response = "Caja abierta: " + caja.getApertura().toString().split("\\.")[0];

        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
        } finally {
            session.close();
        }
        return response;
    }

    public String cerrarCaja(Long id_caja, LocalDateTime cierre) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Caja caja = session.find(Caja.class, id_caja);

            if (caja != null) {
                caja.setCierre(cierre);
                caja.setAbierta(false);

                BigDecimal montoFinal = caja.getMovimientos().stream().map(MovimientosCaja::getMonto).reduce(BigDecimal.ZERO, BigDecimal::add);

                caja.setMonto_Final(caja.getMonto_Inicial().add(montoFinal));
                session.merge(caja);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }

            throw new RuntimeException("No se puede cerrar la caja");
        }

        return "Cierre de caja Exitoso";
    }

    public Caja obtenerCaja(Long id_caja) {
        Caja caja = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            caja = session.find(Caja.class, id_caja);
            if (caja != null) {
                return caja;
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Caja no encontrada");
        }

        return caja;
    }

    public List<Caja> obtenerCajas() {
        List<Caja> cajas = new ArrayList<>();
        String sql = "CALL ObtenerCajas()";
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            NativeQuery<Caja> query = session.createNativeQuery(sql, Caja.class);
            cajas = query.getResultList();
        } catch (Exception e) {
            throw new IllegalArgumentException("No hay cajas registradas aun");
        }

        return cajas;
    }

    public List<Object[]> ObtenerResultadoCaja(Long id_caja) throws Exception {
        List<Object[]> resultado = new ArrayList<>();
        String sql = "CALL resultadosCaja(:id_caja)";
        try (Session session = HibernateUtil.getSessionFactory().openSession()){
            NativeQuery<Object[]> query = session.createNativeQuery(sql, Object[].class)
                    .setParameter("id_caja", id_caja);
            resultado = query.getResultList();
                
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
        return resultado;
    }
}
