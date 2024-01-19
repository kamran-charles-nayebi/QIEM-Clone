package SIMulator.Reponses.ReponseChoix;

import SIMulator.Reponses.Reponse;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.util.ArrayList;
import java.util.List;

public class ReponseChoix extends Reponse {
    String reponse = "";
    @FXML
    private Button boutonA;

    @FXML
    private Button boutonB;

    @FXML
    private Button boutonC;

    @FXML
    private Button boutonD;

    @FXML
    private Label labelIndicatif;

    private Button boutonSelectionne = null;

    /**
     * Gère les évènements lorsque l'utlisateur appui sur un bouton, puis désactive ce bouton pour montrer qu'il est sélectionné
     * @param event Le actionEvent lié au bouton
     */
    @FXML
    void buttonClick(ActionEvent event) {
        if(boutonSelectionne != null){
            boutonSelectionne.setDisable(false);
        }

        Button boutonAppuye = (Button) event.getSource();
        boutonSelectionne = boutonAppuye;
        boutonAppuye.setDisable(true);
    }


    /**
     * Vérifie si la réponse donnée par l'utilisateur est conforme à la réponse attendu par le système
     *
     * @return (True) si la réponse est valide , (False) si la réponse n'est pas valide
     */
    @Override
    public Boolean estValide() {
        if(boutonSelectionne != null){
            return (reponse.equals(boutonSelectionne.getText()));
        }else{
            return false;
        }

    }

    /**
     * Vérifie si l'utilisateur a réponsu à la question.
     *
     * @return (True) si l'utilisateur a entré/sélectionné une réponse , (False) si l'utilisateur n'a rien entré/selectionné
     */
    @Override
    public Boolean aRepondu() {
        if(boutonSelectionne == null){
            return false;
        }else{
            return true;
        }
    }

    /**
     * Retourne la réponse dans un String sous format mmentionné sur confluances
     *
     * @return la réponse associcé à l'objet réponse
     */
    @Override
    public String getReponse() {
        return reponse;
    }

    /**
     * Constructeur par defaut qui appelle configurerFXML pour mettre en place l'interface
     */
    public ReponseChoix() {
        configurerFXML();
    }

    /**
     * Permet de sauvegarder dans l'objet la réponse pour ensuite valider si celle rentrée par l'utilisateur est valide
     * @param reponse la réponse en String attendu de l'utilisateur
     */
    public void setReponse(String reponse) {
        this.reponse = reponse;
    }

    /**
     * Change le texte au dessus du textField, par défault seras "Entrez votre réponse ici"
     * @param str Le string pour remplacer le texte du label par défaut
     */
    public void changerTexteIndication(String str){
        labelIndicatif.setText(str);
    }

    /**
     * Vas charger le fichier FXML associé et met enregistre sa root pour pouvoir aller la récupérer par la suite
     */
    private void configurerFXML(){
        FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("/SIMulator/fxml/Reponses/reponseChoix.fxml"));
        fxmlLoader.setController(this);
        try{
            root = fxmlLoader.load();
        }catch(Exception e){
            System.out.println(e);
        }
    }

    /**
     * Configure tout les choix de réponses qui seront offerts à coté de la répoonse , doit en avoir au moins 3 pour fonctionner
     * @param choix Un ArrayList de strings qui contient tout les choix de réponse
     */
    public void setChoix(ArrayList<String> choix){
        ArrayList<Button> boutons = new ArrayList<Button>(List.of(new Button[]{boutonA, boutonB, boutonC, boutonD}));

        boutons.remove((int)(Math.random()*3)).setText(reponse);
        for (int i = 3; i >0 ; i--) {
            boutons.remove((int)(Math.random()*i)).setText(choix.remove((int)(Math.random()*i)));
        }
    }
}
