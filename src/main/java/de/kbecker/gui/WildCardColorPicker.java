package de.kbecker.gui;

import de.kbecker.utils.Client;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 * @author Kevin Becker (kevin.becker@stud.th-owl.de)
 */
public class WildCardColorPicker extends Group{

    private Circle red,blue,green, yellow;

    public WildCardColorPicker(){
        this.red = new Circle();
        this.blue = new Circle();
        this.green = new Circle();
        this.yellow = new Circle();
        Circle bg = new Circle();
        bg.setCenterY(400);
        bg.setCenterX(540);
        bg.setRadius(250);
        getChildren().add(bg);


        initCircle(red, Color.RED, 460,320);
        initCircle(blue, Color.BLUE,620,320);
        initCircle(green, Color.GREEN, 460,480);
        initCircle(yellow, Color.YELLOW,620,480);
        this.setViewOrder(-3f);
    }


    private void initCircle(Circle circle,Color color, int x, int y){
        circle.setCenterX(x);
        circle.setCenterY(y);
        circle.setRadius(70);
        circle.setViewOrder(-7f);
        circle.setFill(color);
        circle.setOnMouseClicked((MouseEvent e)->{
            Client.getInstance().chooseColor((Color)(circle.getFill()));
            ((Pane)getParent()).getChildren().remove(this);
        });
        getChildren().add(circle);
    }

}
