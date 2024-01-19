package SIMulator.Prof;

import SIMulator.Utils.Json;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

public class InterfaceCreerQuestionControlleur {

    @FXML
    private TextField matiereText;


    @FXML
    private TextField ChoixReponse1Text;

    @FXML
    private TextField ChoixReponse2Text;

    @FXML
    private TextField ChoixReponse3Text;

    @FXML
    private TextField questionText;

    @FXML
    private TextField reponseText;

    @FXML
    private TextField solutionText;
    private Stage secondStage;
    private InterfaceQuestionControlleur interfaceQuestionControlleur;
    private InterfaceProfControlleur interfaceProfControlleur;
    private String button;

    @FXML
    void creerQuestionButton(ActionEvent event) {
        JSONObject jsonObject = Json.getJSON();
        JSONArray jsonArray;
        if(jsonObject==null){
            jsonObject=new JSONObject();
        }
        if(jsonObject.has("ReponseChoix")) {
        jsonArray=jsonObject.getJSONArray("ReponseChoix");
        } else {
            jsonArray=new JSONArray();
        }
        JSONObject question=new JSONObject();
        question.put("Question",questionText.getText());
        question.put("Reponse",reponseText.getText());
        question.put("ChoixReponse",ChoixReponse1Text.getText()+";"+ChoixReponse2Text.getText()+";"+ChoixReponse3Text.getText());
        question.put("Solution",solutionText.getText());
        question.put("Matiere",matiereText.getText());
        jsonArray.put(question);
        jsonObject.put("ReponseChoix",jsonArray);
        Json.ecrireFichierQuestion(jsonObject);
        try {
            interfaceProfControlleur.lancerFenetre(button);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        secondStage.close();
    }

    public void setStage(Stage secondStage) {
        this.secondStage=secondStage;
    }
    public void setInterfaceQuestionControlleur(InterfaceQuestionControlleur interfaceQuestionControlleur){
        this.interfaceQuestionControlleur=interfaceQuestionControlleur;
    }

    public void setInterfaceProf(InterfaceProfControlleur interfaceProfControlleur) {
        this.interfaceProfControlleur=interfaceProfControlleur;
    }

    public void setButton(String button) {
        this.button=button;
    }
}