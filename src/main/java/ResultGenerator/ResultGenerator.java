package ResultGenerator;

import java.util.ArrayList;

public class ResultGenerator {
    private ArrayList<StringBuilder> commands = new ArrayList<>();

    public ResultGenerator(){
        commands.add(new StringBuilder());
    }

    public void turnRight(int number){
        commands.get(number).append("E");
    }

    public void turnLeft(int number){
        commands.get(number).append("Q");
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
    }

    public String getResult() {
        StringBuilder result = new StringBuilder();
        int separators = commands.size() - 1;
        for (StringBuilder item : commands) {
            result.append(item);
            if (separators > 0) {
                result.append("#");
                separators--;
            }
        }
        commands = new ArrayList<>();
        commands.add(new StringBuilder());
        return result.toString();
    }
}
