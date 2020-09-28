package Work1;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;


public class EasyReplacement {
    public static void main(String[] args) throws IOException {

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        Map map;
        String text;
        StringBuilder newStr;

        System.out.println("Шифр простой замены //кириллица\n");

        int act = 3;

        while (act != 0) {

            System.out.println("Что необходимо сделать: \n2...Шифрование\n1...Расшифрование\n0...Завершить");
            act = Integer.parseInt(reader.readLine());

            if (act == 2) { ///шифрование

                System.out.println("Хотите сгенерировать новый шифр? \n1...Да \n0...Нет ");
                if (Integer.parseInt(reader.readLine()) == 1) {
                    map = createNewTable("src/file/table.txt");
                    System.out.println("Новый шифр сгенерирован! ");
                } else {
                    map = createTable("src/file/table.txt");
                }

                System.out.println("Ввод данных: \n1...Консоль\n0...Файл");
                if (Integer.parseInt(reader.readLine()) == 1) {
                    System.out.println("Введите текст для зашифровки: ");
                    text = reader.readLine();
                } else {
                    System.out.print("Введите файл: ");
                    text = createText(reader.readLine());
                }

                newStr = new StringBuilder();
                for (int i = 0; i < text.length(); i++)
                    if (checkLowerCase(text.charAt(i)))
                        newStr.append(map.get(text.charAt(i)));
                    else
                        newStr.append(text.charAt(i));
                System.out.println("Вывод данных: \n1...Консоль\n0...Файл");
                if (Integer.parseInt(reader.readLine()) == 1) {
                    System.out.println("Текст зашифрован: \n");
                    printInConsole(newStr);
                } else {
                    System.out.print("Введите файл: ");
                    createFile(reader.readLine(), newStr);
                }

            } else if (act == 1) { ////расшифрование

                System.out.println("Ввод данных: \n1...Консоль\n0...Файл");
                if (Integer.parseInt(reader.readLine()) == 1) {
                    System.out.println("Введите текст для расшифрования: ");
                    text = reader.readLine();
                } else {
                    System.out.print("Введите файл: ");
                    text = createText(reader.readLine());
                }

                map = createTable("src/file/table.txt");

                newStr = decrypt((HashMap) map, text);

                System.out.println("Вывод данных: \n1...Консоль\n0...Файл");
                if (Integer.parseInt(reader.readLine()) == 1) {
                    System.out.println("Текст расшифрован: \n");
                    printInConsole(newStr);
                } else {
                    System.out.print("Введите файл: ");
                    createFile(reader.readLine(), newStr);
                }
            }
        }
    }

    protected static Map createTable(String way) throws IOException {
        HashMap<Character, Character> map = new HashMap();

        //заносим данные в словарь из файла
        File file = new File(way);
        FileReader fr = new FileReader(file);
        Scanner scanner  = new Scanner(fr);
        while (scanner.hasNext()) {
            String productTXT = scanner.nextLine();
            String[] array = productTXT.split(",");
            map.put(array[0].charAt(0), array[1].charAt(0));
        }
        fr.close();

        return map;
    }

    private static Map createNewTable(String way) throws IOException {
        HashMap<Character, Character> map = new HashMap();

        //создали список в которыый положили алфавит
        ArrayList<Character> list = new ArrayList<>();
        for (int i = 0; i < 32; i++)
            list.add((char)((int)'а' + i ));

        //продублировали этот список, чтобы мы могли его изменять
        ArrayList<Character> listChanged = new ArrayList<>(list);

        //создали словарь нового шифра, в качестве ключа мы брали попорядку элементы из неизменяемого списка алфавита
        //а значение генерировали из изменяемого списка, и удаляли элемент, чтобы он не смог повториться.
        for (int i = 31; i >= 0; i--) {
            Character character = listChanged.get((int)(Math.random() * (i + 1)));
            map.put(list.get(31 - i), character);
            listChanged.remove(character);
        }

        //перезаписали файл, записав туда новый словарь
        File file = new File(way);
        FileWriter fw = new FileWriter(file);
        for (Character ch : map.keySet())
            fw.write(ch + "," + map.get(ch) + "\n");
        fw.close();

        return map;
    }

    private static boolean checkLowerCase (Character character){
        return ((int) character >= (int) 'а') && ((int) character <= (int) 'я');
    }

    private static String createText(String way) throws IOException {

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

        return text.toString();
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

    private static StringBuilder decrypt (HashMap<Character, Character> map, String text){
        //переворачиваем словарь
        HashMap<Character, Character> mapReturn = new HashMap<>();
        for (Character ch : map.keySet())
            mapReturn.put(map.get(ch),  ch);

        StringBuilder newStr = new StringBuilder();
        for (int i = 0; i < text.length(); i++)
            if (checkLowerCase(text.charAt(i)))
                newStr.append(mapReturn.get(text.charAt(i)));
            else
                newStr.append(text.charAt(i));
        return newStr;
    }

    private static void printInConsole(StringBuilder text){
        for (int i = 0; i < text.length()/175; i++)
            System.out.println(text.substring(175*(i), 175*(i+1)));
        System.out.println(text.substring(175*(text.length()/175), text.length()));
    }
}
