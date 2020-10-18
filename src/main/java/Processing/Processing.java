package Processing;

import DataObjects.Field;
import DataObjects.Map;
import DataObjects.RobotWrappy2019;
import Debug.Test;

import java.awt.image.BufferedImage;

public class Processing {
    public void searchShortestWay() {
    }

    public void mapSplitter() {
    }

    public static void leftHandMovingForOneRobot(Map map) {
                RobotWrappy2019 robot = new RobotWrappy2019(map.robotStartX, map.robotStartY, map);
        Field leftField = robot.getLeftField();
        Field frontLeftField = robot.getFrontLeftField();
        Field frontMiddleField = robot.getFrontMiddleField();
        while (map.hasUnpainted()) {
            //добавил создание изображения поля для дебага во время дебага надо нажать show image
            Test debug = new Test();
            BufferedImage debugImage = debug.createImage(map);
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
