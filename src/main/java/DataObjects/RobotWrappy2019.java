package DataObjects;

import ResultGenerator.ResultGenerator;

import java.util.ArrayList;

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
    private Field rightField;
    private Field frontLeftField;
    private Field frontMiddleField;
    private Field bodyField;
    private final ArrayList<Field> hands = new ArrayList<>();

    private final Map map;

    enum Orientation {
        UP(0, 1),
        RIGHT(1, 0),
        DOWN(0, -1),
        LEFT(-1, 0);
        private static final Orientation[] vals = values();

        private final int dx;
        private final int dy;

        Orientation(int dx, int dy) {
            this.dx = dx;
            this.dy = dy;
        }

        public Orientation next() {
            return vals[(this.ordinal() + 1) % vals.length];
        }

        public Orientation previous() {
            return vals[(this.ordinal() + 3) % vals.length];
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
        countFields();
        paintFields();

        id = counter++;
    }

    private void countFields() {
        bodyField = checkField(x, y);
        hands.clear();

        for (int i = sizeOfLeftHand; i > 0; i--) {
            hands.add(checkField(x + orientation.dx - i * orientation.dy,
                    y + orientation.dy + i * orientation.dx));
        }

        for (int i = 0; i <= sizeOfRightHand; i++) {
            hands.add(checkField(x + orientation.dx + i * orientation.dy,
                    y + orientation.dy - i * orientation.dx));
        }


        leftField = checkField(x + orientation.dx - (sizeOfLeftHand + 1) * orientation.dy,
                y + orientation.dy + (sizeOfLeftHand + 1) * orientation.dx);

        rightField = checkField(x + orientation.dx + (sizeOfLeftHand + 1) * orientation.dy,
                y + orientation.dy - (sizeOfLeftHand + 1) * orientation.dx);

        frontLeftField = checkField(x + orientation.dx * 2 - sizeOfLeftHand * orientation.dy,
                y + orientation.dy * 2 + sizeOfLeftHand * orientation.dx);


        frontMiddleField = checkField(x + orientation.dx * 2, y + orientation.dy * 2);
    }

    private Field checkField(int newX, int newY) {
        if (newX < 0 || newX >= map.sizeX || newY < 0 || newY >= map.sizeY) {
            Field res = new Field(1, 1);
            res.setIsObstacle(true);
            return res;
        }
        return map.map[newX][newY];
    }

    private void paintFields() {
        bodyField.setIsPainted(true);
        hands.forEach(field -> field.setIsPainted(true));
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
        paintFields();
        resultGenerator.turnRight(id);
        orientation = orientation.next();
        countFields();
    }

    public void turnLeft() {
        paintFields();
        resultGenerator.turnLeft(id);
        orientation = orientation.previous();
        countFields();
    }

    public void moveStraight() {
        paintFields();
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
                x--;
            }
            break;
            default: {
                resultGenerator.moveRight(id);
                x++;
            }
            break;
        }
        countFields();
    }


    public void moveLeftRegardingTheRobot() {
        paintFields();
        switch (orientation) {
            case UP: {
                resultGenerator.moveLeft(id);
            }
            break;
            case DOWN: {
                resultGenerator.moveRight(id);
            }
            break;
            case LEFT: {
                resultGenerator.moveUp(id);
            }
            break;
            default: {
                resultGenerator.moveDown(id);
            }
            break;
        }
        x--;
        countFields();
    }

    public void moveRightRegardingTheRobot() {
        paintFields();
        switch (orientation) {
            case UP: {
                resultGenerator.moveRight(id);
            }
            break;
            case DOWN: {
                resultGenerator.moveLeft(id);
            }
            break;
            case LEFT: {
                resultGenerator.moveDown(id);
            }
            break;
            default: {
                resultGenerator.moveUp(id);
            }
            break;
        }
        x++;
        countFields();
    }

    public void moveUp() {
        paintFields();
        resultGenerator.moveUp(id);
        y++;
        countFields();
    }

    public void moveDown() {
        paintFields();
        resultGenerator.moveDown(id);
        y--;
        countFields();
    }

    public void moveRight() {
        paintFields();
        resultGenerator.moveRight(id);
        x++;
        countFields();
    }

    public void moveLeft() {
        paintFields();
        resultGenerator.moveLeft(id);
        x--;
        countFields();
    }

    public void addLeftHand() {
        sizeOfLeftHand++;
        resultGenerator.addHand(
                id,
                orientation.dx - sizeOfLeftHand * orientation.dy + 1,
                orientation.dy + sizeOfLeftHand * orientation.dx + 1);
        amountOfManipulatorBooster--;
        countFields();
    }

    public void addRightHand() {
        sizeOfRightHand++;
        resultGenerator.addHand(
                id,
                orientation.dx + sizeOfRightHand * orientation.dy + 1,
                orientation.dy - sizeOfRightHand * orientation.dx + 1);
        amountOfManipulatorBooster--;
        countFields();
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

    public Field getRightField() {
        return rightField;
    }

    public Field getFrontLeftField() {
        return frontLeftField;
    }

    public Field getFrontMiddleField() {
        return frontMiddleField;
    }

    public Field getBodyField() {
        return bodyField;
    }

    public ArrayList<Field> getHands() {
        return hands;
    }

    public Orientation getOrientation() {
        return orientation;
    }



    public boolean areHandsObstacle() {
        return hands.stream().anyMatch(Field::getIsObstacle);
    }

    public static ResultGenerator getResultGenerator() {
        return resultGenerator;
    }
}
