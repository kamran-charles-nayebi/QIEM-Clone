package SIMulator.Prof;

import SIMulator.ClassesQuestions.*;
import SIMulator.ModelesQuestions.RecapQuizController;
import SIMulator.Quiz;
import SIMulator.StatistiqueWrapper;
import SIMulator.Types;
import SIMulator.Utils.Json;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.mariuszgromada.math.mxparser.Argument;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ThreadLocalRandom;

import static SIMulator.Types.*;
import static SIMulator.Types.DESSIN_VECTORIEL;

public class InterfaceQuestionControlleur implements Initializable {

    private int compteur = 0;

    Stage stage;
    private Scene scene;
    Stage SecondStage;

    private ArrayList<Question> Nbquestion;

    public static int NOMBRE_DE_QUESTIONS = 1;
    @FXML
    private TableView<Question> tableView;


    @FXML
    private TableColumn<Question, String> QuestionCol;

    @FXML
    private TableColumn<Question, String> ReponseCol;

    @FXML
    private TableColumn<Question, String> TypeCol;
    private String buttonString;
    private InterfaceProfControlleur interfaceProfControlleur;
    private String button;

    public void lancer(String button) {
        this.buttonString = button;
        ObservableList<Question> data = FXCollections.observableArrayList();
        try {
            Json.lireFichierQuestion("questions/" + button + ".json");
        } catch (JSONException e) {
            System.out.println("Fichier json non valide");
            return;
        }
        JSONObject jsonObject = Json.getJSON();
        List<String> typesPossibles = new ArrayList<>(Json.getJSON().keySet());
        for (int i = 0; i < typesPossibles.size(); i++) {
            JSONArray questionPossibles = jsonObject.getJSONArray(typesPossibles.get(i));
            for (int j = 0; j < questionPossibles.length(); j++) {
                Question question = new Question(questionPossibles.getJSONObject(j).getString("Question"), questionPossibles.getJSONObject(j).getString("Reponse"), typesPossibles.get(i));
                data.add(question);
            }
        }
        QuestionCol.setCellValueFactory(new PropertyValueFactory<>("question"));
        ReponseCol.setCellValueFactory(new PropertyValueFactory<>("reponse"));
        TypeCol.setCellValueFactory(new PropertyValueFactory<>("types"));
        tableView.setItems(data);

        tableView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1) {
                // Get the selected row and column index
                int rowIndex = tableView.getSelectionModel().getSelectedIndex();
                // Get the value of the clicked cell
//                Object cellValue = tableView.getColumns().get(colIndex).getCellData(rowIndex);
                Object cellValue = tableView.getItems().get(rowIndex);
                // Perform some action with the cell value
                Stage testStage = new Stage();
                testStage.setScene(new Scene(new HBox()));
                testStage.show();
                Types type = switch (((Question)cellValue).getTypes()){
                    case "ReponseTextuelle"-> TEXTUELLE;
                    case "ReponseChoix"-> CHOIX_DE_REPONSE;
                    case "ReponseChargeLineaire"->CHARGE_LINEAIRE;
                    case "ReponseDessinVectoriel"->DESSIN_VECTORIEL;
                    case "ReponseLogique"->LOGIQUE;
                    case "ReponseCircuitElectrique"->CIRCUIT_ELECTRIQUE;
                    default -> TEXTUELLE;
                };
                JSONArray jsonArray = Json.getArrayQuestions(type);
                JSONObject json = null;
                for (int i = 0; i < jsonArray.length(); i++) {
                    if (((Question) cellValue).getQuestion().equals(jsonArray.getJSONObject(i).getString("Question"))){
                        json = jsonArray.getJSONObject(i);
                    }
                }
                Json.setJSONquestion(json);
                Quiz newQuiz = new Quiz(testStage,json, type, buttonString);

            }
        });
    }

    @FXML
    void ajouterQuestion(ActionEvent event) throws IOException {
        if (SecondStage != null) {
            SecondStage.close();
        }
        Stage primaryStage = new Stage();

        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/SIMulator/fxml/Prof/InterfaceCreerQuestion.fxml"));
        Parent root = loader.load();
        InterfaceCreerQuestionControlleur interfaceCreerQuestionControlleur = loader.getController();
        interfaceCreerQuestionControlleur.setInterfaceQuestionControlleur(this);
        interfaceCreerQuestionControlleur.setInterfaceProf(interfaceProfControlleur);
        interfaceCreerQuestionControlleur.setButton(button);

        scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
        SecondStage = primaryStage;
        interfaceCreerQuestionControlleur.setStage(SecondStage);
    }

    public void refreshTable() {
        tableView.refresh();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public void setInterfaceProf(InterfaceProfControlleur interfaceProfControlleur) {
        this.interfaceProfControlleur=interfaceProfControlleur;
    }

    public void setButton(String button) {
        this.button=button;
    }
}
