package stepdefinitions;

import PetStore.BC.swaggerParser;
import PetStore.Utils.InputFiles;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.java.Before;
import net.serenitybdd.annotations.Steps;
import net.serenitybdd.screenplay.actors.OnStage;
import net.serenitybdd.screenplay.actors.OnlineCast;


import static PetStore.Questions.dataPetStore.QdataPetStore;
import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import static net.serenitybdd.screenplay.actors.OnStage.theActorCalled;
import static net.serenitybdd.screenplay.actors.OnStage.theActorInTheSpotlight;

public class SwaggerPetStepdefs {

    InputFiles getInputFiles = new InputFiles();
    swaggerParser mySwaggerParser = new swaggerParser();

    @Steps
    PetApiActions petapiActions;

    @Before
    public void Inicio(){
        OnStage.setTheStage(new OnlineCast());
        theActorCalled("Admin");
    }

    @Given("que se tiene acceso a la especificacion Swagger y los datos del caso")
    public void queSeTieneAccesoALaEspecificacionSwagger() {
        theActorInTheSpotlight().should(seeThat(QdataPetStore(getInputFiles.validDataFile())));
        theActorInTheSpotlight().should(seeThat(QdataPetStore(getInputFiles.validOasFile())));

    }

    @When("Se envian los parametros requeridos")
    public void seEnvianLosParametrosRequeridos() {

        petapiActions.executeTestCases(null);
    }



    @Then("se obtiene un codigo de respuesta y los datos de la respuesta")
    public void seObtieneUnCodigoDeEstado() {
        System.out.println("solucion ....");
    }


}
