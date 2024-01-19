package SIMulator.ClassesQuestions;

import SIMulator.Quiz;
import SIMulator.Types;
import SIMulator.Utils.Json;
import org.json.JSONObject;

public class QuestionChoixReponse extends Question{

    //choix de reponse
    private String choixReponse;

    public QuestionChoixReponse(Types type, String chap, Quiz quiz) {
        super(type, chap, quiz);
    }

    public QuestionChoixReponse(Types type, JSONObject jsonObject, Quiz quiz){
        super(type,jsonObject,quiz);
    }

    @Override
    protected void creerQuestion(boolean choisir) {
        super.creerQuestion(choisir);
        JSONObject JSONquestion = Json.getJSONquestion();
        this.choixReponse = JSONquestion.getString("ChoixReponse");
    }

    @Override
    protected String creerTexte() {
        String str = super.creerTexte();
        str += "ChoixReponse = " + choixReponse + "\n";
        return str;
    }
}
