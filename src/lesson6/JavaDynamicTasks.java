package lesson6;

import kotlin.NotImplementedError;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SuppressWarnings("unused")
public class JavaDynamicTasks {
    /**
     * Наибольшая общая подпоследовательность.
     * Средняя
     *
     * Дано две строки, например "nematode knowledge" и "empty bottle".
     * Найти их самую длинную общую подпоследовательность -- в примере это "emt ole".
     * Подпоследовательность отличается от подстроки тем, что её символы не обязаны идти подряд
     * (но по-прежнему должны быть расположены в исходной строке в том же порядке).
     * Если общей подпоследовательности нет, вернуть пустую строку.
     * Если есть несколько самых длинных общих подпоследовательностей, вернуть любую из них.
     * При сравнении подстрок, регистр символов *имеет* значение.
     */
    public static String longestCommonSubSequence(String first, String second) {
        throw new NotImplementedError();
    }

    /**
     * Наибольшая возрастающая подпоследовательность
     * Сложная
     *
     * Дан список целых чисел, например, [2 8 5 9 12 6].
     * Найти в нём самую длинную возрастающую подпоследовательность.
     * Элементы подпоследовательности не обязаны идти подряд,
     * но должны быть расположены в исходном списке в том же порядке.
     * Если самых длинных возрастающих подпоследовательностей несколько (как в примере),
     * то вернуть ту, в которой числа расположены раньше (приоритет имеют первые числа).
     * В примере ответами являются 2, 8, 9, 12 или 2, 5, 9, 12 -- выбираем первую из них.
     */

    //!!!the task in les5 that counts for the both of the lessons is completed too

    //classic of dynamic programming - checking the length of the longest subsequence with
    //length =  max(1, max(prevNumberLength + 1)
    //then restoring the result
    //Complexity: O(n^2) where n is the amount of the elements given, n^2 due to the double loop
    //Memory: O(3*n) => O(n) - there are three arrays of the size n: result, dynamic and prevElements
    public static List<Integer> longestIncreasingSubSequence(List<Integer> list) {
        List<Integer> result = new ArrayList<>();
        if (list.isEmpty()) return result; //empty input -> empty output
        List<Integer> dynamic = new ArrayList<>();
        List<Integer> prevElements = new ArrayList<>(); //index of the previous element in the subsequence(-1 if none)
        //double loop: O(n^2) complexity
        for (int i = 0; i < list.size(); i++) {
            dynamic.add(i, 1);
            prevElements.add(i, -1);

            for (int j = 0; j < i; j++) {
                if ((list.get(j) < list.get(i)) && (1 + dynamic.get(j) > dynamic.get(i))) {
                    dynamic.add(i, 1 + dynamic.get(j));
                    prevElements.add(i, j);
                }
            }
        }

        //looking for last element in the longest subsequence
        int maxLength = dynamic.get(0);
        int position = prevElements.get(0);
        for (int i = 1; i < list.size(); i++) {
            if (dynamic.get(i) > maxLength) {
                maxLength = dynamic.get(i);
                position = i;
            }
        }
        //solving both the case if there are only one element in the list given and
        //the case if the subsequence contains only one element
        if (position == -1) {
            result.add(list.get(0));
            return result;
        }
        //getting the result ready by filling it, starting from the end of the subsequence, then reversing it
        while (position != -1) {
            result.add(list.get(position));
            position = prevElements.get(position);
        }
        Collections.reverse(result);
        return result;
    }

    /**
     * Самый короткий маршрут на прямоугольном поле.
     * Средняя
     *
     * В файле с именем inputName задано прямоугольное поле:
     *
     * 0 2 3 2 4 1
     * 1 5 3 4 6 2
     * 2 6 2 5 1 3
     * 1 4 3 2 6 2
     * 4 2 3 1 5 0
     *
     * Можно совершать шаги длиной в одну клетку вправо, вниз или по диагонали вправо-вниз.
     * В каждой клетке записано некоторое натуральное число или нуль.
     * Необходимо попасть из верхней левой клетки в правую нижнюю.
     * Вес маршрута вычисляется как сумма чисел со всех посещенных клеток.
     * Необходимо найти маршрут с минимальным весом и вернуть этот минимальный вес.
     *
     * Здесь ответ 2 + 3 + 4 + 1 + 2 = 12
     */
    public static int shortestPathOnField(String inputName) {
        throw new NotImplementedError();
    }

    // Задачу "Максимальное независимое множество вершин в графе без циклов"
    // смотрите в уроке 5
}
