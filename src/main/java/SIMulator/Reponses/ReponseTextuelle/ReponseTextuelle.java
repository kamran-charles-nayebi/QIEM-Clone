package SIMulator.Reponses.ReponseTextuelle;

import SIMulator.Reponses.Reponse;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class ReponseTextuelle extends Reponse {
    String reponse = "";
    @FXML
    private Label labelIndicatif = new Label();

    @FXML
    private TextField textFieldReponse = new TextField();


    /**
     * Vérifie si la réponse donnée par l'utilisateur est conforme à la réponse attendu par le système
     * @return (True) si la réponse est valide , (False) si la réponse n'est pas valide
     */
    public Boolean estValide() {
        return reponse.equalsIgnoreCase(textFieldReponse.getText());
    }

    /**
     * Vérifie si l'utilisateur a réponsu à la question.
     * @return (True) si l'utilisateur a entré/sélectionné une réponse , (False) si l'utilisateur n'a rien entré/selectionné
     */
    public Boolean aRepondu() {
        return !textFieldReponse.getText().isEmpty();
    }

    /**
     * Donne a l'objet la réponse que l'on attend de l'utilisateur, n'est pas case sensitive
     * @param reponse le String contenant la réponse qu'on attent de l'utilisateur
     */
    public void setReponse(String reponse){
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
     * Est un getter pour la bonne réponse enregistrée dans l'objet réponse
     * @return le String qui contient la bonne réponse
     */
    public String getReponse(){
        return reponse;
    }

    /**
     * Est un getter pour la texte contenu dans le label au dessus de la barre de réponse
     * @return un String contenu dans le label
     */
    public String getTexteIndication(){
        return labelIndicatif.getText();
    }

    /**
     * Constructeur par défaut de l'objet, doit charger le fichier FXML associé
     */
    public ReponseTextuelle() {
        configurerFXML();
    }

    private void configurerFXML(){
        FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("/SIMulator/fxml/Reponses/reponseTextuelle.fxml"));
        fxmlLoader.setController(this);
        try{
            root = fxmlLoader.load();
        }catch(Exception e){
            System.out.println(e);
        }
    }


    public ReponseTextuelle(String reponse) {
        setReponse(reponse);
        configurerFXML();
    }

    public ReponseTextuelle(String reponse,String texteIndicatif) {

        setReponse(reponse);
        configurerFXML();
        changerTexteIndication(texteIndicatif);
    }

    @Override
    public String toString() {
        return "ReponseTextuelle{" +
                "reponse='" + reponse + '\'' +
                ", labelIndicatif=" + labelIndicatif.getText() +
                '}';
    }
}
