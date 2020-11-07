package Processing;

import DataObjects.Field;
import DataObjects.Map;
import DataObjects.RobotWrappy2019;
import Debug.Test;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static java.lang.Math.abs;

public class Processing {
        // A* algorithm with static obstacles.
        public static class Pathfinding {
            private final int moveStraightCost = 10;

            public static List<Field> findPath(Field startField, Field endField, Map map) {
                final List<Field> openList = new ArrayList<>();
                final List<Field> closedList = new ArrayList<>();
                openList.add(startField);

                startField.setGCost(0);
                startField.setHCost(calculateDistanceCost(startField, endField));
                startField.calculateFCost();

                while (!openList.isEmpty()) { //
                    Field currentField = getLowestFCostField(openList);
                    if (currentField == endField)
                        return calculatePath(endField);
                    openList.remove(currentField);
                    closedList.add(currentField);

                    for (Field neighbourField : getNeighbours(map, currentField)) {
                        if (closedList.contains(neighbourField)) continue;
                        if (neighbourField.getIsObstacle()) {
                            closedList.add(neighbourField);
                            continue;
                        }
                        int tempGCost = currentField.getGCost() + calculateDistanceCost(currentField, neighbourField);
                        if (tempGCost < neighbourField.getGCost()) {
                            neighbourField.setPreviousField(currentField);
                            neighbourField.setGCost(tempGCost);
                            neighbourField.setHCost(calculateDistanceCost(neighbourField, endField));
                            neighbourField.calculateFCost();
                            if (!openList.contains(neighbourField)) {
                                openList.add(neighbourField);
                            }
                        }
                    }
                }
                return Collections.emptyList();
            }

            private static List<Field> getNeighbours(Map map, Field currentField) {
                List<Field> neighbourList = new ArrayList<>();
                if (currentField.getX() - 1 >= 0) {
                    neighbourList.add(map.map[currentField.getX() - 1][currentField.getY()]); // LEFT
                }
                if (currentField.getX() + 1 < map.sizeX) {
                    neighbourList.add(map.map[currentField.getX() + 1][currentField.getY()]); // RIGHT
                }
                if (currentField.getY() - 1 >= 0)
                    neighbourList.add(map.map[currentField.getX()][currentField.getY() - 1]); // DOWN
                if (currentField.getY() + 1 < map.sizeY)
                    neighbourList.add(map.map[currentField.getX()][currentField.getY() + 1]); // UP
                return neighbourList;
            }

            private static int calculateDistanceCost(Field a, Field b) {
                int xDistance = abs(a.getX() - b.getX());
                int yDistance = abs(a.getY() - b.getY());
                int remaining = abs(xDistance - yDistance);
                return 10 * remaining;
            }

            private static List<Field> calculatePath(Field endField) {
                List<Field> path = new ArrayList<>();
                path.add(endField);
                Field currentField = endField;
                while (currentField.getPreviousField() != null) {
                    path.add(currentField.getPreviousField());
                    currentField = currentField.getPreviousField();
                }
                Collections.reverse(path);
                return path;
            }

            private static Field getLowestFCostField(List<Field> nodes) { // LAMBDA ONELINER?
                Field res = nodes.get(0);
                for (int i = 0; i < nodes.size() - 1; i++) {
                    if (nodes.get(i).getFCost() < res.getFCost())
                        res = nodes.get(i);
                }
                return res;
            }
        }

    public void mapSplitter() {
    }

    public static void leftHandMovingForOneRobot(Map map) {
            RobotWrappy2019 robot = new RobotWrappy2019(map.robotStartX, map.robotStartY, map);

        while (map.hasUnpainted()) {
            ArrayList<Field> hands = robot.getHands();
            Field leftField = robot.getLeftField();
            Field frontLeftField = robot.getFrontLeftField();
            Field frontMiddleField = robot.getFrontMiddleField();
            //добавил создание изображения поля для дебага во время дебага надо нажать show image
            Test debug = new Test();
            BufferedImage debugImage = debug.createImage(map);
            if (leftField.isPaintedOrObstacle() && !frontMiddleField.isPaintedOrObstacle() && !robot.areHandsObstacle()) {
                robot.moveStraight();
            } else if ((frontLeftField.getIsObstacle() && frontMiddleField.getIsObstacle() ||
                    (hands.get(0).getIsObstacle() && hands.get(1).getIsObstacle()))) {
                robot.turnRight();
            }else if (!hands.get(0).getIsObstacle() && hands.get(robot.getSizeOfLeftHand()).getIsObstacle()) {
                robot.moveLeft();
                //robot.moveStraight();
            }  else if (!leftField.getIsObstacle() && !hands.get(0).getIsObstacle()) {
                robot.moveStraight();
                if (!frontLeftField.getIsObstacle()) robot.moveStraight();
                robot.turnLeft();
                robot.moveStraight();
            }  else robot.moveStraight();
        }
    }
}
