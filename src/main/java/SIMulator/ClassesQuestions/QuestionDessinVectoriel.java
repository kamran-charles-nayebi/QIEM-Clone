package SIMulator.ClassesQuestions;

import SIMulator.ModelesQuestions.Modele1Controller;
import SIMulator.Quiz;
import SIMulator.Reponses.FabriqueurDeReponses;
import SIMulator.Reponses.Reponse;
import SIMulator.Types;
import SIMulator.Utils.Json;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class QuestionDessinVectoriel extends Question{

    private double reponseXDebut;
    private double reponseYDebut;
    private double reponseXFin;
    private double reponseYFin;
    private int grilleXMin;
    private int grilleXMax;
    private int grilleXStep;
    private int grilleYMin;
    private int grilleYMax;
    private int grilleYStep;
    private ArrayList<Integer> pointsX;
    private ArrayList<Integer> pointsY;
    private ArrayList<String> pointsTexte;
    private ArrayList<String> pointsSousTexte;
    private ArrayList<String> pointsRGB;
    private Reponse reponseQ;

    private static final int MAX_NB_POINTS = 10;

    public QuestionDessinVectoriel(Types type, String chap, Quiz quiz) {
        super(type, chap, quiz);
    }
    public QuestionDessinVectoriel(Types type, JSONObject jsonObject, Quiz quiz){
        super(type,jsonObject,quiz);
    }

    @Override
    protected void creerQuestion(boolean choisir) {
        super.creerQuestion(choisir);
        JSONObject JSONquestion = Json.getJSONquestion();
        this.reponseXDebut = Double.valueOf(JSONquestion.getString("ReponseXDebut"));
        this.reponseYDebut = Double.valueOf(JSONquestion.getString("ReponseYDebut"));
        this.reponseXFin = Double.valueOf(JSONquestion.getString("ReponseXFin"));
        this.reponseYFin = Double.valueOf(JSONquestion.getString("ReponseYFin"));
        this.grilleXMin = Integer.valueOf(JSONquestion.getString("GrilleXMin"));
        this.grilleXMax = Integer.valueOf(JSONquestion.getString("GrilleXMax"));
        this.grilleXStep = Integer.valueOf(JSONquestion.getString("GrilleXStep"));
        this.grilleYMin = Integer.valueOf(JSONquestion.getString("GrilleYMin"));
        this.grilleYMax = Integer.valueOf(JSONquestion.getString("GrilleYMax"));
        this.grilleYStep = Integer.valueOf(JSONquestion.getString("GrilleYStep"));

        boolean fini = false;
        pointsX = new ArrayList<>();
        pointsY = new ArrayList<>();
        pointsTexte = new ArrayList<>();
        pointsSousTexte = new ArrayList<>();
        pointsRGB = new ArrayList<>();

        for (int i = 0; i < MAX_NB_POINTS && !fini; i++) {
            try {
                String strX = "Point" + (i + 1) + "X";
                String strY = "Point" + (i + 1) + "Y";
                String strTexte = "Point" + (i + 1) + "Texte";
                String strSousTexte = "Point" + (i + 1) + "SousTexte";
                String strRGB = "Point" + (i + 1) + "RGB";
                pointsX.add(Integer.valueOf(JSONquestion.getString(strX)));
                pointsY.add(Integer.valueOf(JSONquestion.getString(strY)));
                pointsTexte.add((JSONquestion.getString(strTexte)));
                pointsSousTexte.add((JSONquestion.getString(strSousTexte)));
                pointsRGB.add((JSONquestion.getString(strRGB)));
            } catch (JSONException e){
                fini = true;
            }
        }
    }

    @Override
    protected String creerTexte() {
        String str = "";
        str += "ReponseType = " + type.nom + "\n";
        str += "ReponseXDebut = " + reponseXDebut + "\n";
        str += "ReponseYDebut = " + reponseYDebut + "\n";
        str += "ReponseXFin = " + reponseXFin + "\n";
        str += "ReponseYFin = " + reponseYFin + "\n";
        str += "GrilleXMin = " + grilleXMin + "\n";
        str += "GrilleXMax = " + grilleXMax + "\n";
        str += "GrilleXStep = " + grilleXStep + "\n";
        str += "GrilleYMin = " + grilleYMin + "\n";
        str += "GrilleYMax = " + grilleYMax + "\n";
        str += "GrilleYStep = " + grilleYStep + "\n";
        for (int i = 0; i < pointsX.size(); i++) {
            str += "Point"+(i+1)+"X = " + pointsX.get(i) + "\n";
            str += "Point"+(i+1)+"Y = " + pointsY.get(i) + "\n";
            str += "Point"+(i+1)+"SousTexte = " + pointsSousTexte.get(i) + "\n";
            str += "Point"+(i+1)+"Texte = " + pointsTexte.get(i) + "\n";
            str += "Point"+(i+1)+"RGB = " + pointsRGB.get(i) + "\n";
        }
        return str;
    }

    @Override
    public Node getRoot() throws IOException {
        VBox root = new VBox();
        root.setPadding(new Insets(10));
        root.setSpacing(10);
        root.setAlignment(Pos.TOP_CENTER);

        HBox chapHBox = new HBox();
        chapHBox.setAlignment(Pos.CENTER_LEFT);
        Label chapLabel = new Label(chapitre);
        Label chapProgression = new Label("Question " + Integer.toString(quiz.getCompteur()+1) + "/" + Quiz.NOMBRE_DE_QUESTIONS);
        chapLabel.setStyle("-fx-background-radius: 4;-fx-background-color: #c43d2c");
        chapHBox.getChildren().addAll(chapLabel,chapProgression);
        chapHBox.setSpacing(10);
        chapLabel.setPadding(new Insets(2,4,2,4));
        chapProgression.setPadding(new Insets(2));
        chapLabel.setFont(new Font("System Bold",12));
        chapLabel.setTextFill(Color.WHITE);
        root.getChildren().add(chapHBox);

        Label labelQuestion = new Label(question);
        labelQuestion.setMaxHeight(Double.MAX_VALUE);
        labelQuestion.setPadding(new Insets(10,0,0,0));
        labelQuestion.setWrapText(true);
        labelQuestion.setTextAlignment(TextAlignment.CENTER);
        labelQuestion.setWrapText(true);
        labelQuestion.setFont(new Font("System", 18));
        root.getChildren().add(labelQuestion);

        reponseQ = FabriqueurDeReponses.faireUneReponse(creerTexte());
        Node interractionNode = reponseQ.getInterface();
        root.getChildren().add(interractionNode);

        Button buttonNext = new Button("Prochaine question");
        buttonNext.setOnAction(r ->{
            quiz.lancerQuestion();
        });
        buttonNext.setDisable(true);

        Label labelVerif = new Label();

        HBox HBoxTransform = new HBox();
        Button buttonOK = new Button("Ok");
        buttonOK.setOnAction(r ->{
            if (reponseQ.aRepondu()) {
                HBoxTransform.getChildren().clear();
                HBoxTransform.getChildren().add(labelVerif);
                if (reponseQ.estValide()) {
                    labelVerif.setText("Bonne réponse! Bien joué!");
                    this.setReussi(true);
                } else{
                    labelVerif.setText("Mauvaise réponse, la bonne réponse est " + reponse + "\n" + solution);
                    this.setReussi(false);
                    reponseQ.montrerReponse();
                }
                buttonNext.setDisable(false);
                interractionNode.setDisable(true);
                HBoxTransform.setPrefHeight(100);
                HBoxTransform.setAlignment(Pos.TOP_CENTER);
                labelVerif.setTextAlignment(TextAlignment.CENTER);
                labelVerif.setWrapText(true);
                labelVerif.setMaxHeight(Double.MAX_VALUE);
                labelQuestion.setWrapText(true);
            }
        });
        HBoxTransform.getChildren().add(buttonOK);
        HBoxTransform.setAlignment(Pos.TOP_CENTER);
        HBoxTransform.setPrefHeight(30);
        HBoxTransform.setPrefWidth(200);
        root.getChildren().addAll(HBoxTransform,buttonNext);

        buttonNext.setStyle("-fx-background-color:  #f74d37");
        buttonOK.setStyle("-fx-background-color:  #f74d37");
        buttonNext.setFont(new Font("System Bold", 12));
        buttonOK.setFont(new Font("System Bold", 12));
        buttonNext.setTextFill(Color.WHITE);
        buttonOK.setTextFill(Color.WHITE);

        return root;
    }
}
