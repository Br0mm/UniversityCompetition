package Main;

import DataObjects.Map;
import DataObjects.RobotWrappy2019;
import Processing.Processing;

public class Main {
    public static void main(Map map){
        Processing.leftHandMovingForOneRobot(map);
        String result = RobotWrappy2019.getResultGenerator().getResult();
    }
}
