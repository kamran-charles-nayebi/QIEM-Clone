package SIMulator.Reponses.ReponseChoixLineaire;

import SIMulator.Reponses.Reponse;
import SIMulator.Utils.Utiles;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import static javafx.scene.paint.Color.*;

public class ReponseChargesLineaire extends Reponse {
    int reponse[] = {0, 0};

    String chargeA = "";
    String chargeB = "";
    String chargeUtilisateur = "";


    @FXML
    private VBox groupeParticuleA;

    @FXML
    private VBox groupeParticuleB;

    @FXML
    private VBox groupeParticuleUtilisateur;

    @FXML
    private Label labelIndicatif;

    @FXML
    private Rectangle lineRectangle;

    @FXML
    private Circle particuleA;

    @FXML
    private Circle particuleB;

    @FXML
    private Circle particuleUtilisateur;

    @FXML
    private Label texteParticuleA;

    @FXML
    private Label texteParticuleB;

    @FXML
    private Label texteParticuleUtilisateur;

    @FXML
    private Circle particuleReponse;

    @FXML
    private Label texteParticuleReponse;

    @FXML
    private VBox groupeParticuleReponse;



    /**
     * Fonction appelé lorsqu'il y a un clique sur la scène pour pouvoir déplacer la particule de l'utilisateur
     *
     * @param event Le MouseEvent lié au callback
     */
    @FXML
    void userInput(MouseEvent event) {
        int posX = (int) (event.getScreenX() - lineRectangle.localToScreen(lineRectangle.getBoundsInLocal()).getMinX());
        posX = posX + 50;
        if (posX > 475) {
            posX = 475;
        } else if (posX < 50) {
            posX = 50;
        }
        particuleUtilisateur.setVisible(true);
        groupeParticuleUtilisateur.setTranslateX(posX - 10);
        texteParticuleUtilisateur.setVisible(true);
    }

    /**
     * Stocke en mémoire la bonne réponse pour ensuite valider la réponse de l'utilisateur
     * La réponse doit être [0,100] , 0 étant tout à gauche, 100 tout à droite
     *
     * @param reponseMin la position minimale que l'utilisateur doit mettre
     * @param reponseMax la position maximale que l'utilisateur doit mettre
     */
    public void setReponse(int reponseMin, int reponseMax) {
        this.reponse[0] = reponseMin;
        this.reponse[1] = reponseMax;
    }

    /**
     * Positionne la particule A à l'endroit désiré sur la barre
     *
     * @param position La position que l'on veut pour la particule B entre 0 et 100
     */
    public void setParticuleAPosition(int position) {
        particuleA.setTranslateX(calculerTranslate(position));
        texteParticuleA.setTranslateX(calculerTranslate(position));
    }

    /**
     * Positionne la particule B à l'endroit désiré sur la barre
     *
     * @param position La position que l'on veut pour la particule B entre 0 et 100
     */
    public void setParticuleBPosition(int position) {
        particuleB.setTranslateX(calculerTranslate(position));
        texteParticuleB.setTranslateX(calculerTranslate(position));
    }

    /**
     * Prend une position en % (entre 0 et 100) et calcule la translation que l'on doit faire
     *
     * @param translate la position
     * @return la translation entre 50 et 475
     */
    private int calculerTranslate(int translate) {
        if (translate > 0) {
            translate = 425 * translate / 100;
        }
        translate = translate + 50;
        if (translate < 50) {
            translate = 50;
        } else if (translate > 475) {
            translate = 475;
        }
        return translate;
    }

    /**
     * Prend la translation donnée d'une particule pour en sortir sa position en %
     *
     * @param position la position ( de la translation ) à convertir
     * @return la position en % convertit
     */
    private int calculerPosition(int position) {
        position = position - 50;
        if (position > 0) {
            position = 100 * position / 425;
        }
        return position;
    }

    /**
     * Calcule la position de la particule de l'utilisteur en %,
     * 0 étant  tout à gauche, 100 étant à droite
     *
     * @return la position de la particule entre 0 et 100
     */
    private int getPositionParticuleUtilisateur() {
        return calculerPosition((int) groupeParticuleUtilisateur.getTranslateX());
    }

    public void setTexteIndicatif(String str) {
        labelIndicatif.setText(str);
    }

    /**
     * Permet de configurer la charge (C) de la première particule.
     * Met la bonne couleur (Rouge / noir) puis sa charge dans son label
     *
     * @param str la valeur de sa charge en coulomb en notation scientifique
     */
    public void setChargeParticuleA(String str) {
        chargeA = str;
        setParticule(particuleA, str);
        texteParticuleA.setText(Utiles.valeurScientifique(str) + "C");
    }

    /**
     * Permet de configurer la charge (C) de la deuxième particule.
     * Met la bonne couleur (Rouge / noir) puis sa charge dans son label
     *
     * @param str la valeur de sa charge en coulomb en notation scientifique
     */
    public void setChargeParticuleB(String str) {
        chargeB = str;
        setParticule(particuleB, str);
        texteParticuleB.setText(Utiles.valeurScientifique(str) + "C");
    }

    /**
     * Configure la couleur de la particule selon sa polarité
     *
     * @param particule la particule à modifier
     * @param valeur    la valeur de sa charge en notation scientifique
     */
    private void setParticule(Circle particule, String valeur) {
        if (trouverPolarite(valeur) > 0) {
            Stop[] stops = new Stop[] { new Stop(0, Color.RED), new Stop(1, INDIANRED)};
            LinearGradient lg1 = new LinearGradient(0, 0, 1, 0, true, CycleMethod.NO_CYCLE, stops);
            particule.setFill(lg1);
        } else {
            Stop[] stops = new Stop[] { new Stop(0, BLACK), new Stop(1, GREY)};
            LinearGradient lg1 = new LinearGradient(0, 0, 1, 0, true, CycleMethod.NO_CYCLE, stops);
            particule.setFill(lg1);
        }
    }

    /**
     * Trouver la polarité d'une charge à partir d'une string de la charge en notation scientifique
     *
     * @param charge La charge qu'il faut trouver la polarité
     * @return 1 si la particule est positive, -1 si elle est négative
     */
    private int trouverPolarite(String charge) {
        if (charge.contains(" -") || (charge.indexOf("-") < 3 && charge.contains("-"))) {
            return -1;
        } else {
            return 1;
        }
    }

    /**
     * Donne la charge de la particule que l'utilisateur devras placer pour équilibrer
     *
     * @param str Le string contenant la charge (en Coulombs) sous forme scientifique
     */
    public void setChargeParticuleUtilisateur(String str) {
        chargeUtilisateur = str;
        setParticule(particuleUtilisateur, str);
        texteParticuleUtilisateur.setText(Utiles.valeurScientifique(str) + "C");
    }

    /**
     * Vérifie si la réponse donnée par l'utilisateur est conforme à la réponse attendu par le système
     *
     * @return (True) si la réponse est valide , (False) si la réponse n'est pas valide
     */
    public Boolean estValide() {
        int position = getPositionParticuleUtilisateur();

        return (position > reponse[0] && position < reponse[1]);
    }

    /**
     * Vérifie si l'utilisateur a réponsu à la question.
     *
     * @return (True) si l'utilisateur a entré/sélectionné une réponse , (False) si l'utilisateur n'a rien entré/selectionné
     */
    public Boolean aRepondu() {
        return (particuleUtilisateur.isVisible());

    }

    /**
     * Retourne la réponse dans un String sous format mmentionné sur confluances
     *
     * @return la réponse associcé à l'objet réponse
     */
    @Override
    public String getReponse() {
        return reponse[0] + ":" + reponse[1];
    }

    /**
     * Vas charger le fichier FXML associé et met enregistre sa root pour pouvoir aller la récupérer par la suite
     */
    private void configurerFXML() {
        FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("/SIMulator/fxml/Reponses/reponseChargesLineaire.fxml"));
        fxmlLoader.setController(this);
        try {
            root = fxmlLoader.load();

        } catch (Exception e) {
            System.out.println(e);
        }
        groupeParticuleA.toFront();
        groupeParticuleB.toFront();
        groupeParticuleUtilisateur.toFront();
        lineRectangle.toBack();
        groupeParticuleReponse.toFront();




    }

    public ReponseChargesLineaire() {
        configurerFXML();
    }


    public void montrerReponse() {
        int positionMoyenne = calculerTranslate((reponse[0] + reponse[1]) / 2);
        groupeParticuleReponse.setVisible(true);
        groupeParticuleReponse.setTranslateX(positionMoyenne);


    }
}
