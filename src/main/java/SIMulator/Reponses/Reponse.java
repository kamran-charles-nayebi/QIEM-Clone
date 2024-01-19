package SIMulator.Reponses;

import javafx.scene.Node;

public abstract class Reponse {
    protected Node root;

    /**
     * Est un getter de l'inteface graphique pour les réponses
     * @return La Node à la base de l'interface graphique, provennant du fichier FXML associé au type de réponse
     */
    public Node getInterface(){
        return root;
    }

    /**
     * Vérifie si la réponse donnée par l'utilisateur est conforme à la réponse attendu par le système
     * @return (True) si la réponse est valide , (False) si la réponse n'est pas valide
     */
    public abstract Boolean estValide();

    /**
     * Vérifie si l'utilisateur a réponsu à la question.
     * @return (True) si l'utilisateur a entré/sélectionné une réponse , (False) si l'utilisateur n'a rien entré/selectionné
     */
    public abstract Boolean aRepondu();

    /**
     * Retourne la réponse dans un String sous format mmentionné sur confluances
     * @return la réponse associcé à l'objet réponse
     */
    public abstract String getReponse();

    public void montrerReponse(){

    }
}
