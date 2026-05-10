package com.chess.model;
public class Player implements People{

    private String name;
    private static int nextid=0;
    private int id;
    private PeopleType type;

    public Player(String name) {
        this.name = name;
        id=nextid++;
        type=PeopleType.Player;
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

    @Override
    public PeopleType getType() {
        return type;
    }
}
