package render;

import javafx.scene.Scene;
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
        GridPane root = new GridPane();
        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.setTitle("Selection de la taille de rendu");

        Label textLargeur = new Label("Largeur");
        TextField inputLargeur = new TextField("largeur");
        Label textHauteur = new Label("Hauteur");
        TextField inputHauteur = new TextField("hauteur");
        Button validateButton = new Button("Valider");
        Button cancelButton = new Button("Annuler");

        //gridPane.setPadding(new Insets(2));

        root.add(textLargeur, 0, 0);
        root.add(inputLargeur, 1, 0);
        root.add(textHauteur, 0, 1);
        root.add(inputHauteur, 1, 1);
        root.add(validateButton, 0, 2);
        root.add(cancelButton, 1, 2);

        validateButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                MainApp.HEIGHT = Integer.parseInt(textHauteur.getText());
                MainApp.WIDTH = Integer.parseInt(textLargeur.getText());
                stage.close();
            }
        });

        stage.showAndWait();


    }
}
