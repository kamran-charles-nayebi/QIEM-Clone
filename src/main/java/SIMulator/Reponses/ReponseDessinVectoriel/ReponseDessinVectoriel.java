package SIMulator.Reponses.ReponseDessinVectoriel;

import SIMulator.Reponses.Reponse;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

import java.util.ArrayList;
import java.util.Arrays;

public class ReponseDessinVectoriel extends Reponse {
    private double minX;
    private double maxX;
    private double stepX;
    private double minY;
    private double maxY;
    private double stepY;
    private double zeroX;
    private double zeroY;
    private double startX;
    private double startY;
    private Group canvasGroup;
    private Canvas canvas;
    private GraphicsContext gc;

    private double[] vecteurUtilisateur;
    private double[] bonneReponse;

    private ArrayList<PointCartesien> points = new ArrayList<>();

    /**
     * Constructeur par default pour le dessin vectoriel, on doit lui passer tout les informations de la grille
     *
     * @param minX
     * @param maxX
     * @param stepX
     * @param minY
     * @param maxY
     * @param stepY
     */
    public ReponseDessinVectoriel(double minX, double maxX, double stepX, double minY, double maxY, double stepY) {
        this.minX = minX;
        this.maxX = maxX;
        this.stepX = stepX;
        this.minY = minY;
        this.maxY = maxY;
        this.stepY = stepY;
        this.zeroX = -minX / (maxX - minX) * 500;
        this.zeroY = maxY / (maxY - minY) * 500;
        canvas = new Canvas(500, 500);
        initCanvas();
        canvasGroup = new Group(canvas);
        canvas.setOnMousePressed(this::handleMousePressed);
        canvas.setOnMouseDragged(this::onCanvasMouseDragged);
        canvas.setOnMouseReleased(this::handleMouseReleased);
        bonneReponse = new double[]{0, 0, 0, 0};
        vecteurUtilisateur = new double[]{-1, -1, -1, -1};
    }

    /**
     * Donne la bonne réponse à l'objet afin de pouvoir valider la réponse utilisateur
     *
     * @param bonneReponse La bonne réponse sous forme {débutx,débuty,finx,finy} pour un total de double[4]
     */
    public void setReponse(double[] bonneReponse) {
        this.bonneReponse = bonneReponse;
    }

    /**
     * Dessine la grille et les points du plan cartésien
     */
    private void initCanvas() {

        gc = canvas.getGraphicsContext2D();

        gc.setStroke(Color.LIGHTGRAY);
        gc.setLineWidth(0.5);
        for (double x = minX; x <= maxX; x += stepX) {
            double pos = (x - minX) / (maxX - minX) * 500;
            gc.strokeLine(pos, 0, pos, 500);
        }
        for (double y = minY; y <= maxY; y += stepY) {
            double pos = (maxY - y) / (maxY - minY) * 500;
            gc.strokeLine(0, pos, 500, pos);
        }

        gc.setStroke(Color.BLACK);
        gc.setLineWidth(1.5);
        gc.strokeLine(zeroX, 0, zeroX, 500);
        gc.strokeLine(0, zeroY, 500, zeroY);
        dessinerPoints();
    }

    /**
     * Gére l'animation de la flèche lorsque l'utilisateur drag
     *
     * @param event Le mouseEvent
     */
    private void onCanvasMouseDragged(MouseEvent event) {
        gc.clearRect(0, 0, 500, 500);
        initCanvas();
        gc.setStroke(Color.RED);
        gc.setLineWidth(2);

        dessinerVecteur(event);
    }

    /**
     * Dessine le vecteur avec une flèche au bout, ayant comme point initial le startPos et comme finale les coordonées du mouse Event
     *
     * @param event Le mouseEvent lié
     */
    private void dessinerVecteur(MouseEvent event) {
        double endX = event.getX();
        double endY = event.getY();
        double[] snapStart = snapToGrid(startX / 500 * (maxX - minX) + minX, maxY - startY / 500 * (maxY - minY));
        double[] snapEnd = snapToGrid(endX / 500 * (maxX - minX) + minX, maxY - endY / 500 * (maxY - minY));

        if (!(snapStart[0] == snapEnd[0] && snapStart[1] == snapEnd[1])) {


            gc.setStroke(Color.RED);
            gc.setLineWidth(2);
            double[] startPos = cartesianToPixel(snapStart[0], snapStart[1]);
            double[] endPos = cartesianToPixel(snapEnd[0], snapEnd[1]);
            gc.strokeLine(startPos[0], startPos[1], endPos[0], endPos[1]);

            double angle = Math.atan2(endPos[1] - startPos[1], endPos[0] - startPos[0]);
            double arrowSize = 10;
            gc.strokeLine(endPos[0], endPos[1], endPos[0] - arrowSize * Math.cos(angle - Math.PI / 6), endPos[1] - arrowSize * Math.sin(angle - Math.PI / 6));
            gc.strokeLine(endPos[0], endPos[1], endPos[0] - arrowSize * Math.cos(angle + Math.PI / 6), endPos[1] - arrowSize * Math.sin(angle + Math.PI / 6));
            vecteurUtilisateur = fusionneMatrice(pixelToCartesian(startPos), pixelToCartesian(endPos));
        } else {
            vecteurUtilisateur = new double[]{0, 0, 0, 0};
        }
    }

    private void dessinerVecteur(double debutX,double debutY,double finX,double finY,Color color){
        double[] snapStart = snapToGrid(debutX / 500 * (maxX - minX) + minX, maxY - debutY / 500 * (maxY - minY));
        double[] snapEnd = snapToGrid(finX / 500 * (maxX - minX) + minX, maxY - finY / 500 * (maxY - minY));

        gc.setStroke(color);
        gc.setLineWidth(2);
        double[] startPos = cartesianToPixel(snapStart[0], snapStart[1]);
        double[] endPos = cartesianToPixel(snapEnd[0], snapEnd[1]);
        gc.strokeLine(startPos[0], startPos[1], endPos[0], endPos[1]);

        double angle = Math.atan2(endPos[1] - startPos[1], endPos[0] - startPos[0]);
        double arrowSize = 10;
        gc.strokeLine(endPos[0], endPos[1], endPos[0] - arrowSize * Math.cos(angle - Math.PI / 6), endPos[1] - arrowSize * Math.sin(angle - Math.PI / 6));
        gc.strokeLine(endPos[0], endPos[1], endPos[0] - arrowSize * Math.cos(angle + Math.PI / 6), endPos[1] - arrowSize * Math.sin(angle + Math.PI / 6));
    }

    public void montrerReponse(){
        double[] debut = cartesianToPixel(bonneReponse[0],bonneReponse[1]);
        double[] fin = cartesianToPixel(bonneReponse[2],bonneReponse[3] );
        dessinerVecteur(debut[0],debut[1],fin[0],fin[1],Color.LIMEGREEN );
    }

    /**
     * Fusionne deux matrices double[] pour en former une seule
     *
     * @param premier  La première matrice double[]
     * @param deuxieme la deuxième matrice double[]
     * @return La matrice fusionnée double[]
     */
    private double[] fusionneMatrice(double[] premier, double[] deuxieme) {
        double[] matrice = new double[premier.length + deuxieme.length];
        for (int i = 0; i < premier.length; i++) {
            matrice[i] = premier[i];
        }
        for (int i = premier.length; i < matrice.length; i++) {
            matrice[i] = deuxieme[i - premier.length];
        }
        return matrice;
    }

    /**
     * Event déclanché quand l'utilisateur appuie sur la grille, enregistre la position comme position initiale du vecteur
     *
     * @param event Le mouseEvent relié
     */
    private void handleMousePressed(MouseEvent event) {
        startX = event.getX();
        startY = event.getY();

    }

    /**
     * Dessine le vecteur finale en fonction de la position du clique
     *
     * @param event Le mouse event relié
     */
    private void handleMouseReleased(MouseEvent event) {
        dessinerVecteur(event);

    }

    /**
     * Convertit des coordonées cartésiennes en pixels, utilisable par JavaFX
     *
     * @param x La position cartésienne en X
     * @param y La position cartésienne en Y
     * @return Les positions en pixels sous forme {x,y} -> double[2]
     */
    private double[] cartesianToPixel(double x, double y) {
        double[] result = new double[2];
        result[0] = x / (maxX - minX) * 500;
        //result[0] = 500*((x - minX)/(maxX - minX));
        result[1] = (maxY - y) / (maxY - minY) * 500;
        return result;
    }

    /**
     * Convertit des positions de pixels en coordonées cartésiennes, utilisable par les utilisateurs
     *
     * @param x La position pixel X
     * @param y La position pixel Y
     * @return Les positions cartésiennes sous forme {x,y} -> double[2]
     */
    private double[] pixelToCartesian(double x, double y) {
        double cartesianX = (x / canvas.getWidth()) * (maxX - minX) + minX;
        double cartesianY = (1 - (y / canvas.getHeight())) * (maxY - minY) + minY;
        return new double[]{cartesianX, cartesianY};
    }

    private double[] pixelToCartesian(double[] pos) {
        return pixelToCartesian(pos[0], pos[1]);
    }

    /**
     * Arrondi la position des pixels pour qu'ils corresponde à ceux d'intersection de lignes la plus proche
     *
     * @param x La position pixel en X
     * @param y La position pixel en Y
     * @return La position snapper sur la grille sous forme {x,y} -> double[2]
     */
    private double[] snapToGrid(double x, double y) {
        double[] result = new double[2];


        // Calculer le nombre entier de steps à partir de la position x et y
        double stepsX = Math.round((x - minX) / stepX);
        double stepsY = Math.round((maxY - y) / stepY);

        // Calculer la position en pixels à partir du nombre de steps
        double newX = stepsX * stepX;
        double newY = maxY - stepsY * stepY;

        // Si la position en pixels est en dehors de la plage valide, ramener la position sur le bord
        /*if (newX < minX) {
            newX = minX;
        } else if (newX > maxX) {
            newX = maxX;
        }
        if (newY < minY) {
            newY = minY;
        } else if (newY > maxY) {
            newY = maxY;
        }*/

        result[0] = newX;
        result[1] = newY;
        return result;
    }


    /**
     * Retourne la node root pour avoir l'interface graphique
     *
     * @return la node en dessous
     */
    public Node getInterface() {
        initCanvas();
        return new StackPane(canvasGroup);
    }

    /**
     * Vérifie si le vecteur entrée par l'utilisateur est identique à celui de la réponse
     *
     * @return True si le vecteur entré par l'utilisateur est identique, sinon False
     */
    public Boolean estValide() {
        boolean valide = true;
        for (int i = 0; i < 4; i++) {
            if ((int) vecteurUtilisateur[i] != (int) bonneReponse[i]) {
                valide = false;
            }
        }
        return valide;
    }

    /**
     * Vérifie si l'utilisateur à entré une réponse ou non
     *
     * @return True si il a entré une reponse, sinon False
     */
    public Boolean aRepondu() {
        return !(vecteurUtilisateur[0] == vecteurUtilisateur[2] && vecteurUtilisateur[1] == vecteurUtilisateur[3]);
    }

    /**
     * Retourne la bonne réponse sous la même forme fournie
     *
     * @return Retourne la réponse sous forme de double[] -> {startX,startY,endX,endY}
     */
    public String getReponse() {
        return Arrays.toString(vecteurUtilisateur);
    }

    /**
     * Dessine tout les points sur le plan cartésien
     */
    private void dessinerPoints() {
        for (PointCartesien point : points) {
            gc.setFill(point.getCouleur());
            double[] coordonees = point.getCoordonees();
            double[] coordoneesPixels = cartesianToPixel(coordonees[0], coordonees[1]);
            double rayon = point.getRayon();
            double pointX = coordoneesPixels[0] - (rayon / 2) + 250;
            double pointY = coordoneesPixels[1] - (rayon / 2);
            gc.fillOval(pointX, pointY, rayon, rayon);

            gc.setFill(Color.BLACK);
            gc.setTextAlign(TextAlignment.CENTER);
            gc.setFont(new Font("Verdana", 15));
            gc.fillText(point.getTextePoint(), pointX + rayon / 2, pointY + rayon / 1.5);
            gc.setFont(new Font("Verdana", 10));
            gc.fillText(point.getTexteSousPoint(), pointX + rayon / 2, pointY + rayon * 1.6);
        }
    }

    /**
     * Ajoute un point au plan cartésien
     *
     * @param pointCartesien Le point que l'on souhaite ajouter
     */
    public void ajouterPoint(PointCartesien pointCartesien) {
        points.add(pointCartesien);
    }

}
