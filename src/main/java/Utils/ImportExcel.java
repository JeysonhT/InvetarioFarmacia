/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Utils;

import Configurations.HibernateUtil;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import models.ViewModels.VistaInventario;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.hibernate.type.StandardBasicTypes;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;

/**
 *
 * @author jason
 */
public class ImportExcel {

    public ImportExcel() {

    }

    public List<Object[]> leerExcel(File excelFile) throws FileNotFoundException, IOException {

        List<Object[]> datos = new ArrayList<>();

        try (FileInputStream fis = new FileInputStream(excelFile); XSSFWorkbook workbook = new XSSFWorkbook(fis)) {
            XSSFSheet sheet = workbook.getSheetAt(0); // Primer hoja del Excel
            XSSFRow headerRow = sheet.getRow(0); //la primera fila tiene los nombres de columnas

            // para la correcta lectura de celdas de tipo formula, se crea un objeto que evalua las formulas y las calcula
            FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
            // Obtener los nombres de columnas desde la primera fila
            List<String> columnas = new ArrayList<>();
            for (int j = 0; j < headerRow.getLastCellNum(); j++) {
                XSSFCell cell = headerRow.getCell(j);
                if (cell != null) {
                    columnas.add(cell.getStringCellValue());
                }
            }

            // Procesar las filas restantes
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                XSSFRow row = sheet.getRow(i);
                if (row != null) {
                    Object[] valores = new Object[columnas.size()];

                    for (int j = 0; j < columnas.size(); j++) {
                        XSSFCell cell = row.getCell(j);

                        // Manejar celdas vacías
                        if (cell == null) {
                            valores[j] = null;
                            System.out.println(i + ": la celda esta vacia " + j);
                        } else {
                            switch (cell.getCellType()) {
                                case STRING:
                                    valores[j] = cell.getStringCellValue();
                                    break;
                                case NUMERIC:
                                    if (DateUtil.isCellDateFormatted(cell)) {
                                        // Si la celda es de tipo fecha, manejarla como tal
                                        valores[j] = cell.getDateCellValue();
                                    } else {
                                        // Si es numérico, verificar si es un valor double o entero
                                        valores[j] = cell.getNumericCellValue();
                                        System.out.println("El valor es un numero" + j);
                                    }

                                    break;
                                case BOOLEAN:
                                    valores[j] = cell.getBooleanCellValue();
                                    break;
                                case FORMULA:
                                    CellValue value = evaluator.evaluate(cell);
                                    double result = value.getNumberValue();
                                    valores[j] = result;
                                    System.out.println("El valor es una formula: " + j);
                                    break;
                                default:
                                    valores[j] = null;
                                    System.out.println(i + ": No se pudo obtener el valor " + j);
                            }
                        }
                    }
                    datos.add(valores);
                }
            }
        }
        return datos;
    }

    public void crearExcel() throws FileNotFoundException, IOException {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // Consulta SQL (ajusta según tu entidad y mapeo)
            String sql = "CALL ObtenerInventario()";

            NativeQuery<VistaInventario> query = session.createNativeQuery(sql, VistaInventario.class)
                    .addScalar("id_producto", StandardBasicTypes.INTEGER)
                    .addScalar("nombre_producto", StandardBasicTypes.STRING)
                    .addScalar("indicaciones", StandardBasicTypes.STRING)
                    .addScalar("laboratorio", StandardBasicTypes.STRING)
                    .addScalar("codigo_lote", StandardBasicTypes.STRING)
                    .addScalar("fecha_vencimiento", StandardBasicTypes.DATE)
                    .addScalar("categoria", StandardBasicTypes.STRING)
                    .addScalar("presentacion", StandardBasicTypes.STRING)
                    .addScalar("precio", StandardBasicTypes.DOUBLE)
                    .addScalar("cantidad", StandardBasicTypes.INTEGER)
                    .addScalar("precio_costo", StandardBasicTypes.DOUBLE)
                    .addScalar("costo_total", StandardBasicTypes.DOUBLE)
                    .addScalar("utilidad", StandardBasicTypes.DOUBLE)
                    .addScalar("unidad_mayor", StandardBasicTypes.STRING)
                    .addScalar("conversion", StandardBasicTypes.INTEGER)
                    .addScalar("precio_mayor", StandardBasicTypes.DOUBLE);

            List<VistaInventario> entities = query.getResultList();

            // Crear el libro de trabajo y la hoja
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Datos del inventario");

            // Crear encabezados (ajusta según las propiedades de MiEntidad)
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("Id");
            headerRow.createCell(1).setCellValue("Nombre");
            headerRow.createCell(2).setCellValue("Indicaciones");
            headerRow.createCell(3).setCellValue("Marca");
            headerRow.createCell(4).setCellValue("Lote");
            headerRow.createCell(5).setCellValue("Categoria");
            headerRow.createCell(6).setCellValue("Presentación");
            headerRow.createCell(7).setCellValue("Precio");
            headerRow.createCell(8).setCellValue("Cantidad");
            headerRow.createCell(9).setCellValue("Precio Costo");
            headerRow.createCell(10).setCellValue("Total");
            headerRow.createCell(11).setCellValue("Utilidad");
            headerRow.createCell(12).setCellValue("Unidad Mayor");
            headerRow.createCell(13).setCellValue("Precio mayor");
            headerRow.createCell(14).setCellValue("Conversion");
            headerRow.createCell(15).setCellValue("Fecha Vencimiento");

            // ...
            // Llenar las filas
            int rowNum = 1;
            for (VistaInventario entity : entities) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(entity.getId_producto());
                row.createCell(1).setCellValue(entity.getNombre_Producto());
                row.createCell(2).setCellValue(entity.getIndicaciones());
                row.createCell(3).setCellValue(entity.getLaboratorio());
                row.createCell(4).setCellValue(entity.getCodigo_lote());
                row.createCell(5).setCellValue(entity.getCategoria());
                row.createCell(6).setCellValue(entity.getPresentacion());
                row.createCell(7).setCellValue(entity.getPrecio());
                row.createCell(8).setCellValue(entity.getCantidad());
                row.createCell(9).setCellValue(entity.getPrecio_costo());
                row.createCell(10).setCellValue(entity.getCosto_total());
                row.createCell(11).setCellValue(entity.getUtilidad());
                row.createCell(12).setCellValue(entity.getUnidad_mayor());
                row.createCell(13).setCellValue(entity.getPrecio_mayor());
                row.createCell(14).setCellValue(entity.getConversion());
                row.createCell(15).setCellValue(entity.getFecha_Vencimiento().toString());

                // ...
            }

            // Ajustar el ancho de las columnas automáticamente
            for (int i = 0; i <= 15; i++) {
                sheet.autoSizeColumn(i);
            }

            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setColor(IndexedColors.BLUE.getIndex()); // Cambia el color según tu preferencia
            headerStyle.setFont(headerFont);
            headerStyle.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            headerStyle.setBorderBottom(BorderStyle.THIN);
            headerStyle.setBorderTop(BorderStyle.THIN);
            headerStyle.setBorderLeft(BorderStyle.THIN);
            headerStyle.setBorderRight(BorderStyle.THIN);

            // Aplicar el estilo a los encabezados
            Row headerRow2 = sheet.getRow(0);
            for (Cell cell : headerRow2) {
                cell.setCellStyle(headerStyle);
            }

            // Crea un estilo de borde
            CellStyle style = workbook.createCellStyle();
            style.setBorderBottom(BorderStyle.THIN);
            style.setBorderTop(BorderStyle.THIN);
            style.setBorderLeft(BorderStyle.THIN);
            style.setBorderRight(BorderStyle.THIN);

            // Aplica el estilo a todas las celdas del rango
            for (int row = sheet.getFirstRowNum() + 1; row <= sheet.getLastRowNum(); row++) {
                Row currentRow = sheet.getRow(row);
                for (int col = 0; col <= 15; col++) {
                    Cell cell = currentRow.getCell(col);
                    cell.setCellStyle(style);
                }
            }

            // Escribir el archivo
            String path = System.getProperty("user.home");
            String fecha = LocalDate.now().toString();
            FileOutputStream outputStream = new FileOutputStream(path + "\\Documents\\" + "_InventarioActual_" + fecha + ".xlsx");
            workbook.write(outputStream);
            workbook.close();
            outputStream.close();

            // ... (guardar el archivo)
        }
    }
}
