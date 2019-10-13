package lesson2;

import kotlin.NotImplementedError;
import kotlin.Pair;

import java.io.*;
import java.lang.reflect.Array;
import java.util.*;

import static java.lang.Math.sqrt;

@SuppressWarnings("unused")
public class JavaAlgorithms {
    /**
     * Получение наибольшей прибыли (она же -- поиск максимального подмассива)
     * Простая
     *
     * Во входном файле с именем inputName перечислены цены на акции компании в различные (возрастающие) моменты времени
     * (каждая цена идёт с новой строки). Цена -- это целое положительное число. Пример:
     *
     * 201
     * 196
     * 190
     * 198
     * 187
     * 194
     * 193
     * 185
     *
     * Выбрать два момента времени, первый из них для покупки акций, а второй для продажи, с тем, чтобы разница
     * между ценой продажи и ценой покупки была максимально большой. Второй момент должен быть раньше первого.
     * Вернуть пару из двух моментов.
     * Каждый момент обозначается целым числом -- номер строки во входном файле, нумерация с единицы.
     * Например, для приведённого выше файла результат должен быть Pair(3, 4)
     *
     * В случае обнаружения неверного формата файла бросить любое исключение.
     */
    static public kotlin.Pair<Integer, Integer> optimizeBuyAndSell(String inputName) {
        throw new NotImplementedError();
    }

    /**
     * Задача Иосифа Флафия.
     * Простая
     *
     * Образовав круг, стоят menNumber человек, пронумерованных от 1 до menNumber.
     *
     * 1 2 3
     * 8   4
     * 7 6 5
     *
     * Мы считаем от 1 до choiceInterval (например, до 5), начиная с 1-го человека по кругу.
     * Человек, на котором остановился счёт, выбывает.
     *
     * 1 2 3
     * 8   4
     * 7 6 х
     *
     * Далее счёт продолжается со следующего человека, также от 1 до choiceInterval.
     * Выбывшие при счёте пропускаются, и человек, на котором остановился счёт, выбывает.
     *
     * 1 х 3
     * 8   4
     * 7 6 Х
     *
     * Процедура повторяется, пока не останется один человек. Требуется вернуть его номер (в данном случае 3).
     *
     * 1 Х 3
     * х   4
     * 7 6 Х
     *
     * 1 Х 3
     * Х   4
     * х 6 Х
     *
     * х Х 3
     * Х   4
     * Х 6 Х
     *
     * Х Х 3
     * Х   х
     * Х 6 Х
     *
     * Х Х 3
     * Х   Х
     * Х х Х
     *
     * Общий комментарий: решение из Википедии для этой задачи принимается,
     * но приветствуется попытка решить её самостоятельно.
     */
    static public int josephTask(int menNumber, int choiceInterval) {
        throw new NotImplementedError();
    }

    /**
     * Наибольшая общая подстрока.
     * Средняя
     *
     * Дано две строки, например ОБСЕРВАТОРИЯ и КОНСЕРВАТОРЫ.
     * Найти их самую длинную общую подстроку -- в примере это СЕРВАТОР.
     * Если общих подстрок нет, вернуть пустую строку.
     * При сравнении подстрок, регистр символов *имеет* значение.
     * Если имеется несколько самых длинных общих подстрок одной длины,
     * вернуть ту из них, которая встречается раньше в строке first.
     */

    /*
    * Main idea - to build some sort of matrix with the cells filled with the common substring lengths using diagonals
    * abcdcf, cde:
    *   abcdcf
    * a 100000
    * b 020000
    * c 003010
    *
    * the main thing we need to do here - for every (i, j) element, set it to 0 if the first[i] != second[j], else
    * take the (i - 1, j - 1) element, increase it by 1 and write the value to the (i, j) element.
    * The biggest value will be in the (maxI, maxJ) element -> first[maxI] = second[maxJ] will be the end of
    * the common substring.
    *
    *
    * Complexity: O(N^2) (or is it better to say O(N*M)?) for filling the N * M matrix,
    * O(const) for taking the longest substring (but it still depends on the size of the substring,
    * not on the length of the strings given)
    * total -> O(N^2)
    *
    * Memory: O(const) for maxLength, its indexes and for the maxStr,
    * O(N^2) for the matrix
    * total -> O(N^2)
    */

    static public String longestCommonSubstring(String first, String second) {
        StringBuilder maxStr = new StringBuilder();
        int maxFirst = -1;
        int maxSecond = -1;
        int maxLength = 0;
        int[][] matrix = new int[first.length()][second.length()];
        //filling the matrix + looking for the max number - O(N^2)
        for (int i = 0; i < first.length(); i++) {
            for (int j = 0; j < second.length(); j++) {
                if (first.charAt(i) == second.charAt(j)) {
                    if (i == 0 || j == 0) matrix[i][j] = 1;
                    else matrix[i][j] = matrix[i - 1][j - 1] + 1;
                    if(matrix[i][j] > maxLength) {
                        maxFirst = i;
                        maxLength = matrix[i][j];
                    }
                }
                else {
                    matrix[i][j] = 0;
                }
            }
        }
        //getting our string
        if (maxFirst == -1) return "";
        else {
            char[] maxStrAsChars = new char[maxLength];
            for (int i = maxFirst - maxLength + 1; i <= maxFirst; i++) maxStr.append(first.charAt(i));
            return maxStr.toString();
        }
    }

    /**
     * Число простых чисел в интервале
     * Простая
     *
     * Рассчитать количество простых чисел в интервале от 1 до limit (включительно).
     * Если limit <= 1, вернуть результат 0.
     *
     * Справка: простым считается число, которое делится нацело только на 1 и на себя.
     * Единица простым числом не считается.
     */

    /*
     * previous time out was due to the Sieve of Eratosthenes algorithm usage, as if I needed not the amount of the
     * prime numbers but something like the array with the prime numbers from 1 to limit :C
     *
     *
     * Complexity: O(N) for the i-loop, O(sqrt(N)) for the j-loop, total ->
     * O(N*sqrt(N))
     *
     * Memory: O(const) for holding the values we need - limit, i, j, primes, isPrime
     */

    static public int calcPrimesNumber(int limit) {
        if (limit <= 1) return 0;
        if (limit == 2) return 1;

        int primes = 1;
        for (int i = 3; i <= limit; i+= 2) {
            boolean isPrime = true;
            for (int j = 2; j <= sqrt(i); j++) if (i % j == 0) {
                isPrime = false;
                break;
            }
            if (isPrime) primes += 1;
        }
        return primes;
    }

    /**
     * Балда
     * Сложная
     *
     * В файле с именем inputName задана матрица из букв в следующем формате
     * (отдельные буквы в ряду разделены пробелами):
     *
     * И Т Ы Н
     * К Р А Н
     * А К В А
     *
     * В аргументе words содержится множество слов для поиска, например,
     * ТРАВА, КРАН, АКВА, НАРТЫ, РАК.
     *
     * Попытаться найти каждое из слов в матрице букв, используя правила игры БАЛДА,
     * и вернуть множество найденных слов. В данном случае:
     * ТРАВА, КРАН, АКВА, НАРТЫ
     *
     * И т Ы Н     И т ы Н
     * К р а Н     К р а н
     * А К в а     А К В А
     *
     * Все слова и буквы -- русские или английские, прописные.
     * В файле буквы разделены пробелами, строки -- переносами строк.
     * Остальные символы ни в файле, ни в словах не допускаются.
     */

    /*
    * Complexity: O(N^2) for checking every letter in the field * O(N) for checking every word at every field cell ->
    * Total O(N^3)
    *
    * Memory: O(N^2) for the field, O(N) for the resulting set , O(const) for some constant values in the recursion->
    * Total O(N^2)
    */
    private static class Pair {
        int i;
        int j;
        Pair(int iIn, int jIn) {
            i = iIn;
            j = jIn;
        }
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Pair pair = (Pair) o;
            return i == pair.i &&
                    j == pair.j;
        }

    }

    private static boolean wordFinder(String word, ArrayList<String[]> field,
                                      int i, int j, int iSize, int jSize, ArrayList<Pair> used) {

        String currLetter = Character.toString(word.charAt(0));
        if (!field.get(i)[j].equals(currLetter)) return false;
        if (word.length() == 1) return true;

        word = word.substring(1);
        boolean result = false;
        Pair[] variants = {new Pair(i - 1, j), new Pair(i + 1, j), new Pair(i, j - 1), new Pair(i, j + 1)};

        for (Pair pair : variants) {
            if (!used.contains(pair) && (pair.i >= 0) && (pair.j >= 0) && (pair.i <= iSize - 1) && (pair.j <= jSize - 1)) {
                used.add(pair);
                if (wordFinder(word, field, pair.i, pair.j, iSize, jSize, used)) {
                    result = true;
                    break;
                }
                used.remove(pair);
            }
        }
        return result;
    }

    static public Set<String> baldaSearcher(String inputName, Set<String> words) {
        ArrayList<String[]> field = new ArrayList<>();
        Set<String> result = new HashSet<>();
        //reading part
        try (BufferedReader buffIn = new BufferedReader(new FileReader(inputName))) {
            buffIn.lines().forEach(str -> {
                if (!str.matches("([A-ZА-ЯЁ]\\s)*[A-ZА-ЯЁ]"))
                    throw new IllegalArgumentException("string format is broken for " + str);

                String[] parsedStr = str.split(" ");
                if (field.isEmpty()) field.add(parsedStr);
                else if (parsedStr.length == field.get(field.size() - 1).length) field.add(parsedStr);
                else throw new IllegalArgumentException("string format is broken for " + str);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        int iSize = field.size();
        int jSize = field.get(0).length;

        for (int i = 0; i < iSize; i++) {
            for (int j = 0; j < jSize; j++) {
                for (String word : words) {
                    if (wordFinder(word, field, i, j, iSize, jSize, new ArrayList<>())) {
                        result.add(word);
                    }
                }
            }
        }
        return result;
    }
}
