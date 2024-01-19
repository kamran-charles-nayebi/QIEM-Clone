package SIMulator;

public enum Types{
    TEXTUELLE("ReponseTextuelle"),
    CHOIX_DE_REPONSE("ReponseChoix"),
    CHARGE_LINEAIRE("ReponseChargeLineaire"),
    DESSIN_VECTORIEL("ReponseDessinVectoriel"),
    LOGIQUE("ReponseLogique"),
    CIRCUIT_ELECTRIQUE("ReponseCircuitElectrique");


    public final String nom;
    Types(String str){
        nom = str;
    }

}
