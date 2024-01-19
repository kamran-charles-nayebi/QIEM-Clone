package SIMulator.Reponses.ReponseCircuitElectrique;

import javafx.fxml.FXMLLoader;

public class Condensateur extends PieceElectronique{

    public Condensateur() {
        type = TypeDePiece.CONDENSATEUR;
        super.setFXML("condensateur");
        this.unite = "F";
        root.setUserData(this);
    }

    public Condensateur(double valeur){
        type = TypeDePiece.CONDENSATEUR;
        super.setFXML("condensateur");
        this.unite = "F";
        setValeur(valeur);
        root.setUserData(this);
    }


    public int getCapacite(){
        int capacite = 0;
        //TODO Calculer la capacité

        return capacite;
    }


}
