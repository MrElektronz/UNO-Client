package org.example;

import de.kbecker.cards.Card;
import de.kbecker.cards.Card3D;
import de.kbecker.cards.Deck;
import de.kbecker.gui.WildCardColorPicker;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
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

    public Card3D getCurrentCard() {
        return currentCard;
    }

    public void setCurrentCard(Card card){
        Platform.runLater(
                new Runnable() {
                    @Override
                    public void run() {
                        if(currentCard != null){
                            mainPane.getChildren().remove(currentCard);
                        }
                        currentCard = new Card3D(card,true,530,300,false);
                        mainPane.getChildren().add(currentCard);
                    }
                }
        );
    }

    public void showWildCardPicker(){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                mainPane.getChildren().add(new WildCardColorPicker());
            }
        });
    }
    public void setPlayerCards(ArrayList<Card> cards){


        // Set spacing variable so that the last card will be drawn at X=1020 max
        int space = 90;
        int cardLength = cards.size();
        int lastCardX = 20+((cardLength-1)*space);
        while(lastCardX>1020){
            space-=2;
            lastCardX = 20+((cardLength-1)*space);
        }
        System.out.println("space: "+space);
        System.out.println("cards: "+cardLength);


        int finalSpace = space;
        Platform.runLater(new Runnable() {

            @Override
            public void run() {
                // Use Iterator to remove all hand held cards to avoid ConcurrentModificationException
                for (Iterator<Node> iterator = mainPane.getChildren().iterator(); iterator.hasNext();) {
                    Node n = iterator.next();
                    if(n instanceof Card3D){
                        Card3D card3D = (Card3D) n;
                        if(card3D.isOnHand()){
                            iterator.remove();
                        }
                    }
                }
                for(int i = cards.size()-1; i>=0;i--){
                    Card3D card3D = new Card3D(cards.get(i),true,20+(i* finalSpace),550,true);
                    mainPane.getChildren().add(card3D);
                }
            }
        });


    }

    public static GameGUIController getInstance() {
        return instance;
    }

}
