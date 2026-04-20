package com.chess.model;
public class Player implements People{

    private String name;
    private static int nextid=-1;
    private int id;

    public Player(String name) {
        this.name = name;
        id=nextid++;
    }
    
    @Override
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        
        return name;
    }

    @Override
    public int getId() {
        return id;
    }
}
