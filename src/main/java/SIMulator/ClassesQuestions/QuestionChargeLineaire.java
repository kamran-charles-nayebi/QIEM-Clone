package SIMulator.ClassesQuestions;

import SIMulator.Quiz;
import SIMulator.Types;
import SIMulator.Utils.Json;
import org.json.JSONObject;

public class QuestionChargeLineaire extends Question{

    //charges linéaires
    private String posPartA;
    private String posPartB;
    private String chPartA;
    private String chPartB;
    private String chPartU;

    public QuestionChargeLineaire(Types type, String chap, Quiz quiz) {
        super(type, chap, quiz);
    }

    public QuestionChargeLineaire(Types type, JSONObject jsonObject, Quiz quiz){
        super(type,jsonObject,quiz);
    }

    @Override
    protected void creerQuestion(boolean choisir) {
        super.creerQuestion(choisir);
        JSONObject JSONquestion = Json.getJSONquestion();
        this.posPartA = JSONquestion.getString("PosPartA");
        this.posPartB = JSONquestion.getString("PosPartB");
        this.chPartA = JSONquestion.getString("ChPartA");
        this.chPartB = JSONquestion.getString("ChPartB");
        this.chPartU = JSONquestion.getString("ChPartU");
    }

    @Override
    protected String creerTexte() {
        String str = super.creerTexte();
        str += "PositionParticuleA = " + posPartA + "\n";
        str += "PositionParticuleB = " + posPartB + "\n";
        str += "ChargeParticuleA = " + chPartA + "\n";
        str += "ChargeParticuleB = " + chPartB + "\n";
        str += "ChargeParticuleUtilisateur = " + chPartU + "\n";
        return str;
    }
}
