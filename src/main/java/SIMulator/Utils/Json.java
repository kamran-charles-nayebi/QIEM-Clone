package SIMulator.Utils;

import SIMulator.Types;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Fait toutes les tâches associées au json
 */
public class Json {

    private static String endroit1;
    private static JSONObject JSONCache;
    private static JSONObject JSONquestion;

    /**
     * Donne le fichier json pour lequel on doit choisir des questions
     * @param endroit lien vers le fichier contenant les questions
     */
    public static void lireFichierQuestion(String endroit) {
        endroit1=endroit;
        String jsonString = "";
        try {
            BufferedReader br = new BufferedReader(new FileReader(endroit));

            String nextLine = br.readLine();
            while (nextLine != null) {
                jsonString += nextLine;
                nextLine = br.readLine();
            }
        } catch (IOException e) {
            System.err.println(e);
        }
        try {
            JSONCache = new JSONObject(jsonString);
        } catch (JSONException e) {
            JSONCache=new JSONObject();
        }
    }
    public static void ecrireFichierQuestion(JSONObject jsonObject){
        String jsonString = "";
        try {
            BufferedWriter br = new BufferedWriter(new FileWriter(endroit1));
            br.write(jsonObject.toString());
            br.close();
        } catch (IOException e) {
            System.err.println(e);
        }
        lireFichierQuestion(endroit1);
    }

    /**
     * Choisis une question aléatoirement selon le type et le chapitre
     * @param type le type de questions
     * @return l'objet json de la question
     */
    public static JSONObject choisirQuestion(Types type){
        JSONArray questions = JSONCache.getJSONArray(type.nom);
        int random = ThreadLocalRandom.current().nextInt(0, questions.length());
        return (JSONObject) questions.get(random);
    }

    public static JSONArray getArrayQuestions(Types type){
        return JSONCache.getJSONArray(type.nom);
    }

    /**
     * Set la question actuelle
     * @param obj - la question
     */
    public static void setJSONquestion(JSONObject obj){
        JSONquestion = obj;
    }

    public static JSONObject getJSONquestion(){
        return JSONquestion;
    }

    public static JSONObject getJSON() {
        return JSONCache;
    }
}
