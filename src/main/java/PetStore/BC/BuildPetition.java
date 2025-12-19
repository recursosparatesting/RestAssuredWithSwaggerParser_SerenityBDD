package PetStore.BC;

import io.restassured.response.Response;
import net.serenitybdd.rest.SerenityRest;

import java.util.Map;

public class BuildPetition {

    public static Response getPetition(String basePath, Map<String, String> queryParam) {
        Response getpet = null;

        try {

            getpet= SerenityRest.given()
                    .basePath(basePath)
                    .queryParams(queryParam)
                    .when().get();

        } catch (Exception e) {
                System.out.println("Error al realizar la peticion de tipo Get " + e.getMessage());
        }
        return getpet;
    }
}
