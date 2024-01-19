package SIMulator.ModelesQuestions;

import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import org.mariuszgromada.math.mxparser.Argument;

public class VarCellFactory extends ListCell<Argument> {
    @Override
    public void updateItem(Argument item, boolean empty) {
        super.updateItem(item, empty);
        if (item != null) {
            Label affichage = new Label(item.getArgumentName() + "=" + item.getArgumentValue());
            setGraphic(affichage);
        }
    }

}
