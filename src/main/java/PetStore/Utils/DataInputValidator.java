package PetStore.Utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

import static PetStore.Utils.PathsAndConstants.fileDataFileName;
import static PetStore.Utils.PathsAndConstants.pathDataFile;

public class DataInputValidator {

    public List<Map<String, Object>> dataInput(){
        List<Map<String, Object>> data = new ArrayList<>();
        Map<String, Object> rowMap = new HashMap<>();
        String numCaso="";
        Sheet sheet = null;
        try{
            FileInputStream fileInputStream = new FileInputStream(pathDataFile + File.separator + fileDataFileName);
            Workbook workbook = new XSSFWorkbook(fileInputStream);

            int numerSheets = workbook.getNumberOfSheets();

            for(int i=0; i<numerSheets; i++){
                sheet = workbook.getSheetAt(i);
                if (sheet != null) {
                    Row headerRow = sheet.getRow(1);
                    if (headerRow != null) {
                        numCaso = headerRow.getCell(0).getStringCellValue();
                        if(numCaso!=null &&  numCaso.isEmpty() ){
                            break;
                        }
                    }
                }
            }


            if((sheet != null)) {
                assert numCaso != null;
                if (!numCaso.isEmpty()) {
                    Row headerRow = sheet.getRow(0);

                    for (Row currentRow : sheet) {
                        Iterator<Cell> cellIterator = currentRow.cellIterator();
                        if (currentRow.equals(headerRow)) {
                            continue;
                        }

                        int j = 0;
                        while (cellIterator.hasNext()) {
                            Cell currentCell = cellIterator.next();
                            Object cellObject = getCellValue(currentCell);
                            if (cellObject != null && !cellObject.toString().isEmpty()) {

                                String header = getCellValue(headerRow.getCell(j));
                                String cellValue = getCellValue(currentCell);
                                rowMap.put(header, cellValue);
                                j++;

                            } else {
                                break;
                            }

                        }
                        if (!rowMap.isEmpty()) {
                            data.add(rowMap);
                            rowMap = new HashMap<>();
                        }

                    }
                }
            }


        } catch (Exception e) {
                System.out.println("Error al extraer los datos del archivo excel con los datos para ejecutar el caso " + e.getMessage());
        }
        return data;

    }


    public  <T> T getCellValue(Cell celda){

        if(celda == null) {
            return (T)"";
        }


        switch (celda.getCellType().name()) {
            case "STRING":
                String stringCellValue = celda.getStringCellValue();
                stringCellValue = stringCellValue==null?"":stringCellValue;
                return  (T) celda.getStringCellValue();
            case "NUMERIC":
                if(DateUtil.isCellDateFormatted(celda)) {
                    return (T) celda.getDateCellValue();
                }else {
                    double numeroDoble = celda.getNumericCellValue() ;
                    int numEntero = new BigDecimal(numeroDoble).setScale(0, RoundingMode.HALF_UP).intValue();
                    String numericCellValue = String.valueOf(numEntero);
                    return  (T) numericCellValue;
                }
            case "DATE":
                return  (T) celda.getDateCellValue();
            case "FORMULA":
                return (T) celda.getStringCellValue();
            case "BLANK":
                return  (T) "";

            default:
                System.exit(0);
                break;
        }

        return (T) "";
    }

}
