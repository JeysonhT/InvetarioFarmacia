/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package services;

import Configurations.HibernateUtil;
import java.util.ArrayList;
import java.util.List;
import models.Lotes;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.NativeQuery;

/**
 *
 * @author jason
 */
public class LoteServices {

    public LoteServices() {

    }

    public String guardarLote(Lotes lote) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction;
        try {
            transaction = session.beginTransaction();
            session.persist(lote);
            transaction.commit();
        } catch (Exception e) {
            throw new RuntimeException("error al guardar el lote");
        } finally {
            session.close();
        }

        return "Lote Guardado";
    }

    public List<Lotes> obtenerLotes() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        String sql = "SELECT * FROM Lotes";
        List<Lotes> resultado = new ArrayList<>();
        try{
            NativeQuery<Lotes> query = session.createNativeQuery(sql, Lotes.class);
            
            resultado = query.getResultList();
        } catch(Exception e){
            throw new RuntimeException("No hay lotes registrados");
        }
        return resultado;
    }
}
