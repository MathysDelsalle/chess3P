package com.chess.view.ui;

import com.chess.factories.BoardFactory;
import com.chess.model.Board;
import com.chess.model.Bot;
import com.chess.model.Player;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) {
        Player p = new Player("Tarti");
        Bot b1 = new Bot();
        Bot b2 = new Bot();

        Board board = BoardFactory.createInitialBoard(p, b1, b2);

        BoardView boardView = new BoardView(board);

        Scene scene = new Scene(boardView, 1200, 800);

        stage.setTitle("Jeu d'Échecs Yalta - JavaFX");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}