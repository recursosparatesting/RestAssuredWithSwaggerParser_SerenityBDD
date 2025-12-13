package PetStore.Utils;

import io.swagger.parser.OpenAPIParser;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.parser.core.models.ParseOptions;
import io.swagger.v3.parser.core.models.SwaggerParseResult;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static PetStore.Utils.PathsAndConstants.*;

public class InputFiles {
    DataInputValidator dataInputValidator = new DataInputValidator();

    public String getDataFile(){
        boolean result = false;
        String urlData = System.getProperty(inputDataVariable);
        return  urlData + "/export?format=xlsx";
    }

    public boolean validDataFile(){
        boolean result = false;
        String urlString = getDataFile();
        String nombreArchivoLocal = pathDataFile + File.separator + fileDataFileName;
        List<Map<String, Object>> dataInput = new ArrayList<>();
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = connection.getInputStream();
                FileOutputStream outputStream = new FileOutputStream(nombreArchivoLocal);
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
                outputStream.close();
                inputStream.close();
                System.out.println("Archivo descargado exitosamente como " + nombreArchivoLocal);
                dataInput= dataInputValidator.dataInput();
                result= !dataInput.isEmpty();

            } else {
                System.out.println("Error al descargar el archivo: " + responseCode);
            }
        } catch (Exception e) {
            System.out.println("Error al validar el archivo excel con los datos para ejecutar el caso: " + e.getMessage());
        }
        return result;
    }

    public SwaggerParseResult getOasFile(){

       String oasFile= System.getProperty(inputOasVariable);
        SwaggerParseResult oasParser  =null;
       try{
           ParseOptions options = new ParseOptions();
           options.setResolve(true);
           options.setResolveFully(true);

           oasParser = new OpenAPIParser().readLocation(oasFile, null, options);

       }
       catch(Exception e){
           System.out.println("Error generando el archivo OpenApi " +  e.getMessage());
       }

       return oasParser;
      }

    public boolean validOasFile(){
        boolean result = false;
        SwaggerParseResult oasSwaggerParser= this.getOasFile();
        OpenAPI openAPI = oasSwaggerParser.getOpenAPI();
        try{
            if (openAPI == null) {
                System.err.println("Error leyendo la especificacion.");
                oasSwaggerParser.getMessages().forEach(System.err::println);
            }else{
                result=true;
            }

            System.out.println("--- API: " + openAPI.getInfo().getTitle() + " ---\n");
        } catch (Exception e) {
            System.out.println("Error validando el archivo OpenApi " +  e.getMessage());
        }

        return result;
    }
}
