package SIMulator;

import SIMulator.Prof.InterfaceProfControlleur;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.mariuszgromada.math.mxparser.Argument;
import org.mariuszgromada.math.mxparser.Expression;
import org.mariuszgromada.math.mxparser.License;

import java.io.*;

public class SIMulatorApplication extends Application {
    public static final int APP_PROF=1;
    Statistique statistique=new Statistique();

    Scene scene;
    Stage stage;


    @Override
    public void start(Stage primaryStage) throws Exception {
        //afficher la première fenêtre
        License.iConfirmNonCommercialUse("Okay1");
        if(APP_PROF==0){

        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/SIMulator/fxml/Menus/ElectriciteChapitre.fxml"));
        Parent root  = loader.load();
        ElectriciteChapitreControlleur electriciteChapitreControlleur=loader.getController();
        electriciteChapitreControlleur.setStage(primaryStage);
        electriciteChapitreControlleur.setStatistique(statistique);
        scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
        stage = primaryStage;
        stage.setMinWidth(700);
        stage.setMinHeight(805);
        stage.setWidth(700);
        stage.setHeight(805);
        stage.getIcons().add(new Image("/SIMulator/images/aimant.png"));
        stage.setTitle("Questionnaire Interactif en Électricité et Magnétisme");
        //effacerFichier();
        }
        else {
            FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/SIMulator/fxml/Prof/InterfaceProf.fxml"));
            Parent root  = loader.load();
            InterfaceProfControlleur interfaceProfControlleur=loader.getController();
            scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.show();
            primaryStage.setMinWidth(500);
            primaryStage.setMinHeight(500);
        }
        statistique.lancerGraphique();
        statistique.fermerFenetre();
    }

    public void setScene(Scene scene){
        this.scene = scene;
        stage.setScene(scene);
    }

    private void effacerFichier(){
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
    }

    public static void main(String[] args) {
        launch();
    }
}
