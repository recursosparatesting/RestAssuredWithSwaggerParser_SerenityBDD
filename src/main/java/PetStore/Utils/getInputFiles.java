package PetStore.Utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;

public class getInputFiles {

    public static boolean getDataFile(){
        boolean result = false;
        String urlData = System.getProperty("url.data");
        String urlString = urlData + "/export?format=xlsx";
        String nombreArchivoLocal = "src/test/resources/datos_petstore.xlsx";

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


}
