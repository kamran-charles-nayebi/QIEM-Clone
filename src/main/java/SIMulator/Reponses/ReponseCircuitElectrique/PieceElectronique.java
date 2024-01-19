package SIMulator.Reponses.ReponseCircuitElectrique;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.shape.Line;

import java.util.ArrayList;
import java.util.List;

import static SIMulator.Utils.Utiles.valeurScientifique;

public abstract class PieceElectronique {
    protected Node root;
    protected double valeur;

    @FXML
    protected Label label;

    protected String unite = "";

    protected TypeDePiece type;
    protected ArrayList<PieceElectronique> connectorA = new ArrayList<>();
    protected ArrayList<PieceElectronique> connectorB = new ArrayList<>();

    protected Noeud noeudA;
    protected Noeud noeudB;
    protected ArrayList<Line> fils = new ArrayList<>();

    protected int posX = 0;
    protected int posY = 0;

    public double getValeur() {
        return valeur;
    }

    public void setValeur(double valeur) {
        this.valeur = valeur;
        afficherValeur();
    }

    public Node getInterface() {
        return root;
    }

    public int getPosX() {
        return posX;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

    public int getPosY() {
        return posY;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }

    public void setFXML(String type) {
        FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("/SIMulator/fxml/Reponses/reponseCircuitElectrique/" + type + ".fxml"));
        fxmlLoader.setController(this);
        try {
            root = fxmlLoader.load();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    ;

    protected void afficherValeur() {
        label.setText(valeurScientifique(valeur) + "" + unite);
    }


    @Override
    public String toString() {
        return "PieceElectronique{" + "valeur=" + valeur + ", unite='" + unite + '\'' + ", type=" + type + " | A : " + noeudA + " | B : " + noeudB + '}';
    }

    public void ajouterFil(Line fil) {
        fils.add(fil);
    }

    public void retirerFil(Line fil) {
        fils.remove(fil);
    }

    public void clearFils() {
        fils.clear();
    }

    public ArrayList<Line> getFils() {
        return fils;
    }

    public void ajouterFils(Line... line) {
        fils.addAll(List.of(line));
    }


    public boolean isConectable(PieceElectronique pieceElectronique) {
        return true;
    }

    public boolean isConectable(PieceElectronique pieceElectronique, PieceElectronique self) {
        Boolean correct = true;
        ArrayList<PieceElectronique> piecesPrec;
        if (connectorA.contains(self)) {
            piecesPrec = connectorB;
        } else {
            piecesPrec = connectorA;
        }

        if (piecesPrec.contains(pieceElectronique)) {
            correct = false;
        }

        for (int i = 0; i < piecesPrec.size() && correct; i++) {
            if (!piecesPrec.get(i).isConectable(pieceElectronique, this)) {
                correct = false;
            }
        }

        return correct;
    }

    public TypeDePiece getType() {
        return type;
    }

    public Noeud getNoeudA() {
        return noeudA;
    }

    public void setNoeudA(Noeud noeudA) {
        this.noeudA = noeudA;
    }

    public Noeud getNoeudB() {
        return noeudB;
    }

    public void setNoeudB(Noeud noeudB) {
        this.noeudB = noeudB;
    }

    public Noeud getNoeud(int noeud) {
        if (noeud == 0) {
            return noeudA;
        } else {
            return noeudB;
        }
    }

    public void setNoeud(Noeud noeud, int idNoeud) {
        if (idNoeud == 0) {
            noeudA = noeud;
        } else {
            noeudB = noeud;
        }
    }

    public Boolean contientNoeud(Noeud noeud) {
        return noeudA == noeud || noeudB == noeud;
    }

    public Noeud getAutreNoeud(Noeud noeud) {
        if (noeud == noeudA) {
            return noeudB;
        }

        if (noeud == noeudB) {
            return noeudA;
        }
        return null;
    }

    public void seRetirerDesNoeuds() {
        if (noeudA != null) {
            noeudA.removePiece(this);
        }
        if (noeudB != null) {
            noeudB.removePiece(this);
        }
        noeudA = null;
        noeudB = null;
    }

    public String getNomComplet() {
        return type.toString().toLowerCase() + " de " + valeurScientifique(valeur) + unite;
    }


}
