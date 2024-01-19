package SIMulator.Reponses.ReponseCircuitElectrique;

import java.util.ArrayList;

public class CircuitElectrique {
    ArrayList<PieceElectronique> pieces;

    PieceElectronique source;
    ArrayList<Noeud> noeuds;
    Noeud noeudDebut;
    Noeud noeudFin;
    String explications = "";

    public CircuitElectrique(ArrayList<PieceElectronique> pieces, ArrayList<Noeud> noeuds) {
        this.pieces = pieces;
        this.noeuds = noeuds;
        identifierNoeuds();
        trouverDebutEtFin();

    }

    private void trouverDebutEtFin() {
        for (int i = 0; i < pieces.size(); i++) {
            if (pieces.get(i).getType() == TypeDePiece.SOURCE) {
                source = pieces.get(i);
                noeudDebut = source.getNoeudA();
                noeudFin = source.getNoeudB();
            }
        }
    }

    private void identifierNoeuds() {
        for (int i = 0; i < noeuds.size(); i++) {
            noeuds.get(i).setIdentite(i);
        }
    }

    public double calculerResistance() {
        simplifierResistance();
        for (PieceElectronique piece : pieces) {
            if (piece.type == TypeDePiece.RÉSISTANCE) {
                explications += "Il nous reste donc une seule " + piece.getNomComplet() + "\n";
                return piece.valeur;
            }
        }
        return -1;
    }

    private boolean simplifierResistance() {
        boolean actif = true;
        enleverPieceInutiles();
        while (actif) {
            actif = simplifierResistanceParallele() || simplifierResistanceSerie();

        }
        return true;
    }

    public double calculerCondensateur() {
        simplifierCondensateur();
        for (PieceElectronique piece : pieces) {
            if (piece.type == TypeDePiece.CONDENSATEUR) {
                explications += "Il nous reste donc un seul " + piece.getNomComplet() + " \n";
                return piece.valeur;
            }
        }
        return -1;
    }

    private boolean simplifierCondensateur() {
        boolean actif = true;
        enleverPieceInutiles();
        while (actif) {
            actif = simplifierCondensateursSerie() || simplifierCondensateurParallele();

        }
        return true;
    }

    private void enleverPieceInutiles() {
        ArrayList<PieceElectronique> piecesAEnlever = new ArrayList<>();
        for (PieceElectronique piece : pieces) {
            if (piece.getNoeudA() == null || piece.getNoeudB() == null) {
                piecesAEnlever.add(piece);
                piece.seRetirerDesNoeuds();
                explications += "-On enlève la " + piece.getNomComplet() + " car au moins l'un des cotés n'est pas connecté \n";
            }
        }
        pieces.removeAll(piecesAEnlever);
    }

    private boolean simplifierResistanceParallele() {
        boolean actif = false;
        for (Noeud noeud : noeuds) {
            for (Noeud noeudSecondaire : noeuds) {
                if (noeud != noeudSecondaire) {
                    ArrayList<PieceElectronique> piecesLocal = getPiecesCommunes(noeud, noeudSecondaire);
                    ArrayList<PieceElectronique> pieceAEnlever = new ArrayList<>();
                    if (piecesLocal.size() > 0) {
                        for (PieceElectronique pieceElectronique : piecesLocal) {
                            if (pieceElectronique.getType() != TypeDePiece.RÉSISTANCE) {
                                pieceAEnlever.add(pieceElectronique);
                            }
                        }
                    }
                    piecesLocal.removeAll(pieceAEnlever);
                    if (piecesLocal.size() > 1) {
                        double resistanceEquivalante = 0;
                        explications += "-Les résistances : ";
                        for (int i = 0; i < piecesLocal.size(); i++) {
                            if (piecesLocal.get(i).getType() == TypeDePiece.RÉSISTANCE) {
                                resistanceEquivalante += (1 / piecesLocal.get(i).getValeur());
                                explications += piecesLocal.get(i).getNomComplet() + " , ";
                                pieces.remove(piecesLocal.get(i));
                                noeud.removePiece(piecesLocal.get(i));
                                noeudSecondaire.removePiece(piecesLocal.get(i));
                                actif = true;
                            }
                        }
                        resistanceEquivalante = Math.pow(resistanceEquivalante, -1);

                        Resistance resistance = new Resistance(resistanceEquivalante);
                        explications += " sont en parallèle et peuvent former une " + resistance.getNomComplet() + " \n";
                        resistance.setNoeudA(noeud);
                        resistance.setNoeudB(noeudSecondaire);
                        noeud.addPiece(resistance);
                        noeudSecondaire.addPiece(resistance);
                        pieces.add(resistance);
                    }
                }
            }
        }
        return actif;
    }

    private boolean simplifierResistanceSerie() {
        boolean actif = false;
        Noeud noeudAEnlever = null;
        for (Noeud noeud : noeuds) {
            ArrayList<PieceElectronique> piecesLocal = noeud.getPieces();
            if (noeud.size() == 2 && noeud != noeudAEnlever) {
                if (piecesLocal.get(0).type == TypeDePiece.RÉSISTANCE && piecesLocal.get(1).getType() == TypeDePiece.RÉSISTANCE) {

                    double valeur = piecesLocal.get(0).getValeur() + piecesLocal.get(1).getValeur();
                    Resistance resistanceEquivalante = new Resistance(valeur);
                    explications += "-Les résistances : " + piecesLocal.get(0).getNomComplet() + " et la " + piecesLocal.get(1).getNomComplet() + " sont en série et peuvent former une " + resistanceEquivalante.getNomComplet() + "\n";
                    Noeud start = piecesLocal.get(0).getAutreNoeud(noeud);
                    Noeud fin = piecesLocal.get(1).getAutreNoeud(noeud);
                    resistanceEquivalante.setNoeudA(start);
                    resistanceEquivalante.setNoeudB(fin);
                    start.removePiece(piecesLocal.get(0));
                    fin.removePiece(piecesLocal.get(1));
                    start.addPiece(resistanceEquivalante);
                    fin.addPiece(resistanceEquivalante);
                    pieces.removeAll(piecesLocal);
                    noeud.clearPieces();
                    noeudAEnlever = noeud;
                    noeud.clear();
                    pieces.add(resistanceEquivalante);
                    actif = true;
                }
            }
        }
        noeuds.remove(noeudAEnlever);

        return actif;
    }

    private boolean simplifierCondensateursSerie() {
        boolean actif = false;
        Noeud noeudAEnlever = null;
        for (Noeud noeud : noeuds) {
            ArrayList<PieceElectronique> piecesLocal = noeud.getPieces();
            if (noeud.size() == 2 && noeud != noeudAEnlever) {
                if (piecesLocal.get(0).type == TypeDePiece.CONDENSATEUR && piecesLocal.get(1).getType() == TypeDePiece.CONDENSATEUR) {

                    double valeur = 1 / ((1 / piecesLocal.get(0).getValeur()) + (1 / piecesLocal.get(1).getValeur()));
                    Condensateur condensateurEquivalent = new Condensateur(valeur);
                    explications += "-Les condensateurs : " + piecesLocal.get(0).getNomComplet() + " et  " + piecesLocal.get(1).getNomComplet() + " sont en série et peuvent former une " + condensateurEquivalent.getNomComplet() + "\n";
                    Noeud start = piecesLocal.get(0).getAutreNoeud(noeud);
                    Noeud fin = piecesLocal.get(1).getAutreNoeud(noeud);
                    condensateurEquivalent.setNoeudA(start);
                    condensateurEquivalent.setNoeudB(fin);
                    start.removePiece(piecesLocal.get(0));
                    fin.removePiece(piecesLocal.get(1));
                    start.addPiece(condensateurEquivalent);
                    fin.addPiece(condensateurEquivalent);
                    pieces.removeAll(piecesLocal);
                    noeud.clearPieces();
                    noeudAEnlever = noeud;
                    noeud.clear();
                    pieces.add(condensateurEquivalent);
                    actif = true;
                }
            }
        }
        noeuds.remove(noeudAEnlever);

        return actif;
    }

    private boolean simplifierCondensateurParallele() {
        boolean actif = false;
        for (Noeud noeud : noeuds) {
            for (Noeud noeudSecondaire : noeuds) {
                if (noeud != noeudSecondaire) {
                    ArrayList<PieceElectronique> piecesLocal = getPiecesCommunes(noeud, noeudSecondaire);
                    ArrayList<PieceElectronique> pieceAEnlever = new ArrayList<>();
                    if (piecesLocal.size() > 0) {
                        for (PieceElectronique pieceElectronique : piecesLocal) {
                            if (pieceElectronique.getType() != TypeDePiece.CONDENSATEUR) {
                                pieceAEnlever.add(pieceElectronique);
                            }
                        }
                    }
                    piecesLocal.removeAll(pieceAEnlever);
                    if (piecesLocal.size() > 1) {
                        double capacitanceEquivalante = 0;
                        explications += "-Les résistances : ";
                        for (int i = 0; i < piecesLocal.size(); i++) {
                            if (piecesLocal.get(i).getType() == TypeDePiece.CONDENSATEUR) {
                                capacitanceEquivalante += piecesLocal.get(i).getValeur();
                                explications += piecesLocal.get(i).getNomComplet() + " , ";
                                pieces.remove(piecesLocal.get(i));
                                noeud.removePiece(piecesLocal.get(i));
                                noeudSecondaire.removePiece(piecesLocal.get(i));
                                actif = true;
                            }
                        }


                        Condensateur condensateur = new Condensateur(capacitanceEquivalante);
                        explications += " sont en parallèle et peuvent former une " + condensateur.getNomComplet() + " \n";
                        condensateur.setNoeudA(noeud);
                        condensateur.setNoeudB(noeudSecondaire);
                        noeud.addPiece(condensateur);
                        noeudSecondaire.addPiece(condensateur);
                        pieces.add(condensateur);
                    }
                }
            }
        }
        return actif;
    }

    private ArrayList<PieceElectronique> getPiecesCommunes(Noeud noeudA, Noeud noeudB) {
        ArrayList<PieceElectronique> pieceCommunes = new ArrayList<>();
        for (PieceElectronique piece : noeudA.getPieces()) {
            if (piece.contientNoeud(noeudB)) {
                pieceCommunes.add(piece);
            }
        }
        return pieceCommunes;
    }

    public String toString() {
        String str = "------------[START]--------------- \n";
        for (PieceElectronique piece : pieces) {
            str += piece.getNoeudA().getIdentite() + " -> " + piece.getType() + " : " + piece.getValeur() + " -> " + piece.getNoeudB().getIdentite() + "\n";
        }
        return str;
    }

    public String getExplications() {
        return explications;
    }
}
