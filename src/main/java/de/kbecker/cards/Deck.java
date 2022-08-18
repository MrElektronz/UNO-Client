package de.kbecker.cards;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.ArrayList;

/**
 * @author Kevin Becker (kevin.becker@stud.th-owl.de)
 */
public class Deck {

    private ArrayList<Card> cards;
    private ArrayList<ImageView> deckView;

    public Deck(){
        this.cards = new ArrayList<Card>();
        this.deckView = new ArrayList<ImageView>();
        for(int i = 0; i<5;i++){
            addCardToView(i);
        }
    }

    public ArrayList<Card> getCards() {
        return cards;
    }

    public ArrayList<ImageView> getDeckView() {
        return deckView;
    }

    private void addCardToView(int offset){
        ImageView view = new ImageView(new Image("UNO-Back.png"));
        view.setX((offset*5)+340);
        view.setY((offset*5)+290);
        deckView.add(view);
    }
}
