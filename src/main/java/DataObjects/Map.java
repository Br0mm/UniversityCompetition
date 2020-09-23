package DataObjects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Map {
    public Map(String data){
        mapStringData = data;
        generateMap();
    }

    private String mapStringData;
    public Field[][] map1;
    private List<ArrayList<Field>> map = new ArrayList<>();
    private int robotStartX;
    private int robotStartY;
    public int sizeX = 0;
    public int sizeY = 0;
    public java.util.Map <Booster, List<String>> boosterListMap = new HashMap<>();
    private WallDirection direction = WallDirection.NONE;

    private void generateMap() {
        String[] partOfInput = mapStringData.split("#");
        findMapSize(partOfInput[0]);
        createWalls(partOfInput[0], 1);
        createStartPoint(partOfInput[1]);
        direction = WallDirection.NONE;
        createObstacles(partOfInput[2]);
        createBoosters(partOfInput[3]);
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
        NONE
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
        map1 = new Field[sizeX][sizeY];
        for (int k = 0; k < sizeX; k++) {
            //ArrayList <Field> line = new ArrayList<>();
            for (int j = 0; j < sizeY; j++) {
                map1[k][j] = new Field(k, j);
                //line.add(new Field(k, j));
            }
            //map.add(line);
        }
    }

    //offset нужен, чтобы правильно ставить препятствия в зависимости от того стена у нас илпреграда внутри
    private void connectTwoPointsOfWall(int previousX, int previousY, int currentX, int currentY, int offset) {
        if (currentX != previousX) {
            if (currentX > previousX) {
                direction = WallDirection.RIGHT;
                for (int j = previousX; j < currentX; j++) {
                    if (previousY != 0) map1[j][previousY - offset].setIsObstacle(true);
                }
            } else {
                previousX--;
                direction = WallDirection.LEFT;
                for (int j = previousX; j >= currentX; j--) {
                    map1[j][previousY - (1 - offset)].setIsObstacle(true);
                }
            }
        } else
        if (currentY != previousX) {
            if (currentY > previousY) {
                direction = WallDirection.UP;
                for (int j = previousY; j < currentY; j++) {
                    map1[previousX - (1 - offset)][j].setIsObstacle(true);
                }
            } else {
                previousY--;
                direction = WallDirection.DOWN;
                for (int j = previousY; j >= currentY; j--) {
                    if (previousX != 0) map1[previousX - offset][j].setIsObstacle(true);
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
        map1[robotStartX][robotStartY].booster = Booster.START;
    }

    public void createObstacles(String obstacles) {
        if (obstacles.equals("")) return;
        String[] groups = obstacles.split(";");
        for (String group: groups) {
            createWalls(group, 0);
        }
    }

    public void createBoosters(String obstacles) {
        if (obstacles.equals("")) return;
        String[] boosters = obstacles.split(";");
        for (String booster: boosters) {
            if (booster.startsWith("X")) {
                if (!boosterListMap.containsKey(Booster.MYSTERIOUS_POINT))
                    boosterListMap.put(Booster.MYSTERIOUS_POINT, new ArrayList<>());
                boosterListMap.get(Booster.MYSTERIOUS_POINT).add(booster.substring(1));
                String[] coordinates = booster.split(",");
                map1[Integer.parseInt(coordinates[0].substring(2))]
                        [Integer.parseInt(coordinates[1].substring(0, coordinates[1].length() - 1))].booster = Booster.MYSTERIOUS_POINT;
            }
            //TODO оставшиеся бустеры
        }
    }

    public List<Field> getClowns(){

        return new ArrayList<Field>();
    }

    public List<Field> getXField(){

        return new ArrayList<Field>();
    }
}
