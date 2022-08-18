package de.kbecker.cards;

import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * @author Kevin Becker (kevin.becker@stud.th-owl.de)
 */
public class Card3D {

    private Card card;
    private ImageView cardView;
    private int x,y;
    private boolean faceUp;

    public Card3D(Card card, boolean faceUp, int x, int y){
        this.card = card;
        this.faceUp = faceUp;
        this.x = x;
        this.y = y;
        setCardImage();
        System.out.println("UNO-"+card.getColor().name()+".png");
        cardView.setX(x);
        cardView.setY(y);
    }

    public boolean isFaceUp() {
        return faceUp;
    }


    public ImageView getCardView() {
        return cardView;
    }

    public Card getCard() {
        return card;
    }

    private void setCardImage(){
        cardView = new ImageView(new Image("UNO-"+card.getColor().name()+".png"));
        int number = 0;
        //if its special card
            switch(card.getType()){
                case SKIP:
                    number=10;
                    break;
                case DRAW2:
                    number=11;
                    break;
                case REVERSE:
                    number=12;
                    break;
                case NUMBER:
                    number = card.getNumber();
                    break;
                default:
                    number = 0;
                    break;
        }
        cardView.setViewport(new Rectangle2D((number)*164,0,164,234));
    }

    @Override
    public String toString() {
        return "Card3D{" +
                "card=" + card +
                ", cardView=" + cardView +
                ", x=" + x +
                ", y=" + y +
                ", faceUp=" + faceUp +
                '}';
    }
}
