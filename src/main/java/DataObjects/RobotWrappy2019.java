package DataObjects;

import ResultGenerator.ResultGenerator;

public class RobotWrappy2019 {
    private int x;
    private int y;

    private int sizeOfRightHand = 1;
    private int sizeOfLeftHand = 1;

    public static int amountOfCloneBooster = 0;
    public static int amountOfFastWheelBooster = 0;
    public static int amountOfDrillBooster = 0;
    public static int amountOfTeleportBooster = 0;
    public static int amountOfManipulatorBooster = 0;

    private int id;
    private static int counter = 0;

    private static ResultGenerator resultGenerator = new ResultGenerator();

    private Orientation orientation;

    private Field leftField;
    private Field frontLeftField;
    private Field frontMiddleField;

    private Map map;

    enum Orientation {
        UP(0, 1),
        RIGHT(1, 0),
        DOWN(0, -1),
        LEFT(-1, 0);
        private static Orientation[] vals = values();

        private int dx;
        private int dy;

        Orientation(int dx, int dy) {
            this.dx = dx;
            this.dy = dy;
        }

        public Orientation next() {
            return vals[(this.ordinal() + 1) % vals.length];
        }

        public Orientation previous() {
            return vals[(this.ordinal() - 1) % vals.length];
        }

        public int getDx() {
            return dx;
        }

        public int getDy() {
            return dy;
        }
    }

    public RobotWrappy2019(int x, int y, Map map) {
        this.x = x;
        this.y = y;

        this.orientation = Orientation.UP;

        this.map = map;
        countAndPaintFields();

        id = counter++;
    }

    private void countAndPaintFields() {
        leftField = map.map[x + orientation.dx - (sizeOfLeftHand + 1) * orientation.dy]
                [y + orientation.dy + (sizeOfLeftHand + 1) * orientation.dx];
        frontLeftField = map.map[x + orientation.dx * 2 - sizeOfLeftHand * orientation.dy]
                [y + orientation.dy * 2 + sizeOfLeftHand * orientation.dx];
        frontMiddleField = map.map[x + orientation.dx * 2]
                [y + orientation.dy * 2];
        frontLeftField.setIsPainted(true);
        frontMiddleField.setIsPainted(true);
        //map.map[robot.getX() + 1][robot.getY() + 1].setIsPainted(true);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getSizeOfRightHand() {
        return sizeOfRightHand;
    }

    public int getSizeOfLeftHand() {
        return sizeOfLeftHand;
    }

    public void turnRight() {
        resultGenerator.turnRight(id);
        orientation = orientation.next();
        countAndPaintFields();
    }

    public void turnLeft() {
        resultGenerator.turnLeft(id);
        orientation = orientation.previous();
        countAndPaintFields();
    }

    public void moveStraight() {
        switch (orientation) {
            case UP: {
                resultGenerator.moveUp(id);
                y++;
            }
            break;
            case DOWN: {
                resultGenerator.moveDown(id);
                y--;
            }
            break;
            case LEFT: {
                resultGenerator.moveLeft(id);
                x++;
            }
            break;
            default: {
                resultGenerator.moveRight(id);
                x--;
            }
            break;
        }
        countAndPaintFields();
    }


    public void moveLeft() {
        switch (orientation) {
            case UP: {
                resultGenerator.moveLeft(id);
                x++;
            }
            break;
            case DOWN: {
                resultGenerator.moveRight(id);
                x--;
            }
            break;
            case LEFT: {
                resultGenerator.moveDown(id);
                y--;
            }
            break;
            default: {
                resultGenerator.moveUp(id);
                y++;
            }
            break;
        }
        countAndPaintFields();
    }

    public void moveRight() {
        switch (orientation) {
            case UP: {
                resultGenerator.moveRight(id);
                x--;
            }
            break;
            case DOWN: {
                resultGenerator.moveLeft(id);
                x++;
            }
            break;
            case LEFT: {
                resultGenerator.moveUp(id);
                y++;
            }
            break;
            default: {
                resultGenerator.moveDown(id);
                y--;
            }
            break;
        }
        countAndPaintFields();
    }

    public void addLeftHand() {
        sizeOfLeftHand++;
        resultGenerator.addHand(
                id,
                orientation.dx - sizeOfLeftHand * orientation.dy,
                orientation.dy + sizeOfLeftHand * orientation.dx);
        amountOfManipulatorBooster--;
        countAndPaintFields();
    }

    public void addRightHand() {
        sizeOfRightHand++;
        resultGenerator.addHand(
                id,
                orientation.dx + sizeOfRightHand * orientation.dy,
                orientation.dy - sizeOfRightHand * orientation.dx);
        amountOfManipulatorBooster--;
        countAndPaintFields();
    }


    public void useFastWheel() {
        resultGenerator.useFastWheel(id);
        amountOfFastWheelBooster--;
    }

    public void skipTurn() {
        resultGenerator.skip(id);
    }

    public void spawnCLoWn() {
        resultGenerator.spawnCLoWn(id);
        amountOfCloneBooster--;
    }

    public void resetTeleport() {
        resultGenerator.resetTeleport(id);
        amountOfTeleportBooster--;
    }

    public void useTeleport(int x, int y) {
        resultGenerator.useTeleport(id, x, y);
    }

    public Field getLeftField() {
        return leftField;
    }

    public Field getFrontLeftField() {
        return frontLeftField;
    }

    public Field getFrontMiddleField() {
        return frontMiddleField;
    }

    public static ResultGenerator getResultGenerator() {
        return resultGenerator;
    }
}
