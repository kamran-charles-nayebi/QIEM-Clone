package SIMulator;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.io.*;
import java.util.*;

public class Statistique implements Serializable {

    List<Double> arrayQuiz = new ArrayList<Double>();
    List<Double> arrayChapitre = new ArrayList<Double>();
    Map<String, Pair<Integer,Integer>> compteurChap = new HashMap<>();
    Map<String, Double> pourcentageChap = new HashMap<>();
    List<Double> arrayMatiere = new ArrayList<>();
    private String[] stringMatieres = new String[]{"ManipCharges", "ForceCharge", "Conductivite", "Force électrique", "Calculs de résistances", "Compréhension des circuits"};

    int n = 0;

    public int getN() {
        return n;
    }

    public void setN(int n) {
        this.n = n;
    }

    List<PointStatistique> pointStatistiques;

    GraphiqueControlleur graphiqueControlleur;

    public Statistique() {
        this.pointStatistiques = new ArrayList<>();
        for (int i = 0; i < stringMatieres.length; i++) {
            arrayMatiere.add(0.0);
        }
    }

    public void ajouterPoint(PointStatistique pointStatistique) {
        pointStatistiques.add(pointStatistique);
    }

    public void lancerGraphique() throws IOException {
        if (SecondStage != null) {
            SecondStage.close();
        }
        Stage primaryStage = new Stage();

        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/SIMulator/fxml/Menus/Graphique.fxml"));
        Parent root = loader.load();
        graphiqueControlleur = loader.getController();
        updateDonnees();
        scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
        SecondStage = primaryStage;
        SecondStage.setMinHeight(500);
        SecondStage.setMinWidth(800);
        SecondStage.getIcons().add(new Image("/SIMulator/images/aimant.png"));
        SecondStage.setTitle("Statistiques du questionnaire");
    }

    Stage stage;
    private Scene scene;
    Stage SecondStage;

    public void setScene(Scene scene) {
        this.scene = scene;
        stage.setScene(scene);
    }


    public void updateDonnees() {
        if (graphiqueControlleur != null) {
            graphiqueControlleur.resetGraphique();
        }

        ArrayList<StatistiqueWrapper> array = new ArrayList<>();
        //lire
        try {
            if (new File("statistiques/statistiques.obj").exists()) {
                FileInputStream fis = new FileInputStream("statistiques/statistiques.obj");
                ObjectInputStream ois = new ObjectInputStream(fis);
                try {
                    while (true) {
                        StatistiqueWrapper sw = (StatistiqueWrapper) ois.readObject();
                        if (sw != null) {
                            array.add(sw);
                        }
                    }
                } catch (EOFException e) {


                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        //set l'array chapitres
        compteurChap.clear();
        pourcentageChap.clear();
        arrayChapitre.clear();


        arrayQuiz.clear();

        ArrayList<Double> compteurMat = new ArrayList<>();
        ArrayList<Double> pourcentageMat = new ArrayList<>();
        for (int j = 0; j < stringMatieres.length; j++) {
            compteurMat.add(0.0);
            pourcentageMat.add(0.0);
        }

        for (int i = 0; i < array.size(); i++) {
            //chapitres
            StatistiqueWrapper sw = array.get(i);
            for (int j = 0; j < sw.getQuestionList().size(); j++) {
                StatistiqueWrapper.QuestionWrapper qw = sw.getQuestionList().get(j);

                if (!compteurChap.containsKey(qw.getChapitre()))
                    compteurChap.put(qw.getChapitre(), new Pair<>(0,0));
                Pair<Integer, Integer> anciennePaire = compteurChap.get(qw.getChapitre());
                Pair<Integer,Integer> nouvellePaire = new Pair<>(anciennePaire.getKey()+1, anciennePaire.getValue() + (sw.getQuestionList().get(j).getReussi() ? 1 : 0));
                compteurChap.replace(qw.getChapitre(), nouvellePaire);
            }


            //quiz
            double pourcentageParQuiz = 0;
            for (int j = 0; j < sw.getQuestionList().size(); j++) {
                if (sw.getQuestionList().get(j).getReussi()) {
                    pourcentageParQuiz += 1;
                }
            }
            pourcentageParQuiz = (pourcentageParQuiz / (double) sw.getQuestionList().size()) * 100;
            arrayQuiz.add(pourcentageParQuiz);
            //matières
            for (int j = 0; j < sw.getQuestionList().size(); j++) {
                StatistiqueWrapper.QuestionWrapper qw = sw.getQuestionList().get(j);
                for (int k = 0; k < stringMatieres.length; k++) {
                    if (qw.getMatiere() != null) {
                        if (qw.getMatiere().equals(stringMatieres[k])) {
                            compteurMat.set(k, compteurMat.get(k) + 1);
                            if (qw.getReussi()) {
                                pourcentageMat.set(k, pourcentageMat.get(k) + 1);
                            }
                        }
                    }
                }
            }
        }
        Set<String> chapitres = compteurChap.keySet();
        for (String chapitre : chapitres) {
            if (compteurChap.get(chapitre).getValue() != 0)
                pourcentageChap.put(chapitre,(100.0*(double)compteurChap.get(chapitre).getValue())/compteurChap.get(chapitre).getKey());
            else
                pourcentageChap.put(chapitre,0.0);
        }
        for (int i = 0; i < stringMatieres.length; i++) {
            arrayMatiere.set(i, (pourcentageMat.get(i) / compteurMat.get(i)) * 100);
        }

        if (arrayQuiz != null) {
            for (int i = 0; i < arrayQuiz.size(); i++) {
                graphiqueControlleur.addSeriesQuiz("Quiz" + (i + 1), arrayQuiz.get(i));
            }
            for (String chapitre : chapitres) {
                graphiqueControlleur.addSeriesChapitre(chapitre, pourcentageChap.get(chapitre));
            }
            for (int i = 0; i < arrayMatiere.size(); i++) {
                if (!arrayMatiere.get(i).isNaN()) {
                    graphiqueControlleur.addSeriesMatiere(stringMatieres[i], arrayMatiere.get(i));
                }
            }
        }
        if (arrayQuiz != null) {
            String pireChapitre = "";
            double pirePourcentage = 100.0;
            for (String chapitre : chapitres) {
                if (pourcentageChap.get(chapitre) < pirePourcentage){
                    pireChapitre = chapitre;
                    pirePourcentage = pourcentageChap.get(pireChapitre);
                }
            }
            graphiqueControlleur.changerLabel("Vous devriez faire le chapitre : " + pireChapitre);
        }
    }

    public void fermerFenetre(){
        SecondStage.close();
    }

}
