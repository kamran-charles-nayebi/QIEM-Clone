package SIMulator.Reponses.ReponseCircuitElectrique;

public class Source extends PieceElectronique {
    public Source() {
        type = TypeDePiece.SOURCE;
        setFXML("source");
        this.unite = "V";
        root.setUserData(this);
    }
}
