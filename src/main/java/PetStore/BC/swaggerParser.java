package PetStore.BC;


import PetStore.Utils.InputFiles;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.RequestBody;
import io.swagger.v3.parser.core.models.SwaggerParseResult;

import java.util.Map;


public class swaggerParser {

    InputFiles inputfiles = new InputFiles();

    public void parseOfOas(){

        SwaggerParseResult oasFile = this.inputfiles.getOasFile();
        OpenAPI openAPI = oasFile.getOpenAPI();

        //se recorren los endpoints
        for (Map.Entry<String, PathItem> pathEntry : openAPI.getPaths().entrySet()) {
            String pathName = pathEntry.getKey();
            PathItem pathItem = pathEntry.getValue();

            //se recorren las operaciones de cada uno de los endpoints
            for (Map.Entry<PathItem.HttpMethod, Operation> opEntry : pathItem.readOperationsMap().entrySet()) {
                PathItem.HttpMethod method = opEntry.getKey();
                Operation operation = opEntry.getValue();

                System.out.println("==========================================");
                System.out.println("RUTA: " + method + " " + pathName);


                // FILTRO: Solo nos interesan métodos que suelen tener Body
                if (method == PathItem.HttpMethod.POST || method == PathItem.HttpMethod.PUT || method == PathItem.HttpMethod.PATCH) {
                  //  extractBodyAndParams(operation, openAPI);
                }
            }
        }

    }




    private static void printJsonExample(MediaType jsonContent) {

        // El ejemplo puede estar directamente en el campo 'example' del MediaType
        if (jsonContent.getExample() != null) {
            System.out.println("   JSON Ejemplo (desde MediaType):");
            System.out.println("   " + jsonContent.getExample());
            return;
        }

        // O puede estar en el mapa 'examples' del MediaType (para múltiples ejemplos)
        if (jsonContent.getExamples() != null && !jsonContent.getExamples().isEmpty()) {
            // Tomamos el primer ejemplo disponible
            Map.Entry<String, io.swagger.v3.oas.models.examples.Example> firstExample =
                    jsonContent.getExamples().entrySet().iterator().next();

            // El 'value' del ejemplo es el JSON que buscamos
            if (firstExample.getValue().getValue() != null) {
                System.out.println("   JSON Ejemplo (desde Examples/'" + firstExample.getKey() + "'):");
                System.out.println("   " + firstExample.getValue().getValue());
                return;
            }
        }

        // El ejemplo también puede estar definido dentro del Schema
        if (jsonContent.getSchema() != null && jsonContent.getSchema().getExample() != null) {
            System.out.println("   JSON Ejemplo (desde Schema):");
            System.out.println("   " + jsonContent.getSchema().getExample());
            return;
        }

        System.out.println("   (No se encontró un ejemplo de JSON explícito en la especificación.)");
    }



}
