/** @author Teimurazi Euashvili(Matrikelnummer:18808) */
package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Mein Wikibooks-Browser");
        primaryStage.getIcons().add(new Image("wikiBook_Logo.png"));
        primaryStage.setScene(new Scene(root, 1714, 930));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
