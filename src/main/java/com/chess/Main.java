package com.chess;

import java.util.List;
import java.util.Scanner;

import com.chess.factories.BoardFactory;
import com.chess.model.Board;
import com.chess.model.Bot;
import com.chess.model.People;
import com.chess.model.Player;
import com.chess.model.engine.GameEngine;
import com.chess.view.*;
//import model.*;

public class Main {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        System.out.println("Entrez le nom du joueur :");
        String name1 = sc.nextLine();
        sc.close();

        Player player = new Player(name1);
        Bot b1 = new Bot();
        Bot b2 = new Bot();

        Board board = BoardFactory.createInitialBoard(player,b1,b2);
        List<People> players = List.of(player, b1, b2);

        GameEngine engine = new GameEngine(board,players);
  
        System.out.println(engine.getCurrentPlayerId());
        ConsoleView consoleView = new ConsoleView();

        consoleView.diplayCreationReussie();


        //consoleView.displayPossibleMoves(p, board);
        //consoleView.displayAttackMap(board.getUnderAttack());
        //consoleView.displayControledMap(board.getProtectedRightNow());
    }
}