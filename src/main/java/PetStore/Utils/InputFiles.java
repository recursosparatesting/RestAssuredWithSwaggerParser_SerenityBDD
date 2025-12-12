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

import static PetStore.Utils.PathsAndConstants.*;

public class InputFiles {


    public String getDataFile(){
        boolean result = false;
        String urlData = System.getProperty(inputDataVariable);
        return  urlData + "/export?format=xlsx";
    }

    public boolean validDataFile(){
        boolean result = false;
        String urlString = getDataFile();
        String nombreArchivoLocal = pathDataFile + File.separator + fileDataFileName;

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
                result=true;
            } else {
                System.out.println("Error al descargar el archivo: " + responseCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
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
