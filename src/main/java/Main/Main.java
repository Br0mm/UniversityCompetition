package Main;

import DataObjects.Map;
import DataObjects.RobotWrappy2019;
import Processing.Processing;

import java.io.*;

public class Main {


    public static void main(String[] args) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File("input/prob-003.desc"))))) {
            Map test = new Map(reader.readLine());
            Processing.leftHandMovingForOneRobot(test);
            String result = RobotWrappy2019.getResultGenerator().getResult();
        }
    }
}
