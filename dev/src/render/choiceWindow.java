package render;

import javafx.util.Pair;
import javafx.scene.control.Alert;
import java.util.Optional;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.scene.control.Dialog;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.scene.Node;

public class choiceWindow {

    public static void choiceWindowMain() {

        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Taille du rendu");
        dialog.setHeaderText("Choisissez la taille du rendu");

        ButtonType validateButton = new ButtonType("Valider", ButtonData.OK_DONE);
        ButtonType cancelButton = new ButtonType("Annuler", ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(validateButton, cancelButton);

        // Create the hauteur and largeur labels and fields.
        GridPane grid = new GridPane();
        //grid.setHgap(10);
        //grid.setVgap(10);
        //grid.setPadding(new Insets(20, 150, 10, 10));

        TextField hauteur = new TextField();
        hauteur.setPromptText("hauteur");
        TextField largeur = new TextField();
        largeur.setPromptText("largeur");

        grid.add(new Label("hauteur:"), 0, 0);
        grid.add(hauteur, 1, 0);
        grid.add(new Label("largeur:"), 0, 1);
        grid.add(largeur, 1, 1);

        // Enable/Disable login button depending on whether a hauteur was entered.
        Node loginButton = dialog.getDialogPane().lookupButton(validateButton);
        loginButton.setDisable(true);

        // Do some validation (using the Java 8 lambda syntax).
        hauteur.textProperty().addListener((observable, oldValue, newValue) -> {
            loginButton.setDisable(newValue.trim().isEmpty());
        });

        dialog.getDialogPane().setContent(grid);

        // Request focus on the hauteur field by default.
        //Platform.runLater(() -> hauteur.requestFocus());

        // Convert the result to a hauteur-largeur-pair when the login button is clicked.
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == validateButton) {
                return new Pair<>(hauteur.getText(), largeur.getText());
            }
            return null;
        });

        Optional<Pair<String, String>> result = dialog.showAndWait();

        result.ifPresent(hauteurlargeur -> {
            System.out.println("hauteur=" + hauteurlargeur.getKey() + ", largeur=" + hauteurlargeur.getValue());
            String[] args = new String[2];
            args[0] = hauteurlargeur.getKey();
            args[1] = hauteurlargeur.getValue();
            MainApp.HEIGHT = Integer.parseInt(args[0]);
            MainApp.WIDTH = Integer.parseInt(args[1]);

        });




    }

}
