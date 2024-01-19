package SIMulator.ClassesQuestions;


import SIMulator.ModelesQuestions.Modele1Controller;
import SIMulator.Quiz;
import SIMulator.Reponses.FabriqueurDeReponses;
import SIMulator.Reponses.Reponse;
import SIMulator.Reponses.ReponseChoix.ReponseChoix;
import SIMulator.Reponses.ReponseChoixLineaire.ReponseChargesLineaire;
import SIMulator.Reponses.ReponseDessinVectoriel.ReponseDessinVectoriel;
import SIMulator.Reponses.ReponseTextuelle.ReponseTextuelle;
import SIMulator.Types;
import SIMulator.Utils.Json;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.json.JSONObject;
import org.mariuszgromada.math.mxparser.Argument;
import org.mariuszgromada.math.mxparser.Expression;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ThreadLocalRandom;

public class Question{

    protected String question;
    protected Types type;
    protected String types;
    protected String chapitre;
    protected String reponse;

    protected String solution;
    protected Quiz quiz;
    protected int position;
    protected String matiere;
    protected Boolean reussi;
    protected List<Argument> variables;
    protected boolean test = false;

    /**
     * Constructeur de la questions
     * @param type - le type de question
     * @param chap - chapitre de la question
     * @param quiz - quiz dans lequel la question est posée
     */
    public Question(Types type, String chap,Quiz quiz){
        this.type = type;
        this.chapitre = chap;
        this.quiz = quiz;
        creerQuestion(true);
        test = false;
    }

    public Question(Types type, JSONObject jsonObject, Quiz quiz){
        this.quiz = quiz;
        this.type = type;
        creerQuestion(false);
        test = true;
    }

    public Question(String question, String reponse, String types){
        this.question = question;
        this.reponse = reponse;
        this.types = types;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getTypes() {
        return types;
    }

    public void setTypes(String types) {
        this.types = types;
    }

    public Types getType() {
        return type;
    }

    public void setType(Types type) {
        this.type = type;
    }

    /**
     * Lit le fichier json et choisis une question aléatoirement avec sa réponse appropriée
     */
    protected void creerQuestion(boolean choisir) {
        JSONObject JSONquestion = null;
        if (choisir) {
            JSONquestion = Json.choisirQuestion(type);
            Json.setJSONquestion(JSONquestion);
        } else {
            JSONquestion = Json.getJSONquestion();
        }

            if(type.equals(Types.TEXTUELLE)) {
                int random = ThreadLocalRandom.current().nextInt(0, 500);
                Expression e = new Expression(String.format(Locale.US,JSONquestion.getString("Reponse"), random));
                this.question = String.format(Locale.US,JSONquestion.getString("Question"), random);
                if (!JSONquestion.getString("Format").equals("")){
                    this.reponse = String.format(Locale.US, JSONquestion.getString("Format"), e.calculate());
                } else this.reponse = JSONquestion.getString("Reponse").toLowerCase(Locale.US);
                if(this.reponse.equals("NaN")){
                    this.reponse = JSONquestion.getString("Reponse");
                }
            } else {
                this.question = String.format(Locale.US,JSONquestion.getString("Question"));
                this.reponse = String.format(Locale.US,JSONquestion.getString("Reponse"));
            }

            this.solution = String.format(Locale.US,JSONquestion.getString("Solution"));
            this.matiere = String.format(Locale.US,JSONquestion.getString("Matiere"));

       variables = new ArrayList<>();
        ArrayList<String> listOperation = new ArrayList<>();
        if (JSONquestion.has("Variables")) {
            Iterator<String> it = JSONquestion.getJSONObject("Variables").keys();
            while (it.hasNext()) {
                String var = it.next();
                String etendue = JSONquestion.getJSONObject("Variables").getString(var);
                if (!etendue.equals("")) {
                    if (!etendue.contains("{")) {
                        variables.add(new Argument(var, ThreadLocalRandom.current().nextInt(Integer.parseInt(etendue.split(":")[0]), Integer.parseInt(etendue.split(":")[1]) + 1)));
                        reponse = reponse.replace("{" + var + "}", String.valueOf(variables.get(variables.size() - 1).getArgumentValue()));
                        question = question.replace("{" + var + "}", String.valueOf(variables.get(variables.size() - 1).getArgumentValue()));
                        solution = solution.replace("{" + var + "}", String.valueOf(variables.get(variables.size() - 1).getArgumentValue()));

                    } else {
                       listOperation.add(var);
                    }
                } else
                    variables.add(new Argument(var, Double.NaN));
            }
            for (int i = 0; i < listOperation.size(); i++) {
                String etendue = JSONquestion.getJSONObject("Variables").getString(listOperation.get(i));
                for (int j = 0; j < variables.size(); j++) {
                    etendue = etendue.replace("{" + variables.get(j).getArgumentName()+"}", String.valueOf(variables.get(j).getArgumentValue()));
                }
                Expression expression;
                if (etendue.split(":").length > 0) {
                    expression = new Expression(etendue.split(":")[0]);
                    System.out.println(Double.parseDouble(String.format(Locale.US,etendue.split(":")[1], expression.calculate())));
                    variables.add(new Argument(listOperation.get(i), Double.parseDouble(String.format(Locale.US,etendue.split(":")[1], expression.calculate()))));
                }
                else{
                    expression = new Expression(etendue);
                    variables.add(new Argument(listOperation.get(i), expression.calculate()));
                }
                System.out.println(JSONquestion.getString("Format"));
                reponse = reponse.replace("{" + listOperation.get(i) + "}", String.format(Locale.US, JSONquestion.getString("Format"), variables.get(variables.size() - 1).getArgumentValue()));
                question = question.replace("{" + listOperation.get(i) + "}", String.valueOf(variables.get(variables.size() - 1).getArgumentValue()));
                solution = solution.replace("{" + listOperation.get(i) + "}", String.valueOf(variables.get(variables.size() - 1).getArgumentValue()));
            }
        }
    }

    /**
     * Méthode qui crée le string à envoyer au créateur de réponse
     * @return le string à envoyer au créateur de réponse
     */
    protected String creerTexte(){
        String str = "";
        str += "ReponseType = " + type.nom + "\n";
        str += "Reponse = " + reponse + "\n";
        return str;
    }

    public void setReussi(boolean r){
        reussi = r;
    }

    public boolean getReussi(){
        return reussi;
    }

    public String getMatiere(){
        return matiere;
    }

    /**
     * Méthode qui ajoute l'interface interractive au root principal
     * @return la root principale avec l'interface interractive ajoutée
     * @throws IOException
     */
    public Node getRoot() throws IOException {
        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/SIMulator/fxml/Questions/model1.fxml"));
        Parent root  = loader.load();
        Modele1Controller controller = loader.getController();

        Reponse reponseQ = FabriqueurDeReponses.faireUneReponse(creerTexte());

        //ajouter au FXML
        if (type.equals(Types.TEXTUELLE)) {
            TextField textField = (TextField) ((VBox) reponseQ.getInterface()).getChildren().get(1);
            textField.setOnKeyPressed(r -> {
                if (r.getCode().equals(KeyCode.ENTER)) {
                    controller.verifierReponse(null);
                }
            });
        }
        ((VBox) root).getChildren().add(2, reponseQ.getInterface());
        controller.setQuestion(reponseQ, this);
        return root;
    }
    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getChapitre() {
        return chapitre;
    }

    public void setChapitre(String chapitre) {
        this.chapitre = chapitre;
    }

    public String getReponse() {
        return reponse;
    }

    public void setReponse(String reponse) {
        this.reponse = reponse;
    }

    public String getSolution() {
        return solution;
    }

    public void setSolution(String solution) {
        this.solution = solution;
    }

    public Quiz getQuiz() {
        return quiz;
    }

    public void setQuiz(Quiz quiz) {
        this.quiz = quiz;
    }

    public void setReussi(Boolean reussi) {
        this.reussi = reussi;
    }

    public List<Argument> getVariables() {
        return variables;
    }

    public void setVariables(List<Argument> variables) {
        this.variables = variables;
    }

    public boolean getTest(){
        return test;
    }

}
