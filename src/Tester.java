import edu.duke.FileResource;

import java.util.Arrays;
import java.util.HashSet;

public class Tester {

    // тест алгоритма шифрования Цезаря
    public void testCaesarCipher() {
        System.out.println("  Тестируем класс CaesarCipher - шифрования шифром Цезаря !");
        FileResource fr = new FileResource("data/titus-small.txt");
        String input = fr.asString();
        System.out.println(" Входное сообщение ");
        System.out.println(input);

        int key = 4;
        CaesarCipher cchip = new CaesarCipher(key);
        String encryptMessage = cchip.encrypt(input);
        System.out.println(" Зашифрованое сообщение ключем - " + key);
        System.out.println(encryptMessage);

        String decryptMessage = cchip.decrypt(encryptMessage);
        System.out.println(" расшифрованное при помощи известного ключа(шифра) сообщение ");
        System.out.println(decryptMessage);

    }


    // тестирование 1 метода взлома сообщения , зашифрованного шифром Цезаря
    public void testCaesarCracker1() {
        System.out.println("  Тест 1 ласса CaesarCracker - взлома сообщения , зашифрованного шифром Цезаря !");
        FileResource fr = new FileResource("data/titus-small.txt");
        String input = fr.asString();
        System.out.println(" Входное сообщение ");
        System.out.println(input);

        int key = 4;
        CaesarCipher cChip = new CaesarCipher(key);
        String encryptMessage = cChip.encrypt(input);
        System.out.println(" Зашифрованое сообщение ключем - " + key);
        System.out.println(encryptMessage);

        CaesarCracker cCrack = new CaesarCracker();
        //CaesarCracker cCrack = new CaesarCracker('a');   // -  вариант в конcтрукторе указываем самую частую букву в языке
        String decryptMessage = cCrack.decrypt(encryptMessage);
        System.out.println(" расшифрованное при помощи при помощи метода decrypt класса CaesarCracker ");
        System.out.println(decryptMessage);
    }


    // тестирование 1 метода взлома сообщения , зашифрованного шифром Цезаря
    public void testCaesarCracker2() {
        System.out.println("  Тест 2 класса CaesarCracker - взлома сообщения , зашифрованного шифром Цезаря !");
        FileResource fr = new FileResource("data/oslusiadas_key17.txt");
        String encryptMessage = fr.asString();
        System.out.println(" Входное зашифрованное сообщение ");
        System.out.println(encryptMessage);

        //CaesarCracker cCrack = new CaesarCracker();
        CaesarCracker cCrack = new CaesarCracker('a');   // -  вариант в конcтрукторе указываем самую частую букву в языке
        String decryptMessage = cCrack.decrypt(encryptMessage);
        System.out.println(" расшифрованное при помощи при помощи метода decrypt класса CaesarCracker");
        System.out.println(decryptMessage);
    }


    public void testVigenereCipher() {
        System.out.println("  Тестируем класс VigenereCipher - шифрования шифром Виженера !");
        FileResource fr = new FileResource("data/titus-small.txt");
        String input = fr.asString();
        System.out.println(" Входное сообщение ");
        System.out.println(input);

        int[] key = {17, 14, 12, 4};
        VigenereCipher vChip = new VigenereCipher(key);
        String encryptMessage = vChip.encrypt(input);
        System.out.println(" Зашифрованое сообщение ключем Вижинера - " + Arrays.toString(key));
        System.out.println(encryptMessage);

        String decryptMessage = vChip.decrypt(encryptMessage);
        System.out.println(" расшифрованное при помощи известного ключа(шифра) сообщение ");
        System.out.println(decryptMessage);
    }


    //
    public void testVigenereBreaker1() {
        System.out.println("  Тестируем класс VigenereBreaker - дешифрования сообщений, закодированных шифром Виженера !");
        System.out.println("  Тестируем тут поиск ключа и вспомогательные методы");

        VigenereBreaker vBreak = new VigenereBreaker();

        // тест метода sliceString(String message, int whichSlice, int totalSlices)
        // выдает сообщение , начиная с символа номер whichSlice и потом символы
        // номер которых + totalSlices
        String resMassege = vBreak.sliceString("abcdefghijklm", 4, 5);
        System.out.println(" рез метода vBreak.sliceString - " + resMassege);

        // проверим вспомогательные методы преобразования
        // чисельного ключа в слово и наоборот, слово в массив чисел
        String wordTest = "flute";
        int[] keyTest = vBreak.keyByWord("flute");
        System.out.println(" Слово - " + wordTest + "- это массив чисел - " + Arrays.toString(keyTest));
        wordTest = vBreak.wordByKey(keyTest);
        System.out.println(" Обратно из массива чисел делаем слово - " + wordTest);

        // тест метода tryKeyLength(String encrypted, int klength, char mostCommon)
        // который находит ключ методом getKey класса CaesarCracker
        // encrypted - зашифр сообщение, klength - длинна ключа, mostCommon - самая распрастраненная буква языка
        FileResource fr = new FileResource("data/athens_keyflute.txt");
        //String encryptMessage = "1234512345123451234512345123451234512345";
        String encryptMessage = fr.asString();
        int[] key = vBreak.tryKeyLength(encryptMessage, 5, 'e');
        System.out.println(" Массив найденых ключей шифра Виженера - " + Arrays.toString(key));
        System.out.println(" Слово - ключ  - " + vBreak.wordByKey(key));

        // тест метода который перекачивает из файла в HashSet, одно слово в строке
        FileResource fRes = new FileResource("dictionaries/English");
        HashSet<String> dictSet = vBreak.readDictionary(fRes);
        System.out.println(" Словарь :");
        System.out.println(dictSet.toString());
        System.out.println();

        // тест метода который возвращает сколько слов из сообщения есть в словаре
        FileResource fileResource = new FileResource();
        String message = fileResource.asString();
        int countWord = vBreak.countWords(message, dictSet);
        System.out.println();
        System.out.println(" в словаре dictSet есть " + countWord + " слов из сообщения");
        System.out.println();

        System.out.println(" Найдем самую частую букву в словаре - Надо выбрать словарь в папке dictionaries");
        FileResource fDictionary = new FileResource();
        HashSet<String> dictionary = vBreak.readDictionary(fDictionary);
        char mostCommon = vBreak.mostCommonCharin(dictionary);
        System.out.println(" В словаре " + fDictionary.toString() + " самая частая литера - " + mostCommon);
    }


    public void testVigenereBreakerMain() {
        System.out.println("  Тестируем класс VigenereBreaker - дешифрования сообщений, закодированных шифром Виженера !");
        System.out.println("  Найдем ключ и протестируем тут главный метод класса ");
        VigenereBreaker vBreak = new VigenereBreaker();
        vBreak.breakVigenere();
    }
}
