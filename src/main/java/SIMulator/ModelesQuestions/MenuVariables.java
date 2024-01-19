package SIMulator.ModelesQuestions;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.util.Callback;
import org.mariuszgromada.math.mxparser.Argument;
import org.mariuszgromada.math.mxparser.Expression;
import org.mariuszgromada.math.mxparser.PrimitiveElement;

import java.util.*;

/**
 * Menu qui s'occupe de faire des opérations avec les variables
 */
public class MenuVariables {
    @FXML
    private ListView<Argument> optionsListView;

    @FXML
    protected Label texte;

    private DemarcheControlleur parent;
    /**
     * Toutes les variables du problème
     */
    private List<Argument> variables;

    /**
     * La variable qu'on cherche à remplir avec ce menu
     */
    private Argument variable;

    public void setVariable(Argument variable) {
        this.variable = variable;
        int numDOptions = optionsListView.getItems().size();
        for (int i = 0; i < numDOptions; i++) {
            optionsListView.getItems().remove(0);
        }
        initialize();
    }

    @FXML
    private void initialize() {
        texte.setText("Quelle loi voulez-vous utiliser afin de calculer la variable manquante?");
        //Ajouter les options
        Argument temp = new Argument("a", 5);
        temp.setDescription("F=k|q1q2|/r^2 (Loi de Coulomb)");
        optionsListView.getItems().add(temp);
        temp = new Argument("a", 5);
        temp.setDescription("F=ma (Deuxième loi de Newton)");
        optionsListView.getItems().add(temp);
        temp = new Argument("a", 5);
        temp.setDescription("F=qE");
        optionsListView.getItems().add(temp);
        temp = new Argument("a", 5);
        temp.setDescription("E=kq/r^2");
        optionsListView.getItems().add(temp);
        temp = new Argument("a", 5);
        temp.setDescription("ΔV=Ed");
        optionsListView.getItems().add(temp);
        temp = new Argument("a", 5);
        temp.setDescription("ΔV=RI (loi d'Ohm)");
        optionsListView.getItems().add(temp);
        temp = new Argument("a", 5);
        temp.setDescription("P=ΔVI");
        optionsListView.getItems().add(temp);

        //Formatter les cellules
        optionsListView.setCellFactory(new Callback<ListView<Argument>, ListCell<Argument>>() {
            @Override
            public ListCell<Argument> call(ListView<Argument> list) {
                return new ListCell<>() {
                    @Override
                    public void updateItem(Argument item, boolean empty) {
                        super.updateItem(item, empty);
                        Label affichage;
                        if (item != null) {
                            affichage = new Label(item.getDescription());
                            setEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
                                appliquerFormule(item.getDescription());
                            });
                        }
                        else{
                            affichage = new Label();
                        }
                        affichage.setPadding(new Insets(5));
                        affichage.setFont(new Font(20));
                        setGraphic(affichage);
                    }
                };
            }
        });
    }

    @FXML
    void annuler(ActionEvent event) {
        fermerMenu(new Expression(), "");
    }

    public void setParentVariables(DemarcheControlleur _parent, List<Argument> _variables, Argument _variable) {
        parent = _parent;
        variables = _variables;
        variable = _variable;
    }

    public void setParentVariables(DemarcheControlleur _parent, List<Argument> _variables) {
        int numDOptions = optionsListView.getItems().size();
        for (int i = 0; i < numDOptions; i++) {
            optionsListView.getItems().remove(0);
        }
        texte.setText("Quelle variable voulez-vous calculer?");
        parent = _parent;
        variables = _variables;
        optionsListView.setCellFactory(new Callback<ListView<Argument>, ListCell<Argument>>() {
            @Override
            public ListCell<Argument> call(ListView<Argument> list) {
                return new ListCell<Argument>() {
                    @Override
                    public void updateItem(Argument item, boolean empty) {
                        super.updateItem(item, empty);
                        Label affichage;
                        if (item != null) {
                            affichage = new Label(item.getArgumentName());
                            setEventHandler(MouseEvent.MOUSE_CLICKED, e -> setVariable(item));
                        } else {
                            affichage = new Label();
                        }
                        affichage.setFont(new Font(20));
                        affichage.setPadding(new Insets(5));
                        setGraphic(affichage);
                    }
                };
            }
        });
        for (Argument argument : variables) {
            if (Double.isNaN(argument.getArgumentValue()))
                optionsListView.getItems().add(argument);
        }
    }

    /**
     * Ferme le menu et donne la valeur à la variable qu'on veut remplir
     *
     * @param expression la formule remplie avec toutes les variables pour calculer le résultat
     * @param demarche   la démarche pour l'opération effectuée
     */
    public void fermerMenu(Expression expression, String demarche) {
        if (variable == null)
            variable = new Argument("a=5");
        variable.setArgumentValue(expression.calculate());
        parent.deverrouiller(variable);
        parent.ajouterDemarche(demarche);
    }

    /**
     * Étape intermédiaire qui commence le processus de sélection de variables pour une formule
     *
     * @param formule la formule à remplir
     */
    private void appliquerFormule(String formule) {
        Formule objFormule = switch (formule) {
            case "F=k|q1q2|/r^2 (loi de Coulomb)" -> new Formule("Loi de Coulomb", "F=k|q1*q2|/r^2", this);
            case "F=ma (deuxième loi de Newton)" -> new Formule("Deuxième loi de Newton", "F=m*a", this);
            case "F=qE" -> new Formule("Force selon la charge et le champ", "F=q*E", this);
            case "E=kq/r^2" -> new Formule("Champ électrique", "E=k*q/r^2", this);
            case "ΔV=Ed" -> new Formule("Différence de potentiel selon le champ", "ΔV=E*d", this);
            case "ΔV=RI (loi d'Ohm)" -> new Formule("Loi d'Ohm", "ΔV=R*I", this);
            case "P=ΔVI" -> new Formule("Puissance électrique", "P=ΔV*I", this);
            default -> new Formule(formule, "F=a", this);
        };

        Iterator<Argument> it = optionsListView.getItems().iterator();
        while (it.hasNext()) {
            it.next();
            it.remove();
        }
        optionsListView.setCellFactory(new Callback<ListView<Argument>, ListCell<Argument>>() {
            @Override
            public ListCell<Argument> call(ListView<Argument> list) {
                return new ListCell<Argument>() {
                    @Override
                    public void updateItem(Argument item, boolean empty) {
                        super.updateItem(item, empty);
                        Label affichage;
                        if (item != null) {
                            if (!Double.isNaN(item.getArgumentValue()))
                                affichage = new Label(item.getArgumentName() + " = " + String.format(Locale.US, "%4.1e", item.getArgumentValue()));
                            else {
                                affichage = new Label("C'est la variable que je veux calculer");
                                affichage.setTextFill(Paint.valueOf("#f74d37"));
                            }
                            setEventHandler(MouseEvent.MOUSE_CLICKED, e -> objFormule.remplacerVariable(item));
                        } else {
                            affichage = new Label();
                        }
                        affichage.setFont(new Font(20));
                        affichage.setPadding(new Insets(5));
                        setGraphic(affichage);
                    }
                };
            }
        });
        it = variables.iterator();
        while (it.hasNext()) {
            Argument argument = it.next();
            if (!Double.isNaN(argument.getArgumentValue()))
                optionsListView.getItems().add(argument);
        }
        optionsListView.getItems().add(new Argument("a", Double.NaN));
    }
}

/**
 * Représente la formule à remplir avec le menu
 */
class Formule {
    String formule, formuleBase, nom;
    List<String> variables, argumentsRestants;
    List<Argument> variablesDonnes;
    int indice;
    MenuVariables menuVariables;

    Formule(String _nom, String _formule, MenuVariables _menuVariables) {
        nom = _nom;
        formule = _formule;
        menuVariables = _menuVariables;
        formuleBase = formule;

        argumentsRestants = new ArrayList<>();
        variablesDonnes = new ArrayList<>();
        indice = 0;

        variables = new ArrayList<>(Arrays.asList(formule.split("[-+*/()|=^]")));
        variables.removeAll(Arrays.asList("0", "1", "2", "3", "4", "5", "6", "7", "8", "9", ""));

        menuVariables.texte.setText(formule + "\nSélectionnez la valeur de " + variables.get(indice));
    }

    /**
     * Remplace la variable vers laquelle l'indice pointe et prépare l'interface pour la prochaine sélection
     * Méthode appelée par la listview lorsqu'on clique sur une variable
     *
     * @param variable la variable qui remplace celle qui est dans la formule
     */
    void remplacerVariable(Argument variable) {
        if (!Double.isNaN(variable.getArgumentValue())) {
            formule = formule.replace(variables.get(indice), String.format(Locale.US, "%4.1e", variable.getArgumentValue()));
            variablesDonnes.add(new Argument(variables.get(indice), variable.getArgumentValue()));
        } else
            argumentsRestants.add(variables.get(indice));
        indice++;
        if (indice != variables.size()) {
            menuVariables.texte.setText(formule + "\nQuelle est la valeur de " + variables.get(indice) + "?");
        } else {
            if (argumentsRestants.size() == 1) {
                Expression expression = formatterPourLibrairieDeCul();
                if (!Double.isNaN(expression.calculate()))
                    menuVariables.fermerMenu(expression, genererDemarche(expression));
            } else
                menuVariables.fermerMenu(new Expression(), "");
        }
    }

    /**
     * S'occupe de transformer la formule de base en une expression valide pour être calculée par mathxparser
     *
     * @return une expression valide pour mathxparser
     */
    Expression formatterPourLibrairieDeCul() {
        String formuleFinale = formuleBase;
        if (formuleBase.equals("F=k|q1*q2|/r^2")) {
            if (argumentsRestants.get(0).equals("r")) {
                formuleFinale = "√(k*q1*q2/F)";
                return new Expression(formuleFinale, variablesDonnes.toArray(new Argument[0]));
            } else
                formuleFinale = "k*q1*q2/r^2-F";
        } else if (formuleBase.equals("E=k*q/r^2")) {
            if (argumentsRestants.get(0).equals("r")) {
                formuleFinale = "√(k*q/E)";
                return new Expression(formuleFinale, variablesDonnes.toArray(new Argument[0]));
            } else
                formuleFinale = "k*q/r^2-E";
        } else
            formuleFinale = formuleBase.split("=")[1] + "-" + formuleBase.split("=")[0];
        for (Argument variable : variablesDonnes) {
            formuleFinale = formuleFinale.replace(variable.getArgumentName(), String.format(Locale.US, "%4.1e", variable.getArgumentValue()));
        }
        Expression expression;
        if (argumentsRestants.size() == 1)
            expression = new Expression("solve(" + formuleFinale + "," + argumentsRestants.get(0) + ",-50e9,50e9)");
        else
            expression = new Expression();
        return expression;
    }

    String genererDemarche(Expression expression) {
        String demarche = "";
        demarche += nom + "\n";
        demarche += formuleBase + "\n";
        demarche += formule + "\n";
        demarche += argumentsRestants.get(0) + " = " + String.format(Locale.US, "%4.1e", expression.calculate()) + "\n\n";
        return demarche;
    }

}