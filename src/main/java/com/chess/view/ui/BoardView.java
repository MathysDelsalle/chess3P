package com.chess.view.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.chess.model.Board;
import com.chess.model.Couleur;
import com.chess.model.Move;
import com.chess.model.Piece;
import com.chess.model.PieceType;
import com.chess.model.Position;
import com.chess.model.engine.GameEngine;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;



public class BoardView extends Pane {
    private Board board;
    private GameEngine engine;
    static final double demiLargeur = 28; 
    static final double demiHauteur = 48;
    private List<Move> moves = null;
    private Piece selectedPiece = null;
    private Position selectedPosition = null;
    private Label statusLabel;
    private Label victoryLabel; 


    public BoardView(Board board, GameEngine engine) {
        this.board = board;
        this.engine = engine;

        setStyle("-fx-background-color: transparent;");
        setBackground(javafx.scene.layout.Background.EMPTY);

        statusLabel = new Label();
        statusLabel.setTextFill(Color.SILVER);
        statusLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        victoryLabel = new Label();
        victoryLabel.setTextFill(Color.GOLD);
        victoryLabel.setStyle("-fx-font-size: 36px; -fx-font-weight: bold;");

        sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                newScene.widthProperty().addListener((obsWidth, oldWidth, newWidth) -> drawBoard());
                newScene.heightProperty().addListener((obsHeight, oldHeight, newHeight) -> drawBoard());
            }
        });
    }

    public void drawBoard() {

        Scene scene = getScene();
        if (scene == null) return;

        double sceneWidth = scene.getWidth();
        double sceneHeight = scene.getHeight();

        double originX = sceneWidth / 2;
        double originY = sceneHeight - (sceneHeight / 2);
        double caseDepartX = originX;
        double caseDepartY = originY + demiHauteur;

        int indexCaseBlanc = 0;

        
        
        this.getTransforms().clear();
        this.getChildren().clear();
        this.setBackground(javafx.scene.layout.Background.EMPTY);
        updateStatusLabel();
        this.getChildren().add(statusLabel);


        Map<Integer,Position> positions = board.getPositions();

        for(int k=0;k<6;k++){
            Group groupe = new Group();
            caseDepartX = originX;
            caseDepartY = originY + demiHauteur;
            if(k<=1){
                creationDemiTier(caseDepartX, caseDepartY, indexCaseBlanc, groupe, originX, originY,k,positions);
            }else if(k>1 && k<=3){
                creationDemiTier(caseDepartX, caseDepartY, indexCaseBlanc, groupe, originX, originY,k,positions);
            }
            else{
                creationDemiTier(caseDepartX, caseDepartY, indexCaseBlanc, groupe, originX, originY,k,positions);
            }
            indexCaseBlanc++;
            
            Rotate rotation = new Rotate();
            rotation.setPivotX(originX);
            rotation.setPivotY(originY);
            rotation.setAngle(k*-60);
            groupe.getTransforms().add(rotation);
            this.getChildren().add(groupe);
            }

            Rotate rotationPane = new Rotate();
            rotationPane.setPivotX(originX);
            rotationPane.setPivotY(originY);
            rotationPane.setAngle(30);
            this.getTransforms().add(rotationPane);

            Rotate inverseRotation = new Rotate();
            inverseRotation.setPivotX(originX);
            inverseRotation.setPivotY(originY);
            inverseRotation.setAngle(-30);

            statusLabel.getTransforms().setAll(inverseRotation);

            if (engine.isGameOver()) {
            showVictoryMessage();
            }

            if (victoryLabel != null) {
                victoryLabel.getTransforms().setAll(
                    new Rotate(-30, originX, originY)
                );
            }
    }       



        

        
    

    public void creationDemiTier(double caseDepartX, double caseDepartY , int indexCaseBlanc,
        Group groupe, double originX, double originY, int k, Map<Integer,Position> positions){
            int tiers;
            int ligne;
            int colonne;
            double angleTotal = k * -60 + 30;
            if(k+1 == 1){
                tiers = 1;
                colonne = 4;
                for(int i=0;i<4;i++){
                    ligne = 4;
                    for(int j=0;j<4;j++){
                        Polygon losange = new Polygon(
                            caseDepartX, caseDepartY - demiHauteur,//top
                            caseDepartX + demiLargeur, caseDepartY,//right
                            caseDepartX, caseDepartY + demiHauteur,//bottom
                            caseDepartX - demiLargeur, caseDepartY//left
                        );
                        if(indexCaseBlanc % 2 ==0){
                            losange.setFill(Color.web("#969191"));
                            losange.setStroke(Color.web("#000000"));
                            losange.setStrokeWidth(1);
                            indexCaseBlanc++;
                        }
                        else{
                            losange.setFill(Color.web("#ffffff"));
                            losange.setStroke(Color.web("#000000"));
                            losange.setStrokeWidth(1);
                            indexCaseBlanc++;
                        }
                        int idCase = (tiers - 1) * 32 + (ligne - 1) * 8 + (colonne - 1);
                        Position position = positions.get(idCase);                  

                        Piece pieceOnCase = board.getPiece(position);

                        if (pieceOnCase != null
                                && pieceOnCase.getType() == PieceType.King
                                && engine.isKingInCheck(pieceOnCase.getOwner())) {
                            losange.setFill(Color.web("#fd7b7b"));
                        }
                        
                        if(position!=null){
                            if (isPossibleDestination(position)) {
                                losange.setFill(Color.web("#fffeab"));
                                if(board.getPiece(position)!=null && !(board.getPiece(position).getOwner().equals(selectedPiece.getOwner()))){
                                losange.setFill(Color.web("#fd7b7b"));
                                }
                            }

                        }
                        
                        losange.setUserData(position);
                        losange.setOnMouseClicked(event -> {
                            if (engine.isBotTurnInProgress()) {
                                event.consume();
                                return;
                            }
                            Position destination = (Position) losange.getUserData();
                            System.out.println("Case cliquée : " + destination);

                            Move chosenMove = findMoveTo(destination);

                            if (chosenMove != null) {
                                boolean success = engine.playMove(chosenMove);

                                if (success) {
                                    clearSelection();
                                    drawBoard();

                                    
                                    
                                }
                            } else {
                                clearSelection();
                                drawBoard();
                            }
                        });
                        groupe.getChildren().add(losange);

                        if (position != null && board.getPiece(position) != null) {
                        ImageView pieceView = new ImageView(getImageForPiece(board.getPiece(position)));
                        pieceView.setFitWidth(40);
                        pieceView.setFitHeight(40);
                        pieceView.setX(caseDepartX - 20);
                        pieceView.setY(caseDepartY - 20);
                        pieceView.getTransforms().add(new Rotate(-angleTotal, caseDepartX, caseDepartY));
                        pieceView.setUserData(position);
                        pieceView.setOnMouseClicked(event -> {
                            if (engine.isBotTurnInProgress()) {
                                event.consume();
                                return;
                            }
                            Position clickedPosition = (Position) pieceView.getUserData();
                            Piece clickedPiece = board.getPiece(clickedPosition);

                            if (clickedPiece == null) {
                                return;
                            }

                            Move chosenMove = findMoveTo(clickedPosition);

                            if (selectedPiece != null && chosenMove != null) {
                                boolean success = engine.playMove(chosenMove);

                                if (success) {
                                    clearSelection();
                                    drawBoard();
                                    
                                }

                                event.consume();
                                return;
                            }

                            if (!clickedPiece.getOwner().equals(engine.getCurrentPlayer())) {
                                event.consume();
                                return;
                            }

                            selectedPosition = clickedPosition;
                            selectedPiece = clickedPiece;

                            List<Move> possibleMoves =
                                    clickedPiece.getMovementStrategy().getPossibleMoves(clickedPosition, board, clickedPiece);

                            List<Move> legalMoves = new ArrayList<>();
                            for (Move move : possibleMoves) {
                                if (engine.isMoveValid(move)) {
                                    legalMoves.add(move);
                                }
                            }

                            moves = legalMoves;

                            
                            

                            drawBoard();
                            event.consume();
                        });

                        groupe.getChildren().add(pieceView);
                        }
                        
                        caseDepartX = caseDepartX + demiLargeur;
                        caseDepartY = caseDepartY + demiHauteur;
                        ligne --;
                    }
                    indexCaseBlanc++;
                    caseDepartX=originX - (i+1)*demiLargeur;
                    caseDepartY=originY + demiHauteur +(i+1)*demiHauteur;

                    colonne--;    
                }
            }
            else if(k+1 == 2){
                tiers = 1;
                
                ligne = 4;
                for(int i=0;i<4;i++){
                    colonne = 5;
                    for(int j=0;j<4;j++){
                        Polygon losange = new Polygon(
                            caseDepartX, caseDepartY - demiHauteur,//top
                            caseDepartX + demiLargeur, caseDepartY,//right
                            caseDepartX, caseDepartY + demiHauteur,//bottom
                            caseDepartX - demiLargeur, caseDepartY//left
                        );
                        if(indexCaseBlanc % 2 ==0){
                            losange.setFill(Color.web("#969191"));
                            losange.setStroke(Color.web("#000000"));
                            losange.setStrokeWidth(1);
                            indexCaseBlanc++;
                        }
                        else{
                            losange.setFill(Color.web("#ffffff"));
                            losange.setStroke(Color.web("#000000"));
                            losange.setStrokeWidth(1);
                            indexCaseBlanc++;
                        }
                        int idCase = (tiers - 1) * 32 + (ligne - 1) * 8 + (colonne - 1);
                        Position position = positions.get(idCase);
                        
                        Piece pieceOnCase = board.getPiece(position);

                        if (pieceOnCase != null
                                && pieceOnCase.getType() == PieceType.King
                                && engine.isKingInCheck(pieceOnCase.getOwner())) {
                            losange.setFill(Color.web("#fd7b7b"));
                        }
                        if(position!=null){
                            if (isPossibleDestination(position)) {
                                losange.setFill(Color.web("#fffeab"));
                                if(board.getPiece(position)!=null && !(board.getPiece(position).getOwner().equals(selectedPiece.getOwner()))){
                                losange.setFill(Color.web("#fd7b7b"));
                                }
                            }

                        }
                        losange.setUserData(position);
                        losange.setOnMouseClicked(event -> {
                            if (engine.isBotTurnInProgress()) {
                                event.consume();
                                return;
                            }
                            Position destination = (Position) losange.getUserData();
                            System.out.println("Case cliquée : " + destination);

                            Move chosenMove = findMoveTo(destination);

                            if (chosenMove != null) {
                                boolean success = engine.playMove(chosenMove);

                                if (success) {
                                    clearSelection();
                                    drawBoard();
                                    
                                }
                            } else {
                                clearSelection();
                                drawBoard();
                            }
                        });
                        groupe.getChildren().add(losange);

                        if (position != null && board.getPiece(position) != null) {
                            ImageView pieceView = new ImageView(getImageForPiece(board.getPiece(position)));
                            pieceView.setFitWidth(40);
                            pieceView.setFitHeight(40);
                            pieceView.setX(caseDepartX - 20);
                            pieceView.setY(caseDepartY - 20);
                            pieceView.getTransforms().add(new Rotate(-angleTotal, caseDepartX, caseDepartY));
                            pieceView.setUserData(position);
                            pieceView.setOnMouseClicked(event -> {
                                if (engine.isBotTurnInProgress()) {
                                event.consume();
                                return;
                            }
                            Position clickedPosition = (Position) pieceView.getUserData();
                            Piece clickedPiece = board.getPiece(clickedPosition);

                            if (clickedPiece == null) {
                                return;
                            }

                            Move chosenMove = findMoveTo(clickedPosition);

                            if (selectedPiece != null && chosenMove != null) {
                                boolean success = engine.playMove(chosenMove);

                                if (success) {
                                    clearSelection();
                                    drawBoard();
                                    
                                }

                                event.consume();
                                return;
                            }

                            if (!clickedPiece.getOwner().equals(engine.getCurrentPlayer())) {
                                event.consume();
                                return;
                            }

                            selectedPosition = clickedPosition;
                            selectedPiece = clickedPiece;

                            List<Move> possibleMoves =
                                    clickedPiece.getMovementStrategy().getPossibleMoves(clickedPosition, board, clickedPiece);

                            List<Move> legalMoves = new ArrayList<>();
                            for (Move move : possibleMoves) {
                                if (engine.isMoveValid(move)) {
                                    legalMoves.add(move);
                                }
                            }

                            moves = legalMoves;
                            
                            

                            drawBoard();
                            event.consume();
                        });

                            groupe.getChildren().add(pieceView);
                        }
                        caseDepartX = caseDepartX + demiLargeur;
                        caseDepartY = caseDepartY + demiHauteur;
                        colonne++;  
                    }
                    indexCaseBlanc++;
                    caseDepartX=originX - (i+1)*demiLargeur;
                    caseDepartY=originY + demiHauteur +(i+1)*demiHauteur;
                ligne --;
                }
            }
            else if(k+1 == 3){
                tiers = 3;
                colonne = 4;
                for(int i=0;i<4;i++){
                    ligne = 4;
                    for(int j=0;j<4;j++){
                        Polygon losange = new Polygon(
                            caseDepartX, caseDepartY - demiHauteur,//top
                            caseDepartX + demiLargeur, caseDepartY,//right
                            caseDepartX, caseDepartY + demiHauteur,//bottom
                            caseDepartX - demiLargeur, caseDepartY//left
                        );
                        if(indexCaseBlanc % 2 ==0){
                            losange.setFill(Color.web("#969191"));
                            losange.setStroke(Color.web("#000000"));
                            losange.setStrokeWidth(1);
                            indexCaseBlanc++;
                        }
                        else{
                            losange.setFill(Color.web("#ffffff"));
                            losange.setStroke(Color.web("#000000"));
                            losange.setStrokeWidth(1);
                            indexCaseBlanc++;
                        }
                        int idCase = (tiers - 1) * 32 + (ligne - 1) * 8 + (colonne - 1);
                        Position position = positions.get(idCase);
                        
                        Piece pieceOnCase = board.getPiece(position);

                        if (pieceOnCase != null
                                && pieceOnCase.getType() == PieceType.King
                                && engine.isKingInCheck(pieceOnCase.getOwner())) {
                            losange.setFill(Color.web("#fd7b7b"));
                        }
                        if(position!=null){
                            if (isPossibleDestination(position)) {
                                losange.setFill(Color.web("#fffeab"));
                                if(board.getPiece(position)!=null && !(board.getPiece(position).getOwner().equals(selectedPiece.getOwner()))){
                                losange.setFill(Color.web("#fd7b7b"));
                                }
                            }

                        }
                        losange.setUserData(position);
                        losange.setOnMouseClicked(event -> {
                            if (engine.isBotTurnInProgress()) {
                                event.consume();
                                return;
                            }
                            Position destination = (Position) losange.getUserData();
                            System.out.println("Case cliquée : " + destination);

                            Move chosenMove = findMoveTo(destination);

                            if (chosenMove != null) {
                                boolean success = engine.playMove(chosenMove);

                                if (success) {
                                    clearSelection();
                                    drawBoard();
                                    
                                }
                            } else {
                                clearSelection();
                                drawBoard();
                            }
                        });
                        groupe.getChildren().add(losange);

                        if (position != null && board.getPiece(position) != null) {
                            ImageView pieceView = new ImageView(getImageForPiece(board.getPiece(position)));
                            pieceView.setFitWidth(40);
                            pieceView.setFitHeight(40);
                            pieceView.setX(caseDepartX - 20);
                            pieceView.setY(caseDepartY - 20);
                            pieceView.getTransforms().add(new Rotate(-angleTotal, caseDepartX, caseDepartY));
                            pieceView.setUserData(position);
                            pieceView.setOnMouseClicked(event -> {
                                if (engine.isBotTurnInProgress()) {
                                event.consume();
                                return;
                            }
                            Position clickedPosition = (Position) pieceView.getUserData();
                            Piece clickedPiece = board.getPiece(clickedPosition);

                            if (clickedPiece == null) {
                                return;
                            }

                            Move chosenMove = findMoveTo(clickedPosition);

                            if (selectedPiece != null && chosenMove != null) {
                                boolean success = engine.playMove(chosenMove);

                                if (success) {
                                    clearSelection();
                                    drawBoard();
                                    
                                }

                                event.consume();
                                return;
                            }

                            if (!clickedPiece.getOwner().equals(engine.getCurrentPlayer())) {
                                event.consume();
                                return;
                            }

                            selectedPosition = clickedPosition;
                            selectedPiece = clickedPiece;

                            List<Move> possibleMoves =
                                    clickedPiece.getMovementStrategy().getPossibleMoves(clickedPosition, board, clickedPiece);

                            List<Move> legalMoves = new ArrayList<>();
                            for (Move move : possibleMoves) {
                                if (engine.isMoveValid(move)) {
                                    legalMoves.add(move);
                                }
                            }

                            moves = legalMoves;
                            
                            
                            drawBoard();
                            event.consume();
                        });

                            groupe.getChildren().add(pieceView);
                        }
                        
                        caseDepartX = caseDepartX + demiLargeur;
                        caseDepartY = caseDepartY + demiHauteur;
                        ligne --;
                    }
                    indexCaseBlanc++;
                    caseDepartX=originX - (i+1)*demiLargeur;
                    caseDepartY=originY + demiHauteur +(i+1)*demiHauteur;

                colonne--;     
                }
            }
            else if(k+1 == 4){
                tiers = 3;
                ligne = 4;
                for(int i=0;i<4;i++){
                    colonne = 5;
                    for(int j=0;j<4;j++){
                        Polygon losange = new Polygon(
                            caseDepartX, caseDepartY - demiHauteur,//top
                            caseDepartX + demiLargeur, caseDepartY,//right
                            caseDepartX, caseDepartY + demiHauteur,//bottom
                            caseDepartX - demiLargeur, caseDepartY//left
                        );
                        if(indexCaseBlanc % 2 ==0){
                            losange.setFill(Color.web("#969191"));
                            losange.setStroke(Color.web("#000000"));
                            losange.setStrokeWidth(1);
                            indexCaseBlanc++;
                        }
                        else{
                            losange.setFill(Color.web("#ffffff"));
                            losange.setStroke(Color.web("#000000"));
                            losange.setStrokeWidth(1);
                            indexCaseBlanc++;
                        }
                        int idCase = (tiers - 1) * 32 + (ligne - 1) * 8 + (colonne - 1);
                        Position position = positions.get(idCase);
                        
                        Piece pieceOnCase = board.getPiece(position);

                        if (pieceOnCase != null
                                && pieceOnCase.getType() == PieceType.King
                                && engine.isKingInCheck(pieceOnCase.getOwner())) {
                            losange.setFill(Color.web("#fd7b7b"));
                        }
                        if(position!=null){
                            if (isPossibleDestination(position)) {
                                losange.setFill(Color.web("#fffeab"));
                                if(board.getPiece(position)!=null && !(board.getPiece(position).getOwner().equals(selectedPiece.getOwner()))){
                                losange.setFill(Color.web("#fd7b7b"));
                                }
                            }

                        }
                        losange.setUserData(position);
                        losange.setOnMouseClicked(event -> {
                            if (engine.isBotTurnInProgress()) {
                                event.consume();
                                return;
                            }
                            Position destination = (Position) losange.getUserData();
                            System.out.println("Case cliquée : " + destination);

                            Move chosenMove = findMoveTo(destination);

                            if (chosenMove != null) {
                                boolean success = engine.playMove(chosenMove);

                                if (success) {
                                    clearSelection();
                                    drawBoard();
                                    
                                }
                            } else {
                                clearSelection();
                                drawBoard();
                            }
                        });
                        groupe.getChildren().add(losange);

                        if (position != null && board.getPiece(position) != null) {
                            ImageView pieceView = new ImageView(getImageForPiece(board.getPiece(position)));
                            pieceView.setFitWidth(40);
                            pieceView.setFitHeight(40);
                            pieceView.setX(caseDepartX - 20);
                            pieceView.setY(caseDepartY - 20);
                            pieceView.getTransforms().add(new Rotate(-angleTotal, caseDepartX, caseDepartY));
                            pieceView.setUserData(position);
                            pieceView.setOnMouseClicked(event -> {
                            if (engine.isBotTurnInProgress()) {
                                event.consume();
                                return;
                            }
                            Position clickedPosition = (Position) pieceView.getUserData();
                            Piece clickedPiece = board.getPiece(clickedPosition);

                            if (clickedPiece == null) {
                                return;
                            }

                            Move chosenMove = findMoveTo(clickedPosition);

                            if (selectedPiece != null && chosenMove != null) {
                                boolean success = engine.playMove(chosenMove);

                                if (success) {
                                    clearSelection();
                                    drawBoard();
                                    
                                }

                                event.consume();
                                return;
                            }

                            if (!clickedPiece.getOwner().equals(engine.getCurrentPlayer())) {
                                event.consume();
                                return;
                            }

                            selectedPosition = clickedPosition;
                            selectedPiece = clickedPiece;

                            List<Move> possibleMoves =
                                    clickedPiece.getMovementStrategy().getPossibleMoves(clickedPosition, board, clickedPiece);

                            List<Move> legalMoves = new ArrayList<>();
                            for (Move move : possibleMoves) {
                                if (engine.isMoveValid(move)) {
                                    legalMoves.add(move);
                                }
                            }

                            moves = legalMoves;
                            
                            
                            drawBoard();
                            event.consume();
                        });

                            groupe.getChildren().add(pieceView);
                        }
                        
                        caseDepartX = caseDepartX + demiLargeur;
                        caseDepartY = caseDepartY + demiHauteur;
                        colonne++; 
                    }
                    indexCaseBlanc++;
                    caseDepartX=originX - (i+1)*demiLargeur;
                    caseDepartY=originY + demiHauteur +(i+1)*demiHauteur;
                    ligne --;
                }
            }
            else if(k+1 == 5){
                tiers = 2;
                colonne = 4;
                for(int i=0;i<4;i++){
                    ligne = 4;
                    for(int j=0;j<4;j++){
                        Polygon losange = new Polygon(
                            caseDepartX, caseDepartY - demiHauteur,//top
                            caseDepartX + demiLargeur, caseDepartY,//right
                            caseDepartX, caseDepartY + demiHauteur,//bottom
                            caseDepartX - demiLargeur, caseDepartY//left
                        );
                        if(indexCaseBlanc % 2 ==0){
                            losange.setFill(Color.web("#969191"));
                            losange.setStroke(Color.web("#000000"));
                            losange.setStrokeWidth(1);
                            indexCaseBlanc++;
                        }
                        else{
                            losange.setFill(Color.web("#ffffff"));
                            losange.setStroke(Color.web("#000000"));
                            losange.setStrokeWidth(1);
                            indexCaseBlanc++;
                        }
                        int idCase = (tiers - 1) * 32 + (ligne - 1) * 8 + (colonne - 1);
                        Position position = positions.get(idCase);
                        
                        Piece pieceOnCase = board.getPiece(position);

                        if (pieceOnCase != null
                                && pieceOnCase.getType() == PieceType.King
                                && engine.isKingInCheck(pieceOnCase.getOwner())) {
                            losange.setFill(Color.web("#fd7b7b"));
                        }
                        if(position!=null){
                            if (isPossibleDestination(position)) {
                                losange.setFill(Color.web("#fffeab"));
                                if(board.getPiece(position)!=null && !(board.getPiece(position).getOwner().equals(selectedPiece.getOwner()))){
                                losange.setFill(Color.web("#fd7b7b"));
                                }
                            }

                        }
                        losange.setUserData(position);
                        losange.setOnMouseClicked(event -> {
                            if (engine.isBotTurnInProgress()) {
                                event.consume();
                                return;
                            }
                            Position destination = (Position) losange.getUserData();
                            System.out.println("Case cliquée : " + destination);

                            Move chosenMove = findMoveTo(destination);

                            if (chosenMove != null) {
                                boolean success = engine.playMove(chosenMove);

                                if (success) {
                                    clearSelection();
                                    drawBoard();
                                    
                                }
                            } else {
                                clearSelection();
                                drawBoard();
                            }
                        });
                        groupe.getChildren().add(losange);

                        if (position != null && board.getPiece(position) != null) {
                            ImageView pieceView = new ImageView(getImageForPiece(board.getPiece(position)));
                            pieceView.setFitWidth(40);
                            pieceView.setFitHeight(40);
                            pieceView.setX(caseDepartX - 20);
                            pieceView.setY(caseDepartY - 20);
                            pieceView.getTransforms().add(new Rotate(-angleTotal, caseDepartX, caseDepartY));
                            pieceView.setUserData(position);
                            pieceView.setOnMouseClicked(event -> {
                                if (engine.isBotTurnInProgress()) {
                                event.consume();
                                return;
                            }
                            Position clickedPosition = (Position) pieceView.getUserData();
                            Piece clickedPiece = board.getPiece(clickedPosition);

                            if (clickedPiece == null) {
                                return;
                            }

                            Move chosenMove = findMoveTo(clickedPosition);

                            if (selectedPiece != null && chosenMove != null) {
                                boolean success = engine.playMove(chosenMove);

                                if (success) {
                                    clearSelection();
                                    drawBoard();
                                    
                                }

                                event.consume();
                                return;
                            }

                            if (!clickedPiece.getOwner().equals(engine.getCurrentPlayer())) {
                                event.consume();
                                return;
                            }

                            selectedPosition = clickedPosition;
                            selectedPiece = clickedPiece;

                            List<Move> possibleMoves =
                                    clickedPiece.getMovementStrategy().getPossibleMoves(clickedPosition, board, clickedPiece);

                            List<Move> legalMoves = new ArrayList<>();
                            for (Move move : possibleMoves) {
                                if (engine.isMoveValid(move)) {
                                    legalMoves.add(move);
                                }
                            }

                            moves = legalMoves;
                            
                            

                            drawBoard();
                            event.consume();
                        });

                            groupe.getChildren().add(pieceView);
                        }
                        
                        caseDepartX = caseDepartX + demiLargeur;
                        caseDepartY = caseDepartY + demiHauteur;
                        ligne --;
                    }
                    indexCaseBlanc++;
                    caseDepartX=originX - (i+1)*demiLargeur;
                    caseDepartY=originY + demiHauteur +(i+1)*demiHauteur;

                colonne--;     
                }
            }
            else{
                tiers = 2;
                ligne = 4;
                for(int i=0;i<4;i++){
                    colonne = 5;
                    for(int j=0;j<4;j++){
                        Polygon losange = new Polygon(
                            caseDepartX, caseDepartY - demiHauteur,//top
                            caseDepartX + demiLargeur, caseDepartY,//right
                            caseDepartX, caseDepartY + demiHauteur,//bottom
                            caseDepartX - demiLargeur, caseDepartY//left
                        );
                        if(indexCaseBlanc % 2 ==0){
                            losange.setFill(Color.web("#969191"));
                            losange.setStroke(Color.web("#000000"));
                            losange.setStrokeWidth(1);
                            indexCaseBlanc++;
                        }
                        else{
                            losange.setFill(Color.web("#ffffff"));
                            losange.setStroke(Color.web("#000000"));
                            losange.setStrokeWidth(1);
                            indexCaseBlanc++;
                        }
                        int idCase = (tiers - 1) * 32 + (ligne - 1) * 8 + (colonne - 1);
                        Position position = positions.get(idCase);
                        
                        Piece pieceOnCase = board.getPiece(position);

                        if (pieceOnCase != null
                                && pieceOnCase.getType() == PieceType.King
                                && engine.isKingInCheck(pieceOnCase.getOwner())) {
                            losange.setFill(Color.web("#fd7b7b"));
                        }
                        if(position!=null){
                            if (isPossibleDestination(position)) {
                                losange.setFill(Color.web("#fffeab"));
                                if(board.getPiece(position)!=null && !(board.getPiece(position).getOwner().equals(selectedPiece.getOwner()))){
                                losange.setFill(Color.web("#fd7b7b"));
                                }
                            }

                        }
                        losange.setUserData(position);
                        losange.setOnMouseClicked(event -> {
                            if (engine.isBotTurnInProgress()) {
                                event.consume();
                                return;
                            }
                            Position destination = (Position) losange.getUserData();
                            System.out.println("Case cliquée : " + destination);

                            Move chosenMove = findMoveTo(destination);

                            if (chosenMove != null) {
                                boolean success = engine.playMove(chosenMove);

                                if (success) {
                                    clearSelection();
                                    drawBoard();
                                    
                                }
                            } else {
                                clearSelection();
                                drawBoard();
                            }
                        });
                        groupe.getChildren().add(losange);

                        if (position != null && board.getPiece(position) != null) {
                            ImageView pieceView = new ImageView(getImageForPiece(board.getPiece(position)));
                            pieceView.setFitWidth(40);
                            pieceView.setFitHeight(40);
                            pieceView.setX(caseDepartX - 20);
                            pieceView.setY(caseDepartY - 20);
                            pieceView.getTransforms().add(new Rotate(-angleTotal, caseDepartX, caseDepartY));
                            pieceView.setUserData(position);
                            pieceView.setOnMouseClicked(event -> {
                                if (engine.isBotTurnInProgress()) {
                                event.consume();
                                return;
                            }
                            Position clickedPosition = (Position) pieceView.getUserData();
                            Piece clickedPiece = board.getPiece(clickedPosition);

                            if (clickedPiece == null) {
                                return;
                            }

                            Move chosenMove = findMoveTo(clickedPosition);

                            if (selectedPiece != null && chosenMove != null) {
                                boolean success = engine.playMove(chosenMove);

                                if (success) {
                                    clearSelection();
                                    drawBoard();
                                }

                                event.consume();
                                return;
                            }

                            if (!clickedPiece.getOwner().equals(engine.getCurrentPlayer())) {
                                event.consume();
                                return;
                            }

                            selectedPosition = clickedPosition;
                            selectedPiece = clickedPiece;

                            List<Move> possibleMoves =
                                    clickedPiece.getMovementStrategy().getPossibleMoves(clickedPosition, board, clickedPiece);

                            List<Move> legalMoves = new ArrayList<>();
                            for (Move move : possibleMoves) {
                                if (engine.isMoveValid(move)) {
                                    legalMoves.add(move);
                                }
                            }

                            moves = legalMoves;
                            
                            
                            drawBoard();
                            event.consume();
                        });
                            groupe.getChildren().add(pieceView);
                        }
                        
                        caseDepartX = caseDepartX + demiLargeur;
                        caseDepartY = caseDepartY + demiHauteur;
                        colonne++;
                    }
                    indexCaseBlanc++;
                    caseDepartX=originX - (i+1)*demiLargeur;
                    caseDepartY=originY + demiHauteur +(i+1)*demiHauteur;
                    ligne --;   
                }
            }
            
    }


    public Image getImageForPiece(Piece piece) {
    Couleur color = piece.getColor();

        return loadImage("set_" + color + "_" + piece.getType().name().toLowerCase() + ".png");
            
    }

    public Image loadImage(String name) {
        return new Image(getClass().getResourceAsStream("/pieces/" + name));
    }

    public boolean isPossibleDestination(Position position) {
        if(moves != null){
            for (Move m : moves) {
                if (m.getTo().equals(position)) {
                    return true;
                }
            }
        }
       
    return false;
    }

    public boolean isAttackedByEnemy(Position position, Piece selectedPiece) {
    Set<Piece> attackers = board.getUnderAttack().get(position);

    if (attackers == null || attackers.isEmpty()) {
        return false;
    }

        for (Piece attacker : attackers) {
            if (attacker != null && attacker.getOwner() != selectedPiece.getOwner()) {
            return true;
            }
        }

        return false;
    }

    public Move findMoveTo(Position destination) {
        if (moves == null || destination == null) {
            return null;
        }

        for (Move move : moves) {
            if (move.getTo().equals(destination)) {
                return move;
            }
        }

        return null;
    }

    public void clearSelection() {
        selectedPosition = null;
        selectedPiece = null;
        moves = null;
    }

    public void updateStatusLabel() {
        statusLabel.setLayoutX(20);
        statusLabel.setLayoutY(20);

        if (engine.isGameOver()) {
            statusLabel.setText("Partie terminée");
        } else {
            statusLabel.setText("Tour de : " + engine.getCurrentPlayer());
        }
    }

    public void showVictoryMessage() {
        Scene scene = getScene();
        if (scene == null) return;

        double width = scene.getWidth();
        double height = scene.getHeight();

        //overlay sombre
        Rectangle overlay = new Rectangle(width, height);
        overlay.setFill(Color.color(0, 0, 0, 0.4));

        //panel blanc
        Rectangle bg = new Rectangle(300, 100);
        bg.setFill(Color.web("#a7a7a7"));
        bg.setArcWidth(20);
        bg.setArcHeight(20);
        bg.setStroke(Color.GOLD);

        double centerX = width / 2;
        double centerY = height / 2;

        bg.setX(centerX - 150);
        bg.setY(centerY - 50);

        //texte
        Label victory = new Label("Victoire : " + engine.getWinner() + " 🏆");
        victory.setTextFill(Color.GOLD);
        victory.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        victory.setLayoutX(centerX - 120);
        victory.setLayoutY(centerY - 15);

        Button replayButton = new Button("Rejouer");
        replayButton.setLayoutX(centerX - 110);
        replayButton.setLayoutY(centerY + 20);

        Button quitButton = new Button("Quitter");
        quitButton.setLayoutX(centerX + 30);
        quitButton.setLayoutY(centerY + 20);

        replayButton.setOnAction(e -> {
            engine.resetGame();
            clearSelection();
            drawBoard();
        });

        quitButton.setOnAction(e -> {
            Platform.exit();
        });

        Group panel = new Group(bg, victory, replayButton, quitButton);

        //rotation inverse
        double originX = width / 2;
        double originY = height / 2;
        panel.getTransforms().add(new Rotate(-30, originX, originY));
        overlay.getTransforms().add(new Rotate(-30, originX, originY));

        //ordre d'affichage
        this.getChildren().addAll(overlay, panel);
        overlay.toFront();
        panel.toFront();
    }

}