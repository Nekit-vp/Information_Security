package Work1;

import java.io.*;
import java.util.HashMap;
import java.util.Scanner;

public class FrequencyAnalysis {
    public static void main(String[] args) throws IOException {

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Частотный анализ //// кириллица");

        System.out.print("Введите файл, который необходимо взломать с помощью частотного анализа: ");
        StringBuilder text = createText(reader.readLine());
        HashMap map = createMap(String.valueOf(text));

        System.out.print("Введите файл, который будете брать за основу взлома: ");
        HashMap mapHacking = createMap(String.valueOf(createText(reader.readLine())));

        HashMap<Character, Character> key = createMapKey(mapHacking, map);
        System.out.println(key.toString());

        printInConsole(decrypt(key, String.valueOf(text)));

        System.out.println("Зайти в режим коррекции? \n1...Да \n0...Нет ");

        while (reader.readLine().equals("1")) {

            System.out.println("Режим коррекции ");
            System.out.print("Неправильный символ: ");
            char incorrectSymbol = reader.readLine().charAt(0);
            System.out.print("Правильный символ: ");
            char correctSymbol = reader.readLine().charAt(0);

            //найти слева поменять справа
            char bufer = key.get(correctSymbol);
            key.replace(correctSymbol, key.get(incorrectSymbol));
            key.replace(incorrectSymbol, bufer);

            System.out.println(key.toString());
            if (text.length() > 4000)
                printInConsole(decrypt(key, text.substring(0, 4000)));
            else
                printInConsole(decrypt(key, String.valueOf(text)));
            System.out.println("Зайти в режим коррекции? \n1...Да \n0...Нет ");
        }

        System.out.println("Вывод данных: \n1...Консоль\n0...Файл");
        if (Integer.parseInt(reader.readLine()) == 1) {
            System.out.println("Текст расшифрован: \n");
            printInConsole(decrypt(key, String.valueOf(text)));
        } else {
            System.out.print("Введите файл: ");
            createFile(reader.readLine(), decrypt(key, String.valueOf(text)));
        }

        System.out.println("Текст расшифрован, ключ: \n" + key.toString());

    }

    private static boolean checkLowerCase(Character character) {
        return ((int) character >= (int) 'а') & ((int) character <= (int) 'я');
    }

    // из файла созадет текст
    static StringBuilder createText(String way) throws IOException {

        StringBuilder text = new StringBuilder();

        File file = new File("src/file/" + way);
        FileReader fr = new FileReader(file);
        Scanner scanner = new Scanner(fr);
        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            text.append(line);
            text.append('@');
        }
        fr.close();

        return text;
    }

    // из текста создает словарь частот
    static HashMap createMap(String text) {
        //создали словарь в которыый положили алфавит
        HashMap<Character, Float> map = new HashMap<>();
        for (int i = 0; i < 32; i++)
            map.put((char) ((int) 'а' + i), (float) 0);

        for (int i = 0; i < text.length(); i++)
            if (checkLowerCase(text.charAt(i)))
                map.put(text.charAt(i), map.get(text.charAt(i)) + 1);

        // из обычного словаря делаем словарь частот
        map.replaceAll((c, v) -> map.get(c) / text.length());

        return map;
    }

    /// метод, которые из двух слованей, которые представляют собой частоты некоторых текстов, составляет ключ шифрования
    static HashMap createMapKey(HashMap<Character, Float> mapModel, HashMap<Character, Float> mapMessage) {
        HashMap<Character, Float> mapModelCopy = new HashMap<>(mapModel);
        HashMap<Character, Float> mapMessageCopy = new HashMap<>(mapMessage);

        HashMap<Character, Character> table = new HashMap<>();

        for (int i = 0; i < 32; i++)
            table.put((char) ((int) 'а' + i), '?');

        //имея два словаря с частотами мы их соединяем в один словарь по символам.
        float maxMapMy, maxMapBook;
        char symbolMapMy = '?';
        char symbolMapBook = '?';

        for (int i = 0; i < 32; i++) {
            maxMapBook = -1;
            maxMapMy = -1;
            for (Character cha : mapModelCopy.keySet()) {
                if (maxMapMy < mapMessageCopy.get(cha)) {
                    symbolMapMy = cha;
                    maxMapMy = mapMessageCopy.get(cha);
                }
                if (maxMapBook < mapModelCopy.get(cha)) {
                    symbolMapBook = cha;
                    maxMapBook = mapModelCopy.get(cha);
                }
            }
            table.replace(symbolMapBook, symbolMapMy);
            mapMessageCopy.replace(symbolMapMy, (float) -5);
            mapModelCopy.replace(symbolMapBook, (float) -5);
        }
        return table;
    }


    /// метод для расшифровки текста по ключу, которые представлен в виде словаря
    private static StringBuilder decrypt(HashMap map, String text) {
        //переворачиваем словарь
        HashMap<Character, Character> mapReturn = new HashMap<>();
        for (Object ch : map.keySet())
            mapReturn.put((Character) map.get(ch), (Character) ch);

        StringBuilder newStr = new StringBuilder();
        for (int i = 0; i < text.length(); i++)
            if (checkLowerCase(text.charAt(i)))
                newStr.append(mapReturn.get(text.charAt(i)));
            else
                newStr.append(text.charAt(i));
        return newStr;
    }

    //метод вывода в консоль с переходом на новую строку
    private static void printInConsole(StringBuilder text) {
        for (int i = 0; i < text.length() / 175; i++)
            System.out.println(text.substring(175 * (i), 175 * (i + 1)));
        System.out.println(text.substring(175 * (text.length() / 175), text.length()));
    }

    private static void createFile(String way, StringBuilder text) throws IOException{
        File file = new File("src/file/" + way);
        FileWriter fileWriter = new FileWriter(file);
        String[] mat = String.valueOf(text).split("@");
        for (String str : mat) {
            fileWriter.write(str + "\n");
        }
        fileWriter.close();
    }

}

