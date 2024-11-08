/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package services;

import Configurations.HibernateUtil;
import java.io.InputStream;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.view.JasperViewer;
import org.hibernate.Session;
import org.hibernate.engine.spi.SessionImplementor;

/**
 *
 * @author jason
 */
public class ReportesServices {

    public ReportesServices() {

    }

    public void productosCantidadBaja() {
        String path = "/Reports/Reporte_Letter.jrxml";
        InputStream reportStream = getClass().getResourceAsStream(path);
        

        Session session = HibernateUtil.getSessionFactory().openSession();

        session.doWork(connection -> {
            try {
                JasperReport jasperR = JasperCompileManager.compileReport(reportStream);
                JasperPrint mostrarReporte = JasperFillManager.fillReport(jasperR, null,
                        connection);
                JasperViewer.viewReport(mostrarReporte, false);
            } catch (JRException e) {
                throw new RuntimeException("error en el reporte: " + e.getMessage());
            }
        }
        );
    }

}
