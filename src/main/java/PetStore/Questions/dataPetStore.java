package PetStore.Questions;


import net.serenitybdd.screenplay.Question;

public class dataPetStore {

    public static Question<Boolean> QConfirmaFile;
    public static Question<Boolean> QdataPetStore(Boolean estadoArchivo){
        QConfirmaFile = Question.about("Se obtuvo el archivo correcto ? :").answeredBy(actor -> estadoArchivo);

        return QConfirmaFile;
    }

}
