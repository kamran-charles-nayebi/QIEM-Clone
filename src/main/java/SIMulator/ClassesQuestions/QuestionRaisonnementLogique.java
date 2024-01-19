package SIMulator.ClassesQuestions;

import SIMulator.ModelesQuestions.DemarcheControlleur;
import SIMulator.ModelesQuestions.Modele1Controller;
import SIMulator.Quiz;
import SIMulator.Reponses.FabriqueurDeReponses;
import SIMulator.Reponses.Reponse;
import SIMulator.Types;
import SIMulator.Utils.Json;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import org.json.JSONObject;
import org.mariuszgromada.math.mxparser.Argument;
import org.mariuszgromada.math.mxparser.Expression;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class QuestionRaisonnementLogique extends Question {

    public QuestionRaisonnementLogique(Types type, String chap, Quiz quiz) {
        super(type, chap, quiz);
    }

    public QuestionRaisonnementLogique(Types type, JSONObject jsonObject, Quiz quiz){
        super(type,jsonObject,quiz);
    }

    @Override
    public Node getRoot() throws IOException {
        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/SIMulator/fxml/Questions/Demarche.fxml"));
        Parent root = loader.load();
        DemarcheControlleur controller = loader.getController();
        controller.setQuestion(this);
        return root;
    }

    @Override
    protected void creerQuestion(boolean choisir) {
        JSONObject JSONquestion = null;
        if (choisir) {
            JSONquestion = Json.choisirQuestion(type);
            Json.setJSONquestion(JSONquestion);
        } else {
            JSONquestion = Json.getJSONquestion();
        }

        this.solution = String.format(Locale.US, JSONquestion.getString("Solution"));
        this.matiere = String.format(Locale.US, JSONquestion.getString("Matiere"));
        this.question = String.format(Locale.US,JSONquestion.getString("Question"));
        this.reponse = String.format(Locale.US,JSONquestion.getString("Reponse"));

        variables = new ArrayList<>();
        ArrayList<String> listOperation = new ArrayList<>();
        if (JSONquestion.has("Variables")) {
            Iterator<String> it = JSONquestion.getJSONObject("Variables").keys();
            while (it.hasNext()) {
                String var = it.next();
                String etendue = JSONquestion.getJSONObject("Variables").getString(var);
                if (!etendue.equals("")) {
                    if (etendue.contains(":")) {
                        variables.add(new Argument(var, ThreadLocalRandom.current().nextDouble(Double.parseDouble(etendue.split(":")[0]), Double.parseDouble(etendue.split(":")[1]))));
                        question = question.replace("{" + var + "}", String.format("%4.1e", variables.get(variables.size() - 1).getArgumentValue()));
                    } else {
                        listOperation.add(var);
                    }
                } else
                    variables.add(new Argument(var, Double.NaN));
            }
            for (int i = 0; i < listOperation.size(); i++) {
                String etendue = JSONquestion.getJSONObject("Variables").getString(listOperation.get(i));
                for (int j = 0; j < variables.size(); j++) {
                    etendue = etendue.replace("{" + variables.get(j).getArgumentName() + "}", String.format("%4.1e", variables.get(j).getArgumentValue()));
                }
                Expression expression = new Expression(etendue);
                variables.add(new Argument(listOperation.get(i), expression.calculate()));
                question = question.replace("{" + listOperation.get(i) + "}", String.format("%4.1e", variables.get(variables.size() - 1).getArgumentValue()));
            }
        }
    }
}
