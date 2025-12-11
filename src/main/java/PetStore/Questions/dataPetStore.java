package PetStore.Questions;


import net.serenitybdd.screenplay.Question;

public class dataPetStore {

    public static Question<Boolean> QConfirmaDataExiste;
    public static Question<Boolean> QdataPetStore(Boolean existenDatos){
        QConfirmaDataExiste = Question.about("Se obtuvo el archivo de datos para ejecutar las pruebas ? :").answeredBy(actor -> existenDatos);

        return QConfirmaDataExiste;
    }

}
