import java.util.*;
import edu.duke.*;

public class VigenereBreaker {

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
            String encryptMessage = sliceString(encrypted, i, i + klength);
            key[i] = cCrack.getKey(encryptMessage);
        }
        return key;
    }

    public void breakVigenere () {
        //WRITE YOUR CODE HERE
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
