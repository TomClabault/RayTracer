package render;

import javafx.event.ActionEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.event.EventHandler;

public class SetSizeWindow {

    public void execute() {
        Stage stage = new Stage();
        GridPane gridPane = new GridPane();//root

        Label textLargeur = new Label("Largeur");
        TextField inputLargeur = new TextField("largeur");
        Label textHauteur = new Label("Hauteur");
        TextField inputHauteur = new TextField("hauteur");
        Button validateButton = new Button("Valider");
        Button cancelButton = new Button("Annuler");

        //gridPane.setPadding(new Insets(2));

        gridPane.add(textLargeur, 0, 0);
        gridPane.add(inputLargeur, 1, 0);
        gridPane.add(textHauteur, 0, 1);
        gridPane.add(inputHauteur, 1, 1);
        gridPane.add(validateButton, 0, 2);
        gridPane.add(cancelButton, 1, 2);

        //stage.

        validateButton.setOnAction(new EventHandler<ActionEvent>() {

                @Override
                public void handle(ActionEvent event) {
                    MainApp.HEIGHT = Integer.parseInt(textHauteur.getText());
                    MainApp.WIDTH = Integer.parseInt(textLargeur.getText());
                    stage.close();
                }
            });



    }
}
