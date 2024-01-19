package SIMulator.Reponses.ReponseDessinVectoriel;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class PointCartesien {
    private double x;
    private double y;
    private double rayon = 20;
    private Color couleur = Color.LIGHTGRAY;

    private String textePoint = "";

    private String texteSousPoint = "";

    public void setCoordonee(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void setCoordonee(double[] coordonee) {
        this.x = coordonee[0];
        this.y = coordonee[1];
    }

    public PointCartesien(double x, double y, double rayon, Color couleur) {
        setCoordonee(x, y);
        this.rayon = rayon;
        this.couleur = couleur;
    }

    public PointCartesien(double x, double y, double rayon) {
        setCoordonee(x, y);
        this.rayon = rayon;
    }

    public PointCartesien(double x, double y) {
        setCoordonee(x, y);
    }

    public double getX() {
        return x;
    }

    public String getTextePoint() {
        return textePoint;
    }

    public void setTextePoint(String textePoint) {
        this.textePoint = textePoint;
    }

    public String getTexteSousPoint() {
        return texteSousPoint;
    }

    public void setTexteSousPoint(String texteSousPoint) {
        this.texteSousPoint = texteSousPoint;
    }

    public double getY() {
        return y;
    }

    public double getRayon() {
        return rayon;
    }

    public Color getCouleur() {
        return couleur;
    }

    public double[] getCoordonees() {
        return new double[]{x, y};
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setRayon(double rayon) {
        this.rayon = rayon;
    }

    public void setCouleur(Color couleur) {
        this.couleur = couleur;
    }

    @Override
    public String toString() {
        return "PointCartesien{" +
                "x=" + x +
                ", y=" + y +
                ", rayon=" + rayon +
                ", couleur=" + couleur +
                '}';
    }


}
