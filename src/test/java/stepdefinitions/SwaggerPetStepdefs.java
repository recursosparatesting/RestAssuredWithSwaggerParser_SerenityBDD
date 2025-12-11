package stepdefinitions;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.java.Before;
import net.serenitybdd.screenplay.actors.OnStage;
import net.serenitybdd.screenplay.actors.OnlineCast;


import static PetStore.Questions.dataPetStore.QdataPetStore;
import static PetStore.Utils.getInputFiles.getDataFile;
import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import static net.serenitybdd.screenplay.actors.OnStage.theActorCalled;
import static net.serenitybdd.screenplay.actors.OnStage.theActorInTheSpotlight;

public class SwaggerPetStepdefs {

    @Before
    public void Inicio(){
        OnStage.setTheStage(new OnlineCast());
        theActorCalled("Admin");
    }

    @Given("que se tiene acceso a la especificacion Swagger y los datos del caso")
    public void queSeTieneAccesoALaEspecificacionSwagger() {
        theActorInTheSpotlight().should(seeThat(QdataPetStore(getDataFile())));
    }


    @When("Se envian los parametros requeridos")
    public void seEnvianLosParametrosRequeridos() {
        System.out.println("solucion ....");
    }

    @Then("se obtiene un codigo de respuesta")
    public void seObtieneUnCodigoDeEstado() {
        System.out.println("solucion ....");
    }

    @And("se obtienen los datos de las mascotas")
    public void seObtienenDatosMascotas() {
        System.out.println("solucion ....");
    }

}
