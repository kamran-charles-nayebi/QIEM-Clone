package SIMulator.Prof;

import SIMulator.ClassesQuestions.Question;
import SIMulator.StatistiqueWrapper;
import SIMulator.Utils.Json;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.awt.event.MouseEvent;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class InterfaceProfControlleur implements Initializable {

    Stage stage;
    private Scene scene;
    Stage SecondStage;
    @FXML
    private VBox vboxChapitre;

    @FXML
    void creerChapitre(ActionEvent event) {
        TextInputDialog ajouter = new TextInputDialog();
        ajouter.setTitle("Création d'un chapitre");
        ajouter.setHeaderText("Nouveau Chapitre");
        ajouter.setContentText("Nom du chapitre:");
        String texteSimulation = ajouter.showAndWait().get();
        Button button=new Button(texteSimulation);
        vboxChapitre.getChildren().add(button);
        button.setStyle("-fx-background-color:  #f74d37");
        button.setFont(new Font("System Bold", 12));
        button.setTextFill(Color.WHITE);

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream("questions/"+texteSimulation+".json");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(null);
            oos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        button.setOnAction(e -> {
            try {
                lancerFenetre(button.getText());
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        File file = new File("questions");
        File[] listFiles = file.listFiles();

        for (int i = 0; i < listFiles.length; i++) {
            Button button=new Button(listFiles[i].getName().replace(".json",""));
            vboxChapitre.getChildren().add(button);
            button.setStyle("-fx-background-color:  #f74d37");
            button.setFont(new Font("System Bold", 12));
            button.setTextFill(Color.WHITE);
            button.setOnAction(e -> {
                try {
                    lancerFenetre(button.getText());
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            });
        }
    }

    public void lancerFenetre(String button) throws IOException {
        if (SecondStage != null) {
            SecondStage.close();
        }
        Stage primaryStage = new Stage();

        Json.lireFichierQuestion("questions/"+button+".json");
        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/SIMulator/fxml/Prof/InterfaceQuestion.fxml"));
        Parent root = loader.load();
        InterfaceQuestionControlleur interfaceQuestionControlleur = loader.getController();
        interfaceQuestionControlleur.setInterfaceProf(this);
        interfaceQuestionControlleur.lancer(button);
        interfaceQuestionControlleur.setButton(button);

        scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
        SecondStage = primaryStage;
    }



}