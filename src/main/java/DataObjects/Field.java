package DataObjects;

public class Field {
    Field (int x, int y) {
        this.x = x;
        this.y = y;
    }
    private int x;
    private int y;
    private boolean isPainted;
    private boolean isObstacle;

    public void isPaintedSetter(boolean isPainted) {
        this.isPainted = isPainted;
    }

    public boolean isPaintedGetter() {
        return isPainted;
    }

    public void isObstacleSetter(boolean isObstacle) {
        this.isObstacle = isObstacle;
    }

    public boolean isObstacleGetter() {
        return isObstacle;
    }

    public int xGetter() {
        return x;
    }

    public int yGetter() {
        return y;
    }
}
