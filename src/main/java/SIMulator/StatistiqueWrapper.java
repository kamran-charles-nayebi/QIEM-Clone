package SIMulator;

import SIMulator.ClassesQuestions.Question;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class StatistiqueWrapper implements Serializable {

    private int nbQuestion;
    private List<QuestionWrapper> questionList;

    public StatistiqueWrapper(Quiz quiz){
        nbQuestion = Quiz.NOMBRE_DE_QUESTIONS;
        questionList = new ArrayList<>();
        ArrayList<Question> array = quiz.getArrayQuestions();
        for (int i = 0; i < nbQuestion; i++) {
            questionList.add(new QuestionWrapper(array.get(i).getChapitre(), array.get(i).getReussi(), array.get(i).getMatiere()));
        }
    }

    public class QuestionWrapper implements Serializable{
        private String chapitre;
        private boolean reussi;
        private String matiere;

        public QuestionWrapper(String _chap, Boolean _reussi, String _mat){
            chapitre = _chap;
            reussi = _reussi;
            matiere = _mat;
        }

        public String getChapitre(){
            return chapitre;
        }

        public boolean getReussi(){
            return reussi;
        }

        public String getMatiere(){
            return matiere;
        }
    }

    public List<QuestionWrapper> getQuestionList(){
        return questionList;
    }

}
