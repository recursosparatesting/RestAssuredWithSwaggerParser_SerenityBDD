package PetStore.Utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExcelReader {

    /**
     * Lee los datos de la hoja de cálculo que se descargo al inicio y que fue dada en los datos de entrada
     * Estos datos se convierten en una lista de mapas.
     * Cada mapa representa una fila, donde la clave es el encabezado de la columna y el valor es el contenido de la celda.
     *
     * @param filePath Ruta al archivo .xlsx.
     * @param sheetName Nombre de la hoja de cálculo a leer.
     * @return Una lista de mapas, donde cada mapa es una fila de datos.
     * @throws IOException Si ocurre un error al leer el archivo.
     */
    public List<Map<String, String>> getData(String filePath, String sheetName) throws IOException {
        // Usamos try-with-resources para asegurar que los flujos se cierren automáticamente
        try (FileInputStream fileInputStream = new FileInputStream(new File(filePath));
             Workbook workbook = new XSSFWorkbook(fileInputStream)) {

            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                throw new IllegalArgumentException("No se encontró la hoja: '" + sheetName + "' en el archivo.");
            }

            List<Map<String, String>> data = new ArrayList<>();
            Row headerRow = sheet.getRow(0);
            if (headerRow == null) {
                throw new IllegalArgumentException("La hoja '" + sheetName + "' está vacía o no tiene una fila de encabezado.");
            }

            // Iteramos sobre las filas, comenzando desde la segunda (índice 1)
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row currentRow = sheet.getRow(i);
                if (currentRow == null) continue; // Omitir filas vacías

                Map<String, String> rowMap = new HashMap<>();
                // Iteramos sobre las celdas de la fila actual
                for (int j = 0; j < headerRow.getLastCellNum(); j++) {
                    Cell currentCell = currentRow.getCell(j, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                    String header = getCellValueAsString(headerRow.getCell(j));
                    String cellValue = getCellValueAsString(currentCell);
                    rowMap.put(header, cellValue);
                }
                data.add(rowMap);
            }
            return data;
        }
    }

    /**
     * Convierte el valor de una celda a String, manejando diferentes tipos de celda.
     *
     * @param cell La celda a convertir.
     * @return El valor de la celda como un String.
     */
    private String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return "";
        }
        DataFormatter formatter = new DataFormatter();
        return formatter.formatCellValue(cell);
    }
}