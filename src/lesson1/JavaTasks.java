package lesson1;

import kotlin.NotImplementedError;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.*;

import static java.lang.Math.abs;
import static lesson1.Sorts.countingSort;

@SuppressWarnings("unused")
public class JavaTasks {
    /**
     * Сортировка времён
     *
     * Простая
     * (Модифицированная задача с сайта acmp.ru)
     *
     * Во входном файле с именем inputName содержатся моменты времени в формате ЧЧ:ММ:СС AM/PM,
     * каждый на отдельной строке. См. статью википедии "12-часовой формат времени".
     *
     * Пример:
     *
     * 01:15:19 PM
     * 07:26:57 AM
     * 10:00:03 AM
     * 07:56:14 PM
     * 01:15:19 PM
     * 12:40:31 AM
     *
     * Отсортировать моменты времени по возрастанию и вывести их в выходной файл с именем outputName,
     * сохраняя формат ЧЧ:ММ:СС AM/PM. Одинаковые моменты времени выводить друг за другом. Пример:
     *
     * 12:40:31 AM
     * 07:26:57 AM
     * 10:00:03 AM
     * 01:15:19 PM
     * 01:15:19 PM
     * 07:56:14 PM
     *
     * В случае обнаружения неверного формата файла бросить любое исключение.
     */

    /*
    *inventing the bicycle in the previous attempts was surely not the best idea :c
    *
    *Complexity: O(N) reading, O(N*log(N)) mergesort(Collections.sort()), O(N) writing, -> total O(N * log(N))
    *
    * Memory: O(const) for every Time object + N time objects in input -> O(N) for the input data,
    * O(N) for the mergesort, O(const) for writing, -> total O(N)
    */

    private static class Time implements Comparable{
        int seconds;
        String textTime;
        Time(String str) {
            if (!str.matches("0[1-9]:[0-5][0-9]:[0-5][0-9]\\s[AP]M") &&
                    !str.matches("1[0-2]:[0-5][0-9]:[0-5][0-9]\\s[AP]M"))
                throw new IllegalArgumentException("illegal format: " + str);

            textTime = str;
            String[] arr = str.split(" ")[0].split(":");
            seconds = (Integer.parseInt(arr[0])*60 + Integer.parseInt(arr[1])) * 60 + Integer.parseInt(arr[2]);
            final int twelveHours = 43200;
            if (seconds >= twelveHours && str.charAt(9) == 'A') seconds %= twelveHours; //12:xx:xx AM -> 00:xx:xx
            else if (seconds < twelveHours && str.charAt(9) == 'P') seconds += twelveHours; //12:xx:xx PM -> 12:xx:xx
        }

        @Override
        public int compareTo(@NotNull Object o) {
            Time other = (Time) o;
            return this.seconds - other.seconds;
        }

        @Override
        public String toString() { return textTime; }
    }

    //Note - taking 12:xx:xx AM (12) as 0:xx:xx (24), and 12:xx:xx PM (12) as 12:xx:xx (24)
    static public void sortTimes(String inputName, String outputName) {
        //reading part
        ArrayList<Time> input = new ArrayList<>();
        try (BufferedReader buffIn = new BufferedReader(new FileReader(inputName))) {
            buffIn.lines().forEach(str -> input.add(new Time(str)));
        } catch (IOException e) { throw new IllegalArgumentException("Input file name argument is illegal"); }

        Collections.sort(input);

        //writing part
        try (BufferedWriter buffOut = new BufferedWriter(new FileWriter(outputName))) {
            for (Time time : input) {
                buffOut.write(time.toString());
                buffOut.newLine();
            }
        } catch (IOException e) { throw new IllegalArgumentException("Output file name argument is illegal"); }
    }

    /**
     * Сортировка адресов
     *
     * Средняя
     *
     * Во входном файле с именем inputName содержатся фамилии и имена жителей города с указанием улицы и номера дома,
     * где они прописаны. Пример:
     *
     * Петров Иван - Железнодорожная 3
     * Сидоров Петр - Садовая 5
     * Иванов Алексей - Железнодорожная 7
     * Сидорова Мария - Садовая 5
     * Иванов Михаил - Железнодорожная 7
     *
     * Людей в городе может быть до миллиона.
     *
     * Вывести записи в выходной файл outputName,
     * упорядоченными по названию улицы (по алфавиту) и номеру дома (по возрастанию).
     * Людей, живущих в одном доме, выводить через запятую по алфавиту (вначале по фамилии, потом по имени). Пример:
     *
     * Железнодорожная 3 - Петров Иван
     * Железнодорожная 7 - Иванов Алексей, Иванов Михаил
     * Садовая 5 - Сидоров Петр, Сидорова Мария
     *
     * В случае обнаружения неверного формата файла бросить любое исключение.
     */

    /*
    * Complexity: N elements * logN complexity for every single element -> O(NlogN) for reading+sorting,
    * O(N) for writing, -> total O(NlogN) complexity
    *
    * Memory: O(N) for the input data * O(const) for the temporary data(record, added, etc.),
    * O(N) for input.keySet for writing, -> total O(N) memory
    */

    private static class Address implements Comparable {
        String street;
        int number;

        private Address(String fullAddress) {
            String[] addrRecord = fullAddress.split(" ");
            street = addrRecord[0];
            number = Integer.parseInt(addrRecord[1]);
        }

        @Override
        public int compareTo(@NotNull Object o) {
            Address other = (Address) o;
            if (this.street.compareTo(other.street) < 0) return -1;
            if (this.street.compareTo(other.street) > 0) return 1;
            return this.number - other.number;
        }

        @Override
        public String toString() { return street + " " + number; }
    }
    static public void sortAddresses(String inputName, String outputName) {
        //reading part
        Map<Address, TreeSet<String>> input = new TreeMap<>();

        try (BufferedReader buffIn = new BufferedReader(new FileReader(inputName))) {
            buffIn.lines().forEach(str -> {
                String regex = "[А-ЯЁа-яёA-Za-z\\-]+\\s[А-ЯЁа-яёA-Za-z\\-]+\\s-\\s[А-ЯЁа-яёA-Za-z\\-]+\\s[1-9][0-9]*";
                if (!str.matches(regex))
                    throw new IllegalArgumentException("oops, input format is broken for: " + str);

                String[] record = str.split("\\s-\\s");
                input.computeIfAbsent(new Address(record[1]), k -> new TreeSet<>()).add(record[0]);
                //System.out.println("for " + record[1] + " list is " + input.get(record[1]).toString());
            });

        } catch (IOException e) {throw new IllegalArgumentException("Input file name argument is illegal"); }

        //writing part
        try (BufferedWriter buffOut = new BufferedWriter(new FileWriter(outputName))) {
            for (Address address : input.keySet()) {
                buffOut.write(address.toString() + " - " + String.join(", ", input.get(address)));
                buffOut.newLine();
            }
        } catch (IOException e) { throw new IllegalArgumentException("Output file name argument is illegal"); }
    }

        /**
     * Сортировка температур
     *
     * Средняя
     * (Модифицированная задача с сайта acmp.ru)
     *
     * Во входном файле заданы температуры различных участков абстрактной планеты с точностью до десятых градуса.
     * Температуры могут изменяться в диапазоне от -273.0 до +500.0.
     * Например:
     *
     * 24.7
     * -12.6
     * 121.3
     * -98.4
     * 99.5
     * -12.6
     * 11.0
     *
     * Количество строк в файле может достигать ста миллионов.
     * Вывести строки в выходной файл, отсортировав их по возрастанию температуры.
     * Повторяющиеся строки сохранить. Например:
     *
     * -98.4
     * -12.6
     * -12.6
     * 11.0
     * 24.7
     * 99.5
     * 121.3
     */

    /*countingSort time!
    * Complexity: O(N) - reading, O(N) - sorting positive, O(N) - sorting negative, O(N) - writing
    * total -> O(N)
    *
    * Memory: O(N) - reading(positive and negative arrays hold together N elements),
    * O(N) - for positive and negative arrays,
    * O(1) - writing
    * Total -> O(N) memory
    */

    static public void sortTemperatures(String inputName, String outputName) {
        ArrayList<Integer> positive = new ArrayList<>(); //and zeros
        ArrayList<Integer> negative = new ArrayList<>();
        int[] posArr = new int[0];
        int[] negArr = new int[0];
        //reading part
        try(BufferedReader buffIn = new BufferedReader(new FileReader(inputName))) {

            buffIn.lines().forEach(str -> {
                if (!str.matches("-?[0-9]+\\.[0-9]")) //format: [-]xxxxx.x
                    throw new IllegalArgumentException("oops, incorrect input format for " + str);
                int currInt = (int) (Double.parseDouble(str)*10.0);
                System.out.println(currInt);
                //now we have doubles as integers with the saved decimal part
                if (currInt < 0.0) negative.add(abs(currInt));
                else positive.add(currInt);
            });
        } catch (IOException e) {
            System.out.println("Oops, looks like the input file can not be accessed");
        }

        if (!positive.isEmpty()) {
            int maxPos = Collections.max(positive);
            posArr = countingSort(positive.stream().mapToInt(i -> i).toArray(), maxPos);
        }
        if (!negative.isEmpty()) {
            int maxNeg = Collections.max(negative);
            negArr = countingSort(negative.stream().mapToInt(i -> i).toArray(), maxNeg);
        }

        //writing part
        try(BufferedWriter buffOut = new BufferedWriter(new FileWriter(outputName))) {

            for(int i = negArr.length - 1; i >= 0; i--) {
                buffOut.write("-" + (negArr[i] / 10.0)); //converting Int -> Double -> String
                System.out.println("-" + (negArr[i] / 10.0));
                buffOut.newLine();
            }
            for (int aPosArr : posArr) {
                buffOut.write("" + (aPosArr / 10.0)); //converting Int -> Double -> String
                System.out.println("" + (aPosArr / 10.0));
                buffOut.newLine();
            }

        } catch (IOException e) {
            System.out.println("Oops, looks like the output file can not be accessed");
        }
    }

    /**
     * Сортировка последовательности
     *
     * Средняя
     * (Задача взята с сайта acmp.ru)
     *
     * В файле задана последовательность из n целых положительных чисел, каждое в своей строке, например:
     *
     * 1
     * 2
     * 3
     * 2
     * 3
     * 1
     * 2
     *
     * Необходимо найти число, которое встречается в этой последовательности наибольшее количество раз,
     * а если таких чисел несколько, то найти минимальное из них,
     * и после этого переместить все такие числа в конец заданной последовательности.
     * Порядок расположения остальных чисел должен остаться без изменения.
     *
     * 1
     * 3
     * 3
     * 1
     * 2
     * 2
     * 2
     */
    static public void sortSequence(String inputName, String outputName) {
        throw new NotImplementedError();
    }

    /**
     * Соединить два отсортированных массива в один
     *
     * Простая
     *
     * Задан отсортированный массив first и второй массив second,
     * первые first.size ячеек которого содержат null, а остальные ячейки также отсортированы.
     * Соединить оба массива в массиве second так, чтобы он оказался отсортирован. Пример:
     *
     * first = [4 9 15 20 28]
     * second = [null null null null null 1 3 9 13 18 23]
     *
     * Результат: second = [1 3 4 9 9 13 15 20 23 28]
     */
    static <T extends Comparable<T>> void mergeArrays(T[] first, T[] second) {
        throw new NotImplementedError();
    }
}
