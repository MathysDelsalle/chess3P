package model;
import java.util.Objects;

public class Position {
    private final int tiers;
    private final int ligne;
    private final int colonne;
    private boolean isJunction = false;
    private int id = -1;


    public Position(int tiers,int ligne,int colonne) {
        this.tiers = tiers;
        this.ligne = ligne;
        this.colonne = colonne;

        if(ligne==4){
            isJunction=true;
        }
        
        this.id = (tiers - 1) * 32 + (ligne - 1) * 8 + (colonne - 1);
    }

    public int getTiers() {
        return tiers;
    }

    @Override
    public String toString() {
        return "Tiers : "+tiers+" Ligne : "+ligne+" Colone : "+colonne+"\n";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Position)) return false;
        Position position = (Position) o;
        return id == position.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public int getId() {
        return id;
    }

    public int getColonne() {
        return colonne;
    }

    public int getLigne() {
        return ligne;
    }

    public boolean getIsJunction(){
        return isJunction;
    }

}