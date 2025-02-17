package DataObjects;

import java.util.*;

public class Map {
    public Map(String data){
        mapStringData = data;
        generateMap();
    }

    private final String mapStringData;
    public Field[][] map;
    public int robotStartX;
    public int robotStartY;
    public int currentRobotX;
    public int currentRobotY;
    public int sizeX = 0;
    public int sizeY = 0;
    private List<Field> unpaintedFields = new ArrayList<>();
    public java.util.Map <Booster, List<Field>> boosterListMap = new HashMap<>();
    private WallDirection direction = WallDirection.NONE;

    private void generateMap() {
        String[] partOfInput = mapStringData.split("#");
        findMapSize(partOfInput[0]);
        createWalls(partOfInput[0], 1);
        createStartPoint(partOfInput[1]);
        direction = WallDirection.NONE;
        if (partOfInput.length > 2) {
            createObstacles(partOfInput[2]);
            createBoosters(partOfInput[3]);
        }

        unpaintFields();
        currentRobotX = robotStartX;
        currentRobotY = robotStartY;
    }

    //нужно для построения стен
    private enum WallDirection {
        UP,
        RIGHT,
        LEFT,
        DOWN,
        NONE
    }

    public enum Booster {
        START,
        DRILL,
        CLONE,
        FASTWHEEL,
        TELEPORT,
        MANIPULATOR,
        MYSTERIOUS_POINT,
        NONE,
        TESTPATH
    }

    private void findMapSize(String walls) {
        String[] coordinates = walls.split(",");
        int i = 0;
        int maxX = 0;
        int maxY = 0;
        while (i < coordinates.length - 1) {
            int currentX = Integer.parseInt(coordinates[i].substring(1));
            i++;
            int currentY = Integer.parseInt(coordinates[i].substring(0, coordinates[i].length() - 1));
            i++;
            if (maxX < currentX) maxX = currentX;
            if (maxY < currentY) maxY = currentY;
        }
        sizeX = maxX + 1;
        sizeY = maxY + 1;
        map = new Field[sizeX][sizeY];
        for (int k = 0; k < sizeX; k++) {
            for (int j = 0; j < sizeY; j++) {
                map[k][j] = new Field(k, j);
                map[k][j].setIsPainted(true);
            }
        }
    }

    //offset нужен, чтобы правильно ставить препятствия в зависимости от того стена у нас или преграда внутри
    private void connectTwoPointsOfWall(int previousX, int previousY, int currentX, int currentY, int offset) {
        if (currentX != previousX) {
            if (currentX > previousX) {
                direction = WallDirection.RIGHT;
                for (int j = previousX; j < currentX; j++) {
                    if (previousY != 0) map[j][previousY - offset].setIsObstacle(true);
                }
            } else {
                previousX--;
                direction = WallDirection.LEFT;
                for (int j = previousX; j >= currentX; j--) {
                    map[j][previousY - (1 - offset)].setIsObstacle(true);
                }
            }
        } else
        if (currentY != previousY) {
            if (currentY > previousY) {
                direction = WallDirection.UP;
                for (int j = previousY; j < currentY; j++) {
                    map[previousX - (1 - offset)][j].setIsObstacle(true);
                }
            } else {
                previousY--;
                direction = WallDirection.DOWN;
                for (int j = previousY; j >= currentY; j--) {
                    if (previousX != 0) map[previousX - offset][j].setIsObstacle(true);
                }
            }
        }
    }

    //offset нужен для connectTwoPointsOfWall
    private void createWalls(String walls, int offset) {
        String[] coordinates = walls.split(",");
        int firstX = Integer.parseInt(coordinates[0].substring(1));
        int firstY = Integer.parseInt(coordinates[1].substring(0, coordinates[1].length() - 1));
        int i = 2;
        int previousX = firstX;
        int previousY = firstY;
        int currentX = 0;
        int currentY = 0;
        while (i < coordinates.length) {
            currentX = Integer.parseInt(coordinates[i].substring(1));
            i++;
            currentY = Integer.parseInt(coordinates[i].substring(0, coordinates[i].length() - 1));
            i++;

            connectTwoPointsOfWall(previousX, previousY, currentX, currentY, offset);

            previousX = currentX;
            previousY = currentY;
        }
        connectTwoPointsOfWall(currentX, currentY, firstX, firstY, offset);
    }

    private void createStartPoint(String start) {
        String[] coordinates = start.split(",");
        robotStartX = Integer.parseInt(coordinates[0].substring(1));
        robotStartY = Integer.parseInt(coordinates[1].substring(0, coordinates[1].length() - 1));
        map[robotStartX][robotStartY].booster = Booster.START;
    }

    private void createObstacles(String obstacles) {
        if (obstacles.equals("")) return;
        String[] groups = obstacles.split(";");
        for (String group: groups) {
            createWalls(group, 0);
        }
    }

    private void unpaintFields() {
        Queue<Field> fieldsToVisit = new ArrayDeque<>();
        fieldsToVisit.add(map[robotStartX][robotStartY]);
        Field currentPosition;
        while (!fieldsToVisit.isEmpty()) {
            currentPosition = fieldsToVisit.remove();
            if (!currentPosition.getIsPaintedCheck()) {
                currentPosition.setIsPaintedCheck(true);
                if (!currentPosition.getIsObstacle()) {
                    currentPosition.setIsPainted(false);
                    if (currentPosition.getX() != sizeX - 1) fieldsToVisit.add(map[currentPosition.getX() + 1][currentPosition.getY()]);
                    if (currentPosition.getX() != 0) fieldsToVisit.add(map[currentPosition.getX() - 1][currentPosition.getY()]);
                    if (currentPosition.getY() != sizeY - 1) fieldsToVisit.add(map[currentPosition.getX()][currentPosition.getY() + 1]);
                    if (currentPosition.getY() != 0) fieldsToVisit.add(map[currentPosition.getX()][currentPosition.getY() - 1]);
                }
            }
        }
        for (int k = 0; k < sizeX; k++) {
            for (int j = 0; j < sizeY; j++) {
                map[k][j].setIsPaintedCheck(false);
                if (map[k][j].getIsPainted()) map[k][j].setIsObstacle(true);
            }
        }
    }

    private void createBoosters(String obstacles) {
        if (obstacles.equals("")) return;
        String[] boosters = obstacles.split(";");
        for (String boosterCoordinates: boosters) {
            switch (boosterCoordinates.charAt(0)) {
                case 'X':
                    addBoosterToListMap(Booster.MYSTERIOUS_POINT, boosterCoordinates);
                    break;
                case 'B':
                    addBoosterToListMap(Booster.MANIPULATOR, boosterCoordinates);
                    break;
                case 'F':
                    addBoosterToListMap(Booster.FASTWHEEL, boosterCoordinates);
                    break;
                case 'L':
                    addBoosterToListMap(Booster.DRILL, boosterCoordinates);
                    break;
                case 'C':
                    addBoosterToListMap(Booster.CLONE, boosterCoordinates);
                    break;
                case 'R':
                    addBoosterToListMap(Booster.TELEPORT, boosterCoordinates);
                    break;
            }
        }
    }

    private void addBoosterToListMap(Booster currentBooster, String coordinates) {
        if (!boosterListMap.containsKey(currentBooster))
            boosterListMap.put(currentBooster, new ArrayList<>());
        String[] coordinate = coordinates.split(",");
        int x  = Integer.parseInt(coordinate[0].substring(2));
        int y = Integer.parseInt(coordinate[1].substring(0, coordinate[1].length() - 1));
        map[x][y].booster = currentBooster;
        boosterListMap.get(currentBooster).add(map[x][y]);
    }

    public List<Field> getUnpaintedFields(RobotWrappy2019 robot) {
        unpaintedFields = new ArrayList<>();
        hasUnpaintedCheck(map[currentRobotX][currentRobotY], robot);
        for (int k = 0; k < sizeX; k++) {
            for (int j = 0; j < sizeY; j++) {
                map[k][j].setIsPaintedCheck(false);
            }
        }
        return unpaintedFields;
    }

    private void hasUnpaintedCheck(Field start, RobotWrappy2019 robot) {
        Queue<Field> fieldsToVisit = new ArrayDeque<>();
        Field frontField = robot.getFrontMiddleField();
        Field leftField = robot.getLeftFieldRegardingToHands();
        Field rightField = robot.getRightFieldRegardingToHands();
        //Field backField = map[robot.getX() - robot.getOrientation().getDx()][robot.getX() - robot.getOrientation().getDy()];
        fieldsToVisit.add(frontField);
        fieldsToVisit.add(leftField);
        fieldsToVisit.add(rightField);
        fieldsToVisit.add(start);
        //fieldsToVisit.add(backField);
        Field currentPosition;
        while (!fieldsToVisit.isEmpty()) {
            currentPosition = fieldsToVisit.remove();
            if (!currentPosition.getIsPaintedCheck()) {
                if (!currentPosition.getIsObstacle() && !currentPosition.getIsPainted()) unpaintedFields.add(currentPosition);
                currentPosition.setIsPaintedCheck(true);
                if (!currentPosition.getIsObstacle()) {
                    if (currentPosition.getX() != sizeX - 1) fieldsToVisit.add(map[currentPosition.getX() + 1][currentPosition.getY()]);
                    if (currentPosition.getX() != 0) fieldsToVisit.add(map[currentPosition.getX() - 1][currentPosition.getY()]);
                    if (currentPosition.getY() != sizeY - 1) fieldsToVisit.add(map[currentPosition.getX()][currentPosition.getY() + 1]);
                    if (currentPosition.getY() != 0) fieldsToVisit.add(map[currentPosition.getX()][currentPosition.getY() - 1]);
                }
            }
        }
    }

    public void clearCostsForAStar() {
        for (int k = 0; k < sizeX; k++) {
            for (int j = 0; j < sizeY; j++) {
                map[k][j].setFCost(Integer.MAX_VALUE);
                map[k][j].setGCost(Integer.MAX_VALUE);
                map[k][j].setHCost(0);
                map[k][j].setPreviousField(null);
            }
        }
    }

    public void setCurrentRobotX(int x) {
        currentRobotX = x;
    }

    public void setCurrentRobotY(int y) {
        currentRobotY = y;
    }

    public void setCurrentRobotXY(int x, int y) {
        currentRobotX = x;
        currentRobotY = y;
    }

    public List<Field> getClowns(){
        List<Field> clowns = new ArrayList<>();
        if (boosterListMap.containsKey(Booster.CLONE)) clowns = boosterListMap.get(Booster.CLONE);
        return clowns;
    }

    public List<Field> getXField(){
        List<Field> mysteriousPoint = new ArrayList<>();
        if (boosterListMap.containsKey(Booster.MYSTERIOUS_POINT))
            mysteriousPoint = boosterListMap.get(Booster.MYSTERIOUS_POINT);
        return mysteriousPoint;
    }
}
