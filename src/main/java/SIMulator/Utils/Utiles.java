package SIMulator.Utils;

public class Utiles {
    /**
     * Calcule la valeur scientifique de la valeur donnée et lui attribu le préfixe en lien (p,n,m,ect)
     * @param value La valeur auquel ont doit lui attribuer un préfixe
     * @return le string contenant la valeur n'ayant pas de "10^n" mais plutôt son préfixe
     */
    public static String valeurScientifique(double value) {

        if (value <= 10e-10) {
            return String.format("%.1f", value / 10e-12) + " p";
        } else if (value <= 10e-7) {
            return String.format("%.1f", value / 10e-9) + " n";
        } else if (value <= 10e-4) {
            return String.format("%.1f", value / 10e-6) + " µ";
        } else if (value <= 10e-1) {
            return String.format("%.1f", value / 10e-3) + " m";
        }
        return String.format("%.1f",value) + " ";
    }

    /**
     * Calcule la valeur scientifique de la valeur donnée et lui attribue le préfixe en lien (p,n,m,ect)
     * On peut lui fournir des valeurs sous forme de double (ex : 10e-3) ou encore sous forme plus courante (ex : 10x10^4)
     * @param str Le string de la valeur auquel on doit lui attribuer un préfixe
     * @return Le string contenant la valeur n'ayant pas de "10^n" mais plutôt un préfixe
     */
    public static String valeurScientifique(String str){
        str = str.replace("x10^","e");
        return valeurScientifique(Double.parseDouble(str));
    }
}
