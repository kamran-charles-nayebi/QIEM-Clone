package SIMulator.ModelesQuestions;

import SIMulator.ClassesQuestions.Question;
import SIMulator.Quiz;
import SIMulator.Reponses.Reponse;
import SIMulator.Statistique;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.text.TextAlignment;

import java.io.IOException;

public class Modele1Controller {

    Statistique statistique;
    @FXML
    private Button boutonOk;

    @FXML
    private Label labelChapitre;

    @FXML
    private Label labelQuestion;
    @FXML
    private HBox HBoxTransform;
    @FXML
    private Button boutonQuestion;

    @FXML
    private Label labelProgression;
    private Label labelVerif = new Label();
    private boolean aRepondu = false;
    private String rep;

    @FXML
    void prochaineQuestionButton(ActionEvent event) {
        if (!question.getTest()) {
            quiz.lancerQuestion();
        } else quiz.lancerQuestion2();
    }
    @FXML
    public void verifierReponse(ActionEvent event) {
        if (reponse.aRepondu()) {
            HBoxTransform.getChildren().clear();
            HBoxTransform.getChildren().add(labelVerif);
            if (reponse.estValide()) {
                labelVerif.setText("Bonne réponse! Bien joué!");
                question.setReussi(true);
            } else{
                labelVerif.setText("Mauvaise réponse, la bonne réponse est " + rep + "\n" + solution);
                question.setReussi(false);
                reponse.montrerReponse();
            }
            boutonQuestion.setDisable(false);
            interractionNode.setDisable(true);
            HBoxTransform.setPrefHeight(100);
            HBoxTransform.setAlignment(Pos.TOP_CENTER);
            labelVerif.setTextAlignment(TextAlignment.CENTER);
            labelVerif.setWrapText(true);
            labelVerif.setMaxHeight(Double.MAX_VALUE);
        }
    }

    Reponse reponse;
    Quiz quiz;
    Node interractionNode;
    String solution;
    Question question;
    public void setQuestion(Reponse _reponse, Question _question) throws IOException {
        question = _question;
        labelChapitre.setText(question.getChapitre());
        labelQuestion.setText(question.getQuestion());
        this.reponse = _reponse;
        this.quiz = question.getQuiz();
        boutonQuestion.setDisable(true);
        this.rep = question.getReponse();
        this.labelProgression.setText("Question " + Integer.toString(quiz.getCompteur()+1) + "/" + Quiz.NOMBRE_DE_QUESTIONS);
        this.interractionNode = reponse.getInterface();
        this.solution = question.getSolution();
    }

}
