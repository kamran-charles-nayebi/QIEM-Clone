
package SIMulator.Reponses;

import SIMulator.Reponses.ReponseChoix.ReponseChoix;
import SIMulator.Reponses.ReponseChoixLineaire.ReponseChargesLineaire;
import SIMulator.Reponses.ReponseCircuitElectrique.ReponseCircuitElectrique;
import SIMulator.Reponses.ReponseCircuitElectrique.Resistance;
import SIMulator.Reponses.ReponseCircuitElectrique.Source;
import SIMulator.Reponses.ReponseDessinVectoriel.PointCartesien;
import SIMulator.Reponses.ReponseDessinVectoriel.ReponseDessinVectoriel;
import SIMulator.Reponses.ReponseTextuelle.ReponseTextuelle;
import SIMulator.Reponses.ReponseVecteur.ReponseVecteur;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.MissingFormatArgumentException;

/**
 * Classe qui s'occupe de fabriquer la bonne réponse à partir du fichier texte des informations de la question
 * !! Veuillez ne pas amener des modifications svp (Responsabilité Alexis W), me contacter au besoin
 */
public class FabriqueurDeReponses {

    private static String NOTFOUND = "NOTFOUND";

    /**
     * Prend le fichier texte lié a la question et en sort une Reponse du bon type fonctionnel
     *
     * @param parametre Le contenu du fichier texte
     * @return Un enfant de l'interface réponse, en adéquation avec les indications du fichier texte
     */
    public static Reponse faireUneReponse(String parametre) {
        return trouverReponseType(parametre);
    }

    /**
     * Cherche un paramètre dans un fichier texte et ensuite donne le string qui est après le =
     *
     * @param parametres         Le fichier texte dans lequel chercher
     * @param parametreAChercher le paramètre qu'il faut chercher dans le fichier
     * @return le string trouvé après le paramètre donné
     */
    public static String trouverLeParametre(String parametres, String parametreAChercher) {
        int indexDebut = parametres.indexOf(parametreAChercher);
        if (indexDebut == -1) {
            return NOTFOUND;
        }
        int indexEqual = parametres.indexOf("=", indexDebut) + 1;
        int indexFin = parametres.indexOf("\n", indexEqual);

        if (indexFin == -1) {
            indexFin = parametres.length();
        }

        String stringReponse = parametres.substring(indexEqual, indexFin);
        stringReponse = stringReponse.trim();
        return stringReponse;
    }

    /**
     * Cherche un paramètre dans un fichier texte et ensuite donne le string qui est après le =, lance une erreur si ce n'est pas trouvé
     *
     * @param parametres         Le fichier texte dans lequel chercher
     * @param parametreAChercher le paramètre qu'il faut chercher dans le fichier
     * @return le string trouvé après le paramètre donné
     */
    public static String trouverLeParametreEtValider(String parametres, String parametreAChercher) {
        String str = trouverLeParametre(parametres, parametreAChercher);
        if (str == NOTFOUND) {
            throw new MissingFormatArgumentException("Le paramètre " + parametreAChercher + " n'a pas été trouvé");
        }
        return str;
    }


    /**
     * Trouve le type de réponse ( quel enfant de l'interface Réponse ) que l'on doit créer
     *
     * @param typeParametre Le fichier texte qui contient tout les paramètres
     * @return La réponse du bon type et bien configuré
     */
    private static Reponse trouverReponseType(String typeParametre) {

        String typeString = trouverLeParametre(typeParametre, "ReponseType ");
        typeString = typeString.replace(" ", "");
        Reponse reponse = null;

        switch (typeString) {
            case "ReponseTextuelle":
                reponse = faireReponseTextuelle(typeParametre);
                break;
            case "ReponseChoix":
                reponse = faireReponseChoix(typeParametre);
                break;
            case "ReponseChargeLineaire":
                reponse = faireReponseChargesLineaire(typeParametre);
                break;
            case "ReponseVecteur":
                reponse = faireReponseVecteur(typeParametre);
                break;
            case "ReponseDessinVectoriel":
                reponse = faireReponseDessinVectoriel(typeParametre);
                break;
            case "ReponseCircuitElectrique":
                reponse = faireReponseCircuitElectrique(typeParametre);
                break;
        }
        return reponse;
    }

    private static Reponse faireReponseCircuitElectrique(String typeParametre) {
        ReponseCircuitElectrique reponseCircuitElectrique = new ReponseCircuitElectrique();

        Double reponse[] = new Double[]{-1.0, -1.0};

        if (!trouverLeParametre(typeParametre, "ReponseResistance ").equals(NOTFOUND)) {
            reponse[0] = Double.parseDouble(trouverLeParametre(typeParametre, "ReponseResistance "));
        }
        if (!trouverLeParametre(typeParametre, "ReponseCapacitance ").equals(NOTFOUND)){
            reponse[1] = Double.parseDouble(trouverLeParametre(typeParametre, "ReponseCapacitance "));
        }
        reponseCircuitElectrique.setReponse(reponse);

        Boolean objetExiste = true;
        int n = 1;

        while (objetExiste) {
            if (trouverLeParametre(typeParametre, "Resistance" + n + " ").equals(NOTFOUND)) {
                objetExiste = false;
            } else {
                Resistance resistance = new Resistance(Double.parseDouble(trouverLeParametre(typeParametre, "Resistance" + n + " ")));
                reponseCircuitElectrique.addPiece(resistance);
                n++;
            }
        }

        objetExiste = true;
        n = 1;
        while (objetExiste) {
            if (trouverLeParametre(typeParametre, "Condensateur" + n + " ").equals(NOTFOUND)) {
                objetExiste = false;
            } else {
                Resistance condensateur = new Resistance(Double.parseDouble(trouverLeParametre(typeParametre, "Condensateur" + n + " ")));
                reponseCircuitElectrique.addPiece(condensateur);
                n++;
            }
        }

        reponseCircuitElectrique.addPiece(new Source());

        return reponseCircuitElectrique;
    }

    private static Reponse faireReponseDessinVectoriel(String typeParametre) {
        double[] dataRep = new double[4];
        dataRep[0] = Double.parseDouble(trouverLeParametreEtValider(typeParametre, "ReponseXDebut ").replace(" ", ""));
        dataRep[1] = Double.parseDouble(trouverLeParametreEtValider(typeParametre, "ReponseYDebut ").replace(" ", ""));
        dataRep[2] = Double.parseDouble(trouverLeParametreEtValider(typeParametre, "ReponseXFin ").replace(" ", ""));
        dataRep[3] = Double.parseDouble(trouverLeParametreEtValider(typeParametre, "ReponseYFin ").replace(" ", ""));

        int[] dataGrille = new int[6];
        dataGrille[0] = Integer.parseInt(trouverLeParametreEtValider(typeParametre, "GrilleXMin ").replace(" ", ""));
        dataGrille[1] = Integer.parseInt(trouverLeParametreEtValider(typeParametre, "GrilleXMax ").replace(" ", ""));
        dataGrille[2] = Integer.parseInt(trouverLeParametreEtValider(typeParametre, "GrilleXStep ").replace(" ", ""));
        dataGrille[3] = Integer.parseInt(trouverLeParametreEtValider(typeParametre, "GrilleYMin ").replace(" ", ""));
        dataGrille[4] = Integer.parseInt(trouverLeParametreEtValider(typeParametre, "GrilleYMax ").replace(" ", ""));
        dataGrille[5] = Integer.parseInt(trouverLeParametreEtValider(typeParametre, "GrilleYStep ").replace(" ", ""));

        ReponseDessinVectoriel reponseDessinVectoriel = new ReponseDessinVectoriel(dataGrille[0], dataGrille[1], dataGrille[2], dataGrille[3], dataGrille[4], dataGrille[5]);
        reponseDessinVectoriel.setReponse(dataRep);

        boolean objetExiste = true;
        int n = 1;

        while (objetExiste) {
            if (trouverLeParametre(typeParametre, "Point" + n + "X").equals(NOTFOUND)) {
                objetExiste = false;
            } else {
                int objetX = Integer.parseInt(trouverLeParametreEtValider(typeParametre, "Point" + n + "X"));
                int objetY = Integer.parseInt(trouverLeParametreEtValider(typeParametre, "Point" + n + "Y"));
                String objetTexteSous = trouverLeParametreEtValider(typeParametre, "Point" + n + "SousTexte");
                String objetTexte = trouverLeParametre(typeParametre, "Point" + n + "Texte");
                String objetRGB = trouverLeParametre(typeParametre, "Point" + n + "RGB");
                PointCartesien point = new PointCartesien(objetX, objetY);
                point.setTexteSousPoint(objetTexteSous);
                if (!objetRGB.equals(NOTFOUND)) {
                    String[] RGB = objetRGB.split(";");
                    //reponse.addPoint(objetX, objetY, objetTexte, , 1));
                    Color couleur = new Color(Double.parseDouble(RGB[0].replace(" ", "")), Double.parseDouble(RGB[1].replace(" ", "")), Double.parseDouble(RGB[2].replace(" ", "")), 1);
                    point.setCouleur(couleur);
                }
                if (!objetTexte.equals(NOTFOUND)) {
                    point.setTextePoint(objetTexte);
                }
                reponseDessinVectoriel.ajouterPoint(point);
                n++;
            }
        }

        return reponseDessinVectoriel;
    }

    private static Reponse faireReponseVecteur(String typeParametre) {
        ReponseVecteur reponse = new ReponseVecteur();
        int[] dataRep = new int[4];
        dataRep[0] = Integer.parseInt(trouverLeParametreEtValider(typeParametre, "ReponseDebutX "));
        dataRep[1] = Integer.parseInt(trouverLeParametreEtValider(typeParametre, "ReponseDebutY "));
        dataRep[2] = Integer.parseInt(trouverLeParametreEtValider(typeParametre, "ReponseFinX "));
        dataRep[3] = Integer.parseInt(trouverLeParametreEtValider(typeParametre, "ReponseFinY "));

        reponse.setReponse(dataRep);

        int[] dataGrille = new int[6];
        dataGrille[0] = Integer.parseInt(trouverLeParametreEtValider(typeParametre, "GrilleXMin "));
        dataGrille[1] = Integer.parseInt(trouverLeParametreEtValider(typeParametre, "GrilleXMax "));
        dataGrille[2] = Integer.parseInt(trouverLeParametreEtValider(typeParametre, "GrilleXStep "));
        dataGrille[3] = Integer.parseInt(trouverLeParametreEtValider(typeParametre, "GrilleYMin "));
        dataGrille[4] = Integer.parseInt(trouverLeParametreEtValider(typeParametre, "GrilleYMax "));
        dataGrille[5] = Integer.parseInt(trouverLeParametreEtValider(typeParametre, "GrilleYStep "));

        reponse.setGrilleVecteurs(dataGrille);

        boolean objetExiste = true;
        int n = 1;
        while (objetExiste) {
            if (trouverLeParametre(typeParametre, "Objet" + n + "X").equals(NOTFOUND)) {
                objetExiste = false;
            } else {
                int objetX = Integer.parseInt(trouverLeParametreEtValider(typeParametre, "Objet" + n + "X"));
                int objetY = Integer.parseInt(trouverLeParametreEtValider(typeParametre, "Objet" + n + "Y"));
                String objetTexte = trouverLeParametreEtValider(typeParametre, "Objet" + n + "Texte");
                String objetRGB = trouverLeParametre(typeParametre, "Objet" + n + "RGB");
                if (!objetRGB.equals(NOTFOUND)) {
                    String[] RGB = objetRGB.split(";");
                    reponse.addPoint(objetX, objetY, objetTexte, new Color(Double.parseDouble(RGB[0]), Double.parseDouble(RGB[1]), Double.parseDouble(RGB[2]), 1));
                }

                n++;
            }
        }

        return reponse;
    }

    /**
     * Crée une réponse avec des charges linéaires
     *
     * @param parametres L'intégralité du fichier texte contenant les paramètres relatif à la création de la réponse
     * @return Une réponse de type ReponseChargesLineaires
     */
    private static Reponse faireReponseChargesLineaire(String parametres) {
        ReponseChargesLineaire reponse = new ReponseChargesLineaire();

        String reponseStr[] = trouverLeParametreEtValider(parametres, "Reponse ").split(":");
        reponse.setReponse(Integer.parseInt(reponseStr[0].trim()), Integer.parseInt(reponseStr[1].trim()));

        reponse.setParticuleAPosition(Integer.parseInt(trouverLeParametreEtValider(parametres, "PositionParticuleA ").trim()));
        reponse.setParticuleBPosition(Integer.parseInt(trouverLeParametreEtValider(parametres, "PositionParticuleB ").trim()));
        reponse.setChargeParticuleA(trouverLeParametreEtValider(parametres, "ChargeParticuleA ").trim());
        reponse.setChargeParticuleB(trouverLeParametreEtValider(parametres, "ChargeParticuleB ").trim());
        reponse.setChargeParticuleUtilisateur(trouverLeParametreEtValider(parametres, "ChargeParticuleUtilisateur ").trim());

        String strIndication = trouverLeParametre(parametres, "TexteIndication ");
        if (!strIndication.equals(NOTFOUND)) {
            reponse.setTexteIndicatif(strIndication);
        }

        return reponse;
    }

    private static Reponse faireReponseChoix(String parametres) {
        ReponseChoix reponseChoix = new ReponseChoix();

        reponseChoix.setReponse(trouverLeParametreEtValider(parametres, "Reponse "));

        reponseChoix.setChoix(new ArrayList<String>(List.of(trouverLeParametreEtValider(parametres, "ChoixReponse ").split(";"))));

        String strIndication = trouverLeParametre(parametres, "TexteIndication");
        if (!strIndication.equals(NOTFOUND)) {
            reponseChoix.changerTexteIndication(strIndication);
        }

        return reponseChoix;
    }

    /**
     * Prépare une réponse textuelle en y mettant la bonne réponse et le label indicatif correct
     *
     * @param parametres Le fichier texte qui contient tout les paramètres
     * @return La réponse textuelle fini et bien configurée
     */
    private static Reponse faireReponseTextuelle(String parametres) {
        ReponseTextuelle reponse = new ReponseTextuelle();

        reponse.setReponse(trouverLeParametreEtValider(parametres, "Reponse "));

        String strIndication = trouverLeParametre(parametres, "TexteIndication");
        if (!strIndication.equals(NOTFOUND)) {
            reponse.changerTexteIndication(strIndication);
        }

        return reponse;
    }
}
