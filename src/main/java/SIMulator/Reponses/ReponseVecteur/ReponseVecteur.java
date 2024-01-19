package SIMulator.Reponses.ReponseVecteur;
import SIMulator.Reponses.Reponse;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;

import java.util.Arrays;


public class ReponseVecteur extends Reponse {
    private int[] reponse = new int[4];
    @FXML
    private Pane mainPane;
    int minX;
    int maxX;
    int stepX;
    int stepY;
    int minY;
    int maxY;
    int nbY;
    int nbX;
    Line lineUtilisateur = new Line();

    /**
     * Vérifie si la réponse donnée par l'utilisateur est conforme à la réponse attendu par le système
     *
     * @return (True) si la réponse est valide , (False) si la réponse n'est pas valide
     */
    @Override
    public Boolean estValide() {
        int[] reponseUtilisateur = getPosVecteur();
        return estVecteursProches(reponseUtilisateur,reponse,(stepX / 2));
    }

    /**
     * Vérifie si l'utilisateur a réponsu à la question.
     *
     * @return (True) si l'utilisateur a entré/sélectionné une réponse , (False) si l'utilisateur n'a rien entré/selectionné
     */
    @Override
    public Boolean aRepondu() {
        return (lineUtilisateur.isVisible());
    }

    /**
     * Retourne la réponse dans un String sous format mmentionné sur confluances
     *
     * @return la réponse associcé à l'objet réponse
     */
    @Override
    public String getReponse() {
        return reponse[0] + ";" + reponse[1] + ";" + reponse[2] + ";" + reponse[3];
    }

    /**
     * Pour configurer la bonne réponse, il faut lui donner un array de vecteur de réponse construit comme-suit :
     * DébutX ; Début Y ; FinX ; FinY
     * ex : 0;0;10;20
     * @param reponse le array de 4 nombre construit comme mentionné
     */
    public void setReponse(int[] reponse){
        this.reponse = reponse;
    }

    /**
     * Contructeur par défault, appelle la méthode qui s'occupe du fichier FXML
     */
    public ReponseVecteur() {
        configurerFXML();
    }

    /**
     * Donne tout les informations pour créé le plan cartésien
     * @param infos Les inforamtions dans un array de int de taille 6 sous cette forme (int minX, int maxX, int stepX, int minY, int maxY, int stepY)
     */
    public void setGrilleVecteurs(int[] infos){
        setGrilleVecteurs(infos[0],infos[1],infos[2],infos[3],infos[4],infos[5]);
    }

    /**
     * Ajout à la grille un point (cercle) qui peux représenter par exemple une particule
     * @param x position sur le plan cartésien en X
     * @param y position sur le plan cartésien en Y
     * @param text Texte à mettre en dessous du cercle
     * @param couleur La couleur ( API JavaFX ) du cercle
     */
    public void addPoint(int x,int y,String text,Color couleur){
        Circle circle = new Circle(trouverCoordoneeX(x),trouverCoordoneeY(y),6, couleur);
        addActionsMouse(circle);
        Text textObj = new Text(text);
        textObj.setX(trouverCoordoneeX(x - 5));
        textObj.setY(trouverCoordoneeY(y + 3));
        mainPane.getChildren().add(textObj);
        mainPane.getChildren().add(circle);
    }

    /**
     * Ajout à la grille un point (cercle) avec comme couleur gris qui peut représenter par exemple une particule
     * @param x position sur le plan cartésien en X
     * @param y position sur le plan cartésien en Y
     * @param text Texte à mettre en dessous du cercle
     */
    public void addPoint(int x,int y,String text){
        addPoint(x,y,text,Color.GRAY);
    }

    /**
     * Configure le plan cartésien en fonction des paramètres suivant
     * @param minX Le X minimum
     * @param maxX Le X maximum
     * @param stepX Les écarts entre chaque point en X
     * @param minY Le y minimum
     * @param maxY Le y maximum
     * @param stepY Les écarts entre chaque point en Y
     */
    public void setGrilleVecteurs(int minX, int maxX, int stepX, int minY, int maxY, int stepY) {
        this.minX = minX;
        this.maxX = maxX;
        this.stepX = stepX;
        this.minY = minY;
        this.maxY = maxY;
        this.stepY = stepY;

        this.nbX = (maxX - minX) / stepX;
        this.nbY = (maxY - minY) / stepY;
        for (int x = minX; x < maxX; x += stepX) {
            for (int y = minY; y < maxX; y += stepY) {
                Circle circle = new Circle(trouverCoordonee(x, minX, maxX), trouverCoordonee(y, minY, maxY), 3);
                addActionsMouse(circle);
                mainPane.getChildren().add(circle);
            }
        }

        Line axeY = new Line(trouverCoordonee(0, minX, maxX), 0, trouverCoordonee(0, minX, maxX), 400);
        mainPane.getChildren().add(axeY);

        Line axeX = new Line(0, trouverCoordoneeY(0), 400, trouverCoordoneeY(0));
        mainPane.getChildren().add(axeX);

        lineUtilisateur.setVisible(false);

        mainPane.getChildren().add(lineUtilisateur);
    }

    /**
     * Setup tout les mouseEvent sur les cercles constituant la grille
     * @param circle Le cercle que l'on veut configurer les mouseEvent
     */
    private void addActionsMouse(Circle circle) {
        circle.setOnMousePressed((k) -> {
            mainPane.getChildren().remove(lineUtilisateur);
            lineUtilisateur = new Line();
            Circle circleApp = (Circle) k.getSource();
            lineUtilisateur.setStartY(circleApp.getCenterY());
            lineUtilisateur.setStartX(circleApp.getCenterX());
        });

        circle.setOnMouseReleased((k) -> {
            lineUtilisateur.setEndY(k.getY());
            lineUtilisateur.setEndX(k.getX());
            lineUtilisateur.setVisible(true);
            System.out.println(lineUtilisateur);
            mainPane.getChildren().add(lineUtilisateur);
            System.out.println(Arrays.toString(getPosVecteur()));
        });
    }

    /**
     * Vas récupérer le vecteur dessiné par l'utilisateur
     * @return Le vecteur construit dans un array (DébutX,DébutY,FinX,FinY)
     */
    private int[] getPosVecteur() {
        int[] vecteur = new int[4];
        vecteur[0] = trouverX((int) lineUtilisateur.getStartX());
        vecteur[1] = trouverY((int) lineUtilisateur.getStartY());
        vecteur[2] = trouverX((int) lineUtilisateur.getEndX());
        vecteur[3] = trouverY((int) lineUtilisateur.getEndY());
        return vecteur;
    }

    /**
     * Regarde si la valeur donnée est proche de la valeur de référence selon l'incertitude donnée
     * @param value La valeur à regarder
     * @param refValue La valeur de référence
     * @param incertitude L'incertitude pour valider
     * @return True si la valeur est dans l'incertitude, false si elle ne l'ai pas
     */
    private boolean estProche(int value,int refValue,int incertitude){
        return (value + incertitude > refValue && value - incertitude < refValue);
    }

    /**
     * Regarde si les deux vecteurs sont proches en tenant compte de la tolerance ( incertitude )
     * @param reponse Le premier vecteur, donné par l'utilisateur
     * @param comparaison Le deuxième vecteur, qui est la réponse
     * @param tolerance La tolérance (incertitude) entre les deux vecteurs
     * @return True si ils sont semblables, false sinon
     */
    private boolean estVecteursProches(int[] reponse,int[] comparaison,int tolerance){
        boolean estPareil = true;
        for (int i = 0; i < reponse.length; i++) {
            if(!estProche(comparaison[i],reponse[i],tolerance )){
                estPareil = false;
            }
        }
        return estPareil;
    }

    /**
     * Transforme les positions des cercles en coordonnées cartésiens
     * @param coordonne La coordonné à transformer
     * @param min Le minimum de l'axe
     * @param max Le maximum de l'axe
     * @return La coordonnée cartésienne
     */
    private int trouverCoordonee(int coordonne, int min, int max) {
        int coordonnePos = coordonne - min;
        int intervalle = (max - min);
        return 400 * coordonnePos / intervalle;
    }

    /**
     * Transforme les positions des cercles en coordonnées cartésiens en x
     * @param coordonne La coordonné à transformer
     * @return La coordonnée cartésienne en X
     */
    private int trouverCoordoneeX(int coordonne) {
        int coordonnePos = coordonne - minX;
        int intervalle = (maxX - minX);
        return 400 * coordonnePos / intervalle;
    }

    /**
     * Transforme les positions des cercles en coordonnées cartésiens en Y
     * @param coordonne La coordonné à transformer
     * @return La coordonnée cartésienne en Y
     */
    private int trouverCoordoneeY(int coordonne) {
        int coordonnePos = coordonne - minY;
        int intervalle = (maxY - minY);
        return 400 - (400 * coordonnePos / intervalle);
    }

    /**
     * Tranforme les X (cartésiens) en translation
     * @param X
     * @return la translation correspondant
     */
    private int trouverX(int X) {
        int intervalle = (maxX - minX);
        int XPosition = intervalle * X / 400;
        XPosition = XPosition + minX;
        return XPosition;
    }

    /**
     * Tranforme les Y (cartésiens) en translation
     * @param Y
     * @return la translation correspondant
     */
    private int trouverY(int Y) {
        int intervalle = (maxY - minY);
        int YPos = intervalle * (400 - Y) / 400;
        YPos = YPos + minY;
        return YPos;
    }


    /**
     * Load et configure le fichier FXML associé
     */
    private void configurerFXML() {
        FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("/SIMulator/fxml/Reponses/reponseVecteur.fxml"));
        fxmlLoader.setController(this);
        try {
            root = fxmlLoader.load();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
