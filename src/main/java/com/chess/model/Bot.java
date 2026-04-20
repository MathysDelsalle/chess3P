package com.chess.model;
public class Bot implements People {
    
    private static int nextid=0;
    private int botId;
    private String name;

    public Bot(){
        botId=nextid++;
        name = "Bot : " + botId;
    } 

    @Override
    public String toString() {
        
        return "Bot "+ botId;
    }

    @Override
    public int getId() {
        return botId;
    }

    @Override
    public String getName() {
        return name;
    }

}
