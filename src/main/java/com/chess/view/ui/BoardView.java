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

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.transform.Rotate;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;



public class BoardView extends Pane {
    private Board board;
    static final double demiLargeur = 28; 
    static final double demiHauteur = 48;
    private List<Move> moves = null;
    private Piece selectedPiece = null; 


    public BoardView(Board board) {
        this.board = board;
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
                            Position pos = (Position) losange.getUserData();
                            System.out.println("Case cliquée : " + pos);

                            if (pos == null) {
                                moves = null;
                                selectedPiece = null;
                                drawBoard();
                                return;
                            }

                            if (board.getPiece(pos) == null && !isPossibleDestination(pos)) {
                                moves = null;
                                selectedPiece = null;
                                drawBoard();
                            }
                        });
                        groupe.getChildren().add(losange);

                        if (position != null && board.getPiece(position) != null) {
                        selectedPiece = board.getPiece(position);
                        ImageView pieceView = new ImageView(getImageForPiece(board.getPiece(position)));
                        pieceView.setFitWidth(40);
                        pieceView.setFitHeight(40);
                        pieceView.setX(caseDepartX - 20);
                        pieceView.setY(caseDepartY - 20);
                        pieceView.getTransforms().add(new Rotate(-angleTotal, caseDepartX, caseDepartY));
                        pieceView.setUserData(position);
                        pieceView.setOnMouseClicked(event -> {
                            Position pos = (Position) pieceView.getUserData();
                            Piece piece = board.getPiece(pos);

                            selectedPiece = piece;
                            moves = piece.getMovementStrategy().getPossibleMoves(pos, board, piece);

                            if (piece.getType() == PieceType.King) {
                                List<Move> filteredMoves = new ArrayList<>();

                                for (Move move : moves) {
                                    if (!isAttackedByEnemy(move.getTo(), piece)) {
                                        filteredMoves.add(move);
                                    }
                                }

                                moves = filteredMoves;
                            }

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
                            Position pos = (Position) losange.getUserData();
                            System.out.println("Case cliquée : " + pos);

                            if (pos == null) {
                                moves = null;
                                selectedPiece = null;
                                drawBoard();
                                return;
                            }

                            if (board.getPiece(pos) == null && !isPossibleDestination(pos)) {
                                moves = null;
                                selectedPiece = null;
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
                            Position pos = (Position) pieceView.getUserData();
                            Piece piece = board.getPiece(pos);

                            selectedPiece = piece;
                            moves = piece.getMovementStrategy().getPossibleMoves(pos, board, piece);

                            if (piece.getType() == PieceType.King) {
                                List<Move> filteredMoves = new ArrayList<>();

                                for (Move move : moves) {
                                    if (!isAttackedByEnemy(move.getTo(), piece)) {
                                        filteredMoves.add(move);
                                    }
                                }

                                moves = filteredMoves;
                            }

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
                            Position pos = (Position) losange.getUserData();
                            System.out.println("Case cliquée : " + pos);

                            if (pos == null) {
                                moves = null;
                                selectedPiece = null;
                                drawBoard();
                                return;
                            }

                            if (board.getPiece(pos) == null && !isPossibleDestination(pos)) {
                                moves = null;
                                selectedPiece = null;
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
                                Position pos = (Position) pieceView.getUserData();
                                Piece piece = board.getPiece(pos);

                                selectedPiece = piece;
                                moves = piece.getMovementStrategy().getPossibleMoves(pos, board, piece);

                                if (piece.getType() == PieceType.King) {
                                    List<Move> filteredMoves = new ArrayList<>();

                                    for (Move move : moves) {
                                        if (!isAttackedByEnemy(move.getTo(), piece)) {
                                            filteredMoves.add(move);
                                        }
                                    }

                                    moves = filteredMoves;
                                }

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
                            Position pos = (Position) losange.getUserData();
                            System.out.println("Case cliquée : " + pos);

                            if (pos == null) {
                                moves = null;
                                selectedPiece = null;
                                drawBoard();
                                return;
                            }

                            if (board.getPiece(pos) == null && !isPossibleDestination(pos)) {
                                moves = null;
                                selectedPiece = null;
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
                                Position pos = (Position) pieceView.getUserData();
                                Piece piece = board.getPiece(pos);

                                selectedPiece = piece;
                                moves = piece.getMovementStrategy().getPossibleMoves(pos, board, piece);

                                if (piece.getType() == PieceType.King) {
                                    List<Move> filteredMoves = new ArrayList<>();

                                    for (Move move : moves) {
                                        if (!isAttackedByEnemy(move.getTo(), piece)) {
                                            filteredMoves.add(move);
                                        }
                                    }

                                    moves = filteredMoves;
                                }

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
                            Position pos = (Position) losange.getUserData();
                            System.out.println("Case cliquée : " + pos);

                            if (pos == null) {
                                moves = null;
                                selectedPiece = null;
                                drawBoard();
                                return;
                            }

                            if (board.getPiece(pos) == null && !isPossibleDestination(pos)) {
                                moves = null;
                                selectedPiece = null;
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
                                Position pos = (Position) pieceView.getUserData();
                                Piece piece = board.getPiece(pos);

                                selectedPiece = piece;
                                moves = piece.getMovementStrategy().getPossibleMoves(pos, board, piece);

                                if (piece.getType() == PieceType.King) {
                                    List<Move> filteredMoves = new ArrayList<>();

                                    for (Move move : moves) {
                                        if (!isAttackedByEnemy(move.getTo(), piece)) {
                                            filteredMoves.add(move);
                                        }
                                    }

                                    moves = filteredMoves;
                                }

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
                            Position pos = (Position) losange.getUserData();
                            System.out.println("Case cliquée : " + pos);

                            if (pos == null) {
                                moves = null;
                                selectedPiece = null;
                                drawBoard();
                                return;
                            }

                            if (board.getPiece(pos) == null && !isPossibleDestination(pos)) {
                                moves = null;
                                selectedPiece = null;
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
                                Position pos = (Position) pieceView.getUserData();
                                Piece piece = board.getPiece(pos);

                                selectedPiece = piece;
                                moves = piece.getMovementStrategy().getPossibleMoves(pos, board, piece);

                                if (piece.getType() == PieceType.King) {
                                    List<Move> filteredMoves = new ArrayList<>();

                                    for (Move move : moves) {
                                        if (!isAttackedByEnemy(move.getTo(), piece)) {
                                            filteredMoves.add(move);
                                        }
                                    }

                                    moves = filteredMoves;
                                }

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

}