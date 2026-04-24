package com.chess.view.ui;

import java.util.ArrayList;
import java.util.List;

import com.chess.factories.BoardFactory;
import com.chess.model.Board;
import com.chess.model.Bot;
import com.chess.model.Player;
import com.chess.model.People;
import com.chess.model.engine.GameEngine;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) {
        Player p = new Player("Tarti");
        Bot b1 = new Bot();
        Bot b2 = new Bot();

        Board board = BoardFactory.createInitialBoard(p, b1, b2);

        List<People> people = new ArrayList<>();
        people.add(p);
        people.add(b1);
        people.add(b2);

        GameEngine engine = new GameEngine(board,people);

        BoardView boardView = new BoardView(board,engine);

        System.out.println("Ordre des joueurs :");
        for (People person : people) {
            System.out.println(person);
        }

        Scene scene = new Scene(boardView, 1200, 800);
        scene.setFill(Color.web("#222222"));

        stage.setTitle("Jeu d'Échecs Yalta - JavaFX");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}