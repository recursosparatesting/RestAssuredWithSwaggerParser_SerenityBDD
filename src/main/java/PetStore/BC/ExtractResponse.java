package PetStore.BC;

import PetStore.Utils.SwaggerSchemaExtractor;
import io.restassured.response.Response;
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
    public static Schema<?> getResponseSchema(OpenAPI openAPI, String path, PathItem.HttpMethod httpMethod) {
        return SwaggerSchemaExtractor.getSchemaFromResponse(openAPI, path, httpMethod);
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

}
