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
    public Field[][] map;
    //private List<ArrayList<Field>> map = new ArrayList<>();
    private int robotStartX;
    private int robotStartY;
    public int sizeX = 0;
    public int sizeY = 0;
    public java.util.Map <Booster, List<Field>> boosterListMap = new HashMap<>();
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
        map = new Field[sizeX][sizeY];
        for (int k = 0; k < sizeX; k++) {
            //ArrayList <Field> line = new ArrayList<>();
            for (int j = 0; j < sizeY; j++) {
                map[k][j] = new Field(k, j);
                //line.add(new Field(k, j));
            }
            //map.add(line);
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
        if (currentY != previousX) {
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
