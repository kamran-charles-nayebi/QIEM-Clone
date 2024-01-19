package SIMulator.Reponses;

import SIMulator.Reponses.ReponseChoixLineaire.ReponseChargesLineaire;
import SIMulator.Reponses.ReponseCircuitElectrique.*;
import SIMulator.Reponses.ReponseDessinVectoriel.PointCartesien;
import SIMulator.Reponses.ReponseDessinVectoriel.ReponseDessinVectoriel;
import SIMulator.Reponses.ReponseTextuelle.ReponseTextuelle;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;


public class fabriqueurDeQuestionTest extends Application {

    Scene scene;
    Stage stage;
    VBox root;
    @Override
    public void start(Stage primaryStage) throws Exception {
        root  = new VBox();
        scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
        stage = primaryStage;
        root.setPadding(new Insets(10));
        testDeCircuitElectrique();
        //testCircuitCondensateurs();
        //testDeReponseText();
        //testDeReponseChoix();
        //testDeChargeLineaire();
        //testDeReponseVecteur();
        root.setAlignment(Pos.CENTER);

    }

    private void testCircuitCondensateurs() {
        Condensateur res1 = new Condensateur(10e-6);
        Condensateur res2 = new Condensateur(10e-6);
        Condensateur res3 = new Condensateur(5e-6);
        Condensateur res4 = new Condensateur(100e-6);
        Condensateur res5 = new Condensateur(102.5e-6);
        Source source = new Source();

        Noeud noeud = new Noeud();
        Noeud noeud1 = new Noeud();
        Noeud noeud2 = new Noeud();
        source.setNoeudA(noeud);
        source.setNoeudB(noeud2);
        res1.setNoeudA(noeud);
        res1.setNoeudB(noeud1);
        res2.setNoeudA(noeud);
        res2.setNoeudB(noeud1);
        res3.setNoeudA(noeud);
        res3.setNoeudB(noeud1);
        res4.setNoeudA(noeud1);
        res4.setNoeudB(noeud2);
        res5.setNoeudA(noeud);
        res5.setNoeudB(noeud2);

        noeud.addPieces(new PieceElectronique[]{res1,res2,res3,source,res5});
        noeud1.addPieces(new PieceElectronique[]{res1,res2,res3,res4});
        noeud2.addPieces(new PieceElectronique[]{source,res4,res5});

        ArrayList<PieceElectronique> pieces= new ArrayList<>();
        pieces.add(source);
        pieces.add(res1);
        pieces.add(res2);
        pieces.add(res3);
        pieces.add(res4);
        pieces.add(res5);

        ArrayList<Noeud> noeuds = new ArrayList<>();
        noeuds.add(noeud);
        noeuds.add(noeud1);
        noeuds.add(noeud2);

        CircuitElectrique circuit = new CircuitElectrique(pieces,noeuds);
        System.out.println(" Value : " + circuit.calculerCondensateur());
        System.out.println(circuit.getExplications());

        ReponseCircuitElectrique reponseCircuitElectrique = new ReponseCircuitElectrique();
        reponseCircuitElectrique.addPiece(new Condensateur(4e-5));
        reponseCircuitElectrique.addPiece(new Condensateur(6e-7));
        reponseCircuitElectrique.addPiece(new Source());
        reponseCircuitElectrique.addPiece(new Condensateur(1));
        root.getChildren().add(reponseCircuitElectrique.getInterface());
    }

    private void testDeCircuitElectrique() {
        ReponseCircuitElectrique reponseCircuitElectrique = new ReponseCircuitElectrique();
        reponseCircuitElectrique.addPiece(new Resistance(100));
        reponseCircuitElectrique.addPiece(new Resistance(50));
        reponseCircuitElectrique.addPiece(new Source());
        //root.getChildren().add(reponseCircuitElectrique.getInterface());
        Reponse reponse = FabriqueurDeReponses.faireUneReponse("ReponseType = ReponseCircuitElectrique\n" +
                "\n" +
                "ReponseResistance = 237.5\n" +
                "\n" +
                "Resistance1 = 50\n" +
                "\n" +
                "Resistance2 = 150\n" +
                "\n" +
                "Resistance3 = 200");
        root.getChildren().add(reponse.getInterface());
        Button button = new Button();
        button.setOnMouseClicked((e)->{
                    System.out.println(reponse.estValide());
                    System.out.println(reponse.getReponse());
                    reponse.montrerReponse();
                }
                //System.out.println(reponseCircuitElectrique.toString())
                );

        root.getChildren().add(button);


        Resistance res1 = new Resistance(10);
        Resistance res2 = new Resistance(10);
        Resistance res3 = new Resistance(5);
        Resistance res4 = new Resistance(100);
        Resistance res5 = new Resistance(102.5);
        Source source = new Source();

        Noeud noeud = new Noeud();
        Noeud noeud1 = new Noeud();
        Noeud noeud2 = new Noeud();
        source.setNoeudA(noeud);
        source.setNoeudB(noeud2);
        res1.setNoeudA(noeud);
        res1.setNoeudB(noeud1);
        res2.setNoeudA(noeud);
        res2.setNoeudB(noeud1);
        res3.setNoeudA(noeud);
        res3.setNoeudB(noeud1);
        res4.setNoeudA(noeud1);
        res4.setNoeudB(noeud2);
        res5.setNoeudA(noeud);
        res5.setNoeudB(noeud2);

        noeud.addPieces(new PieceElectronique[]{res1,res2,res3,source,res5});
        noeud1.addPieces(new PieceElectronique[]{res1,res2,res3,res4});
        noeud2.addPieces(new PieceElectronique[]{source,res4,res5});

        ArrayList<PieceElectronique> pieces= new ArrayList<>();
        pieces.add(source);
        pieces.add(res1);
        pieces.add(res2);
        pieces.add(res3);
        pieces.add(res4);
        pieces.add(res5);

        ArrayList<Noeud> noeuds = new ArrayList<>();
        noeuds.add(noeud);
        noeuds.add(noeud1);
        noeuds.add(noeud2);

        CircuitElectrique circuit = new CircuitElectrique(pieces,noeuds);

        circuit.calculerResistance();
        System.out.println(circuit.getExplications());

    }

    private void testDeReponseVecteur() {
        /*ReponseVecteur reponseVecteur = new ReponseVecteur();
        reponseVecteur.setGrilleVecteurs(-50,100,10,-20,100,10);
        reponseVecteur.addPoint(10,10,"Hallo");
        reponseVecteur.addPoint(25,-10,"Gruß nicht" , new Color(0.2,0.5,0,1));
        root.getChildren().add(reponseVecteur.getInterface());*/
        /*ReponseDessinVectoriel reponseDessinVectoriel = new ReponseDessinVectoriel(-50,50,10,-100,100,10);
        reponseDessinVectoriel.setReponse(new double[]{0,0,10,10});
        PointCartesien pointCartesien = new PointCartesien(0,0);
        pointCartesien.setTextePoint("+");
        pointCartesien.setTexteSousPoint("4,5x10^-5 C");
        reponseDessinVectoriel.ajouterPoint(pointCartesien);
        PointCartesien pointCartesien1 = new PointCartesien(30,50,20, Color.ORANGERED);
        pointCartesien1.setTextePoint("n");
        pointCartesien1.setTexteSousPoint("Neutron");
        reponseDessinVectoriel.ajouterPoint(pointCartesien1);
        root.getChildren().add(reponseDessinVectoriel.getInterface());*/
        ReponseDessinVectoriel rep1 = (ReponseDessinVectoriel) FabriqueurDeReponses.faireUneReponse("ReponseType = ReponseDessinVectoriel\n" +
                "ReponseXDebut = 30\n" +
                "ReponseYDebut = 20.0\n" +
                "ReponseXFin = 20\n" +
                "ReponseYFin = 40\n" +
                "GrilleXMin = 0\n" +
                "GrilleXMax = 100\n" +
                "GrilleXStep = 10\n" +
                "GrilleYMin = -20\n" +
                "GrilleYMax = 100\n" +
                "GrilleYStep = 10");
        root.getChildren().add(rep1.getInterface());
        Button button = new Button("reponse");
        button.setOnMouseClicked((event -> {
            rep1.montrerReponse();
        }));
        root.getChildren().add(button);
    }

    private void testDeChargeLineaire(){
        ReponseChargesLineaire reponseChargesLineaire = new ReponseChargesLineaire();
        reponseChargesLineaire.setReponse(50,75);
        reponseChargesLineaire.setParticuleAPosition(80);
        //reponseChargesLineaire.setTexteParticuleB("Test");
        reponseChargesLineaire.setParticuleBPosition(100);
        reponseChargesLineaire.setChargeParticuleA("-10x10^-5");
        reponseChargesLineaire.setChargeParticuleB("5x10^6");
        reponseChargesLineaire.setChargeParticuleUtilisateur("2");
        //root.getChildren().add(reponseChargesLineaire.getInterface());

        Reponse reponseDeux = FabriqueurDeReponses.faireUneReponse("ReponseType = ReponseChargeLineaire\n" +
                "\n" +
                "Reponse = 45:65\n" +
                "\n" +
                "PositionParticuleA = -10\n" +
                "\n" +
                "PositionParticuleB = 100\n" +
                "\n" +
                "ChargeParticuleA = 7x10^-10\n" +
                "\n" +
                "ChargeParticuleB = -14x10^-12\n" +
                "\n" +
                "ChargeParticuleUtilisateur = 10x10^-14");
        Button bouton = new Button();
        bouton.setOnMouseClicked((e)->{
            reponseDeux.montrerReponse();
        });
        root.getChildren().add(bouton);
        root.getChildren().add(reponseDeux.getInterface());


    }

    private void testDeReponseChoix() {
        Reponse reponseChoix = FabriqueurDeReponses.faireUneReponse("ReponseType = ReponseChoix \n Reponse = la reponse \n ChoixReponse = 4.5;tu est gay; ya ich bin da \n");
        root.getChildren().add(reponseChoix.getInterface());
    }

    private void testDeReponseText(){
        ReponseTextuelle reponseTextuelle = new ReponseTextuelle("18","oui");
        Reponse reponseGenere = FabriqueurDeReponses.faireUneReponse("ReponseType = ReponseTextuelle \n Reponse = 18 \n TexteIndication = oui");

        System.out.println(reponseTextuelle);
        System.out.println(reponseGenere);
        reponseTextuelle.changerTexteIndication("oui");
        System.out.println(reponseTextuelle);
        root.getChildren().add(reponseGenere.getInterface());
    }

    public static void main(String[] args) {
        launch();
    }
}