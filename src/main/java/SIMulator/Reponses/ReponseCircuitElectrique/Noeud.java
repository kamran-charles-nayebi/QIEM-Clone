package SIMulator.Reponses.ReponseCircuitElectrique;

import java.util.ArrayList;
import java.util.List;

public class Noeud {
    ArrayList<PieceElectronique> pieces = new ArrayList<>();
    int identite;

    public void addPiece(PieceElectronique piece) {
        if(!pieces.contains(piece)){
            pieces.add(piece);
        }
    }

    public void addPieces(PieceElectronique piece[]) {
        pieces.addAll(List.of(piece));
    }

    public ArrayList<PieceElectronique> getPieces() {
        return pieces;
    }

    public void clearPieces() {
        pieces.clear();
    }

    public void removePiece(PieceElectronique pieceElectronique) {
        pieces.remove(pieceElectronique);
    }

    public void removePieces(PieceElectronique piece[]) {
        pieces.removeAll(List.of(piece));
    }

    public Noeud() {
    }

    public int getIdentite() {
        return identite;
    }

    public void setIdentite(int identite) {
        this.identite = identite;
    }

    public int size(){
        return pieces.size();
    }

    public void clear(){
        pieces.clear();
    }

    public String toString(){
        return "Noeud " + identite;
    }

    public void seTransferer(Noeud noeud){

        for (PieceElectronique piece : pieces){
            if(piece.getNoeudA() == this){
                piece.setNoeudA(noeud);
            }
            if(piece.getNoeudB() == this){
                piece.setNoeudB(noeud);
            }
        }
        for (PieceElectronique piece : pieces){
            noeud.addPiece(piece);
        }
        pieces.clear();
    }


}
