/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package services;

import Configurations.HibernateUtil;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JOptionPane;
import models.Productos;
import models.ViewModels.VentasMes;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;
import org.hibernate.Session;
import services.Caja.CajaServices;
import services.Caja.MovimientoServices;

/**
 *
 * @author jason
 */
public class ReportesServices {

    public ReportesServices() {

    }

    public void productosCantidadBaja() {
        String pathImage = "IconoFarmacia.png";
        Map<String, Object> parametros = new HashMap<>();

        parametros.put("PathImage", pathImage);
        String path = "/Reports/Reporte_Letter.jrxml";
        InputStream reportStream = getClass().getResourceAsStream(path);

        Session session = HibernateUtil.getSessionFactory().openSession();

        session.doWork(connection -> {
            try {
                JasperReport jasperR = JasperCompileManager.compileReport(reportStream);
                JasperPrint mostrarReporte = JasperFillManager.fillReport(jasperR, parametros,
                        connection);
                JasperViewer.viewReport(mostrarReporte, false);
                String RutaArchivo = System.getProperty("user.home");
            System.out.println(RutaArchivo);
            JasperExportManager.exportReportToPdfFile(mostrarReporte, RutaArchivo + "\\Documents\\ReporteProductosCantidad.pdf");
            } catch (JRException e) {
                throw new RuntimeException("error en el reporte: " + e.getMessage());
            }
        }
        );
    }

    public void productosPorVencer(Map<String, Object> parametros) {
        String path = "/Reports/Reporte_Letter_vencimiento.jrxml";

        InputStream reportStream = getClass().getResourceAsStream(path);
        Session session = HibernateUtil.getSessionFactory().openSession();

        session.doWork(connection -> {
            try {
                JasperReport jasperR = JasperCompileManager.compileReport(reportStream);
                JasperPrint mostrarReporte = JasperFillManager.fillReport(jasperR, parametros,
                        connection);
                JasperViewer.viewReport(mostrarReporte, false);
                String RutaArchivo = System.getProperty("user.home");
            System.out.println(RutaArchivo);
            JasperExportManager.exportReportToPdfFile(mostrarReporte, RutaArchivo + "\\Documents\\ReporteProductosPorVencer.pdf");
            } catch (JRException e) {
                throw new RuntimeException("error en el reporte: " + e.getMessage());
            }
        }
        );
    }
    
    public void reporteCreditos(Map<String, Object> parametros){
        String path = "/Reports/ReporteCreditos_Letter.jrxml";

        InputStream reportStream = getClass().getResourceAsStream(path);
        Session session = HibernateUtil.getSessionFactory().openSession();
        
        session.doWork(connection -> {
            try {
                JasperReport jasperR = JasperCompileManager.compileReport(reportStream);
                JasperPrint mostrarReporte = JasperFillManager.fillReport(jasperR, parametros,
                        connection);
                JasperViewer.viewReport(mostrarReporte, false);
                String RutaArchivo = System.getProperty("user.home");
            System.out.println(RutaArchivo);
            JasperExportManager.exportReportToPdfFile(mostrarReporte, RutaArchivo + "\\Documents\\ReporteProductosCreditos.pdf");
            } catch (JRException e) {
                throw new RuntimeException("error en el reporte: " + e.getMessage());
            }
        }
        );
    }

    public void CrearFactura(Map<String, Object> parametros, List<Map<String, Object>> campos, String path) throws JRException {
        try {

            InputStream reportStream = getClass().getResourceAsStream(path);

            String pathImage = "IconoFarmaciaFactura.png";

            parametros.put("PathImage", pathImage);

            JasperReport factura = JasperCompileManager.compileReport(reportStream);

            JRBeanCollectionDataSource dataSrc = new JRBeanCollectionDataSource(campos);

            JasperPrint mostrarFactura = JasperFillManager.fillReport(factura, parametros,
                    dataSrc);

            //JasperViewer.viewReport(mostrarFactura, false);
            JasperPrintManager.printReport(mostrarFactura, false);

        } catch (JRException e) {
            throw new JRException("No se encuentra la ruta del archivo:" + e.getMessage());
        }
    }

    public void ReporteVentasMes(Map<String, Object> parametros, List<VentasMes> ventasMes, String path) throws JRException {
        try {
            InputStream reportStream = getClass().getResourceAsStream(path);

            JasperReport Reporte = JasperCompileManager.compileReport(reportStream);

            JRBeanCollectionDataSource dataSrc = new JRBeanCollectionDataSource(ventasMes);

            JasperPrint mostrarReporte = JasperFillManager.fillReport(Reporte, parametros,
                    dataSrc);

            JasperViewer.viewReport(mostrarReporte, false);
            
            String RutaArchivo = System.getProperty("user.home");
            System.out.println(RutaArchivo);
            JasperExportManager.exportReportToPdfFile(mostrarReporte, RutaArchivo + "\\Documents\\ReporteVentasMes.pdf");
            
        } catch (JRException e) {
            throw new JRException("No se encuentra la ruta del archivo: " + e.getMessage());
        }
    }
    
    
    public void reporteCaja(Long id_caja) {

        try {
            String path = "/Reports/ReporteCaja_Letter.jrxml";
            String pathImage = "IconoFarmacia.png";
            InputStream reportStream = getClass().getResourceAsStream(path);

            if (0 != reportStream.available()) {
                System.out.println("El path esta vacio");
            }

            List<Object[]> valores;
            List<Object[]> campos;
            Map<String, Object> parametros = new HashMap<>();

            valores = new CajaServices().ObtenerResultadoCaja(id_caja);

            if (valores != null) {
                for (int i = 0; i <= 8; i++) {
                    System.out.println(valores.get(0)[i]);
                }
                parametros.put("Id_Caja", Long.valueOf(valores.get(0)[0].toString()));
                parametros.put("Apertura", valores.get(0)[1].toString());
                parametros.put("Cierre", valores.get(0)[2].toString());
                parametros.put("monto_inicial", valores.get(0)[3].toString());
                parametros.put("monto_final", valores.get(0)[4].toString());
                parametros.put("total_ventas", BigDecimal.valueOf(Double.parseDouble(valores.get(0)[5].toString())));
                parametros.put("total_ingresos", BigDecimal.valueOf(Double.parseDouble(valores.get(0)[6].toString())));
                parametros.put("total_retiros", BigDecimal.valueOf(Double.parseDouble(valores.get(0)[7].toString())));
                parametros.put("total_pagos", BigDecimal.valueOf(Double.parseDouble(valores.get(0)[8].toString())));
            } else {
                throw new RuntimeException("No hay valores de la caja actual");
            }

            campos = new MovimientoServices().obtenerMovimientosCaja(id_caja);
            List<Map<String, Object>> datos = new ArrayList<>();
            if (campos != null) {

                for (int i = 0; i < campos.size(); i++) {
                    Map<String, Object> data = new HashMap<>();
                    data.put("fecha", campos.get(i)[0]);
                    data.put("tipoMovimiento", campos.get(i)[1]);
                    data.put("monto", BigDecimal.valueOf(Double.parseDouble(campos.get(i)[2].toString())));
                    data.put("vendedor", campos.get(i)[3]);
                    data.put("id_Turno", Long.valueOf(campos.get(i)[4].toString()));

                    datos.add(data);
                }
            }

            parametros.put("PathImage", pathImage);

            JasperReport Reporte = JasperCompileManager.compileReport(reportStream);

            JRBeanCollectionDataSource dataSrc = new JRBeanCollectionDataSource(datos);

            JasperPrint mostrarReporte = JasperFillManager.fillReport(Reporte, parametros,
                    dataSrc);

            JasperViewer.viewReport(mostrarReporte, false);
            String RutaArchivo = System.getProperty("user.home");
            System.out.println(RutaArchivo);
            JasperExportManager.exportReportToPdfFile(mostrarReporte, RutaArchivo + "\\Documents\\ReporteCaja.pdf");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
            System.out.println(e.getMessage());
        }
    }
    
    public void ReporteMicelaneaMes(String path, List<Map<String, Object>> ventasMes, Map<String, Object> parametros) throws JRException{
        try {
            InputStream reportStream = getClass().getResourceAsStream(path);

            JasperReport Reporte = JasperCompileManager.compileReport(reportStream);

            JRBeanCollectionDataSource dataSrc = new JRBeanCollectionDataSource(ventasMes);

            JasperPrint mostrarReporte = JasperFillManager.fillReport(Reporte, parametros,
                    dataSrc);

            JasperViewer.viewReport(mostrarReporte, false);
            
            String RutaArchivo = System.getProperty("user.home");
            System.out.println(RutaArchivo);
            JasperExportManager.exportReportToPdfFile(mostrarReporte, RutaArchivo + "\\Documents\\ReporteVentasMicelanea.pdf");
            
        } catch (JRException e) {
            throw new JRException("No se encuentra la ruta del archivo: " + e.getMessage());
        }
    }
    
    public void ReportePagosdelMes(String path, List<Map<String, Object>> pagos, Map<String, Object> parametros) throws JRException{
        try {
            InputStream reportStream = getClass().getResourceAsStream(path);

            JasperReport Reporte = JasperCompileManager.compileReport(reportStream);

            JRBeanCollectionDataSource dataSrc = new JRBeanCollectionDataSource(pagos);

            JasperPrint mostrarReporte = JasperFillManager.fillReport(Reporte, parametros,
                    dataSrc);

            JasperViewer.viewReport(mostrarReporte, false);
            
            String RutaArchivo = System.getProperty("user.home");
            System.out.println(RutaArchivo);
            JasperExportManager.exportReportToPdfFile(mostrarReporte, RutaArchivo + "\\Documents\\ReportePagosCreditos.pdf");
            
        } catch (JRException e) {
            throw new JRException("No se encuentra la ruta del archivo: " + e.getMessage());
        }
    }

}
