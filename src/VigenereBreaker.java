import java.util.*;
import edu.duke.*;

public class VigenereBreaker {

    private HashMap<String, HashSet<String>> langs;      // языки с их словарями

    public VigenereBreaker() {
        langs = initializingLanguagesWithDictionaries();
    }

    // инициализируем(заполняем HashMap) словари языков
    private HashMap<String, HashSet<String>> initializingLanguagesWithDictionaries() {
        HashMap<String, HashSet<String>> languages = new HashMap<>();

        System.out.println("......Danish ");
        FileResource frDanish = new FileResource("dictionaries/Danish");
        languages.put("Danish", readDictionary(frDanish));
        System.out.println("......Dutch ");
        FileResource frDutch = new FileResource("dictionaries/Dutch");
        languages.put("Dutch", readDictionary(frDutch));
        System.out.println("......English ");
        FileResource frEnglish = new FileResource("dictionaries/English");
        languages.put("English", readDictionary(frEnglish));
        System.out.println("......French ");
        FileResource frFrench = new FileResource("dictionaries/French");
        languages.put("French", readDictionary(frFrench));
        System.out.println("......German ");
        FileResource frGerman = new FileResource("dictionaries/German");
        languages.put("German", readDictionary(frGerman));
        System.out.println("......Italian ");
        FileResource frItalian = new FileResource("dictionaries/Italian");
        languages.put("Italian", readDictionary(frItalian));
        System.out.println("......Portuguese ");
        FileResource frPortuguese= new FileResource("dictionaries/Portuguese");
        languages.put("Portuguese", readDictionary(frPortuguese));
        System.out.println("......Spanish ");
        FileResource frSpanish= new FileResource("dictionaries/Spanish");
        languages.put("Spanish", readDictionary(frSpanish));

        return languages;
    }

    // метод выдает сообщение , начиная с символа номер whichSlice и потом символы номер которых + totalSlices
    public String sliceString(String message, int whichSlice, int totalSlices) {
        //REPLACE WITH YOUR CODE
        String resMess = "";
        for (int i = whichSlice; i < message.length(); i += totalSlices) {
            resMess = resMess + message.charAt(i);
        }
        return resMess;
    }

    // метод находит массив ключей. encrypted - зашифрованое сообщение,
    // klength - длинна массива ключей, mostCommon - самая распрастраненная пуква в языке
    public int[] tryKeyLength(String encrypted, int klength, char mostCommon) {
        int[] key = new int[klength];
        //WRITE YOUR CODE HERE
        CaesarCracker cCrack = new CaesarCracker(mostCommon);
        for (int i = 0; i < klength; i++) {
            String encryptMessage = sliceString(encrypted, i, klength);
            key[i] = cCrack.getKey(encryptMessage);
        }
        return key;
    }


    // из файла словарь перекачиваем в HashSet (кажд элемент по одному - это специфика HashSet)
    public HashSet<String> readDictionary( FileResource fr) {
        HashSet<String> dictionary = new HashSet<>();
        for (String word : fr.lines()) {    // одно слово в строчке в словаре изначально
            word = word.toLowerCase();
            dictionary.add(word);
        }
        return dictionary;
    }


    // метод возвращает сколько слов из сообщения есть в словаре
    public int countWords(String message, HashSet<String> dictionary) {
        int countW = 0;
        for (String word : message.split("\\W+")) {        // разбивает сообщение по словам
            word = word.toLowerCase();
            if (dictionary.contains(word))  {
                countW++;        // если в словаре есть слово , то +1
            }
        }
        return countW;
    }

    // метод подбирает ключ
    // 1 - перебирает от 1 до 100 кол-во ключей (кол-во элементов в массиве int[] key)
    // 2 - по каждому кол-ву ключей дает расшифрованное currMessage
    // 3 - определяет в currMessage кол-во слов совпавшими со словами из словаря dictionary - countСorrectWords
    // 4 - вщзвращает decryptMessage = currMessage в котором наибольшее кол-во - countСorrectWords
    public String breakForLanguage(String encrypted, HashSet<String> dictionary) {
        String decryptMessage = "";
        int maxCorrectWord = 0;              // макксимальное кол-во слов из сообщения совпадающее со словами словаря
        char mostCommon = mostCommonCharin(dictionary);    // определим самую частую букву в языке
        for (int i = 1; i < 100; i++) {       // перебираем длины ключей от 1 до 100. Но можно и до message.lenght
            int[] key = tryKeyLength(encrypted, i, mostCommon);   // определяем ключи для данного варианта длины ключа(i - кол-во ключей в массиве)
            VigenereCipher vCiper = new VigenereCipher(key);         // для найденного ключа конструктор
            String currMessage = vCiper.decrypt(encrypted);      // расшифровываем сообщение текущим ключем
            int countСorrectWords = countWords(currMessage, dictionary);    // определяем кол-во слов в сообщ которые есть в словаре
            if (countСorrectWords > maxCorrectWord) {        // если кол-во слов сообщения которые есть в словаре больше чем максимум который был в сообщениях до этого, то берем его за основной вариант расшифрованного сообщения
                maxCorrectWord = countСorrectWords;
                decryptMessage = currMessage;
                System.out.println(" последний ключ - " + Arrays.toString(key) + "\t" + " слово ключ - " + wordByKey(key) + "\t" + " длина ключа - " + key.length);
                System.out.println(" Кол-во слов совпадающих со словарем - " + maxCorrectWord);

            }
        }
        //System.out.println(" Кол-во слов совпадающих со словарем - " + maxCorrectWord);
        return decryptMessage;
    }


    // возвращает наиболее частовстречающийся символ в словаре
    public char mostCommonCharin(HashSet<String> dictionary) {
        char mostCommon = '-';
        HashMap<Character, Integer> map = new HashMap<Character, Integer>();
        // заполним HashMap<Character, Integer> map соответствием кажд букве - сколько раз встречается в словаре раз
        for (String word : dictionary) {
            for (int i = 1; i < word.length(); i++) {
                if (!Character.isLetter(word.charAt(i))) continue;
                char ch = Character.toLowerCase(word.charAt(i));
                if (map.containsKey(ch)) {
                    map.put(ch, map.get(ch) + 1);
                } else {
                    map.put(ch, 1);
                }
            }
        }
        // какая буква встречается чаще в HashMap<Character, Integer> map, ту и выведем как рез
        int max = 0;
        for (Character ch : map.keySet()) {
            if (map.get(ch) > max) {
                mostCommon = ch;
                max = map.get(ch);
            }
        }
        return mostCommon;
    }

    // исходные данные - зашифрованное сообщение и HashMap словарей разных языков
    // при помощи методов breakForLanguage и countWords найти наиболее подходящий
    // язык (там где будет больше подходящих слов при расшифровке)
    // И вернуть расшифрованное сообщение
    public String breakForAllLangs(String encrypt, HashMap<String, HashSet<String>> languages) {
        String decrypt = "-";
        int maxCountW = 0;
        String resLanguage = "-";
        for (String language : languages.keySet()) {
            System.out.println();
            System.out.println(" язык - " + "\t" + language);
            System.out.println("......................................");
            HashSet<String> diceionary = languages.get(language);     // получаем словарь языка
            String currDecrypt = breakForLanguage(encrypt, diceionary);    // вариант расшифровки на данном языке
            int countW = countWords(currDecrypt, diceionary);        // кол-во слов совпадающих со словарем
            if (countW > maxCountW) {
                maxCountW = countW;
                decrypt = currDecrypt;
                resLanguage = language;
            }
        }
        System.out.println();
        System.out.println();
        System.out.println(" ----------------------------------------------------------------------");
        System.out.println(" Язык - " + resLanguage + "\t" + " кол-во слов совпавших со словарем = " + maxCountW);
        //System.out.println(" Текст расшифрованного сообщения - " + decrypt);

        return decrypt;
    }


    // главный метод
    // дешифрует выбранный файлресурс
    public void breakVigenere () {
        //WRITE YOUR CODE HERE
        FileResource fr = new FileResource();
        String encrypt = fr.asString();
        String decrypt = breakForAllLangs(encrypt, langs);
        System.out.println();
        System.out.println();
        System.out.println(" Расшифрованное сообщение - ");
        System.out.println(decrypt);
    }



    // ключ по слову находит
    public int[] keyByWord(String word) {
        int[] key = new int[word.length()];
        String alph = "abcdefghijklmnopqrstuvwxyz";
        for (int i = 0; i < word.length(); i++) {
            int index = alph.indexOf(word.charAt(i));
            if (index != -1) {
                key[i] = index;
            } else {System.out.println(" !!! ОШИБКА !!! Некорректное слово - ключ !!!" );}
        }
        return key;
    }


    // слово - ключ по ключу числовому
    public String wordByKey(int[] key) {
        String word = "";
        String alph = "abcdefghijklmnopqrstuvwxyz";
        for (int i = 0; i < key.length; i++) {
            word = word + alph.charAt(key[i]);
        }
        return word;
    }

}
