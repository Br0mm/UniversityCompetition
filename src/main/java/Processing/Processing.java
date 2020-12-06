package Processing;

import DataObjects.Field;
import DataObjects.Map;
import DataObjects.RobotWrappy2019;
import Debug.Test;
import javafx.util.Pair;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.lang.Math.abs;

public class Processing {
    // A* algorithm with static obstacles.
    public static class Pathfinding {
        private final int moveStraightCost = 10;

        public static List<Field> findPath(Field startField, Field endField, Map map) {
            map.clearCostsForAStar();
            final List<Field> openList = new ArrayList<>();
            final List<Field> closedList = new ArrayList<>();
            openList.add(startField);

            startField.setGCost(0);
            startField.setHCost(calculateDistanceCost(startField, endField));
            startField.calculateFCost();

            while (!openList.isEmpty()) { //
                Field currentField = getLowestFCostField(openList);
                if (currentField == endField) return calculatePath(endField);
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
        List<Pair<Integer, Integer>> lastFourXAndY = new ArrayList<>();
        List<Field> unpainted = map.getUnpaintedFields(robot);
        boolean isStayingOnOnePoint = false;

        while (unpainted.size() > 0) {
            map.setCurrentRobotXY(robot.getX(), robot.getY());
            //unpainted = map.getUnpaintedFields();

            ArrayList<Field> hands = robot.getHands();
            Field leftFieldRegardingToHands = robot.getLeftFieldRegardingToHands();
            Field frontLeftField = robot.getFrontLeftField();
            Field frontMiddleField = robot.getFrontMiddleField();
            Field bodyField = robot.getBodyField();
            Field leftField = robot.getLeftField();
            Test debug = new Test();
            BufferedImage debugImage = debug.createImage(map);
            List<Field> pathToClosestUnpainted = Pathfinding.findPath(bodyField, unpainted.get(0), map);


            //добавил создание изображения поля для дебага во время дебага надо нажать show image
            if (pathToClosestUnpainted.size() > 5 || isStayingOnOnePoint) {
                Field previousField;
                Field currentField = robot.getBodyField();
                for (int i = 1; i < pathToClosestUnpainted.size(); i++) {
                    previousField = currentField;
                    currentField = pathToClosestUnpainted.get(i);
                    switch (currentField.getX() - previousField.getX()) {
                        case -1:
                            robot.setOrientationLeft();
                            robot.moveStraight();
                            break;
                        case 1:
                            robot.setOrientationRight();
                            robot.moveStraight();
                            break;
                    }
                    switch (currentField.getY() - previousField.getY()) {
                        case 1:
                            robot.setOrientationUp();
                            robot.moveStraight();
                            break;
                        case -1:
                            robot.setOrientationDown();
                            robot.moveStraight();
                            break;
                    }
                }
                isStayingOnOnePoint = false;
            } else if (leftFieldRegardingToHands.isPaintedOrObstacle() &&
                    !frontMiddleField.isPaintedOrObstacle() &&
                    !hands.get(robot.getSizeOfLeftHand()).getIsObstacle()) {
                robot.moveStraight();
            } else if (hands.get(0).isPaintedOrObstacle() &&
                    !hands.get(robot.getSizeOfLeftHand()).isPaintedOrObstacle() &&
                    !hands.get(hands.size() - 1).isPaintedOrObstacle()) {
                robot.moveStraight();
                robot.moveRightRegardingTheRobot();
            } else if ((!frontLeftField.isPaintedOrObstacle() &&
                    frontMiddleField.isPaintedOrObstacle()) &&
                    !leftField.isPaintedOrObstacle()) {
                robot.moveLeftRegardingTheRobot();
            } else if ((frontLeftField.isPaintedOrObstacle() &&
                    frontMiddleField.isPaintedOrObstacle() &&
                    leftFieldRegardingToHands.isPaintedOrObstacle() ||
                    (hands.get(0).isPaintedOrObstacle() && hands.get(1).isPaintedOrObstacle()))) {
                robot.turnRight();
            } else if (!hands.get(0).isPaintedOrObstacle() &&
                    hands.get(robot.getSizeOfLeftHand()).getIsObstacle()) {
                robot.moveLeftRegardingTheRobot();
                //robot.moveStraight();
            } else if (!leftFieldRegardingToHands.isPaintedOrObstacle() &&
                    !hands.get(0).isPaintedOrObstacle()) {
                robot.moveStraight();
                if (!frontMiddleField.isPaintedOrObstacle() && !frontLeftField.isPaintedOrObstacle()) {
                    robot.moveStraight();
                }
                robot.turnLeft();
                robot.moveStraight();
            } else robot.moveStraight();
            unpainted = map.getUnpaintedFields(robot);
            if (unpainted.size() < 10) {
                System.out.println("Less than 10 unpainted");
            }

            lastFourXAndY.add(new Pair<>(robot.getX(), robot.getY()));
            if (lastFourXAndY.size() > 4) lastFourXAndY.remove(0);

            if (lastFourXAndY.size() == 4 &&
                    lastFourXAndY.get(0).getKey().equals(lastFourXAndY.get(1).getKey()) &&
                    lastFourXAndY.get(1).getKey().equals(lastFourXAndY.get(2).getKey()) &&
                    lastFourXAndY.get(2).getKey().equals(lastFourXAndY.get(3).getKey()) &&
                    lastFourXAndY.get(0).getValue().equals(lastFourXAndY.get(1).getValue()) &&
                    lastFourXAndY.get(1).getValue().equals(lastFourXAndY.get(2).getValue()) &&
                    lastFourXAndY.get(2).getValue().equals(lastFourXAndY.get(3).getValue())) {
                isStayingOnOnePoint = true;
            }

        }
    }
}
