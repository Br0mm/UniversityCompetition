package GUI;

import DataObjects.Field;
import DataObjects.Map;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.*;

public class MapView extends Application {
    int bodySize = 10;

    @Override
    public void start (Stage primaryStage) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File("input/prob-003.desc"))))) {
            Map test = new Map(reader.readLine());

            int height = bodySize * (test.sizeY + 1);
            int width = bodySize * (test.sizeX + 1);

            Pane map = new AnchorPane();
            map.setPrefHeight(height);
            map.setPrefWidth(width);

            for (int i = 0; i < test.sizeX; i++) {
                for (int j = 0; j < test.sizeY; j++) {
                    map.getChildren().add(createMapPart(test.map[i][j], i * bodySize, height - j * bodySize));
                }
            }
            Scene showMap = new Scene(map);
            primaryStage.setScene(showMap);
            primaryStage.show();
        }
    }

    public Node createMapPart(Field field, int x, int y) {
        Rectangle rect = new Rectangle(bodySize, bodySize);
        rect.setX(x);
        rect.setY(y);
        Group point = new Group();
        point.getChildren().add(rect);
        if (field.getIsObstacle()) rect.setFill(Color.GRAY);
        else rect.setFill(Color.WHITE);
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
                    booster.setFill(Color.YELLOW);
                    break;
                case MYSTERIOUS_POINT:
                    booster.setFill(Color.DARKBLUE);
                    break;
            }
            point.getChildren().add(booster);
        }
        return point;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
