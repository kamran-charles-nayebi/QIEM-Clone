package SIMulator;

import SIMulator.Utils.Json;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.json.JSONException;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class ElectriciteChapitreControlleur {
    Statistique statistique;

    ArrayList<String> allChapitre = new ArrayList<>();

    HBox[] chapitres;
    private Scene scene;
    @FXML
    private VBox menuVBox;

    @FXML
   void initialize(){
        File file = new File("questions");
        File[] liste = file.listFiles();
       chapitres = new HBox[liste.length];
       // Désactive tous les chapitres sauf le chapitre 1
       for (int i = 0; i < liste.length; i++) {
           chapitres[i] = new HBox();
           chapitres[i].setAlignment(Pos.CENTER);
           chapitres[i].setSpacing(10);
           Label label = new Label(liste[i].getName().replace(".json", ""));
           label.setFont(new Font(25));
           CheckBox checkBox = new CheckBox();
           chapitres[i].getChildren().addAll(checkBox, label);
           menuVBox.getChildren().add(chapitres[i]);
           try{
               Json.lireFichierQuestion(liste[i].getPath());
           } catch (JSONException e){
               checkBox.setDisable(true);
           }
       }
   }


    @FXML
    void resetStats(ActionEvent event) {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream("statistiques/statistiques.obj");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        statistique.updateDonnees();
    }

    @FXML
    void commencerButton(ActionEvent event) {
        for (HBox chapitre : chapitres) {
            if (((CheckBox) chapitre.getChildren().get(0)).isSelected()) {
                allChapitre.add(((Labeled) chapitre.getChildren().get(1)).getText());
            }
        }
        if(allChapitre.size()!=0){
        Quiz quiz1 = new Quiz(allChapitre, stage);
        quiz1.lancerQuestion();
        quiz1.setStatistique(statistique);
        }
    }

    @FXML
    void statistiqueButton(ActionEvent event) throws IOException {
        //envoyer à statistique
        statistique.lancerGraphique();
    }

    Stage stage;
    Stage SecondStage;
    public void setScene(Scene scene){
        this.scene = scene;
        stage.setScene(scene);
    }
    public void setStage(Stage stage) {
        this.stage = stage;
    }
    public void setSecondStage(Stage stage) {
        this.stage = stage;
    }

    public void setStatistique(Statistique statistique) {
        this.statistique = statistique;
    }
}
