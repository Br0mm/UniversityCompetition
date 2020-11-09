package Debug;

import DataObjects.Field;
import DataObjects.Map;

import java.awt.*;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class Test extends JPanel {

    public BufferedImage createImage(Map currentMap) {
        int height = (currentMap.sizeY + 1);
        int width = (currentMap.sizeX + 1);
        BufferedImage bufferedImage = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);

        Color color;
        for (int i = 0; i < currentMap.sizeX; i++) {
            for (int j = 0; j < currentMap.sizeY; j++) {
                color = createMapPart(currentMap.map[i][j], i, (height - j));
                bufferedImage.setRGB(i,(height - j - 1), color.getRGB());
            }
        }
        return bufferedImage;
    }

    public Color createMapPart(Field field, int x, int y) {
        if (field.getIsObstacle()) return Color.BLACK;
        if (field.getIsPainted()) return Color.YELLOW;
        if (field.booster.equals(Map.Booster.TESTPATH)) return Color.MAGENTA;
        /*
        if (!field.booster.equals(Map.Booster.NONE)) {
            Circle booster = new Circle();
            booster.setCenterX(x + 5);
            booster.setCenterY(y + 5);
            booster.setRadius(2.5);
            switch (field.booster) {
                case START:
                    booster.setFill(Color.RED);
                    break;
                case DRILL:
                    booster.setFill(Color.GREEN);
                    break;
                case CLONE:
                    booster.setFill(Color.BLUE);
                    break;
                case FASTWHEEL:
                    booster.setFill(Color.BROWN);
                    break;
                case TELEPORT:
                    booster.setFill(Color.BLUEVIOLET);
                    break;
                case MANIPULATOR:
                    booster.setFill(Color.ORANGE);
                    break;
                case MYSTERIOUS_POINT:
                    booster.setFill(Color.DARKBLUE);
                    break;
            }
            point.getChildren().add(booster);
        }

         */
        return Color.WHITE;
    }
}