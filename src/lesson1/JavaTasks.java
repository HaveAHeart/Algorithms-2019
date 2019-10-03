package lesson1;

import kotlin.NotImplementedError;
import kotlin.Pair;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

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
    *trying some complex sorting algorithm -> quick sort
    *
    *Complexity: O(N)(reading) + O(N*log(N))quickSort + O(N) (writing) = O(N * log(N)) - if we will be
    *lucky enough to get the median value, then it will be nearer to O(Nlog2(N)). In the worst case quickSort will give
    * us O(N^2) difficulty(if the recursion depth will be N).
    *
    * Memory: O(const) for every element in timeToInt(),
    * O(logN) for the quick sort (recursion depth is logN, and for every instance the memory required is O(const))
    * O(N) for the input data
    * total: O(N) of the input data, O(logN) (average for quick sort) for every recursion instance give us O(N).
    */

    //returns time in seconds
    static int timeToInt(String time) {
        if (!time.matches("0[1-9]:[0-5][0-9]:[0-5][0-9]\\s[AP]M") &&
        !time.matches("1[0-2]:[0-5][0-9]:[0-5][0-9]\\s[AP]M")) {
            /*
            * input data format:
            * should be formatted as xx:xx:xx AM / xx:xx:xx PM
            * no cases with 00 hours (00:xx:xx AM / 00:xx:xx PM) should be met
            * hours [1-12], minutes [0-59], seconds [0-59], [AM-PM]
            */
            throw new IllegalArgumentException("illegal format: " + time);
        }
        int result;
        String[] arr = time.split(" ")[0].split(":");

        //getting result time in seconds: (hours*60 + minutes) * 60 + seconds
        result = (Integer.parseInt(arr[0])*60 + Integer.parseInt(arr[1])) * 60 + Integer.parseInt(arr[2]);

        if (time.charAt(9) == 'A') {
            //interval 12:00:00 AM - 12:59:59 AM equal to 00:00:00 - 00:59:59
            if (result >= 43200) return result % 43200; //result = 43200 and "AM" give us 12:00:00 AM -> 00:00:00
            else return result;
        }
        else {
            //interval 12:00:00 PM - 12:59:59 PM equal to 12:00:00 - 12:59:59
            if (result >= 43200) return result; //result = 43200 and "PM" give us 12:00:00 PM -> 12:00:00
            else return result + 43200; //adding 12 hours for the [1-11]:xx:xx PM (12) -> [13-23]:xx:xx (24)
        }
    }

    static void timeQuickSort(ArrayList<String> input, int i, int j) {
        if (i == j || input.isEmpty()) return;
        System.out.println("got input sized " + input.size() + ", i = " + i + ", j = " + j);
        for (int c = i; c <= j; c++) System.out.println(input.get(c));
        int start = i;
        int end = j;
        int median = timeToInt(input.get(start)); //(trying our luck) taking the median element from the array
        boolean iCondition = timeToInt(input.get(i)) < median;
        boolean jCondition = timeToInt(input.get(j)) > median;
        while (j - i > 0) { //until the "i" and "j" pointers will meet
            iCondition = timeToInt(input.get(i)) < median;
            jCondition = timeToInt(input.get(j)) > median;
            if (!iCondition && !jCondition) { //found proper pair to swap, swapping
                if (input.get(i).equals(input.get(j))) { //avoiding endless loops with changing the same elements
                    i += 1;
                    continue;
                }
                String temp = input.get(i);
                input.set(i, input.get(j));
                input.set(j, temp);
            }
            if (iCondition && i < j) i += 1;
            if (jCondition && i < j) j -= 1;
        }
        //choosing where should go the element where the pointers have met
        if (!jCondition) {
            timeQuickSort(input, start, i - 1);
            timeQuickSort(input, i, end);
        }
        else {
            timeQuickSort(input, start, i);
            timeQuickSort(input, i + 1, end);
        }
    }

    //Note - taking 12:00:00 AM (12) as 0:00:00 (24), and 12:00:00 PM (12) as 12:00:00 (24)
    static public void sortTimes(String inputName, String outputName) {
        //reading part
        ArrayList<String> input = new ArrayList<>();
        try (BufferedReader buffIn = new BufferedReader(new FileReader(inputName))) {
            buffIn.lines().forEach(input::add);
            timeQuickSort(input, 0, input.size() - 1);
        } catch (IOException e) {
            throw new IllegalArgumentException("Input file name argument is illegal");
        }
        //writing part
        try (BufferedWriter buffOut = new BufferedWriter(new FileWriter(outputName))) {
            for (String str : input) {
                buffOut.write(str);
                buffOut.newLine();
            }
        } catch (IOException e) {
            throw new IllegalArgumentException("Output file name argument is illegal");
        }
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
    * trying some simple sort: insertion, for example
    * IMHO, it's quite the good decision, because on every reading iteration we can do the main insertion sort part:
    * compare current record with the other records. Yes, it is still O(N^2), but due to the fact that we set every
    * record on its place in the array instead of filling the array and then - sorting, it should be quite efficient.
    * thinking about the structure like this:
    * all
    * |-street1
    * | |-№1
    * | | |-surname1
    * | | | |-name1
    * | | | |-name2
    * | | |-surname2
    * | | | |-name1
    * ...
    *
    * the memory efficiency will be higher(in the best case: O(3+0.25N) if we will take the name as the 1/4 of
    * all the data in the record),also, no additional formatting will be required while writing,
     * but the code will be quite messed up, like:
    * ArrayList<Pair<Street, ArrayList<Pair<Number, ArrayList<Pair<Surname, ArrayList<Names>>>>>>>,
    * and working with all these pairs is too sad :c
    *
    * Complexity: O(N) reading * O(N) comparing + O(N) writing -> total: O(N^2)
    * amount of operations(worst case) for the elements ->
    * 2(write to input + write to output - 1st el) + 2(compare + write to input + write to output- 2nd el) + ... + N+1
    * sum(2, 3, ..., N+1) = ((3 + N)/2)*N = (3N + N^2)/2 -> O(N^2) complexity
    *
    * Memory: O(N) for the input data * O(const) for the temporary data(record, added, etc.),
    * O(N) in the worst case for writing(if all the people will live in the same building)
    * -> total O(N) memory
    */


    static public void sortAddresses(String inputName, String outputName) {
        //reading part
        ArrayList<String[]> input = new ArrayList<>();
        try (BufferedReader buffIn = new BufferedReader(new FileReader(inputName))) {
            buffIn.lines().forEach(str -> {
                //format: Surname Name - Street number
                if (!str.matches("[А-ЯЁ][а-яё]*\\s[А-ЯЁ][а-яё]*\\s-\\s[А-ЯЁ][а-яё]*\\s[1-9][0-9]*"))
                    throw new IllegalArgumentException("oops, input format is broken for: " + str);

                //for every record - array: [Surname, Name, Street, number]
                String[] record = str.split("\\s-?\\s?");
                boolean added = false;
                for(int i = 0; i < input.size(); i++) {
                    //it is better to place all the comparisons before the if|else part, or the code will be messy
                    int streetComp = record[2].compareTo(input.get(i)[2]); //street comparison
                    int numComp = Integer.parseInt(record[3]) - Integer.parseInt(input.get(i)[3]); //number comparison
                    int surnameComp = record[0].compareTo(input.get(i)[0]); //surname comparison
                    int nameComp = record[1].compareTo(input.get(i)[1]); //name comparison

                    if(streetComp < 0) { //street
                        input.add(i, record);
                        added = true;
                        break;
                    }
                    else if (numComp < 0 && streetComp == 0) { //number of house
                        System.out.println("Number " + record[3] + "lower then " + input.get(i)[3]);
                        input.add(i, record);
                        added = true;
                        break;
                    }
                    else if(surnameComp < 0 && streetComp == 0 && numComp == 0) { //surname
                        System.out.println("Surname " + record[0] + "lower then " + input.get(i)[0]);
                        input.add(i, record);
                        added = true;
                        break;
                    }
                    else if(nameComp < 0 && streetComp == 0  && numComp == 0 && surnameComp == 0) { //name
                        System.out.println("Name " + record[1] + "lower then " + input.get(i)[1]);
                        input.add(i, record);
                        added = true;
                        break;
                    }
                    else if(Arrays.equals(record, input.get(i))) { //if the record are the same
                        System.out.println("Record " + String.join(",", record) +
                                "equal to " + String.join(",", input.get(i)));
                        input.add(i, record);
                        added = true;
                        break;
                    }
                }
                //if the record was not added, that means it should be added to the end of the array
                if (!added) input.add(record);
            });
            input.forEach(str -> System.out.println(String.join(";" ,str)));
        } catch (IOException e) {
            throw new IllegalArgumentException("Input file name argument is illegal");
        }
        //writing part
        //no worries for the order of records - everything was already sorted in the reading part,
        //all we do here is formatting+writing
        try (BufferedWriter buffOut = new BufferedWriter(new FileWriter(outputName))) {

            String streetAndNumber = "";
            ArrayList<String> surnameAndName = new ArrayList<>();

            for (String[] record : input) {
                //if street+number are same for the previous record - just add surname/name
                if ((record[2] + " " + record[3]).equals(streetAndNumber))
                    surnameAndName.add(record[0] + " " + record[1]);
                //else - write current buffer and fill it with new street+number
                else {
                    if (!streetAndNumber.isEmpty()) { //condition for getting rid of the 1st blank line
                        System.out.println("writing: " + streetAndNumber + ", " + surnameAndName);
                        buffOut.write(streetAndNumber + " - " + String.join(", ", surnameAndName));
                        buffOut.newLine();
                    }
                    streetAndNumber = record[2] + " " + record[3];
                    surnameAndName.clear();
                    surnameAndName.add(record[0] + " " + record[1]);
                    System.out.println("Current st&num: " + streetAndNumber +
                            ", sn&n: " + String.join(",", surnameAndName));
                }
            }
            //out of loop to get rid of the empty line at the end
            if (!streetAndNumber.isEmpty())
                buffOut.write(streetAndNumber + " - " + String.join(", ", surnameAndName));

        } catch (IOException e) {
            throw new IllegalArgumentException("Output file name argument is illegal");
        }

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

    /*quickSort time, i guess
    * Complexity: O(N) - reading, O(NlogN) - sorting, O(N) - writing
    * total -> O(NlogN), base of log(N) depends on the chosen median element
    *
    * Memory: O(N) - reading, O(logN) - recursion needs - log base depends on the median element chosen, O(1) - writing
    * Total -> O(N) memory
    */

    private static void temperatureQuickSort(ArrayList<Double> input, int i, int j) {
        if (i == j || input.isEmpty()) return;
        System.out.println("got input sized " + input.size() + ", i = " + i + ", j = " + j);
        for (int c = i; c <= j; c++) System.out.println(input.get(c));
        int start = i;
        int end = j;
        double median = input.get(start); //(trying our luck) taking the median element from the array
        boolean iCondition = input.get(i) < median;
        boolean jCondition = input.get(j) > median;
        while (j - i > 0) { //until the "i" and "j" pointers will meet
            iCondition = input.get(i) < median;
            jCondition = input.get(j) > median;
            if (!iCondition && !jCondition) { //found proper pair to swap, swapping
                if (input.get(i).equals(input.get(j))) { //avoiding endless loops with changing the same elements
                    i += 1;
                    continue;
                }
                Double temp = input.get(i);
                input.set(i, input.get(j));
                input.set(j, temp);
            }
            if (iCondition && i < j) i += 1;
            if (jCondition && i < j) j -= 1;
        }
        //choosing where should go the element where the pointers have met
        if (!jCondition) {
            temperatureQuickSort(input, start, i - 1);
            temperatureQuickSort(input, i, end);
        }
        else {
            temperatureQuickSort(input, start, i);
            temperatureQuickSort(input, i + 1, end);
        }
    }

    static public void sortTemperatures(String inputName, String outputName) {
        ArrayList<Double> input = new ArrayList<>();
        //reading part
        try(BufferedReader buffIn = new BufferedReader(new FileReader(inputName))) {

            buffIn.lines().forEach(str -> {
                if (!str.matches("-?[0-9]+\\.[0-9]")) //format: [-]xxxxx.x
                    throw new IllegalArgumentException("oops, incorrect input format for " + str);
                input.add(Double.parseDouble(str));
            });

        } catch (IOException e) {
            System.out.println("Oops, looks like the input file can not be accessed");
        }

        temperatureQuickSort(input, 0, input.size() - 1);

        //writing part
        try(BufferedWriter buffOut = new BufferedWriter(new FileWriter(outputName))) {

            for(Double number : input) {
                buffOut.write(number.toString());
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
