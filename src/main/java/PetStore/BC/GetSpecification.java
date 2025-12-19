package PetStore.BC;

import PetStore.Utils.InputFiles;
import io.restassured.response.Response;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.RequestBody;
import io.swagger.v3.parser.core.models.SwaggerParseResult;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.security.SecurityRequirement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static PetStore.BC.BuildPetition.getPetition;
import static PetStore.BC.ExtractResponse.getResponseSchema;

public class GetSpecification {

    public void prueba(String metodo, String endPoint){

        InputFiles getInputFiles = new InputFiles();
        String pathName   ="";
        PathItem pathItem =null;
        List<Parameter>  listParametersQuery = new ArrayList<>();
        List<Parameter>  listParametersHeader = new ArrayList<>();
        List<String> listSecurityReq    = new ArrayList<>();
        Map<String, String> queryParam = new HashMap<>();
        Response response = null;

        SwaggerParseResult oasFile = getInputFiles.getOasFile();
        OpenAPI openAPI = oasFile.getOpenAPI();

        for (Map.Entry<String, PathItem> pathEntry : openAPI.getPaths().entrySet()) {
            pathName   = pathEntry.getKey();
            pathItem = pathEntry.getValue();

            if(pathName.equals(endPoint)){
                for (Map.Entry<PathItem.HttpMethod, Operation> opEntry : pathItem.readOperationsMap().entrySet()) {
                    PathItem.HttpMethod method = opEntry.getKey();
                    Operation operation = opEntry.getValue();
                    if(method.toString().toLowerCase().equals(metodo.toLowerCase())){
                            System.out.println("========== ENCONTRADO ==========");
                            listParametersQuery  = getParametersByIn(operation, "query");
                            listParametersHeader = getParametersByIn(operation, "header");
                            listSecurityReq      = getSecurityRequirements(operation, openAPI);

                            if(listParametersQuery.size()>0){
                                for(Parameter param : listParametersQuery){
                                    if(param.getName().toLowerCase().equals("status")){
                                        queryParam.put("status", "available");
                                    }
                                }
                            }
                           response = getPetition(pathName, queryParam);
                        Schema<?> squema= getResponseSchema( openAPI,  pathName, method);
                    }
                }
            }
        }




    }

    /**
     * Extrae los parámetros filtrados por su tipo (query, path, header).
     * @param operation La operación obtenida de getGetOperationByPath
     * @param in "query", "path", o "header"
     */
    public List<Parameter> getParametersByIn(Operation operation, String in) {
        if (operation.getParameters() == null) return new ArrayList<>();

        return operation.getParameters().stream()
                .filter(p -> p.getIn().equalsIgnoreCase(in))
                .collect(Collectors.toList());
    }

    /**
     * Extrae los esquemas de seguridad (Autenticación) requeridos para la operación.
     * Retorna los nombres de los esquemas (ej: "api_key", "petstore_auth").
     */
    public List<String> getSecurityRequirements(Operation operation,OpenAPI openAPI) {
        List<SecurityRequirement> security = operation.getSecurity();

        // Si la operación no tiene seguridad específica, se usa la global del API
        if (security == null || security.isEmpty()) {
            security = openAPI.getSecurity();
        }

        if (security == null) return new ArrayList<>();

        return security.stream()
                .flatMap(s -> s.keySet().stream())
                .collect(Collectors.toList());
    }
}
