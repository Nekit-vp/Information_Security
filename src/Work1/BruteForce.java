package Work1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class BruteForce {
    public static void main(String[] args) throws IOException {

        System.out.println("Программа для взлома сообщения, зашифрованного шифром Цезаря. //латинский алфавит");

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Введите текст для взлома: ");
        String allText = reader.readLine();

        String text;
        if (allText.length() <= 20)
            text = allText;
        else
            text = allText.substring(0, 20);

        StringBuilder newStr;

        for (int j = 1; j < 26; j++) {
            System.out.print(j + "   ");

            newStr = new StringBuilder();

            for (int i = 0; i < text.length(); i++) {
                if (checkLowerCase(text.charAt(i)))
                    newStr.append((char) (((((int) text.charAt(i) - (int) 'a')) - j + 26) % 26 + (int) 'a'));
                else if (checkHighCase(text.charAt(i)))
                    newStr.append((char) (((((int) text.charAt(i) - (int) 'A')) - j + 26) % 26 + (int) 'A'));
                else
                    newStr.append(text.charAt(i));
            }

            System.out.println(newStr);
        }

        System.out.print("Введите правильный шаг сдвига: ");
        int step = Integer.parseInt(reader.readLine());

        System.out.println("Текст взломан: " + decrypt(allText, step));

    }

    private static String decrypt(String text, int delta) {

        StringBuilder newStr = new StringBuilder();

        for (int i = 0; i < text.length(); i++) {
            if (checkLowerCase(text.charAt(i)))
                newStr.append((char) (((((int) text.charAt(i) - (int) 'a')) - delta + 26) % 26 + (int) 'a'));
            else if (checkHighCase(text.charAt(i)))
                newStr.append((char) (((((int) text.charAt(i) - (int) 'A')) - delta + 26) % 26 + (int) 'A'));
            else
                newStr.append(text.charAt(i));
        }

        return newStr.toString();
    }

    private static boolean checkLowerCase(Character character) {
        return ((int) character >= (int) 'a') & ((int) character <= (int) 'z');
    }

    private static boolean checkHighCase(Character character) {
        return ((int) character >= (int) 'A') & ((int) character <= (int) 'Z');
    }
}
