package SIMulator;

public class PointStatistique {
    int un;
    int unM;
    int deux;
    int deuxM;
    int trois;
    int troisM;
    int total;
    String name;

    public void setUnM(int unM) {
        this.unM = unM;
    }

    public void setDeuxM(int deuxM) {
        this.deuxM = deuxM;
    }

    public void setTroisM(int troisM) {
        this.troisM = troisM;
    }

    public void setTotal() {
        this.total = un+unM+deux+deuxM+trois+troisM;
    }

    public PointStatistique(int un, int deux, int trois,int unM,int deuxM,int troisM, String name) {
        this.un = un;
        this.deux = deux;
        this.trois = trois;
        this.unM = un;
        this.deuxM = deux;
        this.troisM = trois;
        this.name = name;
    }

    public int getUn() {
        return un;
    }

    public void setUn(int un) {
        this.un = un;
    }

    public int getDeux() {
        return deux;
    }

    public void setDeux(int deux) {
        this.deux = deux;
    }

    public int getTrois() {
        return trois;
    }

    public void setTrois(int trois) {
        this.trois = trois;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public Double total(){
        setTotal();
        Double somme = Double.valueOf(un+deux+trois);
        Double tot = Double.valueOf(total);
        return (somme/tot)*100;
    }
}
