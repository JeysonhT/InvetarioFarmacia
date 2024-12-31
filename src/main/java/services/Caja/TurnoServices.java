/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package services.Caja;

import Configurations.HibernateUtil;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;
import models.CajaModels.Turno;
import models.Ventas;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author jason
 */
public class TurnoServices {

    public TurnoServices() {

    }

    public String abrirTurno(Turno turno) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;

        String response = "";
        try {
            transaction = session.beginTransaction();

            session.persist(turno);

            transaction.commit();

            response = "Turno abierto a las : " + turno.getInicio().toString().split("\\.")[0];

        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException("No se pudo inicar el turno: " + e.getMessage());
        } finally {
            session.close();
        }
        return response;
    }

    public Turno BuscarTurno(int turnoID) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Turno turno = null;
        try {
            turno = (Turno) session.find(Turno.class, turnoID);

        } catch (Exception e) {
            throw new RuntimeException("No se encontro la entidad: " + e.getMessage());
        } finally {
            session.close();
        }

        return turno;
    }

    public String cerrarTurno(int turnoID, LocalDateTime fin) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Turno turno = session.find(Turno.class, turnoID);
            if (turno != null) {
                System.out.println(turno.getId_turno());
                turno.setFin(fin);
                session.merge(turno);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException("No se pudo actualizar la entidad");
        }
        return "Turno Cerrado";
    }

    public void obtenerVentasTurno(int turnoID) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Turno turno = session.find(Turno.class, turnoID);
            if (turno != null) {
                // Cálculo con double
                double resultado = Optional.ofNullable(turno.getVentas())
                        .orElse(Collections.emptyList()) // Usar una lista vacía si turno.getVentas() es null
                        .stream()
                        .mapToDouble(Ventas::getTotal) // Convertir cada venta a double
                        .sum();
                System.out.println(resultado);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException("No se pudo actualizar la entidad");
        }
    }
}
