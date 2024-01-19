package SIMulator.Reponses.ReponseCircuitElectrique;

public class Resistance extends PieceElectronique{
    public Resistance() {
        type = TypeDePiece.RÉSISTANCE;
        setFXML("resistance");
        this.unite = "Ω";
        root.setUserData(this);
    }

    public Resistance(double valeur){
        type = TypeDePiece.RÉSISTANCE;
        setFXML("resistance");
        this.unite = "Ω";
        setValeur(valeur);
        root.setUserData(this);
    }

    public int getResistance(){
        int resistance = 0;
        //TODO Calculer la resistance du circuit

        return resistance;
    }


}
