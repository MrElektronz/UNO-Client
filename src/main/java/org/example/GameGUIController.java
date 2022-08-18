package org.example;

import de.kbecker.cards.Card;
import de.kbecker.cards.Card3D;
import de.kbecker.cards.Deck;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class GameGUIController implements Initializable {

    private ImageView deckView;
    private Card3D currentCard;

    @FXML
    private Pane mainPane;

    private static GameGUIController instance;
    @FXML
    private void switchToSecondary() throws IOException {
        App.setRoot("GameGUI",1200,800);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        instance = this;
        Deck d = new Deck();
        mainPane.getChildren().add(new ImageView(new Image("background.png")));
        mainPane.getChildren().addAll(d.getDeckView());

    }

    public void setCurrentCard(Card card){
        Platform.runLater(
                new Runnable() {
                    @Override
                    public void run() {
                        if(currentCard != null){
                            mainPane.getChildren().remove(currentCard.getCardView());
                        }
                        currentCard = new Card3D(card,true,530,300);
                        mainPane.getChildren().add(currentCard.getCardView());
                    }
                }
        );

    }

    public static GameGUIController getInstance() {
        return instance;
    }
}
