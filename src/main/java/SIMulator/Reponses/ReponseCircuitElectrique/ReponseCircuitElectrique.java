package SIMulator.Reponses.ReponseCircuitElectrique;

import SIMulator.Reponses.Reponse;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.TextAlignment;

import java.util.ArrayList;

public class ReponseCircuitElectrique extends Reponse {
    Button boutonFils = new Button("Mettre des fils");
    Boolean modeFils = false;
    Pane pane = new Pane();
    GridPane grille = new GridPane();
    HBox banque = new HBox();
    PieceElectronique pieceDebut = null;
    int connectorDebut = -1;
    ArrayList<PieceElectronique> pieces = new ArrayList<PieceElectronique>();
    ArrayList<Noeud> noeuds = new ArrayList<Noeud>();
    Double reponse[] = new Double[]{-1.0, -1.0};

    String explications = "";
    CircuitElectrique circuitElectrique = null;
    double resistanceTot = -1;

    /**
     * Vérifie si la réponse donnée par l'utilisateur est conforme à la réponse attendu par le système
     *
     * @return (True) si la réponse est valide , (False) si la réponse n'est pas valide
     */
    @Override
    public Boolean estValide() {
        circuitElectrique = new CircuitElectrique((ArrayList<PieceElectronique>) pieces.clone(), (ArrayList<Noeud>) noeuds.clone());
        double resistance = circuitElectrique.calculerResistance();
        resistanceTot = resistance;
        explications = circuitElectrique.getExplications();
        //System.out.println("résistance = " + resistance);
        //System.out.println(circuitElectrique.getExplications());
        if (reponse[0] != -1) {
            return (reponse[0] == resistance);
        } else if (reponse[1] != -1) {
            return false; // TODO implémenter la capacitance
        } else {
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
        return true;
    }

    /**
     * Retourne la réponse dans un String sous format mmentionné sur confluances
     *
     * @return la réponse associcé à l'objet réponse
     */
    @Override
    public String getReponse() {
        String str = "";
        if (reponse[0] != -1) {
            str += reponse[0] + "Ω";
        } else if (reponse[1] != -1) {
            str += reponse[1] + "F";
        }
        return str;
    }

    public void setReponse(Double reponse[]) {
        this.reponse = reponse;
    }

    public void addPiece(PieceElectronique pieceElectronique) {
        pieces.add(pieceElectronique);
        //banque.getChildren().add(pieceElectronique.getInterface());
        pieceElectronique.setPosX(pane.getChildren().size() * 90);
        pieceElectronique.getInterface().setLayoutX(pieceElectronique.getPosX());
        pane.getChildren().add(pieceElectronique.getInterface());
        ajouterEventsPiece(pieceElectronique);
    }

    private void ajouterEventsPiece(PieceElectronique pieceElectronique) {
        pieceElectronique.getInterface().setOnMouseDragged(this::mouseDragPiece);
        pieceElectronique.getInterface().setOnMousePressed(this::mouseClickPiece);
    }

    private void mouseClickPiece(MouseEvent event) {

        int connector = -1;
        Pane paneLocal = (Pane) event.getSource();
        PieceElectronique piece = (PieceElectronique) paneLocal.getUserData();
        if (event.getX() > 40) {
            connector = 1;
        } else {
            connector = 0;
        }

        if (modeFils) {


            if (pieceDebut == null) {
                pieceDebut = piece;
                connectorDebut = connector;
                Noeud noeudDebut = pieceDebut.getNoeud(connector);
                if (noeudDebut == null) {
                    Noeud noeud = new Noeud();
                    noeud.setIdentite((int) (Math.random() * 5000));
                    noeuds.add(noeud);
                    noeud.addPiece(pieceDebut);
                    pieceDebut.setNoeud(noeud, connector);
                }
                //System.out.println(piece);
            } else {

                connectePieces(pieceDebut, piece, connectorDebut, connector);
                pieceDebut = null;
            }
        }
    }

    /**
     * Connecte les pièces ensemble avec une ligne
     *
     * @param pieceA
     * @param pieceB
     * @param connectorA
     * @param connectorB
     */
    private void connectePieces(PieceElectronique pieceA, PieceElectronique pieceB, int connectorA, int connectorB) {
        int debutX = pieceA.getPosX() - (int) pane.localToScene(pane.getBoundsInLocal()).getMinX();
        int debutY = pieceA.getPosY() + 20 - (int) pane.localToScene(pane.getBoundsInLocal()).getMinY();
        int finX = pieceB.getPosX() - (int) pane.localToScene(pane.getBoundsInLocal()).getMinX();
        int finY = pieceB.getPosY() + 20 - (int) pane.localToScene(pane.getBoundsInLocal()).getMinY();

        if (connectorA == 1) {
            debutX += 80;
        }
        if (connectorB == 1) {
            finX += 80;
        }
        int midY = (finY + debutY) / 2;
        //Line line = new Line(debutX,debutY,finX,finY);
        Line lineHorizontal1 = new Line(debutX, debutY, debutX, midY);
        Line lineVertical = new Line(debutX, midY, finX, midY);
        Line lineHorizontal2 = new Line(finX, midY, finX, finY);

        pieceA.ajouterFils(lineHorizontal1, lineVertical, lineHorizontal2);
        pieceB.ajouterFils(lineHorizontal1, lineVertical, lineHorizontal2);

        Noeud noeud = pieceA.getNoeud(connectorA);

        if (pieceB.getNoeud(connectorB) != null) {
            pieceB.getNoeud(connectorB).seTransferer(noeud);
        } else {
            pieceB.setNoeud(noeud, connectorB);
        }


        noeud.addPiece(pieceA);
        noeud.addPiece(pieceB);

        pane.getChildren().addAll(lineHorizontal1, lineVertical, lineHorizontal2);


    }

    private void mouseDragPiece(MouseEvent event) {
        Pane paneLocal = (Pane) event.getSource();
        PieceElectronique piece = (PieceElectronique) paneLocal.getUserData();
        if (!modeFils) {
            pane.getChildren().removeAll(piece.getFils());
            piece.seRetirerDesNoeuds();
            piece.clearFils();
            if(piece == pieceDebut){
                pieceDebut = null;
            }

            piece.setPosX(((int) (event.getSceneX() - 40)));
            piece.setPosY((int) (event.getSceneY() - 20));
            paneLocal.setLayoutX(event.getSceneX() - 40- (int) pane.localToScene(pane.getBoundsInLocal()).getMinX());
            paneLocal.setLayoutY(event.getSceneY() - 20- (int) pane.localToScene(pane.getBoundsInLocal()).getMinY());
            if(paneLocal.getLayoutX() < 0){
                paneLocal.setLayoutX(0);
            }else if(paneLocal.getLayoutX() > 500 - 80){
                paneLocal.setLayoutX(500 - 80);
            }
            if(paneLocal.getLayoutY() < 0){
                paneLocal.setLayoutY(0);
            }else if(paneLocal.getLayoutY() > 500 - 40){
                paneLocal.setLayoutY(500 -40);
            }
        } else {

        }

    }

    public ReponseCircuitElectrique() {
        pane.setMaxSize(500,500);
        pane.setMinSize(500,500);
        pane.setBorder(new Border(new BorderStroke(Color.rgb(3,158,211,1),BorderStrokeStyle.SOLID,new CornerRadii(5),new BorderWidths(2))));
        root = pane;
        boutonFils.setLayoutX(5);
        boutonFils.setLayoutY(5);
        boutonFils.setContentDisplay(ContentDisplay.CENTER);
        boutonFils.setStyle("-fx-background-color: #f74d37");
        boutonFils.setTextAlignment(TextAlignment.CENTER);
        boutonFils.setTextFill(Color.WHITE);
        pane.getChildren().add(boutonFils);
        boutonFils.setOnMousePressed((e) -> {
            if (!modeFils) {
                boutonFils.setStyle("-fx-background-color: #18d10f");

                modeFils = !modeFils;
            } else {
                boutonFils.setStyle("-fx-background-color: #f74d37");
                modeFils = !modeFils;
            }
        });
    }

    public String toString() {
        String str = "Reponse circuit électrique \n";
        for (int i = 0; i < pieces.size(); i++) {
            str += pieces.get(i).toString() + "\n";
        }
        str += "Reponse \n";
        CircuitElectrique circuitElectrique = new CircuitElectrique((ArrayList)pieces.clone(), (ArrayList)noeuds.clone());
        circuitElectrique.calculerResistance();
        str += circuitElectrique.toString();


        return str;
    }

    public void montrerReponse(){
        if(circuitElectrique != null && resistanceTot >= 0){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);

            alert.setTitle("Calcul du circuit");
            alert.setHeaderText("Le circuit possède une résistance de : " + resistanceTot + " Ω");
            alert.setContentText(explications);
            alert.show();
            alert.setWidth(600);
        }

    }
}
