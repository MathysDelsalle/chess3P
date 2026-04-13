package modele;

//enumération des direction et méthode d'inversion
public enum Direction {
    UP,
    DOWN,
    LEFT,
    RIGHT,
    DIAG_UP_LEFT,
    DIAG_UP_RIGHT,
    DIAG_DOWN_LEFT,
    DIAG_DOWN_RIGHT,
    DIAG_CENTRE_LEFT,
    DIAG_CENTRE_RIGHT;

        private Direction switchDir;

        static {
        LEFT.switchDir = RIGHT;
        RIGHT.switchDir = LEFT;

        UP.switchDir = DOWN;
        DOWN.switchDir = UP;

        DIAG_UP_LEFT.switchDir = DIAG_DOWN_RIGHT;
        DIAG_DOWN_RIGHT.switchDir = DIAG_UP_LEFT;

        DIAG_UP_RIGHT.switchDir = DIAG_DOWN_LEFT;
        DIAG_DOWN_LEFT.switchDir = DIAG_UP_RIGHT;
        DIAG_CENTRE_LEFT.switchDir = DIAG_CENTRE_RIGHT;
        DIAG_CENTRE_RIGHT.switchDir = DIAG_CENTRE_LEFT;
    }

    public Direction switchDir() {
        return switchDir;
    }
}


