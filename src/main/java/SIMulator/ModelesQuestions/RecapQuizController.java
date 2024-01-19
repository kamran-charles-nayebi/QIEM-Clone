package SIMulator.ModelesQuestions;

import SIMulator.ClassesQuestions.Question;
import SIMulator.ElectriciteChapitreControlleur;
import SIMulator.Quiz;
import SIMulator.Statistique;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public class RecapQuizController {
    private Statistique statistique;
    @FXML
    private Button boutonQuestion;

    @FXML
    private Label labelChapitres;

    @FXML
    private Label labelPourcentage;

    @FXML
    private Label labelQuestion;

    private Stage stage;

    private Quiz quiz;

    @FXML
    void retournerAuMenu(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/SIMulator/fxml/Menus/ElectriciteChapitre.fxml"));
        Pane root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        ElectriciteChapitreControlleur electriciteChapitreControlleur = loader.getController();
        electriciteChapitreControlleur.setStage(stage);
        stage.getScene().setRoot(root);
        electriciteChapitreControlleur.setStatistique(statistique);
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setStatistique(Statistique statistique) {
        this.statistique = statistique;
    }

    public void setQuiz(Quiz quiz){
        this.quiz = quiz;
        //montrer les chapitres
        String str = "Chapitres à l'étude : ";
        ArrayList<String> arrayChapitres = quiz.getAllChapitre();
        for (int i = 0; i < arrayChapitres.size(); i++) {
            if(i!=0){
                str += ", ";
            }
            str += arrayChapitres.get(i);
        }
        labelChapitres.setText(str);
        //montrer la réussite
        double pourcentage = 0;
        ArrayList<Question> arrayQuestions = quiz.getArrayQuestions();
        for (int i = 0; i < Quiz.NOMBRE_DE_QUESTIONS; i++) {
            if(arrayQuestions.get(i).getReussi()){
                pourcentage += 1;
            }
        }
        pourcentage = (pourcentage/Quiz.NOMBRE_DE_QUESTIONS)*100.0;
        labelPourcentage.setText("Pourcentage de réussite : " + (pourcentage) + "%");
    }

}
