/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package services;

import Configurations.HibernateUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import models.Categoria;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;

/**
 *
 * @author jason
 */
public class CategoriaServices {

    public CategoriaServices() {
    }

    public List<Categoria> obtenerCategorias() {
        Session session = HibernateUtil.getSessionFactory().openSession();

        String sql = "CALL ObtenerCategorias()";
        try {
            List<Categoria> categorias;
            NativeQuery<Categoria> query = session.createNativeQuery(sql,
                    Categoria.class);

            categorias = query.getResultList();
            
            return categorias;
        } catch (Exception e) {
            throw new RuntimeException("No se pudo Obtener la categorias de la base de datos " + e.getMessage());
        } finally {
            session.close();
        }
    }
}
