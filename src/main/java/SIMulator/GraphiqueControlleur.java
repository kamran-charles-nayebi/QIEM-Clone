package SIMulator;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.ResourceBundle;

public class GraphiqueControlleur implements Initializable {
    @FXML
    private BarChart barChart;

    @FXML
    void reinitialiserStats(ActionEvent event) {
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

    @FXML
    private Label recommandation;
    @FXML
    private BarChart barChart2;


    @FXML
    private BarChart barChart3;

    @FXML
    private CategoryAxis xAxis;
    @FXML
    private NumberAxis yAxis;

    @FXML
    private CategoryAxis xAxis2;
    @FXML
    private NumberAxis yAxis2;

    public void resetGraphique(){
        barChart3.getData().clear();
        barChart2.getData().clear();
        barChart.getData().clear();
    }
    public void addSeriesQuiz(String name, Double pourcentage) {

        XYChart.Series<String, Double> series = new XYChart.Series<>();

        series.setName(name);

        series.getData().add(new XYChart.Data<>("", pourcentage));

        barChart.getData().addAll(series);
    }
    public void addSeriesChapitre(String name, Double pourcentage){

        XYChart.Series<String, Double> series = new XYChart.Series<>();

        series.setName(name);

        series.getData().add(new XYChart.Data<>("", pourcentage));

        barChart2.getData().addAll(series);
    }
    public void addSeriesMatiere(String name, Double pourcentage){

        XYChart.Series<String, Double> series = new XYChart.Series<>();

        series.setName(name);

        series.getData().add(new XYChart.Data<>("", pourcentage));

        barChart3.getData().addAll(series);
    }

    public void changerLabel(String str){
        recommandation.setText(str);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}