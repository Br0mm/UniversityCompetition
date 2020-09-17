package DataObjects;

import java.util.ArrayList;
import java.util.List;

public class Map {
    Map(String data){
        deskData = data;
    }

    private String deskData;
    private List<ArrayList<Field>> map = new ArrayList<ArrayList<Field>>();
    private int robotStartX;
    private int robotStartY;

    public void generateMap() {
        String[] partOfInput = deskData.split("#");
        createWalls(partOfInput[0]);
    }

    public void createWalls(String walls) {

    }

    public List<Field> getClowns(){

        return new ArrayList<Field>();
    }

    public List<Field> getXField(){

        return new ArrayList<Field>();
    }
}
