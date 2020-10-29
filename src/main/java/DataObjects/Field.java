package DataObjects;

public class Field {
    Field (int x, int y) {
        this.x = x;
        this.y = y;
        this.setGCost(Integer.MAX_VALUE);
        this.calculateFCost();
    }

    public Map.Booster booster = Map.Booster.NONE;
    private int x;
    private int y;
    private boolean isPainted = false;
    //это поле нужно, чтобы находить оставшиеся незакрашенные места, которые нужно закрасить
    private boolean isPaintedCheck = false;
    private boolean isObstacle = false;
    private int gCost = 0;
    private int hCost = 0;
    private int fCost = 0;
    private Field previousField = null;

    public int calculateFCost() {
        fCost = hCost + gCost;
        return fCost;
    }

    public int getGCost() {
        return gCost;
    }

    public void setGCost(int gCost) {
        this.gCost = gCost;
    }

    public int getHCost() {
        return hCost;
    }

    public void setHCost(int hCost) {
        this.hCost = hCost;
    }

    public int getFCost() {
        return fCost;
    }

    public void setFCost(int fCost) {
        this.fCost = fCost;
    }

    public Field getPreviousField() {
        return previousField;
    }

    public void setPreviousField(Field previousField) {
        this.previousField = previousField;
    }

    public void setIsPainted(boolean isPainted) {
        this.isPainted = isPainted;
    }

    public boolean getIsPainted() {
        return isPainted;
    }

    public boolean isPaintedOrObstacle() {
        return isPainted || isObstacle;
    }

    public void setIsObstacle(boolean isObstacle) {
        this.isObstacle = isObstacle;
    }

    public boolean getIsObstacle() {
        return isObstacle;
    }

    public boolean getIsPaintedCheck() { return isPaintedCheck; }

    public void setIsPaintedCheck(boolean isPaintedCheck) { this.isPaintedCheck = isPaintedCheck; }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
