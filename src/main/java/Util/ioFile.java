package Util;

import java.io.*;

/**
 * Created by lishiwei on 16/12/24.
 */
public class ioFile {
    public void readFileLine(String path) {
        FileReader reader = null;
        BufferedReader br = null;
        try {
            br = new BufferedReader(reader);
            reader = new FileReader(path);
            String str = null;
            while ((str = br.readLine()) != null) {
                System.out.println(str);
            }
            br.close();
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void writeFile(String str, BufferedWriter writer) throws IOException {
        writer.write(str);
        writer.newLine();
    }

    //将文件分为训练集合和测试集合
    public void splitTestTrain(String path, String trainPath, String testPath, int partition) throws IOException {
        int lineNumber = 1;
        BufferedReader reader;

        File fileTest = new File(testPath);
        File fileTrain = new File(trainPath);
        FileWriter fwTest;
        FileWriter fwTrain;
        BufferedWriter writerTest;
        BufferedWriter writerTrain;
        fwTest = new FileWriter(fileTest);
        fwTrain = new FileWriter(fileTrain);

        writerTest = new BufferedWriter(fwTest);
        writerTrain = new BufferedWriter(fwTrain);

        reader = new BufferedReader(new FileReader(path));
        String sourceStr = reader.readLine();


        while (sourceStr != null) {
            if (lineNumber % 10 >= partition) {
                writeFile(sourceStr, writerTrain);
            } else {
                writeFile(sourceStr, writerTest);
            }
            lineNumber++;
            sourceStr = reader.readLine();
        }
        writerTest.flush();
        writerTrain.flush();

        writerTest.close();
        fwTest.close();
        writerTrain.close();
        fwTrain.close();
    }
}
