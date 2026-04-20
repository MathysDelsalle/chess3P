package com.chess.view.ui;

import com.chess.model.Board;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.RotateTransition;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.util.Duration;


public class BoardView extends Pane {
    private Board board;
    static final double demiLargeur = 24; 
    static final double demiHauteur = 48; 


    public BoardView(Board board) {
        this.board = board;
        sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                newScene.widthProperty().addListener((obsWidth, oldWidth, newWidth) -> drawBoard());
                newScene.heightProperty().addListener((obsHeight, oldHeight, newHeight) -> drawBoard());
            }
        });
    }

    private void drawBoard() {

        Scene scene = getScene();
        if (scene == null) return;

        double sceneWidth = scene.getWidth();
        double sceneHeight = scene.getHeight();

        double originX = sceneWidth / 2;
        double originY = sceneHeight - (sceneHeight / 2);
        double caseDepartX = originX;
        double caseDepartY = originY + demiHauteur;

        int indexCaseBlanc = 0;

        
        

        this.getChildren().clear();

        //for(int k=0;k<2;k++){
            Group groupe = new Group();
            for(int i=0;i<4;i++){
                for(int j=0;j<4;j++){
                    Polygon losange = new Polygon(
                        caseDepartX, caseDepartY - demiHauteur,//top
                        caseDepartX + demiLargeur, caseDepartY,//right
                        caseDepartX, caseDepartY + demiHauteur,//bottom
                        caseDepartX - demiLargeur, caseDepartY//left
                    );
                    if(indexCaseBlanc % 2 ==0){
                        losange.setFill(Color.web("#fcdb9b"));
                        losange.setStroke(Color.web("#000000"));
                        losange.setStrokeWidth(2);
                        indexCaseBlanc++;
                    }
                    else{
                        losange.setFill(Color.web("#fff7e7"));
                        losange.setStroke(Color.web("#000000"));
                        losange.setStrokeWidth(2);
                        indexCaseBlanc++;
                    }
                    groupe.getChildren().add(losange);
                    caseDepartX = caseDepartX + demiLargeur;
                    caseDepartY = caseDepartY + demiHauteur;
                }
                indexCaseBlanc++;
                caseDepartX=originX - (i+1)*demiLargeur;
                caseDepartY=originY + demiHauteur +(i+1)*demiHauteur;

                
                
                
            }
            Rotate rotation = new Rotate();
            rotation.setPivotX(originX);
            rotation.setPivotY(originY);
            rotation.setAngle(30);
            groupe.getTransforms().add(rotation);
            this.getChildren().add(groupe);
        //}
            
            

        

        
    }
}