package org.example;

import de.kbecker.cards.Card;
import de.kbecker.cards.Card3D;
import de.kbecker.cards.Deck;
import de.kbecker.gui.WildCardColorPicker;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;
import java.util.ResourceBundle;

public class GameGUIController implements Initializable {

    private ImageView arrow;
    private Card3D currentCard;
    Circle wildCardCircle;

    @FXML
    private Pane mainPane;

    private static GameGUIController instance;


    @FXML
    private void switchToSecondary() throws IOException {
        App.setRoot("GameGUI",1200,800);
    }

    public ImageView getArrow() {
        return arrow;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        instance = this;
        Deck d = new Deck();
        arrow = new ImageView(new Image("arrow.png"));
        arrow.setX(557);
        arrow.setY(200);
        mainPane.getChildren().add(new ImageView(new Image("background.png")));
        mainPane.getChildren().addAll(d.getDeckView());
        mainPane.getChildren().add(arrow);
    }

    public Card3D getCurrentCard() {
        return currentCard;
    }

    public void setCurrentCard(Card card){
        Platform.runLater(
                new Runnable() {
                    @Override
                    public void run() {
                        if(wildCardCircle != null){
                            mainPane.getChildren().remove(wildCardCircle);
                            wildCardCircle = null;
                        }
                        if(currentCard != null){
                            mainPane.getChildren().remove(currentCard);
                        }
                        currentCard = new Card3D(card,true,530,300,false);
                        mainPane.getChildren().add(currentCard);
                    }
                }
        );
    }

    public void drawPlayers(ArrayList<Player> players){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                for (Iterator<Node> iterator = mainPane.getChildren().iterator(); iterator.hasNext();) {
                    Node n = iterator.next();
                    if(n instanceof Player){
                        iterator.remove();
                    }
                }
            }
        });
        arrow.setX(557);
        arrow.setY(200);
        for(int i = 0; i< players.size();i++){
            int x,y;

            switch(i){
                case 0:
                    x = 1150;
                    y = 400;
                    break;
                case 1:
                    x = 580;
                    y = 105;
                    break;
                case 2:
                    x = 50;
                    y = 400;
                    break;
                default:
                    x = 0;
                    y = 0;
                    break;
            }
            players.get(i).setLocation(x,y);
            int finalI = i;
            Platform.runLater(new Runnable() {
                @Override
                public void run() {

                    mainPane.getChildren().add(players.get(finalI));
                }
            });

        }
    }

    public void setWildCardColor(String color){
        System.out.println("Wilcard color: "+color);
        String colorCode = "0x008000ff";
        switch(color){
            case "RED":
                colorCode = "0xff0000ff";
                break;
            case "YELLOW":
                colorCode = "0xffff00ff";
                break;
            case "BLUE":
                colorCode = "0x0000ffff";
                break;
            default:
                colorCode = "0x008000ff";
                break;
        }
        wildCardCircle = new Circle();
        wildCardCircle.setFill(Paint.valueOf(colorCode));
        wildCardCircle.setRadius(70);
        wildCardCircle.setCenterX(530);
        wildCardCircle.setCenterY(300);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                mainPane.getChildren().add(wildCardCircle);
            }
        });
    }

    public void endGame(String winner){
        try{
            instance = null;
            App.setRoot("Scene_End",1200,800);
        }catch (Exception ex){
            ex.printStackTrace();
        }

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


    public static class Player extends Group {
        private String name;
        private int cardCount;
        private boolean myTurn;
        private int x,y;
        private Text cards, nameText;
        public Player(String name, int cardCount, boolean myTurn) {
            this.name = name;
            this.cardCount = cardCount;
            this.myTurn = myTurn;
            cards = new Text();
            cards.setFont(Font.font("Verdana", 20));
            cards.setFill(Color.WHITE);
            getChildren().add(cards);
            nameText = new Text();
            nameText.setText(name);
            nameText.setFont(Font.font("Verdana", 20));
            nameText.setFill(Color.WHITE);
            getChildren().add(nameText);
        }

        public void setLocation(int x, int y){
            this.x = x;
            this.y = y;
            cards.setX(x);
            cards.setY(y);
            cards.setText(cardCount+"");
            nameText.setX(x-30);
            nameText.setY(y+30);
            if(myTurn){
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        GameGUIController.getInstance().getArrow().setY(y-105);
                        GameGUIController.getInstance().getArrow().setX(x-42);
                        System.out.println("Arrow to enemy: "+x+", "+(y-60));
                    }
                });
            }

        }

        public void setCardCount(int cardCount) {
            this.cardCount = cardCount;
        }

        public void setMyTurn(boolean myTurn) {
            this.myTurn = myTurn;
        }

        public int getCardCount() {
            return cardCount;
        }

        public String getName() {
            return name;
        }

        public boolean isMyTurn() {
            return myTurn;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Player player = (Player) o;
            return Objects.equals(name, player.name);
        }

        @Override
        public int hashCode() {
            return name != null ? name.hashCode() : 0;
        }
    }

}
