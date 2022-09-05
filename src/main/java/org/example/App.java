package org.example;

import de.kbecker.cards.Card;
import de.kbecker.thread.TaskThread;
import de.kbecker.utils.ConfigManager;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.w3c.dom.events.Event;

import java.io.IOException;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;
    private static Stage stage;
    public static TaskThread gThread;
    @Override
    public void start(Stage stage) throws IOException {
        this.stage = stage;
        scene = new Scene(loadFXML("Scene_Login"), 600, 400);
        scene.setFill(Color.DARKBLUE);
        stage.setScene(scene);
        stage.setTitle("UNO by Kevin Becker");
        stage.setResizable(false);
        stage.show();

        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                System.exit(0);
            }
        });

    }




    public static void setRoot(String fxml, int width, int height) throws IOException {
        scene.setRoot(loadFXML(fxml));
        stage.setWidth(width+16);
        stage.setHeight(height+39);
    }

    private static Parent loadFXML(String fxml) throws IOException {
        System.out.println(App.class.getResource(fxml + ".fxml").getPath());
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

}