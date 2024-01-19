package SIMulator;

import SIMulator.ClassesQuestions.*;
import SIMulator.ModelesQuestions.RecapQuizController;
import SIMulator.Utils.Json;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.json.JSONObject;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static SIMulator.Types.*;

public class Quiz {
    private ArrayList<String> allChapitre;
    private ArrayList<Question> Nbquestion;
    private int compteur = 0;
    public static int NOMBRE_DE_QUESTIONS = 5;
    private Statistique statistique;

    public Quiz(ArrayList<String> allChapitre, Stage stage) {
        this.allChapitre = allChapitre;
        Nbquestion = new ArrayList<>();
        creeQuestion();
        this.stage = stage;
    }
    public Quiz(Stage stage, JSONObject jsonObject, Types type,String chapitre) {
        Nbquestion = new ArrayList<>();
        this.stage = stage;
        creeQuestion(type, jsonObject, chapitre);
    }

    public void lancerQuestion() {
        if(compteur < NOMBRE_DE_QUESTIONS){
        try {
            stage.getScene().setRoot((Parent) Nbquestion.get(compteur).getRoot());
            compteur++;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        }
        else {
        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("fxml/Questions/finQuiz.fxml"));
        Pane root = null;

        try {
            root = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        RecapQuizController recapQuizzController = loader.getController();
        recapQuizzController.setStage(stage);
        recapQuizzController.setQuiz(this);
        recapQuizzController.setStatistique(statistique);
        stage.getScene().setRoot(root);
        ArrayList<StatistiqueWrapper> array = new ArrayList<>();
            try {
                if (new File("statistiques/statistiques.obj").exists()) {
                    FileInputStream fis = new FileInputStream("statistiques/statistiques.obj");
                    ObjectInputStream ois = new ObjectInputStream(fis);
                    try {
                        while (true) {
                            array.add((StatistiqueWrapper) ois.readObject());
                        }
                    } catch (EOFException e) {

                    }
                }
            } catch (FileNotFoundException e){
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

        try {

            FileOutputStream fos = new FileOutputStream("statistiques/statistiques.obj");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            for (int i = 0; i < array.size(); i++) {
                oos.writeObject(array.get(i));
            }
            oos.writeObject(new StatistiqueWrapper(this));
        } catch (FileNotFoundException e){
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        statistique.updateDonnees();
        }
    }
    public void lancerQuestion2() {
        if(compteur < NOMBRE_DE_QUESTIONS){
            try {
                stage.getScene().setRoot((Parent) Nbquestion.get(0).getRoot());
                compteur++;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        else {
            stage.close();
        }
    }

    private void creeQuestion() {
        String precedent = "";
        for (int i = 0; i < NOMBRE_DE_QUESTIONS; i++) {
            String chapitreAleatoire = allChapitre.get(ThreadLocalRandom.current().nextInt(0,allChapitre.size()));
            Json.lireFichierQuestion("questions/"+chapitreAleatoire+".json");
            List<String> typesPossibles = new ArrayList<>(Json.getJSON().keySet());
            String typeAleatoire = typesPossibles.get(ThreadLocalRandom.current().nextInt(0, typesPossibles.size()));
            if (typesPossibles.size() > 1)
                while (typeAleatoire.equals(precedent))
                    typeAleatoire = typesPossibles.get(ThreadLocalRandom.current().nextInt(0, typesPossibles.size()));
            precedent = typeAleatoire;
            //typeAleatoire = "ReponseCircuitElectrique";
            switch (typeAleatoire){
                case "ReponseTextuelle" -> Nbquestion.add(new Question(TEXTUELLE, chapitreAleatoire, this));
                case "ReponseChoix" -> Nbquestion.add(new QuestionChoixReponse(CHOIX_DE_REPONSE, chapitreAleatoire, this));
                case "ReponseChargeLineaire" -> Nbquestion.add(new QuestionChargeLineaire(CHARGE_LINEAIRE, chapitreAleatoire, this));
                case "ReponseLogique" -> Nbquestion.add(new QuestionRaisonnementLogique(LOGIQUE, chapitreAleatoire, this));
                case "ReponseCircuitElectrique" -> Nbquestion.add(new QuestionCircuitElectrique(CIRCUIT_ELECTRIQUE, chapitreAleatoire, this));
                case "ReponseDessinVectoriel" -> Nbquestion.add(new QuestionDessinVectoriel(DESSIN_VECTORIEL, chapitreAleatoire, this));
                default -> System.out.println("Erreur, type non reconnu");
            }
        }
    }
    public void creeQuestion(Types type, JSONObject jsonObject, String chapitre){
        switch (type.nom){
            case "ReponseTextuelle" -> Nbquestion.add(new Question(TEXTUELLE, jsonObject, this));
            case "ReponseChoix" -> Nbquestion.add(new QuestionChoixReponse(CHOIX_DE_REPONSE, jsonObject, this));
            case "ReponseChargeLineaire" -> Nbquestion.add(new QuestionChargeLineaire(CHARGE_LINEAIRE, jsonObject, this));
            case "ReponseLogique" -> Nbquestion.add(new QuestionRaisonnementLogique(LOGIQUE, jsonObject, this));
            case "ReponseCircuitElectrique" -> Nbquestion.add(new QuestionCircuitElectrique(CIRCUIT_ELECTRIQUE, jsonObject, this));
            case "ReponseDessinVectoriel" -> Nbquestion.add(new QuestionDessinVectoriel(DESSIN_VECTORIEL, jsonObject, this));
            default -> System.out.println("Erreur, chapitre non reconnu");
        }
        compteur = NOMBRE_DE_QUESTIONS - 1;
        lancerQuestion2();
    }

    public ArrayList<String> getAllChapitre() {
        return allChapitre;
    }

    public ArrayList<Question> getArrayQuestions() {
        return Nbquestion;
    }

    Stage stage;

    public void setStatistique(Statistique statistique) {
        this.statistique = statistique;
    }

    public int getCompteur(){
        return compteur;
    }
}
