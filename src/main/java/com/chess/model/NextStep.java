package model;

public class NextStep {
        private final Position position;
        private final Direction direction;

        public NextStep(Position position, Direction direction) {
            this.position = position;
            this.direction = direction;
        }

        public Direction getDirection() {
            return direction;
        }

        public Position getPosition() {
            return position;
        }
    }
