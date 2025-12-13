package stepdefinitions;


import PetStore.Utils.DataInputValidator;
import net.serenitybdd.annotations.Step;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PetApiActions {
    DataInputValidator dataInputValidator = new DataInputValidator();

    @Step("Ejecuta el caso de prueba para la url y metodo indicados en los datos de entrada")
    public void executeTestCases(Map<String, Object> petData){
        List<Map<String, Object>> dataInput = dataInputValidator.dataInput();

        for(Map<String, Object> mapa: dataInput){
            for(Map.Entry<String, Object>  data: mapa.entrySet() ){
                String fieldName = data.getKey();
                Object fieldValue = data.getValue();
                System.out.println("Nombre de campo: "+fieldName);
                System.out.println("Valor de campo: "+fieldValue.toString());
            }
        }

        System.out.println("PetApiActions");
    }


}
