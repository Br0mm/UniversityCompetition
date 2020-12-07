package Main;

import DataObjects.Map;
import DataObjects.RobotWrappy2019;
import Processing.Processing;

import java.io.*;

public class Main {
    public static void main(String[] args) throws IOException {
        for (int i = 0; i <= 2; i++)
            for (int j = 0; j < 10; j++)
                for (int k = 0; k < 10; k++)
                    if (!(i == 0 && j == 0 && k == 0))
                        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File("input/prob-" + i + j + k + ".desc"))))) {
                            Map test = new Map(reader.readLine());
                            Processing.leftHandMovingForOneRobot(test);
                            String result = RobotWrappy2019.getResultGenerator().getResult();
                            try (BufferedWriter writer = new BufferedWriter(new FileWriter(new File("output/prob-" + i + j + k + ".sol")))) {
                                writer.write(result);
                            }
                        }
    }
}
