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

    private enum Orientation{
        UP,
        DOWN,
        LEFT,
        RIGHT;
    }

    public RobotWrappy2019(int x, int y, Orientation orientation){
        this.x = x;
        this.y = y;

        this.orientation = orientation;

        id = counter++;
    }

   /* public void turnRight(int number){

    }

    public void turnLeft(int number){
        resultGenerator
    }

    public void moveUp(int number){
        commands.get(number).append("W");
    }

    public void moveDown(int number){
        commands.get(number).append("S");
    }

    public void moveLeft(int number){
        commands.get(number).append("A");
    }

    public void moveRight(int number){
        commands.get(number).append("D");
    }

    public void addHand(int number, int dx, int dy){
        commands.get(number).append("B" + "(").append(dx).append(",").append(dy).append(")");
    }

    public void useFastWheel(int number){
        commands.get(number).append("F");
    }

    public void skip(int number){
        commands.get(number).append("Z");
    }

    public void spawnCLoWn(int number){
        commands.get(number).append("C");
        commands.add(new StringBuilder());
    }

    public void resetTeleport(int number){
        commands.get(number).append("R");
    }

    public void useTeleport(int number, int x, int y){
        commands.get(number).append("T" + "(").append(x).append(",").append(y).append(")");
    }*/
}
