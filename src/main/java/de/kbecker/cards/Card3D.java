package de.kbecker.cards;

import de.kbecker.utils.Client;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import org.example.GameGUIController;

/**
 * @author Kevin Becker (kevin.becker@stud.th-owl.de)
 */
public class Card3D extends ImageView{

    private Card card;
    private int x,y;
    private boolean faceUp;
    private boolean onHand;

    public Card3D(Card card, boolean faceUp, int x, int y, boolean onHand){
        super(new Image("UNO-"+card.getColor().name()+".png"));
        this.card = card;
        this.faceUp = faceUp;
        this.x = x;
        this.y = y;
        this.onHand = onHand;
        setCardImage();
        setX(x);
        setY(y);
    }

    public boolean isFaceUp() {
        return faceUp;
    }



    public Card getCard() {
        return card;
    }

    private void setCardImage(){
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
                case WILD4:
                    number = 1;
                    break;
                default:
                    number = 0;
                    break;
        }
        setViewport(new Rectangle2D((number)*164,0,164,234));
        setOnMouseEntered((MouseEvent event)->{
            if(faceUp){
                setViewOrder(-1d);
            }
        });
        setOnMouseExited((MouseEvent event)->{
            if(faceUp){
                setViewOrder(0d);
            }
        });
        setOnMouseClicked((MouseEvent event)->{
            if(onHand){
                Client.getInstance().setCard(card);
            }
        });
    }

    public boolean isOnHand() {
        return onHand;
    }

    @Override
    public String toString() {
        return "Card3D{" +
                "card=" + card +
                ", x=" + x +
                ", y=" + y +
                ", faceUp=" + faceUp +
                '}';
    }


}
