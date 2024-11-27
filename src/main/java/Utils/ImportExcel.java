/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author jason
 */
public class ImportExcel {

    public ImportExcel() {
        
    }
    
    public String leerExcel(File excelFile, String nombreTabla){
        
        String tabla = nombreTabla;
        
        // se obtiene el archivo con FileInputStream, Luego se especifica que es un excel con workbook de Apache POI
        
        try(FileInputStream fis = new FileInputStream(excelFile); 
                Workbook workbook = new XSSFWorkbook(fis)){
            
            Sheet sheet = workbook.getSheetAt(0); // Primera hoja del Excel
            
            Row headerRow = sheet.getRow(0); // Supongamos que la primera fila tiene los nombres de columnas

            // Obtener los nombres de columnas desde la primera fila
            List<String> columnas = new ArrayList<>();
            for (int j = 0; j < headerRow.getLastCellNum(); j++) {
                Cell cell = headerRow.getCell(j);
                if (cell != null) {
                    columnas.add(cell.getStringCellValue());
                }
            }

            // Procesar las filas restantes
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row != null) {
                    StringBuilder valores = new StringBuilder("INSERT INTO " + nombreTabla + " (");
                    StringBuilder placeholders = new StringBuilder(") VALUES (");

                    for (int j = 0; j < columnas.size(); j++) {
                        Cell cell = row.getCell(j);

                        // Manejar celdas vacías
                        if (cell == null) {
                            placeholders.append("NULL");
                        } else {
                            switch (cell.getCellType()) {
                                case STRING -> placeholders.append("'").append(cell.getStringCellValue().replace("'", "''")).append("'");
                                case NUMERIC -> placeholders.append(cell.getNumericCellValue());
                                case BOOLEAN -> placeholders.append(cell.getBooleanCellValue());
                                default -> placeholders.append("NULL");
                            }
                        }
                        if (j < columnas.size() - 1) {
                            placeholders.append(", ");
                        }
                    }

                    valores.append(String.join(", ", columnas)).append(placeholders).append(");");
                    
                    System.out.println(valores.toString());
                }
                
                
            }
            
            
            
        } catch(IOException e){
            
        }
        
        return "";
    }
}
