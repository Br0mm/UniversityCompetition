package DataObjects;

import java.util.ArrayList;
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
    private WallDirection direction = WallDirection.NONE;

    private void generateMap() {
        String[] partOfInput = mapStringData.split("#");
        findMapSize(partOfInput[0]);
        createWalls(partOfInput[0]);
        createStartPoint(partOfInput[1]);
        direction = WallDirection.NONE;
        createObstacles(partOfInput[2]);
    }

    //нужно для построения стен
    private enum WallDirection {
        UP,
        RIGHT,
        LEFT,
        DOWN,
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

    private void connectTwoPointsOfObstacle(int previousX, int previousY, int currentX, int currentY) {
        if (currentX != previousX) {
            if (currentX > previousX) {
                direction = WallDirection.RIGHT;
                for (int j = previousX; j < currentX; j++) {
                    if (previousY != 0) map1[j][previousY - 1].setIsObstacle(true);
                }
            } else {
                if (direction.equals(WallDirection.DOWN)) previousX--;
                direction = WallDirection.LEFT;
                for (int j = previousX; j >= currentX; j--) {
                    map1[j][previousY].setIsObstacle(true);
                }
            }
        } else
        if (currentY != previousX) {
            if (currentY > previousY) {
                direction = WallDirection.UP;
                for (int j = previousY; j < currentY; j++) {
                    map1[previousX][j].setIsObstacle(true);
                }
            } else {
                if (direction.equals(WallDirection.RIGHT)) previousY--;
                direction = WallDirection.DOWN;
                for (int j = previousY; j >= currentY; j--) {
                    if (previousX != 0) map1[previousX - 1][j].setIsObstacle(true);
                }
            }
        }
    }

    private void createWalls(String walls) {
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

            connectTwoPointsOfObstacle(previousX, previousY, currentX, currentY);

            previousX = currentX;
            previousY = currentY;
        }
        connectTwoPointsOfObstacle(currentX, currentY, firstX, firstY);
    }

    private void createStartPoint(String start) {
        String[] coordinates = start.split(",");
        robotStartX = Integer.parseInt(coordinates[0].substring(1));
        robotStartY = Integer.parseInt(coordinates[1].substring(0, coordinates[1].length() - 1));
        map1[robotStartX][robotStartY].booster = Field.Booster.START;
    }

    public void createObstacles(String obstacles) {
        String[] groups = obstacles.split(";");
        for (String group: groups) {
            //TODO
        }
    }

    public List<Field> getClowns(){

        return new ArrayList<Field>();
    }

    public List<Field> getXField(){

        return new ArrayList<Field>();
    }
}
