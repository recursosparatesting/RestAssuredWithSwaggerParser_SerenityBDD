package PetStore.BC;

import PetStore.Utils.SwaggerSchemaExtractor;
import io.restassured.response.Response;
import io.swagger.models.HttpMethod;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.media.Schema;

public class ExtractResponse {

    /**
     * Retrieves the OpenAPI schema for a successful (200 OK) response of a given request.
     *
     * @param path       The endpoint path (e.g., "/pet/{petId}").
     * @param httpMethod The HTTP method for the request.
     * @return The Schema object for the response.
     */
    public static Schema<?> getResponseSchema(PathItem path, PathItem.HttpMethod httpMethod) {
        return SwaggerSchemaExtractor.getSchemaFromResponse(path, httpMethod);
    }

    /**
     * Extracts the status code from the response.
     * @param response The response object from SerenityRest.
     * @return The HTTP status code.
     */
    public static int getStatusCode(Response response) {
        return response.getStatusCode();
    }

    /**
     * Extracts the entire body from the response as a String.
     * @param response The response object from SerenityRest.
     * @return The body of the response as a String.
     */
    public static String getBodyAsString(Response response) {
        return response.getBody().asString();
    }

    /**
    * Extrae un valor específico del body, pero primero verifica si existe en el esquema.
    *
    * @param response  La respuesta de SerenityRest.
    * @param schema    El esquema del Swagger.
    * @param fieldName El nombre del campo que queremos extraer (ej: "id", "name").
    * @return El valor del campo.
    */
    public static Object extractFieldIfInSchema(Response response, Schema<?> schema, String fieldName) {
        // Verificamos si el esquema tiene propiedades y si contiene nuestro campo
        if (schema.getProperties() != null && schema.getProperties().containsKey(fieldName)) {

            // Opcional: Podrías validar el tipo de dato aquí usando schema.getProperties().get(fieldName).getType()

            return response.jsonPath().get(fieldName);
        } else {
            throw new IllegalArgumentException("El campo '" + fieldName + "' no está definido en el esquema del Swagger para esta operación.");
        }
    }
}
