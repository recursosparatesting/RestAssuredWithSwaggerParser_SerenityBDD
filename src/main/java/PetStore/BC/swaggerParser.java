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
                    extractBodyAndParams(operation, openAPI);
                }
            }
        }

    }

    private static void extractBodyAndParams(Operation operation, OpenAPI openAPI) {
        RequestBody requestBody = operation.getRequestBody();

        if (requestBody == null) {
            System.out.println("   (Este metodo No tiene Request Body definido)");
            return;
        }

        if (requestBody.getContent() == null) return;

        // Generalmente nos interesa JSON
        MediaType jsonContent = requestBody.getContent().get("application/json");

        // Si no hay JSON, probamos con form-data o xml (opcional)
        if (jsonContent == null && !requestBody.getContent().isEmpty()) {
            // Tomamos el primero que haya
            jsonContent = requestBody.getContent().values().iterator().next();
        }

        if (jsonContent != null) {
            System.out.println("   [BODY ENCONTRADO]");

            // 1. Buscamos el ejemplo en el MediaType
            printJsonExample(jsonContent);

            // 2. Imprimimos la estructura y los campos (tu lógica anterior)
            Schema<?> schema = jsonContent.getSchema();
            inspectSchema(schema, openAPI, 1);
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

    // Metodo recursivo para extraer propiedades (parámetros del body)
    private static void inspectSchema(Schema<?> schema, OpenAPI openAPI, int level) {
        String indent = "   " + "  ".repeat(level);

        // CASO 1: Es una referencia ($ref) -> Hay que buscarla en Components
        if (schema.get$ref() != null) {
            String refName = schema.get$ref().substring(schema.get$ref().lastIndexOf('/') + 1);
            System.out.println(indent + "-> Refiere al modelo: '" + refName + "'");

            // Buscamos el esquema real en los componentes
            Schema<?> actualSchema = openAPI.getComponents().getSchemas().get(refName);
            if (actualSchema != null) {
                inspectSchema(actualSchema, openAPI, level);
            }
        }
        // CASO 2: Tiene propiedades directas (Es un objeto)
        else if (schema.getProperties() != null) {
            System.out.println(indent + "-> Propiedades (Campos):");
            for (Map.Entry<String, Schema> prop : schema.getProperties().entrySet()) {
                String fieldName = prop.getKey();
                String fieldType = prop.getValue().getType();
                System.out.println(indent + "   - " + fieldName + " (" + fieldType + ")");

                // Si el campo es a su vez otro objeto complejo, profundizamos (opcional)
                if (prop.getValue().get$ref() != null) {
                    inspectSchema(prop.getValue(), openAPI, level + 2);
                }
            }
        }
        // CASO 3: Es un Array
        else if ("array".equals(schema.getType())) {
            System.out.println(indent + "-> Es una Lista (Array) de:");
            inspectSchema(schema.getItems(), openAPI, level + 1);
        }
    }

}
