package DataObjects;

import ResultGenerator.ResultGenerator;

public class RobotWrappy2019 {
    private int x;
    private int y;

    private int sizeOfRightHand;
    private int sizeOfLeftHand;

    public static int amountOfCloneBooster = 0;
    public static int amountOfFastWheelBooster = 0;
    public static int amountOfDrillBooster = 0;
    public static int amountOfTeleportBooster = 0;
    public static int amountOfManipulatorBooster = 0;

    private int id;
    private static int counter = 0;

    private ResultGenerator resultGenerator = new ResultGenerator();

    private Orientation orientation;

    private enum Orientation {
        UP,
        RIGHT,
        DOWN,
        LEFT;
        private static Orientation[] vals = values();

        public Orientation next() {
            return vals[(this.ordinal() + 1) % vals.length];
        }

        public Orientation previous() {
            return vals[(this.ordinal() - 1) % vals.length];
        }
    }

    public RobotWrappy2019(int x, int y, Orientation orientation) {
        this.x = x;
        this.y = y;

        this.orientation = orientation;

        id = counter++;
    }

    public void turnRight() {
        resultGenerator.turnRight(id);
        orientation = orientation.next();
    }

    public void turnLeft() {
        resultGenerator.turnLeft(id);
        orientation = orientation.previous();
    }

    public void moveUp() {
        resultGenerator.moveUp(id);
    }

    public void moveDown() {
        resultGenerator.moveDown(id);
    }

    public void moveLeft() {
        resultGenerator.moveLeft(id);
    }

    public void moveRight() {
        resultGenerator.moveRight(id);
    }

    public void addHand(int dx, int dy) {
        resultGenerator.addHand(id, dx, dy);
    }

    public void useFastWheel() {
        resultGenerator.useFastWheel(id);
    }

    public void skipTurn() {
        resultGenerator.skip(id);
    }

    public void spawnCLoWn() {
        resultGenerator.spawnCLoWn(id);
    }

    public void resetTeleport() {
        resultGenerator.resetTeleport(id);
    }

    public void useTeleport(int x, int y) {
        resultGenerator.useTeleport(id, x, y);
    }
}
