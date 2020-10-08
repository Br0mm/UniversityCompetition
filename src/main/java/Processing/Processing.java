package Processing;

import DataObjects.Field;
import DataObjects.Map;
import DataObjects.RobotWrappy2019;

import java.io.File;

public class Processing {
    public void searchShortestWay() {
    }

    public void mapSplitter() {
    }

    public void leftHandMovingForOneRobot(String dataForMap) {
        Map map = new Map(dataForMap);
        RobotWrappy2019 robot = new RobotWrappy2019(map.robotStartX, map.robotStartY, map);
        Field leftField = robot.getLeftField();
        Field frontLeftField = robot.getFrontLeftField();
        Field frontMiddleField = robot.getFrontMiddleField();
        while (true /* map.hasUnpainted */) {
            if (leftField.isPaintedOrObstacle() && !frontMiddleField.isPaintedOrObstacle()) {
                robot.moveStraight();
            } else if (leftField.isPaintedOrObstacle() && !frontMiddleField.isPaintedOrObstacle()) {
                robot.moveLeft();
            } else if (!leftField.isPaintedOrObstacle()) {
                robot.moveStraight();
                robot.moveStraight();
                robot.turnLeft();
            } else if (frontLeftField.isPaintedOrObstacle() || frontMiddleField.isPaintedOrObstacle()) {
                robot.turnRight();
            }
        }
    }
}
