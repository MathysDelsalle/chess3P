package modele;
import java.util.*; 

public class Board {

    //map des position des pieces puis des voisin et des diags pour les cases
    private Map<Position, Piece> pieces = new HashMap<>();
    private Map<Position, Map<Direction,Position>> neighbors = new HashMap<>();
    private Map<Position, Map<Direction,Position>> diag = new HashMap<>();
    private Map<Integer, Position> positions = new HashMap<>();

    //ajoute les cases dans neighbor et diag afin de dire ensuite les connexions entre elles
    public void addPosition(Position position) {
        neighbors.putIfAbsent(position, new HashMap<>());
        diag.putIfAbsent(position, new HashMap<>());
        positions.put(position.getId(), position);
    }

    //connecte les case qui sont voisines par les coté
    public void connectSide(Position from, Position to, Direction direction) {
        neighbors.get(from).put(direction,to);
        direction=direction.switchDir();
        neighbors.get(to).put(direction,from);
}
    //connecte les case qui sont voisines par les diagonales
    public void connectDiag(Position from, Position to, Direction direction) {
        diag.get(from).put(direction,to);
        direction=direction.switchDir();
        diag.get(to).put(direction,from);
}

    public Map<Direction, Position> getNeighbors(Position position) {
        return neighbors.getOrDefault(position, new HashMap<>());
    }

    public Map<Direction, Position> getDiags(Position position) {
        return diag.getOrDefault(position, new HashMap<>());
    }

    public Position getNeighborsDirection(Position position, Direction dir){
        return neighbors.getOrDefault(position, new HashMap<>()).get(dir);
    }

    public Position getDiagDirection(Position position, Direction dir){
        return diag.getOrDefault(position, new HashMap<>()).get(dir);
    }

    public Piece getPiece(Position position) {

        return pieces.get(position);
    }

    public void setPiece(Position position, Piece piece) {

        pieces.put(position, piece);
    }

    public Position getPosition(int id) {
    return positions.get(id);
}

}

