package SIMulator.ModelesQuestions;

import SIMulator.ClassesQuestions.Question;
import SIMulator.Quiz;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.util.Callback;
import org.mariuszgromada.math.mxparser.Argument;
import org.mariuszgromada.math.mxparser.Expression;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Controlleur pour la question de raisonnement logique
 */
public class DemarcheControlleur {

    @FXML
    private ListView<Argument> listViewVariables;
    @FXML
    private StackPane stackPane;
    @FXML
    private Label texte;
    @FXML
    private Label labelChapitre;
    @FXML
    private Button boutonVerifier;

    @FXML
    private Button boutonCalculer;
    @FXML
    private TextArea demarcheTextArea;
    @FXML
    private Label labelProgression;
    @FXML
    private HBox hBoxBoutons;
    private boolean verrouille = false;

    private String reponse;
    private Quiz quiz;
    private Question question;

    private List<Argument> variables;

    @FXML
    void initialize() {
        verrouille = false;
        boutonVerifier.setOnAction(this::verifierReponse);
        boutonCalculer.setOnAction(this::calculerVariable);
        listViewVariables.setCellFactory(new Callback<ListView<Argument>, ListCell<Argument>>() {
            @Override
            public ListCell<Argument> call(ListView<Argument> list) {
                ListCell<Argument> cell = new ListCell<Argument>() {
                    @Override
                    public void updateItem(Argument item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item != null) {
                            Label affichage;
                            if (!Double.isNaN(item.getArgumentValue())){
                                setEventHandler(MouseEvent.MOUSE_CLICKED, e->{});
                                affichage = new Label(item.getArgumentName() + "=" + String.format("%4.1e",item.getArgumentValue()));
                            }
                            else{
                                affichage = new Label(item.getArgumentName() + "= ?");
                                affichage.setTextFill(Paint.valueOf("#f74d37"));
                                setEventHandler(MouseEvent.MOUSE_CLICKED, e -> montreMenuVariable(item));
                            }
                            affichage.setFont(new Font(16));
                            setGraphic(affichage);
                        }
                    }
                };
                return cell;
            }
        });
    }

    private void verifierReponse(ActionEvent event) {
        if (estValide()) {
            texte.setText("Bonne réponse! Bien joué!");
            question.setReussi(true);
        } else {
            texte.setText("Mauvaise réponse, la bonne réponse est " + String.format(Locale.US,"%4.1e", new Expression(reponse.split(":")[1]).calculate()) + "\n\n\n" + question.getSolution());
            question.setReussi(false);
        }
        listViewVariables.setDisable(true);
        hBoxBoutons.getChildren().remove(boutonCalculer);
        boutonVerifier.setText("Prochaine question");
        boutonVerifier.setOnAction(this::prochaineQuestion);

    }

    private void calculerVariable(ActionEvent event){
        if (!verrouille) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/SIMulator/fxml/Questions/MenuVariables.fxml"));
                stackPane.getChildren().add(loader.load());
                verrouille = true;
                ((MenuVariables)loader.getController()).setParentVariables(this, variables);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean estValide(){
        boolean resultat = false;
        for (Argument variable : variables) {
            reponse = reponse.replace("{"+variable.getArgumentName()+"}",String.format(Locale.US,"%4.1e",variable.getArgumentValue()));
        }
        String stringReponse = String.format(Locale.US,"%4.1e",new Expression(reponse.split(":")[1]).calculate());
        if (!stringReponse.equals("NaN")) {
            // Méthode grossière de prendre compte des différences d'arrondissements
            if (stringReponse.equals(reponse.split(":")[0].trim())) {
                resultat = true;
            }
            stringReponse = (Double.parseDouble(stringReponse.substring(0, 2)) + 0.1) + stringReponse.substring(3);
            if (stringReponse.equals(reponse.split(":")[0].trim()))
                resultat = true;
            stringReponse = (Double.parseDouble(stringReponse.substring(0, 2)) - 0.2) + stringReponse.substring(3);
            if (stringReponse.equals(reponse.split(":")[0].trim()))
                resultat = true;
        }
        return resultat;
    }

    private void prochaineQuestion(ActionEvent event){
        if (!question.getTest()) {
            quiz.lancerQuestion();
        } else quiz.lancerQuestion2();
    }

    private void montreMenuVariable(Argument variable) {
        if (!verrouille) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/SIMulator/fxml/Questions/MenuVariables.fxml"));
                stackPane.getChildren().add(loader.load());
                verrouille = true;
                ((MenuVariables)loader.getController()).setParentVariables(this, variables, variable);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void deverrouiller(Argument variableChange){
        verrouille = false;
        stackPane.getChildren().remove(1);
        boolean toutValide = true;
        for (int i = 0; i < variables.size(); i++) {
            listViewVariables.getItems().remove(i);
            if (variables.get(i).getArgumentName().equals(variableChange.getArgumentName())) {
                variables.set(i,variableChange);
            }
            listViewVariables.getItems().add(i, variables.get(i));
            if (Double.isNaN(variables.get(i).getArgumentValue()))
                toutValide = false;
        }
        if (toutValide)
            boutonCalculer.setDisable(true);
    }

    public void setQuestion(Question _question){
        question = _question;
        texte.setText(question.getQuestion());
        labelChapitre.setText(question.getChapitre());
        quiz = question.getQuiz();
        reponse = question.getReponse();
        variables = question.getVariables();
        listViewVariables.getItems().addAll(variables);
        this.labelProgression.setText("Question " + Integer.toString(quiz.getCompteur()+1) + "/" + Quiz.NOMBRE_DE_QUESTIONS);
    }

    public void ajouterDemarche(String demarche){
        if (demarcheTextArea.getText().equals("Démarche"))
            demarcheTextArea.setText("");
        demarcheTextArea.setText(demarcheTextArea.getText()+demarche);
    }


}
