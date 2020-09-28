package Work1;

import java.io.*;
import java.util.HashMap;

import static Work1.EasyReplacement.createTable;
import static Work1.FrequencyAnalysis.*;


public class Diagram {
    public static void main(String[] args) throws IOException {

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        System.out.print("Введите зашифрованный файл по которому будет составлена диаграмма: ");
        StringBuilder text = createText(reader.readLine());

        System.out.print("Введите файл в котором содержиться ключ шифрования: ");
        HashMap key = (HashMap) createTable("src/file/" + reader.readLine());

        System.out.print("Введите файл, который будете брать за основу взлома: ");
        HashMap mapHacking = createMap(String.valueOf(createText(reader.readLine())));

        File file = new File("src/file/test.csv");
        FileWriter fileWriter = new FileWriter(file);

        HashMap map;
        int percent;
        for (int i = 0; i < 100; i++) {
            map  = createMapKey(mapHacking,createMap(String.valueOf(text).substring(0, (i+1) * text.length()/100)));
            percent = createPercent(key, map);
            fileWriter.write((i+1) * text.length()/100 + ";" + percent + "\n");
        }
        fileWriter.close();
    }

    private static int createPercent(HashMap key, HashMap map) {
        int count = 0;
        for (Object ch : key.keySet()) {
            if (key.get(ch).equals(map.get(ch)))
                count++;
        }
        return (int) ((float) count / 32 * 100);
    }
    
    

}
