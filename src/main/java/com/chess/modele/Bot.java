package modele;
public class Bot implements People {
    
    private int botId;

    public Bot(int id){
        botId=id;
    } 

    @Override
    public String toString() {
        
        return "Bot "+ botId;
    }

}
