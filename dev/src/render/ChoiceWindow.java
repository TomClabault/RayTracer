package render;

import java.util.Optional;

import javafx.util.Pair;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Dialog;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.TextField;

public class ChoiceWindow {
/*TODO changer dialog en window normal*/
    public void choiceWindowMain() {

        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Taille du rendu");

        ButtonType validateButton = new ButtonType("Valider", ButtonData.OK_DONE);
        ButtonType cancelButton = new ButtonType("Annuler", ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(validateButton, cancelButton);

        GridPane grid = new GridPane();

        TextField hauteur = new TextField();
        hauteur.setPromptText("hauteur");
        TextField largeur = new TextField();
        largeur.setPromptText("largeur");

        grid.add(new Label("hauteur:"), 0, 0);
        grid.add(hauteur, 1, 0);
        grid.add(new Label("largeur:"), 0, 1);
        grid.add(largeur, 1, 1);

        dialog.getDialogPane().setContent(grid);

        // Request focus on the hauteur field by default.
        //Platform.runLater(() -> hauteur.requestFocus());

        // Convert the result to a hauteur-largeur-pair when the login button is clicked.
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == validateButton) {
                return new Pair<>(hauteur.getText(), largeur.getText());/*TODO modifier Pair<> en tableau de String*/
            }
            return null;
        });

        Optional<Pair<String, String>> result = dialog.showAndWait();

        result.ifPresent(hauteurlargeur -> {
            System.out.println("hauteur=" + hauteurlargeur.getKey() + ", largeur=" + hauteurlargeur.getValue());
            String[] args = new String[2];
            args[0] = hauteurlargeur.getKey();
            args[1] = hauteurlargeur.getValue();
            MainApp.HEIGHT = Integer.parseInt(hauteur.getText());
            MainApp.WIDTH = Integer.parseInt(largeur.getText());

        });




    }

}
