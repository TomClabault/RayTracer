package render;

import javafx.scene.Scene;

import java.net.URL;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.event.EventHandler;

/**
 * La classe contenant le code du choix de la taille du rendu.
 */
public class SetSizeWindow 
{
	/**
	 * Méthode éxécutant le code pour afficher la fenêtre du choix de rendu.
	 */
	public void execute() {
        Stage stage = new Stage();
        GridPane root = new GridPane();
        Scene scene = new Scene(root);
        
        URL styleURL = SetSizeWindow.class.getResource("style/window.css");
        if(styleURL == null)
        {
        	System.out.println("Impossible de trouver le style de SetSizeWindow");
        	
        	Platform.exit();
        	System.exit(0);
        }
        	
        scene.getStylesheets().add(styleURL.toExternalForm());

        stage.setScene(scene);
        stage.setTitle("Selection de la taille de rendu");

        Label textLargeur = new Label("Largeur");
        TextField inputLargeur = new TextField("largeur");
        Label textHauteur = new Label("Hauteur");
        TextField inputHauteur = new TextField("hauteur");
        CheckBox checkbox = new CheckBox("Mode automatique");
        Button validateButton = new Button("Valider");
        Button cancelButton = new Button("Annuler");

        root.add(textLargeur, 0, 0);
        root.add(inputLargeur, 1, 0);
        root.add(textHauteur, 0, 1);
        root.add(inputHauteur, 1, 1);
        root.add(checkbox, 1, 2);
        root.add(validateButton, 0, 3);
        root.add(cancelButton, 1, 3);

        validateButton.setOnAction(new EventHandler<ActionEvent>() 
    	{
            @Override
            public void handle(ActionEvent event) {
            	try {
            		if(checkbox.isSelected() == true) {
            			MainApp.AUTO_MODE = true;
            		}
        			if (Integer.parseInt(inputHauteur.getText()) < 0 || Integer.parseInt(inputLargeur.getText()) < 0) {
    					throw new NumberFormatException();
    				}
            		MainApp.HEIGHT = Integer.parseInt(inputHauteur.getText());
                    MainApp.WIDTH = Integer.parseInt(inputLargeur.getText());
            		
                    stage.close();
    			} catch (NumberFormatException e) {
    				System.out.println("Les arguments doivent être des entiers positifs");
    			}
                
            }
        });
        
        cancelButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
            	Platform.exit();
        		System.exit(0);
            }
        });
        
        stage.setOnCloseRequest(new EventHandler<WindowEvent>(){
        	@Override
        	public void handle(WindowEvent e) {
        		Platform.exit();
        		System.exit(0);
        	}
        });
        
        stage.showAndWait();


    }
}
