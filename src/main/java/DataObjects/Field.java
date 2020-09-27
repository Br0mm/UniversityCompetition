package DataObjects;

public class Field {
    Field (int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Map.Booster booster = Map.Booster.NONE;
    private int x;
    private int y;
    private boolean isPainted = false;
    private boolean isObstacle = false;

    public void setIsPainted(boolean isPainted) {
        this.isPainted = isPainted;
    }

    public boolean getIsPainted() {
        return isPainted;
    }

    public void setIsObstacle(boolean isObstacle) {
        this.isObstacle = isObstacle;
    }

    public boolean getIsObstacle() {
        return isObstacle;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
