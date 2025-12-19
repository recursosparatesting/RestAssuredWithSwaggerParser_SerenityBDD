package PetStore.Utils;

import io.swagger.models.HttpMethod;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.parser.OpenAPIV3Parser;

public class SwaggerSchemaExtractor {

    /**
     * Extracts the response schema for a given path and HTTP method from the OpenAPI specification.
     *
     * @param httpMethod The HTTP method (e.g., "GET", "POST").
     * @return The response Schema object, or null if not found.
     */
    public static Schema<?> getSchemaFromResponse(PathItem pathItem, PathItem.HttpMethod httpMethod) {
        if (pathItem == null) {
            return null;
        }

        Operation operation = pathItem.readOperationsMap().get(httpMethod);
        if (operation == null) {
            return null;
        }

        // Assuming we are interested in the '200 OK' response
        ApiResponse apiResponse = operation.getResponses().get("200");
        if (apiResponse == null || apiResponse.getContent() == null) {
            return null;
        }

        // Assuming the response is JSON
        return apiResponse.getContent().get("application/json").getSchema();
    }
}
