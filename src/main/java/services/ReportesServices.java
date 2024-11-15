/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package services;

import Configurations.HibernateUtil;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import javax.swing.JOptionPane;
import models.Productos;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;
import org.hibernate.Session;

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

    public void CrearFactura(Map<String, Object> parametros, List<Productos> productos, String path) throws JRException {
        try {

            InputStream reportStream = getClass().getResourceAsStream(path);

            JasperReport factura = JasperCompileManager.compileReport(reportStream);

            JRBeanCollectionDataSource dataSrc = new JRBeanCollectionDataSource(productos);

            JasperPrint mostrarFactura = JasperFillManager.fillReport(factura, parametros,
                    dataSrc);

            JasperViewer.viewReport(mostrarFactura, false);

        } catch (JRException e) {
            throw new JRException("No se encuentra la ruta del archivo:" + e.getMessage());
        }
    }

}
