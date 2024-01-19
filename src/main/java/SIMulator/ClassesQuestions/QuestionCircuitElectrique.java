package SIMulator.ClassesQuestions;

import SIMulator.Quiz;
import SIMulator.Types;
import org.json.JSONObject;

import java.util.ArrayList;

public class QuestionCircuitElectrique extends Question{

    private ArrayList<Integer> arrayNbResistances;
    
    /**
     * Constructeur de la questions
     * @param type - le type de question
     * @param chap - chapitre de la question
     * @param quiz - quiz dans lequel la question est posée
     */
    public QuestionCircuitElectrique(Types type, String chap, Quiz quiz) {
        super(type, chap, quiz);
    }

    public QuestionCircuitElectrique(Types type, JSONObject jsonObject, Quiz quiz){
        super(type,jsonObject,quiz);
    }

    @Override
    protected void creerQuestion(boolean choisir) {
        super.creerQuestion(choisir);
        arrayNbResistances = new ArrayList<>();
        for (int i = 0; i < variables.size()-1; i++) {
            arrayNbResistances.add((int)variables.get(i).getArgumentValue());
        }
    }

    @Override
    protected String creerTexte(){
        String str = "";
        str += "ReponseType = " + type.nom + "\n";
        str += "ReponseResistance = " + reponse+ "\n";
        for (int i = 0; i < arrayNbResistances.size(); i++) {
            str += "Resistance" + (i + 1) + " = " + arrayNbResistances.get(i) + "\n";
        }
        return str;
    }

}
